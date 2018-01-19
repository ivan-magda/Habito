package com.ivanmagda.habito.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.ivanmagda.habito.R
import com.ivanmagda.habito.adapters.HabitsAdapter
import com.ivanmagda.habito.analytics.HabitoAnalytics
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.model.HabitList
import com.ivanmagda.habito.model.HabitRecord
import com.ivanmagda.habito.sync.FirebaseSyncUtils
import com.ivanmagda.habito.utils.HabitoScoreUtils
import com.ivanmagda.habito.utils.ReminderUtils
import com.ivanmagda.habito.utils.SharedPreferencesUtils
import com.ivanmagda.habito.view.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_habit_list.*
import java.util.*

class HabitListActivity : AppCompatActivity(), HabitsAdapter.OnClickListener {

    private lateinit var habitsAdapter: HabitsAdapter

    // Firebase instance variables.
    private var userHabitsQuery: Query? = null
    private var valueEventListener: ValueEventListener? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
    }

    private fun configure() {
        setContentView(R.layout.activity_habit_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        initFirebase()

        val sortOrder = SharedPreferencesUtils.getSortOrder(this)
        val habitList = HabitList(ArrayList(), sortOrder)
        habitsAdapter = HabitsAdapter(habitList, this)

        rv_habits.layoutManager = GridLayoutManager(this, NUM_OF_COLUMNS)
        rv_habits.addItemDecoration(GridSpacingItemDecoration(NUM_OF_COLUMNS, SPACE_BETWEEN_ITEMS, true))
        rv_habits.adapter = habitsAdapter
        rv_habits.setHasFixedSize(true)
        rv_habits.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && fab.isShown) {
                    fab.hide()
                } else if (dy < 0 && !fab.isShown) {
                    fab.show()
                }
            }
        })

        fab.setOnClickListener { createHabit() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(findViewById(R.id.activity_habit_list), R.string.auth_success_msg,
                        Snackbar.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, R.string.auth_canceled_msg, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()

        firebaseAuth.removeAuthStateListener(authStateListener)
        detachDatabaseReadListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_habit_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                signOut()
                true
            }
            R.id.action_sort_by_name -> {
                sortBy(HabitList.SortOrder.NAME)
                true
            }
            R.id.action_sort_by_created_date -> {
                sortBy(HabitList.SortOrder.DATE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(habit: Habit, position: Int) {
        showDetail(habit)
    }

    private fun initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                onSignedInInitialize()
            } else {
                onSignedOutCleanup()
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setProviders(Arrays.asList<AuthUI.IdpConfig>(
                                        AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
                                ).build(),
                        RC_SIGN_IN)
            }
        }
    }

    private fun sortBy(sortOrder: HabitList.SortOrder) {
        SharedPreferencesUtils.setSortOrder(this, sortOrder)
        habitsAdapter.setSortOrder(sortOrder)
    }

    private fun signOut() {
        AlertDialog.Builder(this)
                .setTitle(R.string.sign_out)
                .setMessage(R.string.sign_out_message)
                .setPositiveButton(android.R.string.yes) { _, _ -> AuthUI.getInstance().signOut(this@HabitListActivity) }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    private fun onSignedInInitialize() {
        detachDatabaseReadListener()

        HabitoAnalytics.logLogin(FirebaseAuth.getInstance().currentUser!!)
        userHabitsQuery = FirebaseSyncUtils.currentUserHabitsQuery
        assert(userHabitsQuery != null)
        userHabitsQuery!!.keepSynced(true)

        attachDatabaseReadListener()
    }

    private fun onSignedOutCleanup() {
        ReminderUtils.cancelAll(habitsAdapter.habits!!, this)
        habitsAdapter.clear()
        detachDatabaseReadListener()
    }

    private fun attachDatabaseReadListener() {
        if (valueEventListener != null) {
            return
        }

        showProgressIndicator()

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hideProgressIndicator()
                processOnDataChange(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                hideProgressIndicator()
                Log.e(TAG, "Cancelled to query habits: " + databaseError.toString())
            }
        }

        userHabitsQuery!!.addValueEventListener(valueEventListener)
    }

    private fun detachDatabaseReadListener() {
        if (valueEventListener != null) {
            userHabitsQuery!!.removeEventListener(valueEventListener!!)
            valueEventListener = null
        }
        userHabitsQuery = null
    }

    private fun processOnDataChange(dataSnapshot: DataSnapshot) {
        val habits = ArrayList<Habit>(dataSnapshot.childrenCount.toInt())
        for (data in dataSnapshot.children) {
            val parsedRecord = data.getValue(HabitRecord::class.java)
            habits.add(Habit(data.key, parsedRecord!!))
        }

        habitsAdapter.habits = habits
        empty_view.visibility = if (habitsAdapter.habits!!.isEmpty()) View.VISIBLE else View.INVISIBLE

        HabitoScoreUtils.processAll(habits)
        ReminderUtils.processAll(habits, this)
    }

    private fun showDetail(habit: Habit) {
        HabitoAnalytics.logViewHabitListItem(habit)
        val intent = Intent(this, DetailHabitActivity::class.java)
        intent.putExtra(DetailHabitActivity.HABIT_EXTRA_KEY, habit)
        startActivity(intent)
    }

    private fun createHabit() {
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, EditHabitActivity::class.java))
        }
    }

    private fun showProgressIndicator() {
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressIndicator() {
        progress_bar.visibility = View.INVISIBLE
    }

    companion object {
        private val TAG = "HabitListActivity"

        private val NUM_OF_COLUMNS = 2
        private val SPACE_BETWEEN_ITEMS = 32
        private val RC_SIGN_IN = 10
    }
}

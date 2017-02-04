package com.ivanmagda.habito.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ivanmagda.habito.R;
import com.ivanmagda.habito.adapters.HabitsAdapter;
import com.ivanmagda.habito.view.GridSpacingItemDecoration;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HabitListActivity extends AppCompatActivity implements HabitsAdapter.HabitAdapterOnClickListener {

    private static final String TAG = "HabitListActivity";

    private static final int NUM_OF_COLUMNS = 2;
    private static final int SPACE_BETWEEN_ITEMS = 50;
    private static final int RC_SIGN_IN = 10;

    @BindView(R.id.rv_habits)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private HabitsAdapter mHabitsAdapter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configure();
    }

    private void configure() {
        setContentView(R.layout.activity_habit_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initializeFirebase();

        mHabitsAdapter = new HabitsAdapter(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_OF_COLUMNS));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUM_OF_COLUMNS,
                SPACE_BETWEEN_ITEMS, true));
        mRecyclerView.setAdapter(mHabitsAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                } else if (dy < 0 && !floatingActionButton.isShown()) {
                    floatingActionButton.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(findViewById(R.id.activity_habit_list), R.string.auth_success_msg,
                        Snackbar.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.auth_canceled_msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_habit_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(int position) {
        Log.d(TAG, "Select item at position: " + position);
    }

    @OnClick(R.id.fab)
    void onAddClick() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(HabitListActivity.this, EditHabitActivity.class));
        }
    }

    private void initializeFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize();
                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
                                    ).build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialize() {
    }

    private void onSignedOutCleanup() {
    }

    private void signOut() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_out)
                .setMessage(R.string.sign_out_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance().signOut(HabitListActivity.this);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}

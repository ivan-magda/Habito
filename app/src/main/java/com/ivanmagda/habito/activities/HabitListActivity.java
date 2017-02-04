package com.ivanmagda.habito.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ivanmagda.habito.view.GridSpacingItemDecoration;
import com.ivanmagda.habito.model.HabitsAdapter;
import com.ivanmagda.habito.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HabitListActivity extends AppCompatActivity implements HabitsAdapter.HabitAdapterOnClickListener {

    private static final String TAG = "HabitListActivity";

    private static final int NUM_OF_COLUMNS = 2;
    private static final int SPACE_BETWEEN_ITEMS = 50;

    @BindView(R.id.rv_habits)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private HabitsAdapter mHabitsAdapter;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_habit_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
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
        startActivity(new Intent(HabitListActivity.this, EditHabitActivity.class));
    }

}

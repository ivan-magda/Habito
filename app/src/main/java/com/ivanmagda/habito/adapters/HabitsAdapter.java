package com.ivanmagda.habito.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.models.HabitRecord;
import com.ivanmagda.habito.view.HabitListItemViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitAdapterViewHolder> {

    /**
     * The interface that receives onClick messages.
     */
    public interface HabitAdapterOnClickListener {
        /**
         * @param position Index of the selected item.
         */
        void onClick(int position);
    }

    private List<HabitRecord> mHabitRecords;
    private HabitAdapterOnClickListener mClickListener;

    public HabitsAdapter() {
        this.mHabitRecords = new ArrayList<>();
    }

    /**
     * Creates a HabitsAdapter.
     *
     * @param habitRecords  The data source.
     * @param clickListener The on-click handler for this adapter. This single handler is called
     *                      when an item is clicked.
     */
    public HabitsAdapter(List<HabitRecord> habitRecords, HabitAdapterOnClickListener clickListener) {
        this.mHabitRecords = habitRecords;
        this.mClickListener = clickListener;
    }

    public void setHabitRecords(List<HabitRecord> habitRecords) {
        if (habitRecords == null) {
            this.mHabitRecords.clear();
        } else {
            this.mHabitRecords = habitRecords;
        }
        notifyDataSetChanged();
    }

    public List<HabitRecord> getHabitRecords() {
        return mHabitRecords;
    }

    public void setClickListener(HabitAdapterOnClickListener onClickListener) {
        this.mClickListener = onClickListener;
    }

    public void clear() {
        mHabitRecords.clear();
        notifyDataSetChanged();
    }

    public void add(HabitRecord habitRecord) {
        mHabitRecords.add(habitRecord);
        notifyDataSetChanged();
    }

    @Override
    public HabitAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_item,
                parent, false);
        listView.setFocusable(true);
        return new HabitAdapterViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(HabitAdapterViewHolder viewHolder, int position) {
        viewHolder.bindAtPosition(position);
    }

    @Override
    public int getItemCount() {
        return mHabitRecords.size();
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    class HabitAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_list_item_habit_title)
        TextView nameTextView;

        @BindView(R.id.tv_list_item_reset_period)
        TextView resetPeriodTextView;

        @BindView(R.id.tv_list_item_count)
        TextView countTextView;

        private HabitListItemViewModel mViewModel;

        HabitAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mViewModel = new HabitListItemViewModel(itemView.getContext());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mClickListener != null) mClickListener.onClick(position);
        }

        private void bindAtPosition(int position) {
            mViewModel.setHabitRecord(mHabitRecords.get(position));

            itemView.setBackgroundColor(mViewModel.getBackgroundColor());
            nameTextView.setText(mViewModel.getHabitName());
            resetPeriodTextView.setText(mViewModel.getResetFreq());
            countTextView.setText(mViewModel.getScore());
        }

    }

}

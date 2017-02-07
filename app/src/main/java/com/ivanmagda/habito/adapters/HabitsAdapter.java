package com.ivanmagda.habito.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.models.HabitList;
import com.ivanmagda.habito.view.model.HabitListItemViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitAdapterViewHolder> {

    /**
     * The interface that receives onClick messages.
     */
    public interface HabitAdapterOnClickListener {
        /**
         * @param selectedHabit Selected habit.
         * @param position      Index of the selected item.
         */
        void onClick(Habit selectedHabit, int position);
    }

    private HabitList mHabitList;
    private HabitAdapterOnClickListener mClickListener;

    /**
     * Creates a HabitsAdapter.
     *
     * @param habitList     The data source.
     * @param clickListener The on-click handler for this adapter. This single handler is called
     *                      when an item is clicked.
     */
    public HabitsAdapter(@NonNull HabitList habitList,
                         @Nullable HabitAdapterOnClickListener clickListener) {
        this.mHabitList = habitList;
        this.mClickListener = clickListener;
    }

    public HabitsAdapter(@NonNull HabitList habitList) {
        this(habitList, null);
    }

    public void setHabits(List<Habit> habits) {
        if (habits == null) {
            this.mHabitList.clear();
        } else {
            this.mHabitList.setHabits(habits);
        }
        notifyDataSetChanged();
    }

    public List<Habit> getHabits() {
        return mHabitList.getHabits();
    }

    public void setSortOrder(HabitList.SortOrder sortOrder) {
        if (mHabitList.getSortOrder() != sortOrder) {
            mHabitList.setSortOrder(sortOrder);
            notifyDataSetChanged();
        }
    }

    public void setClickListener(HabitAdapterOnClickListener onClickListener) {
        this.mClickListener = onClickListener;
    }

    public void clear() {
        mHabitList.clear();
        notifyDataSetChanged();
    }

    public void add(Habit habit) {
        mHabitList.add(habit);
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
        return mHabitList.getHabits().size();
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
            if (mClickListener != null) {
                int position = getAdapterPosition();
                Habit selectedHabit = mHabitList.getHabits().get(position);
                mClickListener.onClick(selectedHabit, position);
            }
        }

        private void bindAtPosition(int position) {
            mViewModel.setHabit(mHabitList.getHabits().get(position));

            if (itemView instanceof CardView) {
                ((CardView) itemView).setCardBackgroundColor(mViewModel.getBackgroundColor());
            } else {
                itemView.setBackgroundColor(mViewModel.getBackgroundColor());
            }

            nameTextView.setText(mViewModel.getHabitName());
            nameTextView.setTextColor(mViewModel.getHabitNameTextColor());
            resetPeriodTextView.setText(mViewModel.getResetFreq());
            countTextView.setText(mViewModel.getScore());
        }

    }

}

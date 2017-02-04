package com.ivanmagda.habito.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanmagda.habito.R;

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

    private HabitAdapterOnClickListener mClickListener;

    /**
     * Creates a HabitsAdapter.
     *
     * @param clickListener The on-click handler for this adapter. This single handler is called
     *                      when an item is clicked.
     */
    public HabitsAdapter(HabitAdapterOnClickListener clickListener) {
        this.mClickListener = clickListener;
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
        return 10;
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

        HabitAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mClickListener != null) mClickListener.onClick(position);
        }

        private void bindAtPosition(int position) {
            String title = (position % 2 == 0 ? "Read 10 minutes" : "Brush teeth");
            String resetAt = (position % 2 == 0 ? "Today" : "This week");

            nameTextView.setText(title);
            resetPeriodTextView.setText(resetAt);
            countTextView.setText(String.valueOf(position));
        }
    }

}

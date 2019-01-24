package com.lollykrown.rccgdc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.lollykrown.rccgdc.utils.ColorUtils;
import com.lollykrown.rccgdc.R;

public class WorkforceAdapter extends RecyclerView.Adapter<WorkforceAdapter.WorkforceViewHolder> {

    private static final String TAG = WorkforceAdapter.class.getSimpleName();

    final private WfListItemClickListener mWfOnClickListener;

    private static int viewHolderCount;

    private int mNumberItems;
    String[] mWf;

    public interface WfListItemClickListener {
        void onWfListItemClick(int clickedItemIndex);
    }

    public WorkforceAdapter(int numberOfItems, String[] wf, WfListItemClickListener listener) {
        mNumberItems = numberOfItems;
        mWfOnClickListener = listener;
        mWf = wf;
        viewHolderCount = 0;
    }

    @Override
    public WorkforceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.workforce_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        WorkforceViewHolder viewHolder = new WorkforceViewHolder(view);

        int backgroundColorForViewHolder = ColorUtils
                .getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);

        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WorkforceViewHolder holder, int position) {
        holder.workforceButton.setText(mWf[position]);
    }

    @Override
    public int getItemCount() {
        return mWf.length;
    }


    class WorkforceViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        Button workforceButton;


        public WorkforceViewHolder(View itemView) {
            super(itemView);

            workforceButton = itemView.findViewById(R.id.button_workforce);
            itemView.setOnClickListener(this);
        }

//        void bind(int listIndex) {
//            workforceButton.setText(String.valueOf(listIndex));
//        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mWfOnClickListener.onWfListItemClick(clickedPosition);
        }
    }
}


package com.networkcommittee.wc.bh4nitjtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PollRecyclerViewAdapter extends
        RecyclerView.Adapter<PollRecyclerViewAdapter.ViewHolder> {

    public int selectedOption=0;
    private List<String> optionsList;
    private Context context;

    public int lastSelectedPosition = -1;

    public PollRecyclerViewAdapter(List<String> optionsListIn
            , Context ctx) {
        optionsList = optionsListIn;
        context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_selected_poll_item, parent, false);

        ViewHolder viewHolder =
                new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {
        String offersModel = optionsList.get(position);
        holder.offerName.setText(offersModel);
//        holder.offerAmount.setText("" + offersModel.getSavings());

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        holder.selectionState.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView offerName;
        public RadioButton selectionState;

        public ViewHolder(View view) {
            super(view);
            offerName = (TextView) view.findViewById(R.id.offer_name);
//            offerAmount = (TextView) view.findViewById(R.id.offer_amount);
            selectionState = (RadioButton) view.findViewById(R.id.offer_select);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    for(int i=0;i<optionsList.size();i++){
                        if(optionsList.get(i).equals(offerName.getText())){
                            selectedOption=i;
                        }
                    }

                }
            });
        }
    }

}

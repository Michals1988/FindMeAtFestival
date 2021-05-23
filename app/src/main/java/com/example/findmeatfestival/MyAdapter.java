package com.example.findmeatfestival;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {


    private ArrayList<Festival> mFestival = new ArrayList<>();
    private RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mFestivalName;
        public TextView mFestivalDate;

        public MyViewHolder(View pItem) {
            super(pItem);
            mFestivalName = (TextView) pItem.findViewById(R.id.textFestivalName);
            mFestivalDate = (TextView) pItem.findViewById(R.id.textFestivalDate);
        }
    }

    public MyAdapter(ArrayList<Festival> pFestival, RecyclerView pRecyclerView) {
        mFestival=pFestival;
        mRecyclerView=pRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_adapter, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Festival festival=new Festival();
                int positionToOpen=mRecyclerView.getChildAdapterPosition(v);

                festival=mFestival.get(positionToOpen);

                String idFestival=String.valueOf(festival.getId());

                Intent intentOpenList=new Intent(v.getContext(), FestivalOneShowActivity.class);
                String id=festival.getId();

                intentOpenList.putExtra("documentUserID", festival.getDocumentUserID());
                intentOpenList.putExtra("userId",festival.getUserID());
                intentOpenList.putExtra("festivalID", id);
                ((Activity)v.getContext()).finish();
                v.getContext().startActivity(intentOpenList);

            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        Festival festival=mFestival.get(i);
        ((MyViewHolder) viewHolder).mFestivalName.setText(festival.getFestivalName());
        ((MyViewHolder) viewHolder).mFestivalDate.setText(festival.getFestivalDate());


    }

    @Override
    public int getItemCount(){return mFestival.size();}
}

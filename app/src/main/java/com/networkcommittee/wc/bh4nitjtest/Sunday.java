package com.networkcommittee.wc.bh4nitjtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Sunday extends Fragment
{

    ListView listView1;
    FirebaseDatabase database;
    DatabaseReference messMenuRef;
    ArrayList<String> mealList;
    ArrayAdapter<String> mealAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sunday, container, false);
        listView1 = (ListView) rootView.findViewById(R.id.listView7);

        database = FirebaseDatabase.getInstance();
        messMenuRef = database.getReference("messMenu");
        mealList = new ArrayList<>();
        mealAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_info, R.id.item_info_1, mealList);
        messMenuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Meal sunday =dataSnapshot.getValue(WeekDay.class).sunday;
                mealList.add("Breakfast: "+sunday.getBreakfast());
                mealList.add("Lunch : "+sunday.getLunch());
                mealList.add("Snacks: "+sunday.getSnacks());
                mealList.add("Dinner: "+sunday.getDinner());
                listView1.setAdapter(mealAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return rootView;
    }

}

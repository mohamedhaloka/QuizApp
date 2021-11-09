package com.nasr.quizapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

public class HistoryFragment extends ListFragment {


    HistoryActivity historyActivity = null;

    public HistoryFragment() {
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        historyActivity.onClick(id);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        historyActivity = (HistoryActivity) context;
        System.out.println("Welcommme");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setListAdapter(historyActivity.simpleCursorAdapter);
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
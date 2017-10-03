package com.dp.uheadmaster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.AnnouncementAdapter;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;

/**
 * Created by DELL on 24/09/2017.
 */

public class AnnouncementsFragment extends Fragment {

    private RecyclerView recyclerQustions;
    private LinearLayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_questions_answers_layout,container,false);
        recyclerQustions = (RecyclerView) v.findViewById(R.id.recycler1);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());
        recyclerQustions.setAdapter(new AnnouncementAdapter(getActivity().getApplicationContext()));
        return v;

    }
}

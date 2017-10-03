package com.dp.uheadmaster.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ExpandedAdapter;
import com.dp.uheadmaster.interfaces.ParentPositionChecker;
import com.dp.uheadmaster.interfaces.VideoPathChecker;
import com.dp.uheadmaster.models.TitleChild;
import com.dp.uheadmaster.models.TitleParent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DELL on 24/09/2017.
 */

public class LecturesFrag extends Fragment implements ParentPositionChecker{

        private RecyclerView recyclerView;

        private static ExpandedAdapter expandedAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_lecture_layout,container,false);
        initializeUi(v);
        return v;
    }




    @Override
    public void getParentPosition(Context context, int position) {
        System.out.println("Adapters Count:"+expandedAdapter.getItemCount()+" "+position);
        for (int i=0;i<expandedAdapter.getItemCount();i++){
            if(i!=position)
                expandedAdapter.collapseParent(i);
        }

    }

    public void initializeUi(View v) {
        recyclerView=(RecyclerView)v.findViewById(R.id.recycler1);
        //mWebView=(WebView)v.findViewById(R.id.simpleVideoView) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        expandedAdapter=new ExpandedAdapter(getActivity().getApplicationContext(),initiateData());


        recyclerView.setAdapter(expandedAdapter);
    }

        public List<TitleParent> initiateData(){
            TitleChild beef = new TitleChild("beef","https://www.youtube.com/watch?v=_nzx5ioXCO4");
            TitleChild cheese = new TitleChild("cheese","https://www.youtube.com/watch?v=vEfW9sm70tg");
            TitleChild salsa = new TitleChild("salsa","https://www.youtube.com/watch?v=D8VEwqef9XY");
            TitleChild tortilla = new TitleChild("tortilla","https://www.youtube.com/watch?v=kEpOv49P6Yg");

            TitleParent taco = new TitleParent("Meet", Arrays.asList(beef, cheese, salsa, tortilla));
            TitleParent quesadilla = new TitleParent("Cheese",Arrays.asList(cheese, tortilla));
            TitleParent parent3 = new TitleParent("Chicken",Arrays.asList(cheese, tortilla));
            TitleParent parent4 = new TitleParent("Fish",Arrays.asList(cheese, tortilla));
            List<TitleParent> parents = Arrays.asList(taco, quesadilla,parent3,parent4);
            return parents;
        }

}

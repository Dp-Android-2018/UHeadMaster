package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dp.uheadmaster.R;

import java.util.ArrayList;

/**
 * Created by DELL on 27/08/2017.
 */

public class ProfileSpinnerAdapter extends BaseAdapter {
   private Context context;
    private ArrayList<String>data;
    public ProfileSpinnerAdapter(Context context, ArrayList<String>data) {
        this.context=context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v=new View(context);

        // get layout from mobile.xml
        v = inflater.inflate(R.layout.custom_spinner_layout, null);
        TextView txt=(TextView)v.findViewById(R.id.tv_page_title);
        txt.setText(data.get(position));

        return v;
    }
}

package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Languages;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.List;

/**
 * Created by DELL on 28/10/2017.
 */

public class LanguagesAdapter extends BaseAdapter {

    private Context context;
    private List<Languages> languages;
    private FontChangeCrawler fontChanger;
    public LanguagesAdapter(Context context, List<Languages> languages) {
        this.context=context;
        this.languages=languages;
    }

    @Override
    public int getCount() {
        return languages.size();
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
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        TextView txt=(TextView)v.findViewById(R.id.tv_page_title);
        txt.setText(languages.get(position).getLang()

        );

        return v;
    }
}

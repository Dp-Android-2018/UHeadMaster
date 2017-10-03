package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.activites.CoursesListAct;
import com.dp.uheadmaster.activites.SubCategoriesAct;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.fragments.BasicSearchFrag;
import com.dp.uheadmaster.fragments.CategoriesFrag;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class CategoriesAdapter extends BaseAdapter {
    private List<CategoryModel> categoriesArray;
    private Context context;
    private boolean mainCategory;

    public CategoriesAdapter(Context context, List<CategoryModel> categoriesArray, boolean mainCategory) {
        this.context = context;
        this.categoriesArray = categoriesArray;
        this.mainCategory = mainCategory;
    }

    @Override
    public int getCount() {
        return categoriesArray.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        //  if (convertView == null) {
        final CategoryModel categoriesModel = categoriesArray.get(position);
        gridView = new View(context);

        // get layout from mobile.xml
        gridView = inflater.inflate(R.layout.item_category, null);
        ImageView imgCategory = (ImageView) gridView.findViewById(R.id.img_category_icon);
        RelativeLayout item = (RelativeLayout) gridView.findViewById(R.id.item_category);
        TextView tvCategoryTitle = (TextView) gridView.findViewById(R.id.tv_category_title);
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) ){
            tvCategoryTitle.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font1"));
        }else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ){
            tvCategoryTitle.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
        }

        System.out.println("Category / image : " + categoriesModel.getImage());
        try {
            tvCategoryTitle.setText(categoriesModel.getTitle());
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mainCategory) {
                       Intent intent = new Intent(context, SubCategoriesAct.class);
                        intent.putExtra("category_id", categoriesModel.getId());
                        intent.putExtra("category_title", categoriesModel.getTitle());
                        CategoriesFrag.categoryId=categoriesModel.getId();
                        context.startActivity(intent);
                     //   BasicSearchFrag.viewPager.setCurrentItem(1);
                    } else {
                        //call courses act
                        SubCategoriesAct.subCategoryId=categoriesModel.getId();
                        Intent intent = new Intent(context, CoursesListAct.class);
                        intent.putExtra("reqest_type", 3);
                        intent.putExtra("sub_category_id", categoriesModel.getId());
                        intent.putExtra("sub_category_title", categoriesModel.getTitle());

                        context.startActivity(intent);
                    }

                }
            });
            if (categoriesModel.getImage() == null && categoriesModel.getImage().isEmpty()) {

                imgCategory.setImageResource(R.drawable.ic_logo);

            } else {
                Picasso.with(context)
                        .load(categoriesModel.getImage())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(imgCategory);
            }



        } catch (Exception e) {
            System.out.println("Category / error :" + e.getMessage());
        }


//        } else {
//            gridView = (View) convertView;
//        }

        return gridView;
    }
}

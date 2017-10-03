package com.dp.uheadmaster.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.fragments.CartFrag;
import com.dp.uheadmaster.fragments.SearchCategoriesFrag;
import com.dp.uheadmaster.fragments.SearchSubCategoriesFrag;
import com.dp.uheadmaster.holders.CartHolder;
import com.dp.uheadmaster.holders.SearchCategoryHolder;
import com.dp.uheadmaster.interfaces.SwitchFragmentInterface;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 06/09/2017.
 */

public class CategorySearchAdapter extends RecyclerView.Adapter<SearchCategoryHolder> {
    private Context context;
    private List<CategoryModel> categories;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    public static SwitchFragmentInterface delegate=null;
    private boolean mainCategory;
    View view;

    public CategorySearchAdapter(Context context, List<CategoryModel> categories,boolean mainCategory) {
        this.context = context;
        sharedPrefManager = new SharedPrefManager(context);
        this.categories = categories;
        this.mainCategory=mainCategory;

    }

    @Override
    public SearchCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_search_item, parent, false);
        view=v;
        return new SearchCategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchCategoryHolder holder, final int position) {


        holder.title.setText(categories.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainCategory) {
                    SearchCategoriesFrag.search_category_id = categories.get(position).getId();
                    delegate.moveFragment(1);
                }else if (! mainCategory){
                    SearchSubCategoriesFrag.search_sub_category_id = categories.get(position).getId();
                    delegate.moveFragment(1);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return categories.size();
    }



}


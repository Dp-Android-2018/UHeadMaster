package com.dp.uheadmaster.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.dialogs.DeleteCartCourseDialog;
import com.dp.uheadmaster.dialogs.DeleteCourseDialog;
import com.dp.uheadmaster.fragments.CartFrag;
import com.dp.uheadmaster.fragments.WishListFrag;
import com.dp.uheadmaster.holders.CartHolder;
import com.dp.uheadmaster.interfaces.RefershWishList;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.FontChangeCrawler;
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
 * Created by DELL on 21/08/2017.
 */

public class CartCoursesAdapter extends RecyclerView.Adapter<CartHolder> {
    private Context context;
    private List<CourseModel> cartCoursesList;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private FontChangeCrawler fontChanger;
    public CartCoursesAdapter(Context context, List<CourseModel> cartCoursesList) {
        this.context = context;
        sharedPrefManager = new SharedPrefManager(context);
        this.cartCoursesList = cartCoursesList;
        calculateTotalPrice();
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new CartHolder(v,context);
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {

        final CourseModel courseModel = cartCoursesList.get(position);
        holder.tvTitle.setText(courseModel.getTitle());
        holder.tvOldPrice.setText(courseModel.getOldProice() + courseModel.getCurrency());
        holder.tvPrice.setText(courseModel.getPrice() + courseModel.getCurrency());
        if (courseModel.getImagePath() != null && courseModel.getImagePath().isEmpty()) {
            holder.imgCourseLogo.setImageResource(R.drawable.ic_logo);
        } else {
            Picasso.with(context)
                    .load(courseModel.getImagePath())
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.imgCourseLogo);
        }

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove course from list and server
                //DeleteCourseDialog deleteCourseDialog=new DeleteCourseDialog(context,2);
              //  deleteCourseDialog.show();
                DeleteCartCourseDialog deleteCartCourseDialog=new DeleteCartCourseDialog(context,courseModel.getId(),0);
                deleteCartCourseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteCartCourseDialog.show();
                //removefromCart(courseModel.getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartCoursesList.size();
    }



    private void calculateTotalPrice() {
        double totalPrice = 0.0;
        String currency = "";
        for (int i = 0; i < cartCoursesList.size(); i++) {
            if(cartCoursesList.get(i).getPrice() !=null && !cartCoursesList.get(i).getPrice().equals("")){
                totalPrice +=Double.parseDouble(cartCoursesList.get(i).getPrice());
            }
            currency = cartCoursesList.get(0).getCurrency();
        }
        CartFrag.tvCourseCount.setText(cartCoursesList.size()+context.getString(R.string.course_in_cart));
        CartFrag.tvTotalPrice.setText(context.getString(R.string.total_payment)+totalPrice+" "+currency);
    }

}

package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by DELL on 09/09/2017.
 */

public class ReviewHolder extends RecyclerView.ViewHolder {
    public TextView tvReviewername;
    public RatingBar rbarValue;
    public EditText etComment;


    public ReviewHolder(View itemView, Context context) {
        super(itemView);
        this.tvReviewername=(TextView)itemView.findViewById(R.id.tv_reviewer_name);
        this.rbarValue=(RatingBar) itemView.findViewById(R.id.rating_value);
        this.etComment=(EditText) itemView.findViewById(R.id.et_comment_text);


            if ( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
                this.tvReviewername.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font2"));
                this.etComment.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font2"));

            } else if ( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
                this.tvReviewername.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
                this.etComment.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
            }

    }
}

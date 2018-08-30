package com.dp.uheadmaster.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.WriteReviewAct;

/**
 * Created by DELL on 09/01/2018.
 */

public class WelcomeMessageDialog extends Dialog {

    private Button btnOk,btnWriteReview;

    private Context context;
    private String msg;
    private TextView tvMsg;
    private int type;
    private int courseId;
    public WelcomeMessageDialog(@NonNull Context context,String msg,int type,int cid) {
        super(context);
        this.context=context;
        this.msg=msg;
        this.type=type;
        this.courseId=cid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_welcome_message);
        tvMsg=(TextView)findViewById(R.id.tv_message_title);
        if(!msg.equals(""))
                tvMsg.setText(msg);
        else
            tvMsg.setText(R.string.congratulations);
        btnOk=(Button)findViewById(R.id.btn_cancel);
        btnWriteReview=(Button)findViewById(R.id.btn_delete_course);
        if(type==1){
            btnOk.setVisibility(View.GONE);
            btnWriteReview.setVisibility(View.GONE);
        }else {
            btnOk.setVisibility(View.VISIBLE);
            btnWriteReview.setVisibility(View.VISIBLE);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomeMessageDialog.this.cancel();
            }
        });

        btnWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //   Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                WelcomeMessageDialog.this.cancel();
                Intent i=new Intent(context, WriteReviewAct.class);
                i.putExtra("CID",courseId);
                context.startActivity(i);

            }
        });
    }
}

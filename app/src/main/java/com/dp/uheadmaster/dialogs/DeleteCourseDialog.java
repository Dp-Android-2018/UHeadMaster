package com.dp.uheadmaster.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dp.uheadmaster.R;

/**
 * Created by DELL on 28/08/2017.
 */

public class DeleteCourseDialog extends Dialog {


    public DeleteCourseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_course_dialog_layout);
    }
}

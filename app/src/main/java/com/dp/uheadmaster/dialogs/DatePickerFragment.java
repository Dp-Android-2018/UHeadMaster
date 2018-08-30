package com.dp.uheadmaster.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.dp.uheadmaster.activites.AnnounceMentAct;
import com.dp.uheadmaster.interfaces.DateValue;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DELL on 22/04/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Date d=null;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);




        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
       // Toast.makeText(getActivity().getApplicationContext(),"Year:"+year+"\n Month:"+(month+1)+"\n Day:"+dayOfMonth,Toast.LENGTH_LONG).show();
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        String monthname=months[month];
        String date=String.valueOf(dayOfMonth).concat(" ").concat(monthname).concat(" | ").concat(String.valueOf(year));

        /////////////////////////////////////////////////////////////////////////////////////////////
        String serverdate=year+"-"+(month+1)+"-"+dayOfMonth;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
             d = dateFormat.parse(serverdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        serverdate=dateFormat.format(d);
        /////////////////////////////////////////////////////////////////////////////////////////////////
        Intent i = new Intent();
        i.putExtra("selectedDate",date);
        i.putExtra("serverdate",serverdate);
        DateValue dateValue=new AnnounceMentAct();
        AnnounceMentAct.dateValue.get().getDateValue(serverdate);
       // onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

    }
}

package com.dp.uheadmaster.activites;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dp.uheadmaster.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by DELL on 26/08/2017.
 */

public class ChartAct extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        HorizontalBarChart chart = (HorizontalBarChart)findViewById(R.id.chart);

        BarDataSet set1;
        set1 = new BarDataSet(getDataSet(), "The year 2017");

        set1.setColors(Color.parseColor("#00887A"), Color.parseColor("#00887A"), Color.parseColor("#00887A"), Color.parseColor("#00887A"), Color.parseColor("#00887A"));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.3f);

        // hide Y-axis
        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false);

        // custom X-axis labels
        String[] values = new String[] { "1 star", "2 stars", "3 stars", "4 stars", "5 stars"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



        YAxis yl = chart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//        yr.setInverted(true);
        //chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
       yl.setEnabled(false);
        yr.setEnabled(false);
        chart.setData(data);

        // custom description
        Description description = new Description();
        description.setText("Rating");
        chart.setDescription(description);

        // hide legend
        chart.getLegend().setEnabled(true);

        chart.animateY(1000);
        chart.invalidate();
    }

    class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }

    }

    private ArrayList<BarEntry> getDataSet() {

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        BarEntry v1e2 = new BarEntry(1, 3000);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2, 2000);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(3, 2500);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(4, 4000);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(5, 1200);
        valueSet1.add(v1e6);

        return valueSet1;
    }



}

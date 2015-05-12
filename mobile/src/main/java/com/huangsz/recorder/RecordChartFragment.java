package com.huangsz.recorder;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huangsz.recorder.data.utils.RecordDataHelper;
import com.huangsz.recorder.data.utils.TimeDataHelper;
import com.huangsz.recorder.model.Record;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Show the chart of selected record
 */
public class RecordChartFragment extends Fragment {

    private static final String TAG = "RecordChartFragment";

    private Record record;

    private GraphicalView mChartView;

    private TimeSeries mRecordSeries;

    private XYMultipleSeriesRenderer mChartRenderer;

    private void initChart() {
        LinearLayout chartHolder = (LinearLayout) getActivity().findViewById(R.id.chart_holder);
        mRecordSeries = new TimeSeries("Sleeping Record");

        // Creating a dataset to hold each series.
        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        dataSet.addSeries(mRecordSeries);

        // Creating XYSeriesRenderer to customize mRecordSeries
        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        seriesRenderer.setColor(Color.RED);
        seriesRenderer.setPointStyle(PointStyle.CIRCLE);
        seriesRenderer.setFillPoints(true);
        seriesRenderer.setLineWidth(4);
        seriesRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        mChartRenderer = new XYMultipleSeriesRenderer();
        mChartRenderer.setChartTitle("Sleeping Record");
        mChartRenderer.setXTitle("Date");
        mChartRenderer.setYTitle("Time");
        mChartRenderer.setZoomButtonsVisible(true);
        mChartRenderer.setLabelsTextSize(18);
        mChartRenderer.setGridColor(Color.WHITE);
        mChartRenderer.setLabelsColor(Color.GREEN);
        mChartRenderer.setChartTitleTextSize(22);
        mChartRenderer.setYLabelsAlign(Paint.Align.CENTER);
        mChartRenderer.setBackgroundColor(Color.WHITE);
        mChartRenderer.setApplyBackgroundColor(true);
        mChartRenderer.setMarginsColor(Color.GRAY);  // The border
        // Note that the order of adding series should be the same as adding dataset. But we only
        // have one dataset and one series here, so we don't have the problem.
        mChartRenderer.addSeriesRenderer(seriesRenderer);

        // Create a time chart view.
        mChartView = ChartFactory.getTimeChartView(
                getActivity(), dataSet, mChartRenderer, "dd-MMM-yyyy");

        mChartRenderer.setClickEnabled(true);
        mChartRenderer.setSelectableBuffer(10);

        chartHolder.addView(mChartView);
    }

    private void addChartData() {
        mRecordSeries.clear();
        mChartRenderer.clearYTextLabels();  // Disable the original y labels.
        for (Pair<String, String> record : this.record.getDataInList()) {
            Date date = TimeDataHelper.parseDate(record.first);
            Date time = TimeDataHelper.parseTime(record.second);
            int value = TimeDataHelper.getHourOfDay(time) * 60 + TimeDataHelper.getMinute(time);
            mRecordSeries.add(date, value);
            mChartRenderer.addYTextLabel(value, new SimpleDateFormat("HH:mm").format(time));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_chart, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        long recordId = getArguments().getLong(RecordDetailActivity.KEY_RECORD_ID);
        Log.i(TAG, "RecordId: " + recordId);
        record = RecordDataHelper.getRecord(getActivity(), recordId);
        Log.i(TAG, record.getData());
        initChart();
    }

    @Override
    public void onResume() {
        super.onResume();
        addChartData();
        mChartView.repaint();
    }
}

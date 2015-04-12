package com.huangsz.recorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huangsz.recorder.data.utils.RecordDataHelper;
import com.huangsz.recorder.model.Record;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Show the chart of selected record
 */
public class RecordChartFragment extends Fragment {

    private static final String TAG = "RecordChartFragment";

    private Record record;

    private GraphicalView mChart;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;

    private void initChart() {
        LinearLayout chartHolder = (LinearLayout) getActivity().findViewById(R.id.chart_holder);
        mCurrentSeries = new XYSeries("Sample Data");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mChart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, 0.3f);
        chartHolder.addView(mChart);
    }

    private void addSampleData() {
        mCurrentSeries.clear();
        mCurrentSeries.add(1, 2);
        mCurrentSeries.add(2, 3);
        mCurrentSeries.add(3, 2);
        mCurrentSeries.add(4, 5);
        mCurrentSeries.add(5, 4);
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
        addSampleData();
        mChart.repaint();
    }
}

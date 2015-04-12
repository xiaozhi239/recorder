package com.huangsz.recorder;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RecordDetailActivity extends ActionBarActivity {

    public static final String KEY_RECORD_ID = "RECORD_ID";

    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        if (savedInstanceState == null) {
            createFragment();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createFragment() {
        long recordId = getIntent().getLongExtra(KEY_RECORD_ID, Long.MIN_VALUE);
        if (fragment == null) {
            fragment = new RecordChartFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.record_detail_container, fragment)
                    .commit();
        }
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_RECORD_ID, recordId);
        fragment.setArguments(bundle);
    }
}

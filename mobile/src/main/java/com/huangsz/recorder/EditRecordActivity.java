package com.huangsz.recorder;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huangsz.recorder.data.RecordProvider;
import com.huangsz.recorder.model.Record;

public class EditRecordActivity extends BaseActionBarActivity {

    public static final String COMMAND = "Command";

    public static final String COMMAND_EDIT = "Edit";

    public static final String COMMAND_NEW = "New";

    public static final int SIG_SAVE = 0;

    public static final int SIG_CANCEL = 1;

    private boolean isEdit;

    private Button saveBtn, cancelBtn;

    private EditText nameText, descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        isEdit = getIntent().getStringExtra(COMMAND).equals(COMMAND_EDIT);
        saveBtn = (Button) findViewById(R.id.save_record_change_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                if (!isEdit) {
                    createRecord();
                }
//                returnIntent.putExtra()
                setResult(SIG_SAVE, returnIntent);
                finish();
            }
        });
        cancelBtn = (Button) findViewById(R.id.cancel_record_change_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(SIG_CANCEL);
                finish();
            }
        });
        nameText = (EditText) findViewById(R.id.record_name_text);
        descText = (EditText) findViewById(R.id.record_desc_text);
    }

    private void createRecord() {
        ContentValues record = new ContentValues();
        record.put(Record.Entry.COLUMN_NAME, nameText.getText().toString());
        record.put(Record.Entry.COLUMN_DESC, descText.getText().toString());
        getContentResolver().insert(RecordProvider.CONTENT_URI, record);
    }


}

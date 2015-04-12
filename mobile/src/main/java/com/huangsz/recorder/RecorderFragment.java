package com.huangsz.recorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangsz.recorder.data.RecordProvider;
import com.huangsz.recorder.data.utils.RecordDataHelper;
import com.huangsz.recorder.data.utils.TimeDataHelper;
import com.huangsz.recorder.model.Record;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecorderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecorderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecorderFragment extends Fragment {

    private static final String TAG = RecorderFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private static final int CREATE_RECORD_REQUEST = 0;

    private RecordListAdaptor recordListAdaptor;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecorderFragment.
     */
    public static RecorderFragment newInstance(String param1, String param2) {
        return new RecorderFragment();
    }

    public RecorderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recorder, container, false);

        Button addRecordBtn = (Button) view.findViewById(R.id.add_record_btn);
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditRecordActivity.class);
                intent.putExtra(EditRecordActivity.COMMAND, EditRecordActivity.COMMAND_NEW);
                startActivityForResult(intent, CREATE_RECORD_REQUEST);
            }
        });

        ListView recordList = (ListView) view.findViewById(R.id.record_list);
        Cursor recordListCursor = getActivity().getContentResolver().query(
                RecordProvider.CONTENT_URI,
                Record.Entry.projection(),
                null, null, null);
        recordListAdaptor = new RecordListAdaptor(getActivity(), recordListCursor,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        recordList.setAdapter(recordListAdaptor);
        recordList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onRecordItemLongClick((long) view.getTag());
                return false;
            }
        });
        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onRecordItemClick((long) view.getTag());
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_RECORD_REQUEST && resultCode == EditRecordActivity.SIG_SAVE) {
            onRecordChange();
        }
    }

    private void onRecordItemClick(final long recordId) {
        Intent intent = new Intent(getActivity(), RecordDetailActivity.class);
        intent.putExtra(RecordDetailActivity.KEY_RECORD_ID, recordId);
        startActivity(intent);
    }

    private void onRecordItemLongClick(final long recordId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete?").setMessage("Are you sure to delete the record?");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RecordDataHelper.deleteRecord(getActivity(), recordId);
                onRecordChange();
            }
        }).show();
    }

    private void onRecordChange() {
        Cursor recordListCursor = getActivity().getContentResolver().query(
                RecordProvider.CONTENT_URI,
                Record.Entry.projection(),
                null, null, null);
        recordListAdaptor.changeCursor(recordListCursor);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * Note that cursor should use the projection of
     * {@link com.huangsz.recorder.model.Record.Entry#projection()}
     */
    public class RecordListAdaptor extends CursorAdapter {

        public RecordListAdaptor(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(final Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.listitem_record, parent, false);
        }

        @Override
        public void bindView(View view, final Context context, Cursor cursor) {
            final long recordId = cursor.getLong(0);
            view.setTag(recordId);

            TextView name = (TextView) view.findViewById(R.id.record_name);
            name.setText(cursor.getString(1));
            TextView desc = (TextView) view.findViewById(R.id.record_desc);
            desc.setText(cursor.getString(2));

            ImageButton settings = (ImageButton) view.findViewById(R.id.record_setting_btn);
            settings.setFocusable(false);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "dashabi", Toast.LENGTH_SHORT).show();
                }
            });

            ImageButton track = (ImageButton) view.findViewById(R.id.record_track_btn);
            track.setFocusable(false);
            track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trackRecord(recordId, context);
                }
            });
        }

        private void trackRecord(long recordId, Context context) {
            Record record = RecordDataHelper.getRecord(context, recordId);
            Log.d(TAG, "recordData: " + record.getData());
            record.putData(TimeDataHelper.getCurrentData(), TimeDataHelper.getCurrentTime());
            ContentValues newData = new ContentValues();
            newData.put(Record.Entry.COLUMN_DATA, record.getData());
            RecordDataHelper.updateRecord(context, newData, recordId);
            Toast.makeText(context,"Today's time has been tracked", Toast.LENGTH_LONG).show();
        }
    }

}

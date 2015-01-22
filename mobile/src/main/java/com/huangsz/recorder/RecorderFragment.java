package com.huangsz.recorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huangsz.recorder.data.RecordProvider;
import com.huangsz.recorder.data.utils.RecordDataHelper;
import com.huangsz.recorder.model.Record;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecorderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecorderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecorderFragment extends Fragment {

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
            Cursor recordListCursor = getActivity().getContentResolver().query(
                    RecordProvider.CONTENT_URI,
                    Record.Entry.projection(),
                    null, null, null);
            recordListAdaptor.changeCursor(recordListCursor);
        }
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
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.listitem_record, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView name = (TextView)view.findViewById(R.id.record_name);
            name.setText(cursor.getString(1));
            TextView desc = (TextView)view.findViewById(R.id.record_desc);
            desc.setText(cursor.getString(2));
        }
    }

}

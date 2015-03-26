package ru.rsdev.arrowproject;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rsdev on 10.03.2015.
 */
public class DialogSynonim extends DialogFragment implements View.OnClickListener
{
    final String LOG_TAG = "myLogs";
    ListView listView;
    ArrayList<String> data;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.dialog_synonim, null);

        listView = (ListView)v.findViewById(R.id.listView);


        Integer position = getArguments().getInt("position");
        data = getArguments().getStringArrayList("data");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        String word = adapter.getItem(position);
        getDialog().setTitle(word);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

            onDismiss(getDialog());

            }
        });



        return v;
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
        //dismiss();
        onDismiss(getDialog());
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }


}

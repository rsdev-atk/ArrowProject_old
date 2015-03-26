package ru.rsdev.arrowproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ArrowDictionaryActivity extends ActionBarActivity {

    Button button9;
    EditText editText;
    ListView listView4;

    ArrayList<String> dictionaryList= new ArrayList<String>();//Коллекция вариантов ответов
    private static final String DB_NAME = "sinonim";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_dictionary);

        button9 = (Button)findViewById(R.id.button9);
        editText = (EditText)findViewById(R.id.editText);
        listView4 = (ListView)findViewById(R.id.listView4);

        dictionaryList.add("Введите запрос");
        inputInListView();

        //showDictionary();

    }

    public void inputInListView()
    {

        listView4.setAdapter(new ArrayAdapter<String>(this, R.layout.my_list_item,dictionaryList));
        listView4.setTextFilterEnabled(true);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arrow_dictionary, menu);
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



    public void clickButton(View view)
    {
        MyTask mt = new MyTask();
        mt.execute();

    }


    class MyTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            button9.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                ArrowClass myClass = new ArrowClass(getApplication(),DB_NAME);
                dictionaryList = myClass.getWordInDBtoDictionary(editText.getText().toString());
                if(dictionaryList.size()==0) {
                    dictionaryList.add("Уточните запрос");
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),String.valueOf(e).toString(),Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            inputInListView();
            button9.setEnabled(true);
        }
    }


}

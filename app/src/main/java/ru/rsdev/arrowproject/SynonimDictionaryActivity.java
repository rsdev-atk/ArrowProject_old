package ru.rsdev.arrowproject;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class SynonimDictionaryActivity extends ActionBarActivity {

    EditText editText2;
    Button button2;
    ListView listView5;

    SinonimClass synonim;
    ArrayList<String> findWords;

    public DialogFragment dlg1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synonim_dictionary);

        editText2 = (EditText)findViewById(R.id.editText2);
        button2 = (Button)findViewById(R.id.button2);
        listView5 = (ListView)findViewById(R.id.listView5);
        findWords = new ArrayList<>();


        //Обрабатываем щелчки на элементах ListView:
        listView5.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                String data = findWords.get(position);
                synonim = new SinonimClass(SynonimDictionaryActivity.this);
                ArrayList<String> likeWord = synonim.getLikeWords(data);
                ArrayList<String> clickWordsSynonum = synonim.getAllSynonimsInDictionary(data);


                if(clickWordsSynonum.size()<3) {
                    //data = "Нет синонимов";

                    Toast.makeText(getApplication(),"Синонимы не нейдены",Toast.LENGTH_SHORT).show();

                }
                else {
                    //data = clickWordsSynonum.get(1);



                    dlg1 = new DialogSynonim();
                    dlg1.show(getFragmentManager(), "dlg1");
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putStringArrayList("data", clickWordsSynonum);
                    dlg1.setArguments(args);

                }



            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_synonim_dictionary, menu);
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
            button2.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                String findData = editText2.getText().toString();

                synonim = new SinonimClass(getApplication());
                //Находим id слов, похожих на введенное
                ArrayList<String> likeWord = synonim.getLikeWords(findData);
                findWords = synonim.getAllSynonimsInDictionary(findData);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), String.valueOf(e).toString(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            inputInListView();
            button2.setEnabled(true);
        }
    }


    public void inputInListView()
    {
        listView5.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, findWords));
        listView5.setTextFilterEnabled(true);
    }












}

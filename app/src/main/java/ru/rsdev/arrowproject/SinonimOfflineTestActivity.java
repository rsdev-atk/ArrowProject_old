package ru.rsdev.arrowproject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SinonimOfflineTestActivity extends ActionBarActivity {


    private ListView listView3;
    private ListView listView6;
    private TextView textView5;
    private Button button8;
    private EditText editText3;
    private ArrayList sinonimWords;
    private SinonimClass synonim;
    public String thisWord;

    // �������� ��� ����������� �����������
    int positive;
    int negative;

    // ����� ��������� ��� Map
    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_VALUE = "value";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    ArrayList<String> userVariant = new ArrayList<>();//������ ���������������� �������� ���������

    MyTask mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinonim_offline_test);

        listView3 = (ListView)findViewById(R.id.listView3);
        listView6 = (ListView)findViewById(R.id.listView6);
        textView5 = (TextView)findViewById(R.id.textView5);
        button8 = (Button)findViewById(R.id.button8);
        editText3 = (EditText)findViewById(R.id.editText3);

        //��������� ������� �����������
        negative = getResources().getIdentifier("no_test", "drawable", getPackageName());
        positive = getResources().getIdentifier("ok_test", "drawable", getPackageName());

        sinonimWords = new ArrayList();

        mt = new MyTask();
        mt.execute();

        ResizeList();



    }

    private void ResizeList()
    {
        // ����������� ������ � �������� ��� �������� ���������
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                userVariant.size());
        Map<String, Object> m;
        int img = 0;
        String typeAnswer[] = new String[userVariant.size()];

        for (int i = 0; i < userVariant.size(); i++)
        {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, userVariant.get(i));

            for (int j=0;j<userVariant.size();j++)
            {
                if(userVariant.get(i).equals(sinonimWords.get(j)))
                {
                    typeAnswer[i] = "�����";
                    break;
                }
                else
                {
                    typeAnswer[i] = "������";

            }
            }

            if(typeAnswer[i] == "�����")
            {
                //m.put(ATTRIBUTE_NAME_VALUE, "�����");
                m.put(ATTRIBUTE_NAME_VALUE, "");
            }
            else
            {
                //m.put(ATTRIBUTE_NAME_VALUE, "������");
                m.put(ATTRIBUTE_NAME_VALUE, "");
            }

            //�������� �� ������ � ����� ��������
            if (typeAnswer[i] == "�����")
            {
                img = positive;
            }
            else {
                img = negative;
            }
            m.put(ATTRIBUTE_NAME_IMAGE, img);
            data.add(m);
        }

        // ������ ���� ���������, �� ������� ����� �������� ������
        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_VALUE,
                ATTRIBUTE_NAME_IMAGE };
        // ������ ID View-�����������, � ������� ����� ��������� ������
        int[] to = { R.id.tvText, R.id.tvValue, R.id.ivImg };

        // ������� �������
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data,
                R.layout.item, from, to);

        // ���������� ������ � ����������� ��� �������
        listView6.setAdapter(sAdapter);

    }


    public void ClickAddVariant(View view)
    {
        //��������� ���������������� ������� ��������
        userVariant.add(editText3.getText().toString());
        ResizeList();
        editText3.setText("");


    }

    class MyTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            button8.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                getSynonims();
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
            textView5.setText(thisWord);
            inputInListView();
            button8.setEnabled(true);
        }
    }

    public void getSynonims()
    {
        synonim = new SinonimClass(this);
        String idWord = synonim.getRndWord();
        thisWord = synonim.getMainWord(idWord);

        String[] whereArgsVert = new String[] {idWord};
        sinonimWords=synonim.getAllSynonims(whereArgsVert);
    }


    public void inputInListView()
    {
        listView3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sinonimWords));
        listView3.setTextFilterEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sinonim_offline_test, menu);
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
        mt = new MyTask();
        mt.execute();
        userVariant.clear();
        ResizeList();
    }

    class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public void setViewText(TextView v, String text) {
            // ����� �����-������, ������� ��������� �����
            super.setViewText(v, text);


            // ���� ������ ��� TextView, �� �������������


            if (v.getId() == R.id.tvValue) {
                String value = String.valueOf(text);


                if (value.equals("�����"))
                    v.setTextColor(Color.GREEN);
                else
                    v.setTextColor(Color.RED);
            }

        }

        @Override
        public void setViewImage(ImageView v, int value) {
            // ����� �����-������
            super.setViewImage(v, value);
            // ������������� ImageView
            //if (value == negative) v.setBackgroundColor(Color.RED); else
            //if (value == positive) v.setBackgroundColor(Color.GREEN);
        }
    }


}

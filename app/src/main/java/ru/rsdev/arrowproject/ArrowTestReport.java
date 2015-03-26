package ru.rsdev.arrowproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ArrowTestReport extends ActionBarActivity {

    public ListView listView2;

    public ArrayList myList = new ArrayList();//коллекция ошибок
    public ArrayList goodDataList = new ArrayList();//коллекция верных ответов
    public SQLiteDatabase db;
    private static final String DB_NAME = "sinonim";
    private SQLiteDatabase database;
    int arrowNumber;//Номер ударной буквы

    // имена атрибутов для Map
    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_VALUE = "value";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    // картинки для отображения результатов
    int positive;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_test_report);
        myList.clear();
        goodDataList.clear();
        listView2 = (ListView)findViewById(R.id.listView2);



        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this);
        database = dbOpenHelper.openDataBase();

        getErrorData();


        //Обрабатываем щелчки на элементах ListView:
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {

            }

        });


        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                myList.size());
        Map<String, Object> m;

        for (int i = 0; i < myList.size(); i++)
        {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, String.valueOf(myList.get(i)).toString());
            m.put(ATTRIBUTE_NAME_VALUE, String.valueOf(goodDataList.get(i)).toString());
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_VALUE,
                ATTRIBUTE_NAME_IMAGE };

        // массив ID View-компонентов, в которые будут вставлять данные

        int[] to = { R.id.tvText, R.id.tvValue };

        // создаем адаптер
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data,
                R.layout.item_error_report, from, to);

        // определяем список и присваиваем ему адаптер
        listView2 = (ListView) findViewById(R.id.listView2);
        listView2.setAdapter(sAdapter);

    }

    class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public void setViewText(TextView v, String text) {
            // метод супер-класса, который вставляет текст
            super.setViewText(v, text);


        }

        @Override
        public void setViewImage(ImageView v, int value) {
            // метод супер-класса
            super.setViewImage(v, value);

        }
    }


    //Метод для извлечения всех слов с ошибкой из БД
    public void getErrorData()
    {
        myList.clear();

        //Запрос для вывода данных об ошибках и верных вариантах из связанных таблиц
        String sqlQuery ="SELECT Err._id, Err.errortext, MY.text "
        + "FROM arrowerror as Err "
        + "inner join arrow as MY "
        + "ON Err._id=MY._id";
        Cursor curs = database.rawQuery(sqlQuery, new  String[] {});


        if (curs.moveToFirst())
        {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = curs.getColumnIndex("errortext");
            int idGoodDataindex = curs.getColumnIndex("text");


            do {

                myList.add(curs.getString(nameColIndex));
                String textArrow = (curs.getString(idGoodDataindex));
                //Перенос знака ударения правее от ударной гласной

                char[] word = textArrow.toCharArray();
                for(int i=0;i<word.length;i++)
                {
                    if(word[i]=='`')
                    arrowNumber=i;
                }
                textArrow=textArrow.replaceAll("\\`", "");
                String newTextArrow =textArrow.substring(0,arrowNumber+1) + "'" + textArrow.substring(arrowNumber+1,textArrow.length());

               //String variant = word.substring(0,number) + "'" + word.substring(number,word.length());




                goodDataList.add(newTextArrow);


            } while (curs.moveToNext());
        } else
        {}
        curs.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arrow_test_report, menu);
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
}

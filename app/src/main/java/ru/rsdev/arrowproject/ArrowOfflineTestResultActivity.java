package ru.rsdev.arrowproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ArrowOfflineTestResultActivity extends ActionBarActivity {

    // имена атрибутов для Map
    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_VALUE = "value";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    // картинки для отображения результатов
    int positive;
    int negative;
    ListView lvSimple;
    int errorCount;//Количество ошибок
    TextView textView7;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_offline_test_result);

        Intent intent = getIntent();
        ArrayList<String> userAnswer = intent.getStringArrayListExtra("userAnswer");
        final ArrayList<String> correctAnswer = intent.getStringArrayListExtra("correctAnswer");
        boolean typeAnswer[] = intent.getBooleanArrayExtra("typeAnswer");

        //Извлекаем ресурсы изображений
        negative = getResources().getIdentifier("no_test", "drawable", getPackageName());
        positive = getResources().getIdentifier("ok_test", "drawable", getPackageName());

        textView7 = (TextView)findViewById(R.id.textView7);



        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                typeAnswer.length);
        Map<String, Object> m;
        int img = 0;

        for (int i = 0; i < typeAnswer.length; i++)
        {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, String.valueOf(userAnswer.get(i)).toString());
            if(typeAnswer[i] == true)
            {
                //m.put(ATTRIBUTE_NAME_VALUE, "Верно");
                m.put(ATTRIBUTE_NAME_VALUE, "");
            }
            else
            {
                //m.put(ATTRIBUTE_NAME_VALUE, "Ошибка");
                m.put(ATTRIBUTE_NAME_VALUE, "");
                //Вычмсляем количество ошибок
                errorCount++;
            }

            if (typeAnswer[i] == false)
            {
                img = negative;
            }
            else {
                img = positive;
            }
            m.put(ATTRIBUTE_NAME_IMAGE, img);
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_VALUE,
                ATTRIBUTE_NAME_IMAGE };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tvText, R.id.tvValue, R.id.ivImg };

        // создаем адаптер
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data,
                R.layout.item, from, to);

        // определяем список и присваиваем ему адаптер
        lvSimple = (ListView) findViewById(R.id.lvSimple);
        lvSimple.setAdapter(sAdapter);

        //Обрабатываем щелчки на элементах ListView:
        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                Toast.makeText(getApplicationContext(),"Верный ответ " + correctAnswer.get(position),Toast.LENGTH_SHORT).show();
            }

    });

        textView7.setText(String.valueOf(errorCount).toString());

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


            // если нужный нам TextView, то разрисовываем


            if (v.getId() == R.id.tvValue) {
                String value = String.valueOf(text);


                if (value.equals("Верно"))
                v.setTextColor(Color.GREEN);
                else
                v.setTextColor(Color.RED);
            }

        }

        @Override
        public void setViewImage(ImageView v, int value) {
            // метод супер-класса
            super.setViewImage(v, value);
            // разрисовываем ImageView
            if (value == negative) v.setBackgroundColor(Color.RED); else
            if (value == positive) v.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arrow_offline_test_result, menu);
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

    public void clickButton(View view) {
        finish();
    }
}

package ru.rsdev.arrowproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ArrowOffilineTestActivity extends ActionBarActivity {

    Button button,button2,button3;
    public int testCount=0;//Текущий номер вопроса в тесте
    public ListView listView;
    public TextView textView;
    public TextView textView8;
    public TextView textView9;

    int countArrowOfflineTest = 10;//количество вопросов в тесте
    ArrayList<String> variantAnswer= new ArrayList<String>();//Коллекция вариантов ответов
    ArrayList<String> userAnswer= new ArrayList<String>();//Коллекция номеров ответов пользователя
    ArrayList<String> correctAnswer = new ArrayList<String>();//Коллекция номеров верных ответов
    boolean typeAnswer[] = new boolean[countArrowOfflineTest];//Массив типов ответов(верно или нет)


    public ArrowClass myClass;
    private MyCountDownTimer mCountDownTimer;
    private static final String DB_NAME = "sinonim";

    MyTask mt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_offiline_test);

        button = (Button)findViewById(R.id.button);

        textView = (TextView)findViewById(R.id.textView);
        textView8 = (TextView)findViewById(R.id.textView8);
        textView9 = (TextView)findViewById(R.id.textView9);
        listView = (ListView)findViewById(R.id.listView);

        mCountDownTimer = new MyCountDownTimer(10000, 1000);
        mCountDownTimer.start();
    }

    //Метод, для возобновления работы Activity
    protected void onResume()
    {



        variantAnswer.clear();
        userAnswer.clear();
        correctAnswer.clear();

        //goAllMetodForTest();
        mt = new MyTask();
        mt.execute();

        //Обрабатываем щелчки на элементах ListView:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                int realTestCount = testCount - 1;
                try {

                    userAnswer.add(variantAnswer.get(position));//Записываем ответы пользователя
                    correctAnswer.add(myClass.getCorrectAnswer());//Записываем верные ответы

                    //Проверка на совпадение, т.е. верный ответ или нет
                    if (correctAnswer.get(realTestCount).equals(userAnswer.get(realTestCount))) {

                        //myClass.inputErrorInDB(myClass.getNumberToDB(),variantAnswer.get(position));
                        typeAnswer[realTestCount] = true;

                        Toast.makeText(getApplicationContext(), "Верно", Toast.LENGTH_SHORT).show();
                    } else {
                        typeAnswer[realTestCount] = false;

                        //Вызываем метод для записи ошибки в БД
                        myClass.inputErrorInDB(myClass.getNumberToDB(), (userAnswer.get(realTestCount)));
                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Ошибка при нажатии", Toast.LENGTH_SHORT).show();
                }
               // goAllMetodForTest();
                mt = new MyTask();
                mt.execute();
            }
        });

        super.onResume();
    }


    public void inputInListView()
    {
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.my_list_item,variantAnswer));
        listView.setTextFilterEnabled(true);
    }

    class MyTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                if(testCount<countArrowOfflineTest)
                {

                    myClass = new ArrowClass(getApplication(), DB_NAME);
                    String word = myClass.getWordInDB();
                    variantAnswer = myClass.detectArrowInWord(word);
                    testCount++;
                }
                else
                {
                    goToResult();
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
            textView8.setText("Слово №" + testCount + " из " + countArrowOfflineTest);
            inputInListView();
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            textView9.setText("Время закончилось");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            textView9.setText((String.valueOf(millisUntilFinished / 1000)));
        }
    }

    //Запускаем все методы для тестирования
    public void goAllMetodForTest()
    {

        if(testCount<countArrowOfflineTest)
        {

            myClass = new ArrowClass(this, DB_NAME);
            String word = myClass.getWordInDB();
            variantAnswer = myClass.detectArrowInWord(word);
            inputInListView();

            testCount++;
        }
        else
        {
            goToResult();
        }

    }

    //Завершение тестирования и переход на Activity с результатами
    public void goToResult()
    {
        //Открываем активити с результатами
        Intent intent = new Intent(this,ArrowOfflineTestResultActivity.class);
        //Передача данных в другой Activity
        intent.putExtra("userAnswer",userAnswer);
        intent.putExtra("correctAnswer",correctAnswer);
        intent.putExtra("typeAnswer",typeAnswer);

        startActivity(intent);
        testCount=0;

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arrow_offiline_test, menu);
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

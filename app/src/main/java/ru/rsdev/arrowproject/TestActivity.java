package ru.rsdev.arrowproject;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TestActivity extends ActionBarActivity
{

    Button button4;
    private MyCountDownTimer mCountDownTimer;
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        button4 = (Button)findViewById(R.id.button4);
        textView2 = (TextView)findViewById(R.id.textView2);

        mCountDownTimer = new MyCountDownTimer(10000, 1000);
        mCountDownTimer.start();

    }


    public class MyCountDownTimer extends CountDownTimer
    {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Toast.makeText(getApplication(),"Конец", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            textView2.setText((String.valueOf(millisUntilFinished / 1000)));
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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


    }







}

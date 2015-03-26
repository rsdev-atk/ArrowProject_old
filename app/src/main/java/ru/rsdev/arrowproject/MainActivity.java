package ru.rsdev.arrowproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    final String LOG_TAG = "myLogs";

    ExpandableListView expandableListView;
    AdapterHelper ah;
    SimpleExpandableListAdapter adapter;
    TextView textView;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);



        // создаем адаптер
        ah = new AdapterHelper(this);
        adapter = ah.getAdapter();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setAdapter(adapter);

        // нажатие на элемент
        expandableListView.setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition,   int childPosition, long id)
            {
                Log.d(LOG_TAG, "onChildClick groupPosition = " + groupPosition +
                        " childPosition = " + childPosition +
                        " id = " + id);

                if (groupPosition == 0 && childPosition == 0 ) {
                    Intent intentArrowTest = new Intent(MainActivity.this, ArrowOffilineTestActivity.class);
                    startActivity(intentArrowTest);

                }
                else if(groupPosition == 0 && childPosition == 1)
                {
                    //Нажат 2й пункт меню
                }

                else if(groupPosition == 0 && childPosition == 2)
                {
                    //Ударения, словарь
                    Intent arrowDictionary = new Intent(MainActivity.this,ArrowDictionaryActivity.class);
                    startActivity(arrowDictionary);

                }

                else if(groupPosition == 0 && childPosition == 3)
                {
                    //Нажат 4й пункт меню
                }

                else if(groupPosition == 0 && childPosition == 4)
                {
                    //Работа над ошибками
                    Intent intentRNO = new Intent(MainActivity.this,ArrowTestReport.class);
                    startActivity(intentRNO);
                }

                else if(groupPosition == 0 && childPosition == 4)
                {
                    //Нажат 5й пункт меню
                }

                else if(groupPosition == 1 && childPosition == 0)
                {
                    //Синонимы оффлайн
                    Intent sinonimOffline = new Intent(MainActivity.this,SinonimOfflineTestActivity.class);
                    startActivity(sinonimOffline);
                }

                else if(groupPosition == 1 && childPosition == 2)
                {
                    //Синонимы словарь
                    Intent synonimDictionary = new Intent(MainActivity.this,SynonimDictionaryActivity.class);
                    startActivity(synonimDictionary);
                }

                else if(groupPosition == 2 && childPosition == 0)
                {
                    //Activity для тестирования
                    Intent intentArrowTest = new Intent(MainActivity.this, TestActivity.class);
                    startActivity(intentArrowTest);
                }

                return false;
            }
        });

        // нажатие на группу
        expandableListView.setOnGroupClickListener(new OnGroupClickListener()
        {
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id)
            {
                Log.d(LOG_TAG, "onGroupClick groupPosition = " + groupPosition +
                        " id = " + id);
                return false;
            }
        });

        // сворачивание группы
        expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
                Log.d(LOG_TAG, "onGroupCollapse groupPosition = " + groupPosition);
                //textView.setText("Свернули " + ah.getGroupText(groupPosition));
            }
        });

        // разворачивание группы
        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                Log.d(LOG_TAG, "onGroupExpand groupPosition = " + groupPosition);
                //textView.setText("Равзвернули " + ah.getGroupText(groupPosition));
            }
        });


        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

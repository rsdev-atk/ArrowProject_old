package ru.rsdev.arrowproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rsdev on 12.02.2015.
 */
public class ArrowClass
{
    //Константы для БД
    private static String TABLE_NAME_FOR_ARROW = "arrow";
    private static String FIELD1_FOR_ARROW = "text";

    String ERROR_TABLE="arrowerror";
    String ERROR_VALUE_ID ="_id" ;
    String ERROR_VALUE_TEXT="errortext";

    private static final String DB_NAME = "sinonim";


    private SQLiteDatabase database;
    SQLiteDatabase db;
    public String wordInDB;
    ArrayList<String> variantList;//

    char[] Glas = {'а','е','ё','и','о','у','я','э','ы','ю'};//Массив гласных букв

    public int goodAnswer;//Номер правильного ответа
    Random random = new Random();
    public int randNumberBD;
    int arrowNumber;//Номер ударной буквы для переноса ударения


    public ArrowClass(Context context, String databaseName)
    {
        //Подключаемся к общей БД
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(context);
        database = dbOpenHelper.openDataBase();
    }



    //Извлечение из бд слова по случайному значению
    public String getWordInDB()
    {
        //Cursor curs = db.query(TABLE_NAME_FOR_ARROW, null, null, null, null, null, null);
        Cursor curs = database.query(TABLE_NAME_FOR_ARROW, null, null, null, null, null, null);
        //Отбор случайного значения из БД
        int countDB = curs.getCount();

        randNumberBD = random.nextInt(countDB) + 1;

        String selection = "_id = ?";
        String selectionArgs[] = new String[]{String.valueOf(randNumberBD).toString()};
        curs=null;
        curs = database.query(TABLE_NAME_FOR_ARROW,null,selection,selectionArgs,null,null,null);

        if (curs.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = curs.getColumnIndex(FIELD1_FOR_ARROW);
            do {
               wordInDB = curs.getString(nameColIndex);
            }
            while (curs.moveToNext());
        } else {}
        curs.close();

        return wordInDB;
    }

    public ArrayList<String> getWordInDBtoDictionary(String word)
    {
        ArrayList<String> dictionaryArray = new ArrayList<String>();
        //String selection = "text >= " + word + " AND text < ?";
        //String selectionArgs[] = {word+"яяя"};

        

        //Старый запрос выглядит так: SELECT * FROM arrow WHERE text >= 'ма' AND word < 'мамаяяя'
        //Реализовано по причине проблем LIKE в SQLite с русскими символами и возможным уменьшением времени запроса
        //Cursor curs = database.query(TABLE_NAME_FOR_ARROW,null,selection,selectionArgs,null,null,null);

        //SELECT * FROM ARROW WHERE text LIKE 'мам%'

        String query = "SELECT * FROM ARROW WHERE text LIKE '" + word + "%'";

        //String query = "SELECT * FROM ARROW WHERE text >= '" + word + "' AND text < '" + word + "яяя'";
        Cursor curs = database.rawQuery(query,null);

        if (curs.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = curs.getColumnIndex(FIELD1_FOR_ARROW);
            do {
                //Удаляем знаки ударения
                String textArrow = (curs.getString(nameColIndex));
                char[] word2 = textArrow.toCharArray();
                for(int i=0;i<word2.length;i++)
                {
                    if(word2[i]=='`')
                        arrowNumber=i;
                }
                textArrow=textArrow.replaceAll("\\`", "");
                String newTextArrow =textArrow.substring(0,arrowNumber+1) + "'" + textArrow.substring(arrowNumber+1,textArrow.length());


                dictionaryArray.add(newTextArrow);
            }
            while (curs.moveToNext());
        } else {}
        curs.close();

        return dictionaryArray;
    }


    //Разбор слова на символы и определение гласных и ударного слога и формирование ответов
    public ArrayList<String> detectArrowInWord(String word)
    {
        char[] buf = word.toCharArray();
        int arrwPlays;//Номер ударного символа

        int counGlas=0;//Количество гласных в слове
        ArrayList listArrow = new ArrayList();//Массив позиций гласных
        variantList = new ArrayList<String>();
        goodAnswer=0;//номер ударной гласной

        for (int i = 0;i<buf.length;i++)
        {
            if(buf[i] == '`')
            {
                arrwPlays = i+1;
                goodAnswer=counGlas;
            }
            else
            {
                for (int j=0;j<Glas.length;j++)
                {
                    if(buf[i]==Glas[j])
                    {
                        counGlas++;
                        //Смещаем значение на 1 символ правее, чтобы знак ударения стоял правее ударной гласной
                        //listArrow.add(i+1);
                        listArrow.add(i);
                    }
                }
            }
        }

        //Формируем варианты ответов
        for (int i=0;i<listArrow.size();i++)
        {
            int number = Integer.parseInt(listArrow.get(i).toString())+1;
            String variant = word.substring(0,number) + "'" + word.substring(number,word.length());
            //Регулярным выражением удаляем все старые символы ударения
            variantList.add(variant.replaceAll("\\`", ""));
        }


        return variantList;
        }


    public String getCorrectAnswer()
    {
        return variantList.get(goodAnswer);
    }

    //Получение верного ответа
    public int getGoodAnswer()
    {
        return goodAnswer;
    }

    public int getNumberToDB()
    {
        return randNumberBD;
    }

    public void inputErrorInDB(int id, String errorText)
    {
        ContentValues cv = new ContentValues();
        cv.put(ERROR_VALUE_ID, id);
        cv.put(ERROR_VALUE_TEXT, errorText);
        database.insert(ERROR_TABLE, null, cv);
    }



}

package ru.rsdev.arrowproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rsdev on 19.02.2015.
 */
public class SinonimClass
{
    private static final String DB_NAME = "sinonim";
    //Хорошей практикой является задание имен полей БД константами
    private static final String TABLE_NAME = "words";
    private static final String WORD_ID = "_id";
    private static final String WORD_NAME = "word";
    int countSinonim = 13136;//Количество слов в таблице синонимов
    String  mainWordInDB;
    String numberWord;


    private SQLiteDatabase database;
    ArrayList<String> sinonimWords = new ArrayList<String>();//Хранилище найденных слов



    public SinonimClass(Context context)
    {
        //Подключаемся к общей БД
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(context);
        database = dbOpenHelper.openDataBase();
    }

    //Получаем ID случайного слова
    public String getRndWord()
    {
        Random rand = new Random();
        int rndInDB = rand.nextInt(countSinonim)+1;

//Определяем случайное слово из таблицы возможных значений
        String sqlQuery2 = "SELECT number FROM synonymsCount WHERE _id=" + String.valueOf(rndInDB).toString();
        Cursor mainWord2 = database.rawQuery(sqlQuery2, new  String[] {});


        if (mainWord2.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = mainWord2.getColumnIndex("number");
            do {
                numberWord = mainWord2.getString(nameColIndex);//Строка с номером слова из БД
            } while (mainWord2.moveToNext());
        } else
        {}
        mainWord2.close();
        return numberWord;
    }

    public String getMainWord(String word)
    {
        //Извлекаем слово по рандомному идентификатору
        String sqlQuery = "SELECT word FROM WORDS WHERE _id=" + word;
        Cursor mainWord = database.rawQuery(sqlQuery, new  String[] {});

        if (mainWord.moveToFirst())
        {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = mainWord.getColumnIndex(WORD_NAME);
            do {

                String textMainWord = mainWord.getString(0);
                mainWordInDB="Слово: " + mainWord.getString(nameColIndex);
            } while (mainWord.moveToNext());
        } else
        {}
        mainWord.close();
        return mainWordInDB;
    }

    public ArrayList<String> getAllSynonims(String[] wordArgs)
    {
        String[] vert = new String[] {"word"};

        Cursor friendCursor = database.query(TABLE_NAME, vert, "_id IN(SELECT s_id FROM synonyms WHERE w_id=?)", wordArgs, null, null, null);
        friendCursor.moveToFirst();
        if(!friendCursor.isAfterLast()) {
            do {
                String name = friendCursor.getString(0);
                sinonimWords.add(name);
            } while (friendCursor.moveToNext());
        }
        friendCursor.close();
        return sinonimWords;
    }

    public ArrayList<String> getAllSynonimsInDictionary(String word)
    {
        String[] vert = new String[] {"word"};

        //String sqlQuery = "SELECT _id FROM WORDS WHERE word LIKE '" + findWord + "%'";
        //Cursor friendCursor = database.query(TABLE_NAME, vert, "_id IN(?)", wordArgs, null, null, null);
        Cursor friendCursor = database.query(TABLE_NAME, vert, "_id IN(SELECT _id FROM WORDS WHERE word LIKE '" + word +"%')", null, null, null, null);
        friendCursor.moveToFirst();
        if(!friendCursor.isAfterLast()) {
            do {
                String name = friendCursor.getString(0);
                sinonimWords.add(name);
            } while (friendCursor.moveToNext());
        }
        friendCursor.close();
        return sinonimWords;
    }


    public ArrayList<String> getLikeWords(String findWord)
    {
        ArrayList<String> likeWord = new ArrayList<>();
        //Извлекаем слово по рандомному идентификатору
        String sqlQuery = "SELECT _id FROM WORDS WHERE word LIKE '" + findWord + "%'";
        Cursor mainWord = database.rawQuery(sqlQuery, new  String[] {});

        if (mainWord.moveToFirst())
        {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = mainWord.getColumnIndex(WORD_NAME);
            do {

                likeWord.add(mainWord.getString(0));

            } while (mainWord.moveToNext());
        } else
        {}
        mainWord.close();
        return likeWord;
    }


}

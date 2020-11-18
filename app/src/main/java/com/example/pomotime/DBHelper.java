package com.example.pomotime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String CATEGORY_TABLE = "CATEGORY_TABLE";
    public static final String COLUMN_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String TODO_TABLE = "TODO_TABLE";
    public static final String CURRENTLY_WORKING = "CURRENTLY_WORKING";
    public static final String CURRENTLY_WORKING_ID = "ID";
    public static final String CURRENTLY_WORKING_TODO = "TODO";
    public static final String COLUMN_TODO_TITLE = "TODO_TITLE";
    public static final String COLUMN_TODO_CATEGORY = "TODO_CATEGORY";
    public static final String COLUMN_CATEGORY_ID = "ID";
    public static final String COLUMN_TODO_ID = "ID";

    public DBHelper(@Nullable Context context) {
        super(context, "pomo.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE + "(" + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CATEGORY_NAME + " TEXT)";
        String createTableStatement2 = "CREATE TABLE IF NOT EXISTS " + TODO_TABLE + " (" + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TODO_TITLE + " TEXT, " + COLUMN_TODO_CATEGORY + " TEXT);";
        db.execSQL(createTableStatement);
        db.execSQL(createTableStatement2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CURRENTLY_WORKING);
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + CURRENTLY_WORKING + " (" + CURRENTLY_WORKING_ID + " INTEGER PRIMARY KEY, " + CURRENTLY_WORKING_TODO + " TEXT);";
        db.execSQL(createTableStatement);
    }

    public boolean insertCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY_NAME, category.getName());

        long insert = db.insert(CATEGORY_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        else {

            return true;
        }
    }

    public List<String> getAllCategories(){
        List<String> categories = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + CATEGORY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                categories.add(cursor.getString(1));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }

    public boolean insertTodo(ListItem todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TODO_TITLE, todo.getTitle());
        cv.put(COLUMN_TODO_CATEGORY, todo.getCategory());

        long insert = db.insert(TODO_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        else {

            return true;
        }
    }

    public List<ListItem> getAllTodos(){

        List<ListItem> listitems = new ArrayList<ListItem>();

        String queryString = "SELECT * FROM " + TODO_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int todoId = cursor.getInt(0);
                String todoTitle = cursor.getString(1);
                String todoCategory = cursor.getString(2);
                ListItem newitem = new ListItem(todoId, todoTitle, todoCategory);
                listitems.add(newitem);
            }while(cursor.moveToNext());
        }
        else{

        }

        cursor.close();
        db.close();
        return listitems;
    }

    public boolean deleteTodo(ListItem todoItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TODO_TABLE + " WHERE " + COLUMN_TODO_ID + " = " + todoItem.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public List<ListItem> getFilteredTodos(String category){

        List<ListItem> listitems = new ArrayList<ListItem>();

        String queryString = "SELECT * FROM " + TODO_TABLE + " WHERE " + COLUMN_TODO_CATEGORY + " = " +" '"+ category + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int todoId = cursor.getInt(0);
                String todoTitle = cursor.getString(1);
                String todoCategory = cursor.getString(2);
                ListItem newitem = new ListItem(todoId, todoTitle, todoCategory);
                listitems.add(newitem);
            }while(cursor.moveToNext());
        }
        else{

        }

        cursor.close();
        db.close();
        return listitems;
    }

    public boolean insertCurrentlyWorking(CurrentlyWorkingTodo currentlyWorking){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CURRENTLY_WORKING_TODO, currentlyWorking.getTodo());

        long insert = db.insert(CURRENTLY_WORKING, null, cv);
        if(insert == -1){
            return false;
        }
        else {

            return true;
        }
    }
    public String getCurrentlyWorking(){
        String todo = "";

        String selectQuery = "SELECT * FROM " + CURRENTLY_WORKING;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            todo = cursor.getString(1);
        }

        cursor.close();
        db.close();

        return todo;
    }
    public boolean deleteCurrentlyWorking(){

        String queryString = "DELETE FROM " + CURRENTLY_WORKING;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isCurrentlyWorking(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, CURRENTLY_WORKING) == 0;
    }
}

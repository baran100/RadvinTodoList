package com.radvin_app.radvintodolist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TodoDatabase extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteHelper";

    //TASK
    private static final String TABLE_TASKS = "tbl_tasks";
    public static final String TASKS_CONTACTS_COLUMN_ID = "id";
    public static final String TASKS_CONTACTS_COLUMN_TITLE = "title";
    public static final String TASKS_CONTACTS_COLUMN_COMPLETED = "completed";
    public static final String TASKS_CONTACTS_COLUMN_ID_F = "id_category";
    public static final String TASKS_CONTACTS_COLUMN_PERSIAN_DATE = "persian_date";
    public static final String TASKS_CONTACTS_COLUMN_NOTE = "note";
    public static final String TASKS_CONTACTS_COLUMN_PRIORITY = "priority";

    //CATEGORY
    private static final String TABLE_CATEGORIES = "tbl_categories";
    public static final String CATEGORIES_CONTACTS_COLUMN_NAME = "name";
    public static final String CATEGORIES_CONTACTS_COLUMN_ID = "id";

    public TodoDatabase(@Nullable Context context) {
        super(context, "DB_APP", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            db.execSQL("CREATE TABLE " + TABLE_TASKS + "("
                    + TASKS_CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TASKS_CONTACTS_COLUMN_TITLE + " TEXT,"
                    + TASKS_CONTACTS_COLUMN_COMPLETED + " BOOLEAN,"
                    + TASKS_CONTACTS_COLUMN_ID_F + " INTEGER REFERENCES " + TABLE_CATEGORIES +" ( " + CATEGORIES_CONTACTS_COLUMN_ID +" ) ON DELETE CASCADE,"
                    + TASKS_CONTACTS_COLUMN_PERSIAN_DATE + " DATE,"
                    + TASKS_CONTACTS_COLUMN_NOTE + " TEXT( 150 ),"
                    + TASKS_CONTACTS_COLUMN_PRIORITY + " INTEGER"
                    + ");");

            db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                    + CATEGORIES_CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CATEGORIES_CONTACTS_COLUMN_NAME + " TEXT" + ");");

            db.execSQL("INSERT INTO " + TABLE_CATEGORIES +" (" + CATEGORIES_CONTACTS_COLUMN_NAME +") VALUES ('خانه')");
            db.execSQL("INSERT INTO " + TABLE_CATEGORIES +" (" + CATEGORIES_CONTACTS_COLUMN_NAME +") VALUES ('کار')");
            db.execSQL("INSERT INTO " + TABLE_CATEGORIES +" (" + CATEGORIES_CONTACTS_COLUMN_NAME +") VALUES ('مسافرت')");

        } catch (SQLiteException e){
            Log.e(TAG, "onCreate: " + e.toString() );
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public long addCategory(Category category){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORIES_CONTACTS_COLUMN_NAME,category.getName());
        long result = sqLiteDatabase.insert(TABLE_CATEGORIES, null,contentValues);
        sqLiteDatabase.close();
        return result;
    }

    public long addTask(Task task){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_CONTACTS_COLUMN_TITLE,task.getTitle());
        contentValues.put(TASKS_CONTACTS_COLUMN_COMPLETED,task.isCompleted());
        contentValues.put(TASKS_CONTACTS_COLUMN_PERSIAN_DATE,task.getPersianDate());
        contentValues.put(TASKS_CONTACTS_COLUMN_ID_F,task.getIdCategory());
        contentValues.put(TASKS_CONTACTS_COLUMN_NOTE,task.getNote());
        contentValues.put(TASKS_CONTACTS_COLUMN_PRIORITY,task.getPriority());

        long result = sqLiteDatabase.insert(TABLE_TASKS,null,contentValues);
        sqLiteDatabase.close();
        return result;
    }


    public List<Category> getCategories(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +TABLE_CATEGORIES, null);
        List<Category>categoryList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Category category = new Category();
                category.setId(cursor.getLong(0));
                category.setName(cursor.getString(1));
                categoryList.add(category);
            } while (cursor.moveToNext());

        }
        sqLiteDatabase.close();
        return categoryList;
    }

    public String getCategory(long getID) // you should pass getID from your Class
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String get_Category_Name = "0";
        String selectQuery = "SELECT " + CATEGORIES_CONTACTS_COLUMN_NAME +" FROM "+ TABLE_CATEGORIES + " WHERE " + CATEGORIES_CONTACTS_COLUMN_ID + " = '"+ getID +"'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()) {
            get_Category_Name = c.getString(0); // Return 1 selected result
        }
        db.close();
        return get_Category_Name;
    }

    public List<Task> getTasks(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +  TABLE_TASKS +"  ORDER BY " +" julianday( " + CATEGORIES_CONTACTS_COLUMN_ID +" ) " +"  ASC ", null);
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Task task = new Task();
                task.setId(cursor.getLong(0));
                task.setTitle(cursor.getString(1));
                task.setCompleted(cursor.getInt(2)==1);
                task.setIdCategory(cursor.getInt(3));
                task.setPersianDate(cursor.getString(4));
                task.setNote(cursor.getString(5));
                task.setPriority(cursor.getInt(6));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return tasks;
    }
    public List<Task> getTasksById(long getID){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +  TABLE_TASKS + " WHERE " + TASKS_CONTACTS_COLUMN_ID_F + " = '"+ getID +"'" +"  ORDER BY " +" julianday( " + CATEGORIES_CONTACTS_COLUMN_ID +" ) " +"  ASC ", null);
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Task task = new Task();
                task.setId(cursor.getLong(0));
                task.setTitle(cursor.getString(1));
                task.setCompleted(cursor.getInt(2)==1);
                task.setIdCategory(cursor.getInt(3));
                task.setPersianDate(cursor.getString(4));
                task.setNote(cursor.getString(5));
                task.setPriority(cursor.getInt(6));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return tasks;
    }
    public int updateTask(Task task){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_CONTACTS_COLUMN_TITLE,task.getTitle());
        contentValues.put(TASKS_CONTACTS_COLUMN_COMPLETED,task.isCompleted());
        contentValues.put(TASKS_CONTACTS_COLUMN_PERSIAN_DATE,task.getPersianDate());
        contentValues.put(TASKS_CONTACTS_COLUMN_ID_F,task.getIdCategory());
        contentValues.put(TASKS_CONTACTS_COLUMN_NOTE,task.getNote());
        int result = sqLiteDatabase.update(TABLE_TASKS,contentValues,"ID = ?",new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
        return result;
    }

    public int updateCategory(Category category){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORIES_CONTACTS_COLUMN_NAME,category.getName());
        int result = sqLiteDatabase.update(TABLE_CATEGORIES,contentValues,"ID = ?",new String[]{String.valueOf(category.getId())});
        sqLiteDatabase.close();
        return result;
    }

    public int deleteTask(Task task){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        int result = sqLiteDatabase.delete(TABLE_TASKS,"ID = ?",new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
        return result;
    }
    
    public int deleteCategory(Category category){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_CATEGORIES,"ID = ?",new String[]{String.valueOf(category.getId())});
        sqLiteDatabase.close();
        return result;
    }

    public void clearAllTask(){
        try {
            SQLiteDatabase sqLiteDatabase= getWritableDatabase();
            sqLiteDatabase.execSQL("DELETE FROM " +TABLE_TASKS);
        }catch (SQLiteException e){
            Log.i(TAG, "clearAllTask: ");
        }
    }

    public List<Task> searchTask(String query){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_TASKS+ " WHERE " +TASKS_CONTACTS_COLUMN_TITLE +" LIKE '%"+query+"%'",null);
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Task task = new Task();
                task.setId(cursor.getLong(0));
                task.setTitle(cursor.getString(1));
                task.setCompleted(cursor.getInt(2)==1);
                task.setIdCategory(cursor.getInt(3));
                task.setPersianDate(cursor.getString(4));
                task.setNote(cursor.getString(5));
                task.setPriority(cursor.getInt(6));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return tasks;
    }

    public List<Category> searchCategories(String query){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_CATEGORIES+ " WHERE " + CATEGORIES_CONTACTS_COLUMN_NAME +" LIKE '%"+query+"%'",null);
        List<Category>categoryList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Category category = new Category();
                category.setId(cursor.getLong(0));
                category.setName(cursor.getString(1));
                categoryList.add(category);
            } while (cursor.moveToNext());

        }
        sqLiteDatabase.close();
        return categoryList;
    }

    public List<Task> getTasksByDate(String fromDate , String toDate){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TASKS+ " WHERE " +TASKS_CONTACTS_COLUMN_PERSIAN_DATE + " between '" + fromDate + "' and '" + toDate + "'" , null);
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Task task = new Task();
                task.setId(cursor.getLong(0));
                task.setTitle(cursor.getString(1));
                task.setCompleted(cursor.getInt(2)==1);
                task.setIdCategory(cursor.getInt(3));
                task.setPersianDate(cursor.getString(4));
                task.setNote(cursor.getString(5));
                task.setPriority(cursor.getInt(6));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return tasks;
    }
}

package com.bridgelabz.todoo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bridgelabz.todoo.addnote.interactor.AddNoteInteractorInteface;
import com.bridgelabz.todoo.home.interactor.ToDoActivityInteractor;
import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.home.presenter.ToDoActivityPresenter;
import com.bridgelabz.todoo.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper
{
    private String TAG = "RegistrationDatabase";
    private static final int DATABASE_VERSION = 1;
    private ToDoActivityInteractor todoActivityInteractor;
    private ToDoActivityPresenter toDoActivityPresenter;
    AddNoteInteractorInteface mAddNoteInteractoreInteface;

    public DatabaseHandler(Context context, ToDoActivityInteractor todoActivityInteractor)
    {
        super(context, Constants.RequestParam.DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.todoActivityInteractor = todoActivityInteractor;
    }


    public DatabaseHandler(Context context)
    {
        super(context, Constants.RequestParam.DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }


    public DatabaseHandler(Context context, ToDoActivityPresenter toDoActivityPresenter)
    {
        super(context, Constants.RequestParam.DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.toDoActivityPresenter = toDoActivityPresenter;
    }

    public DatabaseHandler(Context mContext, AddNoteInteractorInteface addNoteInteractoreInteface)
    {
        super(mContext, Constants.RequestParam.DATABASE_NAME, null, DATABASE_VERSION);
        this.mAddNoteInteractoreInteface =addNoteInteractoreInteface;
    }

    // Creating Tables  
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT," + Constants.RequestParam.KEY_STARTDATE + " TEXT," + Constants.RequestParam.KEY_ARCHIVE + " TEXT," + Constants.RequestParam.KEY_SETTIME + " TEXT" + ")";
        db.execSQL(CREATE_ToDoS_TABLE);

        String CREATE_LOCAL_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT," + Constants.RequestParam.KEY_STARTDATE + " TEXT," + Constants.RequestParam.KEY_ARCHIVE + " TEXT," + Constants.RequestParam.KEY_SETTIME + " TEXT" + ")";
        db.execSQL(CREATE_LOCAL_ToDoS_TABLE);

        String CREATE_TRASH_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.TRASH_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT," + Constants.RequestParam.KEY_STARTDATE + " TEXT," + Constants.RequestParam.KEY_ARCHIVE + " TEXT," + Constants.RequestParam.KEY_SETTIME + " TEXT" + ")";
        db.execSQL(CREATE_TRASH_TABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME);
        //drop local database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.TRASH_TABLE_NAME);
        // Create tables again  
        onCreate(db);
    }

    public void dropLocaltable(SQLiteDatabase db)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME);

        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT," + Constants.RequestParam.KEY_STARTDATE + " TEXT," + Constants.RequestParam.KEY_ARCHIVE + " TEXT," + Constants.RequestParam.KEY_SETTIME + " TEXT" + ")";
        db.execSQL(CREATE_ToDoS_TABLE);

    }

    // code to add the new ToDo  
    public void addToDo(ToDoItemModel toDoItemModel)
    {
        SQLiteDatabase db = null;
        try
        {
            db = this.getWritableDatabase();

            Log.i(TAG, "addToDo: start");
            ContentValues values = new ContentValues();
            values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.getTitle()); // ToDo Name
            values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.getNote()); // ToDo Phone
            values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.getReminder()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.getStartdate()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.getArchive());
            values.put(Constants.RequestParam.KEY_SETTIME, toDoItemModel.getSettime());
            // Inserting Row

            db.insert(Constants.RequestParam.NOTES_TABLE_NAME, null, values);

            Log.i(TAG, "addToDo: success");
            todoActivityInteractor.getResponce(true);

            //2nd argument is String containing nullColumnHack

        }
        catch (Exception e)
        {
            todoActivityInteractor.getResponce(false);
            Log.i(TAG, "addToDo: " + e);
        }
        finally
        {
            db.close(); // Closing database connection
        }
    }


    //save to local database when network is not present
    public void addNoteToLocal(ToDoItemModel toDoItemModel)
    {
        SQLiteDatabase db = null;
        try
         {
             db = this.getWritableDatabase();
             Log.i(TAG, "addToDo: start");
             ContentValues values = new ContentValues();
             values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.getTitle()); // ToDo Name
             values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.getNote()); // ToDo Phone
             values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.getReminder()); // ToDo REMINDER
             values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.getStartdate()); // ToDo REMINDER
             values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.getArchive());
             values.put(Constants.RequestParam.KEY_SETTIME, toDoItemModel.getSettime());
            // Inserting Row

            db.insert(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, null, values);
            Log.i(TAG, "addToDo: success");
            mAddNoteInteractoreInteface.getResponce(true);
            //2nd argument is String containing nullColumnHack

        }
        catch (Exception e)
        {
            mAddNoteInteractoreInteface.getResponce(false);
            Log.i(TAG, "addToDo: " + e);
        }
        finally
        {
            db.close(); // Closing database connection
        }
    }


    //save to local database when network is not present
    public void addNoteToTrash(ToDoItemModel toDoItemModel)
    {
        SQLiteDatabase db = null;
        try
        {
            db = this.getWritableDatabase();
            Log.i(TAG, "addToDo: start");
            ContentValues values = new ContentValues();
            values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.getTitle()); // ToDo Name
            values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.getNote()); // ToDo Phone
            values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.getReminder()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.getStartdate()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.getArchive());
            values.put(Constants.RequestParam.KEY_SETTIME, toDoItemModel.getSettime());
            // Inserting Row
            db.insert(Constants.RequestParam.TRASH_TABLE_NAME, null, values);
            Log.i(TAG, "addToDo:  success");
        }
        catch (Exception e)
        {
            Log.i(TAG, "addToDo: " + e);
        }
        finally
        {
            db.close(); // Closing database connection
        }
    }


    // Deleting single ToDo
    public void deleteTrashToDos(ToDoItemModel toDoItemModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleteToDo: delete");
        db.delete(Constants.RequestParam.TRASH_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.getId())});
        db.close();
    }

    // code to get all ToDos in a list view
    public List<ToDoItemModel> getAllTrashToDos()
    {
        Log.i(TAG, "getAllToDos: ");
        List<ToDoItemModel> mToDoList = new ArrayList<ToDoItemModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.RequestParam.TRASH_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                ToDoItemModel ToDo = new ToDoItemModel();
                ToDo.setId(Integer.parseInt(cursor.getString(0)));
                ToDo.setTitle(cursor.getString(1));
                ToDo.setNote(cursor.getString(2));
                ToDo.setReminder(cursor.getString(3));
                ToDo.setStartdate(cursor.getString(4));
                ToDo.setArchive(cursor.getString(5));
                ToDo.setSettime(cursor.getString(6));
                // Adding ToDo to list
                mToDoList.add(ToDo);
            }
            while (cursor.moveToNext());
        }
        // return ToDo list
        return mToDoList;
    }



    //add all models to local database
    public void addAllNotesToLocal(List<ToDoItemModel> toDoItemModels)
    {
        SQLiteDatabase db = null;
        try
        {
            db = this.getWritableDatabase();
            dropLocaltable(db);
            Log.i(TAG, "addToDo: start");
            for (ToDoItemModel toDoItemModel : toDoItemModels)
            {
                ContentValues values = new ContentValues();
                values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.getTitle()); // ToDo Name
                values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.getNote()); // ToDo Phone
                values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.getReminder()); // ToDo REMINDER
                values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.getStartdate()); // ToDo REMINDER
                values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.getArchive()); // ToDo REMINDER
                values.put(Constants.RequestParam.KEY_SETTIME, toDoItemModel.getSettime());
                db.insert(Constants.RequestParam.NOTES_TABLE_NAME, null, values);
            }

            toDoActivityPresenter.sendCallBackNotes(toDoItemModels);

        }
        catch (Exception e)
        {
            Log.i(TAG, "addToDo: " + e);
        }
        finally
        {
            db.close(); // Closing database connection
        }
    }

    // code to get the single ToDo  
    public ToDoItemModel getToDo(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.RequestParam.NOTES_TABLE_NAME, new
                        String[]{Constants.RequestParam.KEY_ID,
                         Constants.RequestParam.KEY_TITLE,    Constants.RequestParam.KEY_NOTE,
                         Constants.RequestParam.KEY_REMINDER, Constants.RequestParam.KEY_STARTDATE,
                         Constants.RequestParam.KEY_ARCHIVE,  Constants.RequestParam.KEY_SETTIME},
                         Constants.RequestParam.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ToDoItemModel toDoItemModel = new ToDoItemModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5),cursor.getString(6));
        // return toDoItemModel
        return toDoItemModel;

    }

    // code to get all ToDos in a list view
    public List<ToDoItemModel> getAllToDos()
    {
        Log.i(TAG, "getAllToDos: ");
        List<ToDoItemModel> ToDoList = new ArrayList<ToDoItemModel>();
        // Select All Query  
        String selectQuery = "SELECT  * FROM " + Constants.RequestParam.NOTES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list  
        if (cursor.moveToFirst())
        {
            do
            {
                ToDoItemModel ToDo = new ToDoItemModel();
                ToDo.setId(Integer.parseInt(cursor.getString(0)));
                ToDo.setTitle(cursor.getString(1));
                ToDo.setNote(cursor.getString(2));
                ToDo.setReminder(cursor.getString(3));
                ToDo.setStartdate(cursor.getString(4));
                ToDo.setArchive(cursor.getString(5));
                ToDo.setSettime(cursor.getString(6));
                // Adding ToDo to list  
                ToDoList.add(ToDo);
            }
            while (cursor.moveToNext());
        }

        String selectQuerylocal = "SELECT  * FROM " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME;
        Cursor cursor2 = db.rawQuery(selectQuerylocal, null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst())
        {
            do
            {
                Log.i(TAG, "getAllToDos: " + cursor2.getColumnNames());
                ToDoItemModel ToDo = new ToDoItemModel();
                ToDo.setId(Integer.parseInt(cursor2.getString(0)));
                ToDo.setTitle(cursor2.getString(1));
                ToDo.setNote(cursor2.getString(2));
                ToDo.setReminder(cursor2.getString(3));
                ToDo.setStartdate(cursor2.getString(4));
                ToDo.setArchive(cursor2.getString(5));
                ToDo.setSettime(cursor2.getString(6));
                // Adding ToDo to list
                ToDoList.add(ToDo);
            }
            while (cursor2.moveToNext());
        }
        // return ToDo list
        return ToDoList;
    }


    //get all local data to upload

    public List<ToDoItemModel> getLocalData()
    {
        List<ToDoItemModel> localNotes = new ArrayList<ToDoItemModel>();
        String selectQuerylocal = "SELECT  * FROM " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor2 = db.rawQuery(selectQuerylocal, null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst())
        {
            do
            {
                Log.i(TAG, "getAllToDos: " + cursor2.getColumnNames());
                ToDoItemModel localNote = new ToDoItemModel();
                localNote.setId(Integer.parseInt(cursor2.getString(0)));
                localNote.setTitle(cursor2.getString(1));
                localNote.setNote(cursor2.getString(2));
                localNote.setReminder(cursor2.getString(3));
                localNote.setStartdate(cursor2.getString(4));
                localNote.setArchive(cursor2.getString(5));
                localNote.setSettime(cursor2.getString(6));
                // Adding ToDo to list
                localNotes.add(localNote);
            }
            while (cursor2.moveToNext());
        }
        return localNotes;
    }


    // code to update the single ToDo  
    public int updateToDo(ToDoItemModel toDoItemModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.getNote());
        values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.getTitle());
        values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.getReminder());
        values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.getStartdate());
        values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.getArchive());
        values.put(Constants.RequestParam.KEY_SETTIME, toDoItemModel.getSettime());
        // updating row  
        return db.update(Constants.RequestParam.NOTES_TABLE_NAME, values, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.getId())});
    }

    //update Local data
    // code to update the single ToDo
    public int updateLocal(ToDoItemModel toDoItemModel)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.getNote());
        values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.getTitle());
        values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.getReminder());
        values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.getStartdate());
        values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.getArchive());
        values.put(Constants.RequestParam.KEY_SETTIME, toDoItemModel.getSettime());
        // updating row
        return db.update(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME,
                values, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.getId())});
    }
    //delete local data

    // Deleting single ToDo
    public void deleteLocal(List<ToDoItemModel> toDoItemModels)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ToDoItemModel todo : toDoItemModels)
        {
            db.delete(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                    new String[]{String.valueOf(todo.getId())});
        }
        db.close();
    }
    // Deleting single ToDo  
    public void deleteToDo(ToDoItemModel toDoItemModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleteToDo: delete");
        db.delete(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.getId())});
        db.close();
    }
    // Deleting single ToDo
    public void deleteLocaltodoNote(ToDoItemModel toDoItemModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleteToDo: delete");
        db.delete(Constants.RequestParam.NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.getId())});
        db.delete(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.getId())});
        db.close();
    }

    // Getting ToDos Count  
    public int getToDosCount()
    {
        String countQuery = "SELECT  * FROM " + Constants.RequestParam.NOTES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count  
        return cursor.getCount();
    }

}  
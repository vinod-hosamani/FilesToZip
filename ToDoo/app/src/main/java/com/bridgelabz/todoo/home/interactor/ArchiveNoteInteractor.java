package com.bridgelabz.todoo.home.interactor;

import android.content.Context;
import android.util.Log;

import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.home.presenter.ArchiveNotePresenter;
import com.bridgelabz.todoo.util.Connection;
import com.bridgelabz.todoo.util.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */

public class ArchiveNoteInteractor
{
    Context mContext;
    ArchiveNotePresenter mArchiveNotePresenter;
    DatabaseReference mDatabase;
    private String TAG="ArchiveNoteInteractor";
    private String startdate;
    private int index;
    private DatabaseReference mRef;
    private int pos;

    public ArchiveNoteInteractor(Context mContext, ArchiveNotePresenter archiveNotePresenter)
    {
        this.mContext=mContext;
        this.mArchiveNotePresenter=archiveNotePresenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void undoArchivedFirbaseData(String uid, String date, ToDoItemModel toDoItemModel)
    {
        Connection con=new Connection(mContext);

        if(con.isNetworkConnected())
        {
            try
            {
                toDoItemModel.setArchive(Constants.Stringkeys.FLAG_FALSE);
                mDatabase.child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD).child(uid).child(date).child(String.valueOf(toDoItemModel.getId())).setValue(toDoItemModel);
            }
            catch (Exception e)
            {
                Log.i(TAG, "updateFirbaseData: ");
            }
        }
        else
        {
        }
    }
    public void getIndexUpdateNotes(ToDoItemModel doItemModel,
                                    List<ToDoItemModel> toDoItemModels, String mUserUID)
    {
        List<ToDoItemModel> toDoItems = new ArrayList<>();
        //db.deleteLocaltodoNote(doItemModel);
        startdate = doItemModel.getStartdate();
        index = doItemModel.getId();
        for (ToDoItemModel todo : toDoItemModels)
        {
            if (todo.getStartdate().equals(startdate) && todo.getId() > index)
            {
                toDoItems.add(todo);
            }
        }
        if (toDoItemModels != null)
        {
            Connection con = new Connection(mContext);
            if (con.isNetworkConnected())
            {
                removeData(toDoItems, mUserUID, startdate, index);
            }

        }
    }

    private void removeData(List<ToDoItemModel> toDoItemModels, String mUserUID, String startdate, int index)
    {
        mRef = mDatabase.child("usersdata");
        pos = index;
        if (toDoItemModels.size() == 1)
        {
            toDoItemModels.get(0).setId(pos);
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(toDoItemModels.get(0));
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos + 1)).setValue(null);
        }
        else if (toDoItemModels.size() == 0)
        {
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);
        }
        else
        {
            for (ToDoItemModel todoNote : toDoItemModels)
            {
                try
                {
                    Log.i(TAG, "setSize: " + pos);
                    todoNote.setId(pos);
                    mRef.child(mUserUID).child(todoNote.getStartdate()).child(String.valueOf(pos)).setValue(todoNote);
                    pos = pos + 1;
                }
                catch (Exception f)
                {
                    Log.i(TAG, "setData: ");
                }
            }
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);
        }
    }
}

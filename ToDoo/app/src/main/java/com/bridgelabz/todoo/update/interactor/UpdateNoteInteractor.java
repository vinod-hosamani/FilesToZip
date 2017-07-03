package com.bridgelabz.todoo.update.interactor;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bridgelabz.todoo.database.DatabaseHandler;
import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.update.presenter.UpdateNotePresenterInterface;
import com.bridgelabz.todoo.util.Connection;
import com.bridgelabz.todoo.util.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UpdateNoteInteractor implements  UpdateNoteInteractorInterface
{
    private String TAG ="UpdateNoteInteractor";
    UpdateNotePresenterInterface mUpdateNotePresenter;
    Context mContext;
    DatabaseReference mDatabase;

    public UpdateNoteInteractor(Context context, UpdateNotePresenterInterface updateNotePresenter)
    {
        this.mUpdateNotePresenter=updateNotePresenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mContext=context;
    }

    @Override
    public void updateFirbaseData(String uid, String date, ToDoItemModel toDoItemModel)
    {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected())
        {
        mUpdateNotePresenter.showProgress();
        try
        {
            mDatabase.child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD).child(uid).child(date).child(String.valueOf(toDoItemModel.getId())).setValue(toDoItemModel);
            mUpdateNotePresenter.getResponce(true);
            mUpdateNotePresenter.closeProgress();

        }
        catch (Exception e)
        {
            mUpdateNotePresenter.getResponce(false);
            mUpdateNotePresenter.closeProgress();
        }
        }
        else
        {
            Toast.makeText(mContext, Constants.InternateConnnection.CHICK_CONNECTION, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
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

    @Override
    public void updateLocal(ToDoItemModel toDoItemModel)
    {
        DatabaseHandler db=new DatabaseHandler(mContext);
        db.updateLocal(toDoItemModel);
    }

    @Override
    public void getArchiveFirebaseData(String uid, String date, ToDoItemModel toDoItemModel)
    {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected())
        {
            mUpdateNotePresenter.showProgress();
            try
            {
                Log.i(TAG, "getArchiveFirebaseData: ");
                 toDoItemModel.setArchive(Constants.Stringkeys.FLAGT_TRUE);
                 mDatabase.child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD).child(uid).child(date).
                        child(String.valueOf(toDoItemModel.getId())).setValue(toDoItemModel);
                 mUpdateNotePresenter.getResponce(true);
                 mUpdateNotePresenter.closeProgress();

            }
            catch (Exception e)
            {
                mUpdateNotePresenter.getResponce(false);
                mUpdateNotePresenter.closeProgress();
            }
        }
        else
        {

        }
    }

   /* @Override
    public void getMoveNotes(String uid, ToDoItemModel fromNote, ToDoItemModel desinationNote) {
        ToDoItemModel fromNotes=fromNote;
        ToDoItemModel destNotes=desinationNote;
        int start_idm,end_id;

        mDatabase.child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD).child(uid).child(fromNote.getStartdate()).child(String.valueOf(fromNote.getId())).setValue(fromNote);
        mDatabase.child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD).child(uid).child(destNotes.getStartdate()).child(String.valueOf(destNotes.getId())).setValue(destNotes);

    }*/
}

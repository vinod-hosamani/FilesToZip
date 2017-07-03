package com.bridgelabz.todoo.update.presenter;

import android.content.Context;
import android.util.Log;

import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.home.view.ToDoActivityInteface;
import com.bridgelabz.todoo.update.interactor.UpdateNoteInteractor;


public class UpdateNotePresenter implements  UpdateNotePresenterInterface
{
    private  static String TAG ="UpdateNotePresenter";
    ToDoActivityInteface updateNoteActivityInterface;
    UpdateNoteInteractor mUpdateNoteInteractor;

    Context mContext;

    public UpdateNotePresenter(Context context, ToDoActivityInteface updateNoteActivity)
    {
        this.updateNoteActivityInterface =updateNoteActivity;
        mUpdateNoteInteractor=new UpdateNoteInteractor(context,this);
        this.mContext=context;
    }

    @Override
    public void closeProgress() {
        updateNoteActivityInterface.closeProgressDialog();
    }

    @Override
    public void showProgress() {
        updateNoteActivityInterface.showProgressDialog();
    }

    @Override
    public void getResponce(boolean flag) {
        updateNoteActivityInterface.getResponce(flag);
    }

    @Override
    public void updateNote(String uid, String date, ToDoItemModel toDoItemModel) {
            mUpdateNoteInteractor.updateFirbaseData(uid,date,toDoItemModel);
    }

    @Override
    public void getAchiveNote(String uid, String date, ToDoItemModel toDoItemModel) {
        Log.i(TAG, "getAchiveNote: ");
        mUpdateNoteInteractor.getArchiveFirebaseData(uid,date,toDoItemModel);
    }

    @Override
    public void getUndoAchiveNote(String uid, String date, ToDoItemModel toDoItemModel) {

        mUpdateNoteInteractor.undoArchivedFirbaseData(uid,date,toDoItemModel);
    }

    /*@Override
    public void getMoveNotes(String uid, ToDoItemModel fromNote, ToDoItemModel desinationNote) {
        mUpdateNoteInteractor.getMoveNotes( uid, fromNote, desinationNote);
    }*/

}

package com.bridgelabz.todoo.home.presenter;

import android.content.Context;

import com.bridgelabz.todoo.database.DatabaseHandler;
import com.bridgelabz.todoo.home.interactor.RemoveFirebaseDataInteractor;
import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.home.view.ToDoActivity;
import com.bridgelabz.todoo.home.view.TrashFragment;
import com.bridgelabz.todoo.util.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class RemoveNotePresenter  {
    private  static String TAG="RemoveNotePresenter";
    DatabaseHandler db;
    RemoveFirebaseDataInteractor removeFirebaseDataInteractor;
    List<ToDoItemModel> newtoDoItemModels= new ArrayList<ToDoItemModel>();
    String startdate;
    Context mContext;
    DatabaseHandler databaseHandler;
    ToDoActivity mToDoActivity;
    TrashFragment trashFragment;
    int index;
    public RemoveNotePresenter(Context applicationContext, ToDoActivity toDoActivity) {
        this.mContext=applicationContext;
        this.mToDoActivity=toDoActivity;
        removeFirebaseDataInteractor =new RemoveFirebaseDataInteractor(mContext,this);
        this.databaseHandler=new DatabaseHandler(mContext);
    }

    public RemoveNotePresenter(Context baseContext, TrashFragment trashFragment) {
        this.mContext=baseContext;
        this.trashFragment=trashFragment;
        removeFirebaseDataInteractor =new RemoveFirebaseDataInteractor(mContext,this);
        this.databaseHandler=new DatabaseHandler(mContext);
    }


    public void removeFirebaseData(ToDoItemModel doItemModel, List<ToDoItemModel> toDoItemModel, String mUserUID){
        databaseHandler.addNoteToTrash(doItemModel);
        removeFirebaseDataInteractor.getIndexUpdateNotes(doItemModel,toDoItemModel,mUserUID);
    }

    public void  getArchiveData(ToDoItemModel toDoItemModel, String mUserUID, int position){
        db.updateToDo(toDoItemModel);
        startdate=toDoItemModel.getStartdate();
        index=toDoItemModel.getId();

            Connection con=new Connection(mContext);
            if(con.isNetworkConnected()){
                removeFirebaseDataInteractor.updateFirebaseData(toDoItemModel,mUserUID,startdate,index);
            }
    }

    public void getTrashNotes() {
       ArrayList<ToDoItemModel> todoItemModels= (ArrayList<ToDoItemModel>) databaseHandler.getAllTrashToDos();
        trashFragment.displayTrashNotes(todoItemModels);
    }

    public void getDeleteTrashNote(ToDoItemModel toDoItemModel) {
        databaseHandler.deleteTrashToDos(toDoItemModel);
    }

    public void getRestoreNote(String mUserUID, ToDoItemModel toDoItemModel) {

        removeFirebaseDataInteractor.getRestore(mUserUID,toDoItemModel);
        databaseHandler.deleteTrashToDos(toDoItemModel);
    }

    public void undoRemoveFirebaseData(ToDoItemModel toDoItemModel, List<ToDoItemModel> toDoAllItemModels, String mUserUID) {
        databaseHandler.deleteTrashToDos(toDoItemModel);
        removeFirebaseDataInteractor.getUndoDeleteNotes(toDoItemModel,toDoAllItemModels,mUserUID);
    }
}

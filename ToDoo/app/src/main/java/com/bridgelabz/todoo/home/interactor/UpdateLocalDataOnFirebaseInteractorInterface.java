package com.bridgelabz.todoo.home.interactor;


import com.bridgelabz.todoo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 30/3/17.
 */

public interface UpdateLocalDataOnFirebaseInteractorInterface {
    public  void updatetoFirebase(String uid, List<ToDoItemModel> localNotes);
    public  void setData(int size, ToDoItemModel toDoItemModel);

}

package com.bridgelabz.todoo.addnote.interactor;


import com.bridgelabz.todoo.home.model.ToDoItemModel;

/**
 * Created by bridgeit on 20/4/17.
 */

public interface AddNoteInteractorInteface {
    public  void setData(int size);
    public  void getResponce(boolean flag);
    public  void storeNote(String date, ToDoItemModel toDoItemModel);
    public  void uploadNotes(String uid, String date, ToDoItemModel toDoItemModel);
}

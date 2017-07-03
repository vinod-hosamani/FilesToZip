package com.bridgelabz.todoo.home.presenter;


import com.bridgelabz.todoo.addnote.view.NoteInterface;
import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.home.view.ToDoActivityInteface;

import java.util.List;


public interface ToDoPresenterInteface  extends ToDoActivityInteface, NoteInterface {

    public  void getPresenterNotes(String uid);
    public  void getCallBackNotes(List<ToDoItemModel> toDoItemModels);
    public  void sendCallBackNotes(List<ToDoItemModel> toDoItemModels);
     public  void getPresenterNotesAfterUpdate(String uid);

}

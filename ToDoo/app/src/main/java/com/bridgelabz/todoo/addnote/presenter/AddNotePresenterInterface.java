package com.bridgelabz.todoo.addnote.presenter;


import com.bridgelabz.todoo.addnote.view.NoteInterface;
import com.bridgelabz.todoo.home.model.ToDoItemModel;

/**
 * Created by bridgeit on 20/4/17.
 */

public interface AddNotePresenterInterface extends NoteInterface {

    public  void loadNotetoFirebase(String uid, String date, ToDoItemModel toDoItemModel);
}

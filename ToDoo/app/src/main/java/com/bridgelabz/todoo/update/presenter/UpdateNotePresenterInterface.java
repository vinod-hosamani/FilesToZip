package com.bridgelabz.todoo.update.presenter;


import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.update.view.UpdateNoteActivityInterface;

public interface UpdateNotePresenterInterface extends UpdateNoteActivityInterface
{
        public  void updateNote(String uid, String date, ToDoItemModel toDoItemModel);
        public  void getAchiveNote(String uid, String date, ToDoItemModel toDoItemModel);
        public  void getUndoAchiveNote(String uid, String date, ToDoItemModel toDoItemModel);
        /*public  void getMoveNotes(String uid,ToDoItemModel fromNote,ToDoItemModel desinationNote);*/
}

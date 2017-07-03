package com.bridgelabz.todoo.home.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.bridgelabz.todoo.R;
import com.bridgelabz.todoo.home.adapter.ItemAdapter;
import com.bridgelabz.todoo.home.model.ToDoItemModel;
import com.bridgelabz.todoo.home.presenter.ArchiveNotePresenter;
import com.bridgelabz.todoo.home.presenter.RemoveNotePresenter;
import com.bridgelabz.todoo.home.presenter.ToDoActivityPresenter;
import com.bridgelabz.todoo.update.presenter.UpdateNotePresenterInterface;
import com.bridgelabz.todoo.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ToDoNotesFragment extends Fragment implements  ToDoActivityInteface,View.OnClickListener
{
    private static final String TAG = "TrashFragment";
    private ItemAdapter mToDoNotesAdapter;
    List<ToDoItemModel> mAllToDONotes;
    List<ToDoItemModel> mTodoNotesNotes;
    private RecyclerView mToDoRecyclerView;
    private AppCompatTextView mTextView_blank_recycler;
    private ArchiveNotePresenter mArchiveNotePresenter;
    private AppCompatEditText mEditText_Search;
    private boolean mLinear = false;
    AppCompatImageView mImageView_Linear_Grid;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String mUserUID;
    AppCompatTextView titleTextView;
    private RemoveNotePresenter removeNotePresenter;
    private UpdateNotePresenterInterface updateNotePresenter;
    private ToDoActivityPresenter mToDoActivityPresenter;

    public ToDoNotesFragment(List<ToDoItemModel> toDoAllItemModels)
    {
        this.mAllToDONotes=toDoAllItemModels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater,container,savedInstanceState);
        View view =inflater.inflate(R.layout.fragment_trash,container,false);
        mToDoRecyclerView = (RecyclerView)view. findViewById(R.id.gridview_fragment_notes);
        mTextView_blank_recycler=(AppCompatTextView)view.findViewById(R.id.textview_blank_fragment_recyclerview);
       // mArchiveNotePresenter = new ArchiveNotePresenter(getActivity().getBaseContext(), ToDoNotesFragment.this);
        mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        titleTextView =(AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);
        mEditText_Search=(AppCompatEditText) getActivity().findViewById(R.id.edittext_search_toolbar);
        mImageView_Linear_Grid=(AppCompatImageView) getActivity().findViewById(R.id.imageView_grid_linear);
        // mArchiveNotePresenter.getTrashNotes();
        titleTextView.setText(Constants.NotesType.ALL_NOTES);
        addTextListener();
        initSwipe();
        mImageView_Linear_Grid.setOnClickListener(this);
        mTodoNotesNotes =getAllToDo();
        displayArchiveNotes(mTodoNotesNotes);
        pref =getActivity(). getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        // editor = pref.edit();
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
        if (!mUserUID.equals(Constants.Stringkeys.NULL_VALUIE)) {
            mToDoActivityPresenter = new ToDoActivityPresenter(this, getActivity().getBaseContext());
            mToDoActivityPresenter.getPresenterNotes(mUserUID);
        }
        return view;
    }

    public void displayArchiveNotes(List<ToDoItemModel> todoItemModel)
    {
        mTodoNotesNotes =todoItemModel;
        if(mTodoNotesNotes.size()==0)
        {
            mTextView_blank_recycler.setVisibility(View.VISIBLE);
            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        }
        else
        {
            mTextView_blank_recycler.setVisibility(View.INVISIBLE);
            mToDoNotesAdapter =new ItemAdapter(getActivity(),todoItemModel);
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mToDoRecyclerView.setAdapter(mToDoNotesAdapter);
        }

    }

    public void addTextListener()
    {
        mEditText_Search.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            public void onTextChanged(CharSequence query, int start, int before, int count)
            {
                if (mToDoNotesAdapter != null)
                {
                    Filter filter = mToDoNotesAdapter.getFilter();
                    filter.filter(query);
                }

            }
        });
    }

    //swipe view delete / Archive
    private void initSwipe()
    {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // Collections.swap(toDoItemModels,viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.i(TAG, "onMove: " + viewHolder.getAdapterPosition() + "  target" + target.getAdapterPosition());
                int from, destination;
                from = viewHolder.getAdapterPosition();
                destination = target.getAdapterPosition();
                mToDoNotesAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            /*updateNotePresenter.getMoveNotes(mUserUID, toDoItemModels.get(from), toDoItemModels.get(destination));*/
                return true;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
            //    String typeOfNotes = mTextView_Title.getText().toString();
                final List<ToDoItemModel> forArchiveAlldataModels = mAllToDONotes;
                                       Log.i(TAG, "onSwiped: ");
                    final ToDoItemModel toDoItem=mTodoNotesNotes.get(position);
                    if (direction == ItemTouchHelper.LEFT) {
                        removeNotePresenter.removeFirebaseData(toDoItem,forArchiveAlldataModels, mUserUID);
                        Snackbar snackbar = Snackbar
                                .make(getActivity().getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_ARCHIVED, Snackbar.LENGTH_LONG)
                                .setAction(Constants.Stringkeys.ARCHIVE_UNDO, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        removeNotePresenter.undoRemoveFirebaseData(toDoItem,forArchiveAlldataModels, mUserUID);
                                    }
                                });
                        snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                        snackbar.show();

                        if(mTodoNotesNotes.size()==1){
                            mTodoNotesNotes.remove(0);
                        }
                    } else {
                        getArchive(mToDoNotesAdapter, position, mTodoNotesNotes.get(position));
                    }

            }

            //Archive Note Methode  And do Undo if required
            public void getArchive(final ItemAdapter archiveitemAdapter, final int position, final ToDoItemModel toDoItemModel) {
                //UpdateNotePresenter updateNotePresenter = new UpdateNotePresenter(ToDoActivity.this, this);
                final String date = toDoItemModel.getStartdate();
                // archiveitemAdapter.removeItem(position);
                updateNotePresenter.getAchiveNote(mUserUID, date, toDoItemModel);
                Snackbar snackbar = Snackbar
                        .make(getActivity().getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_ARCHIVED, Snackbar.LENGTH_LONG)
                        .setAction(Constants.Stringkeys.ARCHIVE_UNDO, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateNotePresenter.getUndoAchiveNote(mUserUID, date, toDoItemModel);
                                toDoItemModel.setArchive(Constants.Stringkeys.FLAG_FALSE);
                                archiveitemAdapter.reduNote(toDoItemModel, position);
                            }
                        });
                snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mToDoRecyclerView);

    }

    @Override
    public void onClick(View v)
    {

        getAlterRecyclerLayout();
    }

    private void getRecyclerLayout()
    {
        if (mLinear)
        {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        }
        else
        {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    //set Linear or grid Layout
    private void getAlterRecyclerLayout()
    {
        if (!mLinear)
        {
            mLinear = true;
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        }
        else
        {
            mLinear = false;
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }
    //get All notes
    public List<ToDoItemModel> getAllToDo()
    {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (mAllToDONotes != null)
        {
            for (ToDoItemModel todoItem : mAllToDONotes)
            {
                if (todoItem.getArchive().equals(getString(R.string.flag_false)))
                {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    @Override
    public void closeProgressDialog()
    {

    }

    @Override
    public void showProgressDialog()
    {

    }

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModels)
    {
        this.mAllToDONotes = toDoItemModels;
        if (mAllToDONotes.size() != 0)
        {
            toDoItemModels = getAllToDo();
                getupdatedView(toDoItemModels, 1);
        }
        else
        {
      getupdatedView(toDoItemModels, 1);


        }
    }

    private void getupdatedView(List<ToDoItemModel> toDoItemModels, int i)
    {

        switch (i)
        {
            case 1:
                if(toDoItemModels.size()==0){
                    mTextView_blank_recycler.setVisibility(View.VISIBLE);
                    mTextView_blank_recycler.setText(getString(R.string.data_is_null));
                    mToDoRecyclerView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    mTextView_blank_recycler.setVisibility(View.INVISIBLE);
                    mToDoRecyclerView.setVisibility(View.VISIBLE);
                    getRecyclerLayout();
                    mToDoNotesAdapter = new ItemAdapter(getActivity().getBaseContext(), toDoItemModels);
                    mToDoRecyclerView.setAdapter(mToDoNotesAdapter);
                }
                break;
        }
    }

    @Override

    public void getResponce(boolean flag) {

    }

    @Override
    public void getUndoArchivedNote(int position) {

    }
}

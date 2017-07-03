package com.bridgelabz.todoo.home.view;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.bridgelabz.todoo.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ArchiveFragment extends Fragment implements  View.OnClickListener {

    private static final String TAG = "TrashFragment";
    private ItemAdapter mArchiveAdapter;
    List<ToDoItemModel> mAllToDONotes;
    List<ToDoItemModel> mArchiveNotes;
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
    public ArchiveFragment(AppCompatEditText editText_search, AppCompatImageView mImageView_Linear_Grid, List<ToDoItemModel> toDoAllItemModels) {
        this.mEditText_Search=editText_search;
        this.mAllToDONotes=toDoAllItemModels;
        this.mImageView_Linear_Grid=mImageView_Linear_Grid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view =inflater.inflate(R.layout.fragment_trash,container,false);
        mToDoRecyclerView = (RecyclerView)view. findViewById(R.id.gridview_fragment_notes);
        mTextView_blank_recycler=(AppCompatTextView)view.findViewById(R.id.textview_blank_fragment_recyclerview);
        mArchiveNotePresenter = new ArchiveNotePresenter(getActivity().getBaseContext(), ArchiveFragment.this);
        mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        titleTextView =(AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);
       // mArchiveNotePresenter.getTrashNotes();
        titleTextView.setText(Constants.NotesType.ARCHIVE_NOTES);
        addTextListener();
        initSwipe();
        mImageView_Linear_Grid.setOnClickListener(this);
        mArchiveNotes=getArchivedToDos();
        displayArchiveNotes(mArchiveNotes);
        pref =getActivity(). getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
       // editor = pref.edit();
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
        return view;
    }

    public void displayArchiveNotes(List<ToDoItemModel> todoItemModel) {
        mArchiveNotes =todoItemModel;
        if(mArchiveNotes.size()==0){
            mTextView_blank_recycler.setVisibility(View.VISIBLE);
            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        }else {
            mTextView_blank_recycler.setVisibility(View.INVISIBLE);
            mArchiveAdapter =new ItemAdapter(getActivity(),todoItemModel);
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mToDoRecyclerView.setAdapter(mArchiveAdapter);
        }

    }



    public void addTextListener() {

        mEditText_Search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (mArchiveAdapter != null) {
                    Filter filter = mArchiveAdapter.getFilter();
                    filter.filter(query);
                }

            }
        });
    }

    //swipe view delete / Archive
    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                mArchiveAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                Log.i(TAG, "onSwiped: ");
                if (direction == ItemTouchHelper.LEFT) {
                    mArchiveNotePresenter.getRestoreArchiveNote(mUserUID,mArchiveNotes.get(position));
                    mArchiveAdapter.removeItem(position);
                    mArchiveAdapter.notifyDataSetChanged();
                    if(mArchiveAdapter.getItemCount()==0){
                        mTextView_blank_recycler.setVisibility(View.VISIBLE);
                        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
                    }
                }else {
                    mArchiveNotePresenter.removeFirebaseData(mArchiveNotes.get(position),mAllToDONotes,mUserUID);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mToDoRecyclerView);

    }

    @Override
    public void onClick(View v) {

        getAlterRecyclerLayout();
    }

    private void getRecyclerLayout() {
        if (mLinear) {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    //set Linear or grid Layout
    private void getAlterRecyclerLayout() {
        if (!mLinear) {
            mLinear = true;
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        } else {
            mLinear = false;
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }



    //get Archived todo notes
    public List<ToDoItemModel> getArchivedToDos() {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (mAllToDONotes != null) {
            for (ToDoItemModel todoItem : mAllToDONotes) {
                if (todoItem.getArchive().equals(getString(R.string.flag_true))) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }
}

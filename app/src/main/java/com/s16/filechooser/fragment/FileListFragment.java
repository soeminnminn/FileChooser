package com.s16.filechooser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.R;
import com.s16.filechooser.adapter.FileListAdapter;
import com.s16.filechooser.entries.FileEntry;
import com.s16.filechooser.entries.HomeFileEntry;
import com.s16.filechooser.entries.RootFileEntry;
import com.s16.filechooser.utils.FileEntryTypes;

import java.util.List;

/**
 * Created by SMM on 8/17/2016.
 */
public class FileListFragment extends Fragment
        implements FileListAdapter.OnFileListClickListener {

    protected static final String TAG = FileListFragment.class.getSimpleName();
    private static final String KEY_LAYOUT_MANAGER = FileSettings.KEY_VIEW_MODE;

    public interface FileListInteractionCallback {
        public void onOpenNewEntry(FileEntry entry);
        public void onEntryCheckedChanged(FileEntry entry, boolean isChecked, boolean isSelectMode, int selectedCount);
        public void onEntrySelectAll(int selectedCount);
    }

    protected int mCurrentLayoutManagerType;
    private RecyclerView mRecyclerView;
    private int mGridColumnCount = 2;

    private FileListInteractionCallback mListener;
    private FileEntry mEntry;
    private FileListAdapter mAdapter;
    private FileSettings mSettings;

    public FileListFragment() {
    }

    public FileEntry setSettings(FileSettings settings) {
        mSettings = settings;
        if (mEntry == null && settings != null) {
            if (settings.isShowFromRoot()) {
                mEntry = new RootFileEntry(settings, null);
            } else {
                mEntry = new HomeFileEntry(settings, null);
            }
        }
        return mEntry;
    }

    public void setFileEntry(FileEntry entry) {
        mEntry = entry;
    }

    public FileEntry getFileEntry() {
        return mEntry;
    }

    public String getFileName() {
        if (mEntry != null) {
            return mEntry.getName();
        }
        return "Entry is null";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_file_list, container, false);

        if (mSettings == null) {
            mSettings = FileSettings.getInstance(getContext());
        }
        if (mEntry == null) {
            if (mSettings.isShowFromRoot()) {
                mEntry = new RootFileEntry(mSettings, null);
            } else {
                mEntry = new HomeFileEntry(mSettings, null);
            }
        }

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.fs_fileList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCurrentLayoutManagerType = mSettings.getViewMode();
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = savedInstanceState.getInt(KEY_LAYOUT_MANAGER, mSettings.getViewMode());
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new FileListAdapter(getContext(), mEntry);
        mAdapter.setViewMode(mCurrentLayoutManagerType);
        mAdapter.setOnFileListClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FileListInteractionCallback) {
            mListener = (FileListInteractionCallback)context;
        }
    }

    @Override
    public void onDetach() {
        if (mListener != null) {
            mListener = null;
        }
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(outState);
    }

    private void onBackPressed() {
        getActivity().onBackPressed();
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    private void setRecyclerViewLayoutManager(int layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        RecyclerView.LayoutManager layoutManager;

        mCurrentLayoutManagerType = layoutManagerType;
        switch (layoutManagerType) {
            case FileSettings.VIEW_GRID:
                layoutManager = new GridLayoutManager(getActivity(), mGridColumnCount);
                break;
            case FileSettings.VIEW_LIST:
            default:
                layoutManager = new LinearLayoutManager(getActivity());
                break;
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onItemClick(FileEntry entry) {
        if (entry != null) {
            FileEntryTypes entryType = entry.getEntryType();
            if (entryType == FileEntryTypes.ALIAS_UP) {
                onBackPressed();

            } else if (entryType == FileEntryTypes.ALIAS_SMB_ADD) {
                performAddShare(entry);

            } else if (entryType == FileEntryTypes.FILE || entryType == FileEntryTypes.SMB_FILE) {
                performOpenFile(entry);

            } else {
                performNew(entry);
            }
        }
    }

    @Override
    public void onEditShareClick(FileEntry entry) {
        FileListAddNetwork fragment = new FileListAddNetwork();
        fragment.setEntry(entry);
        fragment.show(getChildFragmentManager(), "FileListAddNetwork");
    }

    @Override
    public void onDeleteShareClick(FileEntry entry) {

    }

    @Override
    public void onItemCheckedChanged(FileEntry entry, boolean isChecked) {
        if (mListener != null) {
            List<FileEntry> selectedEntries = mAdapter.getSelectedEntries();
            mListener.onEntryCheckedChanged(entry, isChecked, mAdapter.isSelectMode(), selectedEntries.size());
        }
    }

    @Override
    public void onSelectAllItem() {
        if (mListener != null) {
            List<FileEntry> selectedEntries = mAdapter.getSelectedEntries();
            mListener.onEntrySelectAll(selectedEntries.size());
        }
    }

    public boolean isSelectMode() {
        if (mAdapter != null) {
            return mAdapter.isSelectMode();
        }
        return false;
    }

    public void setSelectMode(boolean selectMode) {
        if (mAdapter != null) {
            mAdapter.setSelectMode(selectMode);
        }
    }

    public void selectAll() {
        if (mAdapter != null) {
            mAdapter.selectAll();
        }
    }

    public void setSortMode(int sortMode) {
        if (mAdapter != null) {
            mAdapter.setSortMode(sortMode);
        }
    }

    public void setViewMode(int viewMode) {
        if (mAdapter != null) {
            mAdapter.setViewMode(viewMode);
        }
        setRecyclerViewLayoutManager(viewMode);
    }

    private void performAddShare(FileEntry entry) {
        FileListAddNetwork fragment = new FileListAddNetwork();
        fragment.show(getChildFragmentManager(), "FileListAddNetwork");
    }

    private void performOpenFile(FileEntry entry) {

    }

    private void performNew(FileEntry entry){
        if (mListener != null) {
            mListener.onOpenNewEntry(entry);
        }
    }
}

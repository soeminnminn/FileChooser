package com.s16.filechooser.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.R;
import com.s16.filechooser.entries.FileEntry;
import com.s16.filechooser.utils.FileEntryTypes;
import com.s16.filechooser.utils.ResourcesUtils;
import com.s16.filechooser.utils.UiUtils;
import com.s16.widget.CheckableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SMM on 8/30/2016.
 */
public class FileListAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
        implements PopupMenu.OnMenuItemClickListener {

    protected static final String TAG = FileListAdapter.class.getSimpleName();

    public interface OnFileListClickListener {
        public void onItemClick(FileEntry entry);
        public void onItemCheckedChanged(FileEntry entry, boolean isChecked);
        public void onSelectAllItem();
        public void onEditShareClick(FileEntry entry);
        public void onDeleteShareClick(FileEntry entry);
    }

    private View.OnClickListener mSmbShareMenuClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag() instanceof FileEntry) {
                mClickedEntry = (FileEntry)v.getTag();
            }
            PopupMenu menu = new PopupMenu(getContext(), v);
            menu.inflate(R.menu.fs_share_menu);
            menu.setOnMenuItemClickListener(FileListAdapter.this);
            menu.show();
        }
    };

    private View.OnClickListener mItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FileEntry entry = null;
            if (view.getTag() != null && view.getTag() instanceof FileEntry) {
                entry = (FileEntry) view.getTag();
            }
            if (entry != null) {
                if (!mSelectMode) {
                    mClickedEntry = entry;
                    performItemClick(view);

                } else if (isSelectableEntry(entry)) {
                    ViewGroup parent = (ViewGroup) view;
                    CheckableRelativeLayout checkableView = (CheckableRelativeLayout) parent.findViewById(R.id.fileItemCheck);
                    checkableView.toggle();
                    onCheckedChanged(entry, checkableView.isChecked());
                }
            }
        }
    };

    private View.OnLongClickListener mItemLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            FileEntry entry = null;
            if (view.getTag() != null && view.getTag() instanceof FileEntry) {
                entry = (FileEntry) view.getTag();
            }
            if (entry != null && isSelectableEntry(entry)) {
                ViewGroup parent = (ViewGroup) view;
                CheckableRelativeLayout checkableView = (CheckableRelativeLayout) parent.findViewById(R.id.fileItemCheck);
                checkableView.setChecked(true);
                mSelectMode = true;
                onCheckedChanged(entry, true);
                return true;
            }
            return false;
        }
    };

    private final Context mContext;
    private FileEntry mEntry;
    private List<FileEntry> mFiles = new ArrayList<FileEntry>();
    private FileEntry mClickedEntry;
    private boolean mSelectMode;
    private int mViewMode;
    private List<FileEntry> mSelectedEntries;
    private OnFileListClickListener mClickListener;

    public FileListAdapter(Context context, FileEntry entry) {
        mContext = context;
        mEntry = entry;
        mSelectedEntries = new ArrayList<FileEntry>();
        if (mEntry != null) {
            mEntry.listFiles(mFiles);
        }
    }

    protected Context getContext() {
        return mContext;
    }

    public void changeEntry(FileEntry entry) {
        if (entry != null && !entry.equals(mEntry)) {
            mEntry = entry;
            if (mFiles != null) {
                mFiles.clear();
            }
            mEntry.listFiles(mFiles);
            notifyDataSetChanged();
        }
    }

    public void notifyRefresh() {
        if (mEntry != null) {
            if (mFiles != null) {
                mFiles.clear();
            }
            mEntry.listFiles(mFiles);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view;
        if (mViewMode == FileSettings.VIEW_GRID) {
            view = layoutInflater.inflate(R.layout.file_grid_item, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.file_list_item, parent, false);
        }
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        TextView fileItemName = (TextView)holder.findViewById(R.id.fileItemName);
        TextView fileItemSize = (TextView)holder.findViewById(R.id.fileItemSize);
        TextView fileItemDate = (TextView)holder.findViewById(R.id.fileItemDate);
        ImageView fileItemIcon = (ImageView)holder.findViewById(R.id.fileItemImage);
        ImageView fileItemMenu = (ImageView)holder.findViewById(R.id.fileItemMenu);

        FileEntry file = getItem(position);
        if (file != null) {
            fileItemName.setText(file.getName());
            fileItemDate.setText(UiUtils.getReadableLastModified(getContext(), file));
            if (file.getEntryType() == FileEntryTypes.SMB_SHARE) {
                fileItemSize.setText(file.getPath());
                fileItemMenu.setVisibility(View.VISIBLE);
                fileItemMenu.setClickable(true);
                fileItemMenu.setOnClickListener(mSmbShareMenuClick);
            } else {
                fileItemSize.setText(UiUtils.getReadableFileSize(file));
                fileItemMenu.setClickable(false);
                fileItemMenu.setVisibility(View.GONE);
            }
            fileItemIcon.setImageDrawable(ResourcesUtils.getFileIconDrawable(getContext(), file));
            fileItemMenu.setTag(file);

            CheckableRelativeLayout checkableView = (CheckableRelativeLayout)holder.findViewById(R.id.fileItemCheck);
            checkableView.setChecked(mSelectedEntries.contains(file));

            holder.getItemView().setTag(file);
            holder.setOnClickListener(mItemClick);
            if (isSelectableEntry(file)) {
                holder.getItemView().setLongClickable(true);
                holder.getItemView().setOnLongClickListener(mItemLongClick);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        if (mFiles != null) {
            FileEntry item = getItem(position);
            return item.getId();
        }
        return super.getItemId(position);
    }

    public FileEntry getItem(int position) {
        if (mFiles != null) {
            return mFiles.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mFiles != null) {
            return mFiles.size();
        }
        return 0;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            performEditShare();
        } else if (id == R.id.action_delete) {
            performDeleteShare();
        }
        return false;
    }

    public void setOnFileListClickListener(OnFileListClickListener listener) {
        mClickListener = listener;
    }

    public void setViewMode(int viewMode) {
        FileSettings.getInstance(getContext()).setViewMode(viewMode);
        mViewMode = viewMode;
        notifyRefresh();
    }

    public void setSortMode(int sortMode) {
        FileSettings.getInstance(getContext()).setSortBy(sortMode);
        notifyRefresh();
    }

    public boolean isSelectMode() {
        return mSelectMode;
    }

    public void setSelectMode(boolean selectMode) {
        if (mSelectMode != selectMode) {
            mSelectMode = selectMode;
            if (!selectMode) {
                mSelectedEntries.clear();
            }
            notifyDataSetChanged();
        }
    }

    public void selectAll() {
        if (mSelectMode) {
            mSelectedEntries.clear();
            for (FileEntry file : mFiles) {
                if (isSelectableEntry(file)) {
                    mSelectedEntries.add(file);
                }
            }
            notifyDataSetChanged();

            if (mClickListener != null) {
                mClickListener.onSelectAllItem();
            }
        }
    }

    public List<FileEntry> getSelectedEntries() {
        return mSelectedEntries;
    }

    private boolean isSelectableEntry(FileEntry entry) {
        if (entry == null) return false;
        FileEntryTypes type = entry.getEntryType();
        if (type == FileEntryTypes.ALIAS_UP ||
                type == FileEntryTypes.ALIAS_SMB_ADD ||
                type == FileEntryTypes.STORAGE ||
                type == FileEntryTypes.NETWORK) {
            return false;
        }
        return true;
    }

    private void onCheckedChanged(FileEntry entry, boolean isChecked) {
        if (isChecked) {
            if (!mSelectedEntries.contains(entry)) {
                mSelectedEntries.add(entry);
            }
        } else {
            if (mSelectedEntries.contains(entry)) {
                mSelectedEntries.remove(entry);
            }
        }
        if (mClickListener != null) {
            mClickListener.onItemCheckedChanged(entry, isChecked);
        }
    }

    private void performItemClick(View view) {
        if (mClickedEntry == null) return;
        if (mClickListener != null) {
            mClickListener.onItemClick(mClickedEntry);
        }
    }

    private void performEditShare() {
        if (mClickedEntry == null) return;
        if (mClickListener != null) {
            mClickListener.onEditShareClick(mClickedEntry);
        }
    }

    private void performDeleteShare() {
        if (mClickedEntry == null) return;
        if (mClickListener != null) {
            mClickListener.onDeleteShareClick(mClickedEntry);
        }
    }
}

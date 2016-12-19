package com.s16.filechooser.entries;

import java.util.List;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryFilter;
import com.s16.filechooser.utils.FileEntryTypes;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

public class NetworkFileEntry extends BaseFileEntry {

	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

		public FileEntry createFromParcel(Parcel data) {
			return new NetworkFileEntry(data);
		}

		public FileEntry[] newArray(int size) {
			return new NetworkFileEntry[size];
		}

	};
	
	public NetworkFileEntry(FileSettings settings, FileEntry parent, long id) {
		super(settings, parent, id);
		mEntryType = FileEntryTypes.NETWORK;
	}

    public NetworkFileEntry(Parcel source) {
        super(source);
        mEntryType = FileEntryTypes.NETWORK;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
	
	@Override
	public String getPath() {
		return FileEntryConstants.NETWORK_PATH;
	}

	@Override
	public boolean exists() {
		return true;
	}
	
	@Override
	public boolean isDirectory() {
		return true;
	}
	
	@Override
	public boolean hasChild() {
		return true;
	}
	
	@Override
	public boolean canRead() {
		return true;
	}
	
	@Override
	public String getAbsolutePath() {
		return FileEntryConstants.NETWORK_PATH;
	}
	
	@Override
	public String getName() {
		return FileEntryConstants.NETWORK;
	}

	@Override
	public void listFiles(List<FileEntry> list, FileEntryFilter filter) {
		list.clear();
		if (getSettings().isShowAliasUp()) {
			list.add(new AliasUpFileEntry(getSettings(), getParent()));
		}
		list.add(new AliasSmbAddFileEntry(getSettings(), this));
		
		// Add Saved SMB Share
		String sortOrder = FileEntryConstants.KEY_SMB_DISPLAY_NAME + " ASC";
        Cursor cursor = getSettings().querySmbShareList(sortOrder);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int columnIdx = cursor.getColumnIndex(BaseColumns._ID);
                    long id = cursor.getLong(columnIdx);

                    columnIdx = cursor.getColumnIndex(FileEntryConstants.KEY_SMB_DISPLAY_NAME);
                    String displayName = cursor.getString(columnIdx);
                    columnIdx = cursor.getColumnIndex(FileEntryConstants.KEY_SMB_SERVER);
                    String server = cursor.getString(columnIdx);
                    columnIdx = cursor.getColumnIndex(FileEntryConstants.KEY_SMB_DOMAIN);
                    String domain = cursor.getString(columnIdx);
                    columnIdx = cursor.getColumnIndex(FileEntryConstants.KEY_SMB_USER_NAME);
                    String userName = cursor.getString(columnIdx);
                    columnIdx = cursor.getColumnIndex(FileEntryConstants.KEY_SMB_PASSWORD);
                    String password = cursor.getString(columnIdx);

                    list.add(SmbShareFileEntry.createSmbShare(getSettings(), this, id, displayName, server, domain, userName, password));
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
	}
}

package com.s16.filechooser.entries;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryTypes;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class SmbShareFileEntry extends SmbFileEntry {

	public static FileEntry createSmbShare(FileSettings settings, FileEntry parent, long id,
										   String displayName, String server, String domain,
										   String userName, String password) {
		if (TextUtils.isEmpty(server)) return null;
		if (TextUtils.isEmpty(displayName)) {
			displayName = server;
		}
		String path = FileEntryConstants.SMB_PROTOCOL + server;
		
		jcifs.Config.registerSmbURLHandler();
		return new SmbShareFileEntry(settings, parent, id, path, displayName, domain, userName, password);
	}

    public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

        public FileEntry createFromParcel(Parcel data) {
            return new SmbShareFileEntry(data);
        }

        public FileEntry[] newArray(int size) {
            return new SmbShareFileEntry[size];
        }

    };
	
	private String mName;
	
	private SmbShareFileEntry(FileSettings settings, FileEntry parent, long id, String url,
                              String name, String domain, String userName, String password) {
		super(settings, parent, id, url, domain, userName, password);
		mName = name;
		mEntryType = FileEntryTypes.SMB_SHARE;
	}

	public SmbShareFileEntry(Parcel source) {
		super(source);
        mName = source.readString();
		mEntryType = FileEntryTypes.SMB_SHARE;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
        dest.writeString(mName);
	}
	
	@Override
	public String getName() {
		return mName;
	}
	
	@Override
	public boolean createNewFile() {
		return false;
	}
	
	@Override
	public boolean delete() {
        if (getContext() != null) {
            String selection = FileEntryConstants.KEY_SMB_DISPLAY_NAME + " IS ?";
            String[] selectionArgs = new String[]{mName};
            int result = getSettings().deleteSmbShare(selection, selectionArgs);
            return result == 1;
        }
        return false;
	}
	
	@Override
	public boolean mkdir() {
		return false;
	}
	
	@Override
	public boolean mkdirs() {
		return false;
	}
	
	@Override
	public boolean setReadOnly() {
		return false;
	}
	
	@Override
	public boolean setReadWrite() {
		return false;
	}
	
	@Override
	public boolean setLastModified(long time) {
		return false;
	}
}

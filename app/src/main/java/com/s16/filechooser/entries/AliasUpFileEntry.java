package com.s16.filechooser.entries;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryFilter;
import com.s16.filechooser.utils.FileEntryTypes;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AliasUpFileEntry extends BaseFileEntry {

	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

		public FileEntry createFromParcel(Parcel data) {
			return new AliasUpFileEntry(data);
		}

		public FileEntry[] newArray(int size) {
			return new AliasUpFileEntry[size];
		}

	};

	public AliasUpFileEntry(FileSettings settings, FileEntry baseEntry) {
		super(settings, baseEntry, FileEntryConstants.ALIAS_UP_ID);
		mEntryType = FileEntryTypes.ALIAS_UP;
	}
	
	protected AliasUpFileEntry(FileSettings settings, FileEntry baseEntry, long id) {
		super(settings, baseEntry, id);
		mEntryType = FileEntryTypes.ALIAS_UP;
	}

	public AliasUpFileEntry(Parcel source) {
		super(source);
        mEntryType = FileEntryTypes.ALIAS_UP;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}
	
	@Override
	public String getPath() {
		if (mParent != null) {
			return mParent.getPath();
		}
		return null;
	}

	@Override
	public boolean exists() {
		if (mParent != null) {
			return mParent.exists();
		}
		return false;
	}
	
	@Override
	public boolean isAbsolute() {
		if (mParent != null) {
			return mParent.isAbsolute();
		}
		return false;
	}

	@Override
	public boolean isDirectory() {
		if (mParent != null) {
			return mParent.isDirectory();
		}
		return false;
	}

	@Override
	public boolean isFile() {
		if (mParent != null) {
			return mParent.isFile();
		}
		return false;
	}

	@Override
	public boolean isHidden() {
		if (mParent != null) {
			return mParent.isHidden();
		}
		return false;
	}

	@Override
	public boolean isReadonly() {
		if (mParent != null) {
			return mParent.isReadonly();
		}
		return false;
	}

	@Override
	public boolean isRemovable() {
		if (mParent != null) {
			return mParent.isRemovable();
		}
		return false;
	}

	@Override
	public boolean hasChild() {
		if (mParent != null) {
			return mParent.hasChild();
		}
		return false;
	}
	
	@Override
	public boolean canExecute() {
		if (mParent != null) {
			return mParent.canExecute();
		}
		return false;
	}

	@Override
	public boolean canRead() {
		if (mParent != null) {
			return mParent.canRead();
		}
		return false;
	}
	
	@Override
	public boolean canWrite() {
		if (mParent != null) {
			return mParent.canWrite();
		}
		return false;
	}

	@Override
	public FileEntry getParent() {
		if (mParent != null) {
			return mParent.getParent();
		}
		return null;
	}

	@Override
	public String getAbsolutePath() {
		if (mParent != null) {
			return mParent.getAbsolutePath();
		}
		return null;
	}

	@Override
	public String getCanonicalPath() {
		if (mParent != null) {
			return mParent.getCanonicalPath();
		}
		return null;
	}

	@Override
	public String getName() {
		return FileEntryConstants.ALIAS_UP;
	}
	
	@Override
	public long getFreeSpace() {
		if (mParent != null) {
			return mParent.getFreeSpace();
		}
		return 0;
	}

	@Override
	public long getTotalSpace() {
		if (mParent != null) {
			return mParent.getTotalSpace();
		}
		return 0;
	}

	@Override
	public long getUsableSpace() {
		if (mParent != null) {
			return mParent.getUsableSpace();
		}
		return 0;
	}

	@Override
	public long lastModified() {
		if (mParent != null) {
			return mParent.lastModified();
		}
		return 0;
	}

	@Override
	public long length() {
		if (mParent != null) {
			return mParent.length();
		}
		return 0;
	}
	
	@Override
	public boolean createNewFile() {
		if (mParent != null) {
			return mParent.createNewFile();
		}
		return false;
	}

	@Override
	public boolean delete() {
		if (mParent != null) {
			return mParent.delete();
		}
		return false;
	}

	@Override
	public boolean mkdir() {
		if (mParent != null) {
			return mParent.mkdir();
		}
		return false;
	}

	@Override
	public boolean mkdirs() {
		if (mParent != null) {
			return mParent.mkdirs();
		}
		return false;
	}

	@Override
	public boolean setReadOnly() {
		if (mParent != null) {
			return mParent.setReadOnly();
		}
		return false;
	}

	@Override
	public boolean setReadWrite() {
		if (mParent != null) {
			return mParent.setReadWrite();
		}
		return false;
	}
	
	@Override
	public boolean setLastModified(long time) {
		if (mParent != null) {
			return mParent.setLastModified(time);
		}
		return false;
	}
	
	@Override
	public URL toURL() throws MalformedURLException {
		if (mParent != null) {
			return mParent.toURL();
		}
		return null;
	}
	
	@Override
	public Uri toUri() {
		if (mParent != null) {
			return mParent.toUri();
		}
		return null;
	}

	@Override
	public void listFiles(List<FileEntry> list) {
		if (mParent != null) {
			mParent.listFiles(list);
		}
	}

	@Override
	public void listFiles(List<FileEntry> list, FileEntryFilter filter) {
		if (mParent != null) {
			mParent.listFiles(list, filter);
		}
	}
}

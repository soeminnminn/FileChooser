package com.s16.filechooser.entries;

import android.os.Parcel;
import android.os.Parcelable;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryTypes;


public class RootFileEntry extends LocalFileEntry {

	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

		public FileEntry createFromParcel(Parcel data) {
			return new RootFileEntry(data);
		}

		public FileEntry[] newArray(int size) {
			return new RootFileEntry[size];
		}

	};

	public RootFileEntry(FileSettings settings, FileEntry parent) {
		super(settings, parent, FileEntryConstants.ROOT_ID, FileEntryConstants.ROOT);
		mEntryType = FileEntryTypes.ROOT;
	}

	protected RootFileEntry(FileSettings settings, FileEntry parent, long id) {
		super(settings, parent, id, FileEntryConstants.ROOT);
		mEntryType = FileEntryTypes.ROOT;
	}

	public RootFileEntry(Parcel source) {
		super(source);
		mEntryType = FileEntryTypes.ROOT;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}
	
	@Override
	public boolean delete() {
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
	public boolean setLastModified(long time) {
		return false;
	}
}
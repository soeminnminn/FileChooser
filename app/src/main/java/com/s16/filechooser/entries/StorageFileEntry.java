package com.s16.filechooser.entries;

import android.os.Parcel;
import android.os.Parcelable;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryTypes;

public class StorageFileEntry extends LocalFileEntry {

	private final String mName;
	private final boolean mIsReadonly;
	private final boolean mIsRemovable;

	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

		public FileEntry createFromParcel(Parcel data) {
			return new HomeFileEntry(data);
		}

		public FileEntry[] newArray(int size) {
			return new HomeFileEntry[size];
		}

	};
	
	public StorageFileEntry(FileSettings settings, FileEntry parent, long id, String path,
							String name, boolean readonly, boolean removable) {
		super(settings, parent, id, path);
		mName = name;
		mIsReadonly = readonly;
		mIsRemovable = removable;
		mEntryType = FileEntryTypes.STORAGE;
	}

	public StorageFileEntry(Parcel source) {
		super(source);
        mName = source.readString();
		mIsReadonly = source.readByte() == 1;
		mIsRemovable = source.readByte() == 1;
		mEntryType = FileEntryTypes.HOME;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
        dest.writeString(mName);
        dest.writeByte(mIsReadonly ? (byte)1 : (byte)0);
        dest.writeByte(mIsRemovable ? (byte)1 : (byte)0);
	}
	
	@Override
	public boolean isReadonly() {
		return mIsReadonly;
	}

	@Override
	public boolean isRemovable() {
		return mIsRemovable;
	}
	
	@Override
	public String getName() {
		return mName;
	}
}

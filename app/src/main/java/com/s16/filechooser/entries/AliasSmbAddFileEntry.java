package com.s16.filechooser.entries;

import android.os.Parcel;
import android.os.Parcelable;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryTypes;

public class AliasSmbAddFileEntry extends AliasUpFileEntry {

	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

		public FileEntry createFromParcel(Parcel data) {
			return new AliasSmbAddFileEntry(data);
		}

		public FileEntry[] newArray(int size) {
			return new AliasSmbAddFileEntry[size];
		}

	};

	public AliasSmbAddFileEntry(FileSettings settings, FileEntry baseEntry) {
		super(settings, baseEntry, FileEntryConstants.ALIAS_SMB_ADD_ID);
		mEntryType = FileEntryTypes.ALIAS_SMB_ADD;
	}

    public AliasSmbAddFileEntry(Parcel source) {
        super(source);
        mEntryType = FileEntryTypes.ALIAS_SMB_ADD;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

	@Override
	public String getName() {
		return FileEntryConstants.ALIAS_ADD_NETWORK;
	}

}

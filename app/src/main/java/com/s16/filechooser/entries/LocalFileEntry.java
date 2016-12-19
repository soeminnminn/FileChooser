package com.s16.filechooser.entries;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryComparator;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryFilter;
import com.s16.filechooser.utils.FileEntryTypes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class LocalFileEntry extends BaseFileEntry {
	
	private File mFile;
	
	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

		public FileEntry createFromParcel(Parcel data) {
			return new LocalFileEntry(data);
		}

		public FileEntry[] newArray(int size) {
			return new LocalFileEntry[size];
		}

	};

	public LocalFileEntry(FileSettings settings, FileEntry parent, long id, String path) {
		super(settings, parent, id);
		if(TextUtils.isEmpty(path)) throw new IllegalArgumentException();
		mFile = new File(path);
		if (mFile.isDirectory())
			mEntryType = FileEntryTypes.DIRECTORY;
		else
			mEntryType = FileEntryTypes.FILE;
	}
	
	public LocalFileEntry(FileSettings settings, FileEntry parent, long id, File file) {
		super(settings, parent, id);
		if(file == null) throw new IllegalArgumentException();
		mFile = file;
		if (mFile.isDirectory())
			mEntryType = FileEntryTypes.DIRECTORY;
		else
			mEntryType = FileEntryTypes.FILE;
	}
	
	public LocalFileEntry(Parcel source) {
		super(source);
		String path = source.readString();
		mFile = new File(path);
		if (mFile.isDirectory())
			mEntryType = FileEntryTypes.DIRECTORY;
		else
			mEntryType = FileEntryTypes.FILE;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(mFile.getPath());
	}

	@Override
	public String getPath() {
		return mFile.getPath();
	}

	@Override
	public boolean exists() {
		return mFile.exists();
	}

	@Override
	public boolean isAbsolute() {
		return mFile.isAbsolute();
	}

	@Override
	public boolean isDirectory() {
		return mFile.isDirectory();
	}

	@Override
	public boolean isFile() {
		return mFile.isFile();
	}

	@Override
	public boolean isHidden() {
		return mFile.isHidden();
	}

	@Override
	public boolean canExecute() {
		return mFile.canExecute();
	}

	@Override
	public boolean canRead() {
		return mFile.canRead();
	}

	@Override
	public boolean canWrite() {
		return mFile.canWrite();
	}

	@Override
	public String getAbsolutePath() {
		return mFile.getAbsolutePath();
	}

	@Override
	public String getCanonicalPath() {
		try {
			return mFile.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getName() {
		return mFile.getName();
	}

	@Override
	public long getFreeSpace() {
		return mFile.getFreeSpace();
	}

	@Override
	public long getTotalSpace() {
		return mFile.getTotalSpace();
	}

	@Override
	public long getUsableSpace() {
		return mFile.getUsableSpace();
	}

	@Override
	public long lastModified() {
		return mFile.lastModified();
	}

	@Override
	public long length() {
		return mFile.length();
	}

	@Override
	public boolean createNewFile() {
		try {
			mFile.createNewFile();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete() {
		return mFile.delete();
	}

	@Override
	public boolean mkdir() {
		return mFile.mkdir();
	}

	@Override
	public boolean mkdirs() {
		return mFile.mkdirs();
	}

	@Override
	public boolean setReadOnly() {
		return mFile.setReadOnly();
	}

	@Override
	public boolean setReadWrite() {
		if (mFile.setReadable(true)) {
			return mFile.setWritable(true);
		}
		return false;
	}

	@Override
	public boolean setLastModified(long time) {
		return mFile.setLastModified(time);
	}

	@SuppressWarnings("deprecation")
	@Override
	public URL toURL() throws MalformedURLException {
		return mFile.toURL();
	}

	@Override
	public Uri toUri() {
		return Uri.fromFile(mFile);
	}
	
	@Override
	public String getUrl() {
		String absolutePath = getAbsolutePath();
		if (!TextUtils.isEmpty(absolutePath)) {
			return FileEntryConstants.FILE_PROTOCOL + absolutePath;
		}
		return null;
	}

	@Override
	public void listFiles(List<FileEntry> list, FileEntryFilter filter) {
        if (isDirectory()) {
			list.clear();
			if (getSettings().isShowAliasUp()) {
				list.add(new AliasUpFileEntry(getSettings(), getParent()));
			}
			File[] files = null;
			if (filter != null) {
				files = mFile.listFiles(filter);
			} else {
				files = mFile.listFiles();
			}
			if (files != null) {
				boolean showHidden = getSettings().isShowHidden();
				Arrays.sort(files, new FileEntryComparator<File>(getSettings()));
				for(File f : files) {
                    if (!showHidden && f.isHidden()) {
                        continue;
                    }
					list.add(new LocalFileEntry(getSettings(), this, -1, f));
				}
			}
		}
	}

}

package com.s16.filechooser.entries;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.s16.filechooser.utils.FileEntryFilter;
import com.s16.filechooser.utils.FileEntryTypes;

import android.net.Uri;
import android.os.Parcelable;

/**
 * Created by SMM on 8/29/2016.
 */
public interface FileEntry extends Parcelable {

    public long getId();

    public Object getField(String key);

    public String getPath();

    public boolean exists();

    public boolean isAbsolute();

    public boolean isDirectory();

    public boolean isFile();

    public boolean isHidden();

    public boolean isReadonly();

    public boolean isRemovable();

    public boolean hasChild();

    public boolean canExecute();

    public boolean canRead();

    public boolean canWrite();

    public FileEntry getParent();

    public String getAbsolutePath();

    public String getCanonicalPath();

    public String getName();

    public long getFreeSpace();

    public long getTotalSpace();

    public long getUsableSpace();

    public long lastModified();

    public long length();

    public FileEntryTypes getEntryType();

    public boolean createNewFile();

    public boolean delete();

    public boolean mkdir();

    public boolean mkdirs();

    public boolean setReadOnly();

    public boolean setReadWrite();

    public boolean setLastModified(long time);

    public URL toURL() throws MalformedURLException;

    public Uri toUri();

    public String getUrl();

    public void listFiles(List<FileEntry> list);

    public void listFiles(List<FileEntry> list, FileEntryFilter filter);

    public String getMD5hash();
}

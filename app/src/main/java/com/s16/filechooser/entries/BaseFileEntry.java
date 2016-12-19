package com.s16.filechooser.entries;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryFilter;
import com.s16.filechooser.utils.FileEntryTypes;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;

public abstract class BaseFileEntry implements FileEntry {

    private FileSettings mSettings;
    protected FileEntry mParent;
    protected long mId;
    protected FileEntryTypes mEntryType = FileEntryTypes.NONE;

    public BaseFileEntry(FileSettings settings, FileEntry parent, long id) {
        mSettings = settings;
        mParent = parent;
        mId = id;
    }

    public BaseFileEntry(FileSettings settings, FileEntry parent, long id, FileEntryTypes entryType) {
        mSettings = settings;
        mParent = parent;
        mId = id;
        mEntryType = entryType;
    }

    public BaseFileEntry(Parcel source) {
        mId = source.readLong();
        mEntryType = FileEntryTypes.fromNativeInt(source.readInt());
    }

    protected Context getContext() {
        return mSettings;
    }

    protected FileSettings getSettings() {
        return mSettings;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mEntryType.getNativeValue());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public Object getField(String key) {
        if (TextUtils.isEmpty(key)) return null;
        String methodName = FileEntryConstants.MAP_KEY_METHOD.get(key);

        try {
            Method method = BaseFileEntry.class.getMethod(methodName);
            if (method != null) {
                return method.invoke(this);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isReadonly() {
        return (exists() && canRead() && !canWrite());
    }

    @Override
    public boolean isRemovable() {
        return false;
    }

    @Override
    public boolean hasChild() {
        return isDirectory();
    }

    @Override
    public boolean canExecute() {
        return false;
    }

    @Override
    public boolean canRead() {
        return false;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public FileEntry getParent() {
        return mParent;
    }

    @Override
    public String getAbsolutePath() {
        return null;
    }

    @Override
    public String getCanonicalPath() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public long getFreeSpace() {
        return 0;
    }

    @Override
    public long getTotalSpace() {
        return 0;
    }

    @Override
    public long getUsableSpace() {
        return 0;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public FileEntryTypes getEntryType() {
        return mEntryType;
    }

    @Override
    public boolean createNewFile() {
        return false;
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
    public boolean setReadWrite() {
        return false;
    }

    @Override
    public boolean setLastModified(long time) {
        return false;
    }

    @Override
    public URL toURL() throws MalformedURLException {
        return null;
    }

    @Override
    public Uri toUri() {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public int hashCode() {
        int entryType = getEntryType().getNativeValue();
        String url = getUrl();
        String codeStr = entryType + "";
        if (!TextUtils.isEmpty(url)) {
            codeStr += url;
        }
        return codeStr.hashCode();
    }

    @Override
    public String getMD5hash() {
        int entryType = getEntryType().getNativeValue();
        String url = getUrl();
        String codeStr = entryType + "";
        if (!TextUtils.isEmpty(url)) {
            codeStr += url;
        }
        return md5(codeStr);
    }

    @Override
    public void listFiles(List<FileEntry> list) {
        listFiles(list, null);
    }

    @Override
    public void listFiles(List<FileEntry> list, FileEntryFilter filter) {
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            StringBuilder hexString = new StringBuilder();
            for (byte digestByte : md.digest(input.getBytes()))
                hexString.append(String.format("%02X", digestByte));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

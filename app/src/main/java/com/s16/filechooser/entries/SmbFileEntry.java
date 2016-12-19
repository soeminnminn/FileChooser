package com.s16.filechooser.entries;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryComparator;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryFilter;
import com.s16.filechooser.utils.FileEntryTypes;

public class SmbFileEntry extends BaseFileEntry {
	
	private String mDomain;
	private String mUserName;
	private String mPassword;
	private SmbFile mFile;
	
	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {
		
		public FileEntry createFromParcel(Parcel data) {
			return new SmbFileEntry(data);
		}
		
		public FileEntry[] newArray(int size) {
			return new SmbFileEntry[size];
		}
		
	};
	
	public SmbFileEntry(FileSettings settings, FileEntry parent, long id, String url, String domain, String userName, String password) {
		super(settings, parent, id);
		if(TextUtils.isEmpty(url)) throw new IllegalArgumentException();
		if (!url.startsWith(FileEntryConstants.SMB_PROTOCOL)) throw new IllegalArgumentException();
		
		mDomain = domain;
		mUserName = userName;
		mPassword = password;
		
		if (TextUtils.isEmpty(userName)) {
			try {
				mFile = new SmbFile(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}	
		} else {
			try {
				mFile = new SmbFile(url, new NtlmPasswordAuthentication(domain, userName, password));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		if (mFile != null) {
			if (isDirectory())
				mEntryType = FileEntryTypes.SMB_DIRECTORY;
			else
				mEntryType = FileEntryTypes.SMB_FILE;
		}
	}
	
	public SmbFileEntry(FileSettings settings, FileEntry parent, long id, SmbFile file) {
		super(settings, parent, id);
		if(file == null) throw new IllegalArgumentException();
		mFile = file;
		
		if (mFile != null) {
			if (isDirectory())
				mEntryType = FileEntryTypes.SMB_DIRECTORY;
			else
				mEntryType = FileEntryTypes.SMB_FILE;
		}
	}
	
	public SmbFileEntry(Parcel source) {
		super(source);
		String url = source.readString();
		if (!TextUtils.isEmpty(url)) {
			Bundle urlBundle = parseUrl(url);
			
			String fileUrl = FileEntryConstants.SMB_PROTOCOL;
			fileUrl += urlBundle.getString(FileEntryConstants.KEY_SMB_SERVER);
			fileUrl += urlBundle.getString(FileEntryConstants.KEY_SMB_PATH);
			
			mDomain = urlBundle.getString(FileEntryConstants.KEY_SMB_DOMAIN);
			mUserName = urlBundle.getString(FileEntryConstants.KEY_SMB_USER_NAME);
			mPassword = urlBundle.getString(FileEntryConstants.KEY_SMB_PASSWORD);
			
			if (TextUtils.isEmpty(mUserName)) {
				try {
					mFile = new SmbFile(fileUrl);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}	
			} else {
				try {
					mFile = new SmbFile(fileUrl, new NtlmPasswordAuthentication(mDomain, mUserName, mPassword));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			
			if (mFile != null) {
				if (isDirectory())
					mEntryType = FileEntryTypes.SMB_DIRECTORY;
				else
					mEntryType = FileEntryTypes.SMB_FILE;
			}
		}
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(getUrl());
	}
	
	@Override
	public Object getField(String key) {
		if (TextUtils.isEmpty(key)) return null;
		String methodName = FileEntryConstants.MAP_KEY_METHOD.get(key);
		
		try {
			Method method = SmbFileEntry.class.getMethod(methodName);
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
		if (mFile != null) {
			return mFile.getPath();
		}
		return null;
	}

	@Override
	public boolean exists() {
		if (mFile != null) {
			try {
				return mFile.exists();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean isDirectory() {
		if (mFile != null) {
			try {
				return mFile.isDirectory();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean isFile() {
		if (mFile != null) {
			try {
				return mFile.isFile();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean isHidden() {
		if (mFile != null) {
			try {
				return mFile.isHidden();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean isRemovable() {
		return true;
	}

	@Override
	public boolean canRead() {
		if (mFile != null) {
			try {
				return mFile.canRead();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean canWrite() {
		if (mFile != null) {
			try {
				return mFile.canWrite();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public FileEntry getParent() {
		return mParent;
	}

	@Override
	public String getAbsolutePath() {
		if (mFile != null) {
			return mFile.getPath();
		}
		return null;
	}

	@Override
	public String getCanonicalPath() {
		if (mFile != null) {
			return mFile.getCanonicalPath();
		}
		return null;
	}
	
	public String getDomain() {
		return mDomain;
	}
	
	public String getUserName() {
		return mUserName;
	}
	
	public String getPassword() {
		return mPassword;
	}
	
	public String getShare() {
		if (mFile != null) {
			return mFile.getShare();
		}
		return null;
	}
	
	public String getServer() {
		if (mFile != null) {
			return mFile.getServer();
		}
		return null;
	}

	@Override
	public String getName() {
		if (mFile != null) {
			return mFile.getName();
		}
		return null;
	}

	@Override
	public long getFreeSpace() {
		if (mFile != null) {
			try {
				mFile.getDiskFreeSpace();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public long lastModified() {
		if (mFile != null) {
			try {
				return mFile.lastModified();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public long length() {
		if (mFile != null) {
			try {
				return mFile.length();
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public FileEntryTypes getEntryType() {
		if (isDirectory()) {
			return FileEntryTypes.SMB_DIRECTORY;
		} else {
			return FileEntryTypes.SMB_FILE;
		}
	}

	@Override
	public boolean createNewFile() {
		if (mFile != null) {
			try {
				mFile.createNewFile();
				return true;
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean delete() {
		if (mFile != null) {
			try {
				mFile.delete();
				return true;
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean mkdir() {
		if (mFile != null) {
			try {
				mFile.mkdir();
				return true;
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean mkdirs() {
		if (mFile != null) {
			try {
				mFile.mkdirs();
				return true;
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean setReadOnly() {
		if (mFile != null) {
			try {
				mFile.setReadOnly();
				return true;
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean setReadWrite() {
		if (mFile != null) {
			try {
				mFile.setReadWrite();
				return true;
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public boolean setLastModified(long time) {
		if (mFile != null) {
			try {
				mFile.setLastModified(time);
				return true;
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public URL toURL() throws MalformedURLException {
		if (mFile != null) {
			return mFile.toURL();
		}
		return null;
	}
	
	@Override
	public Uri toUri() {
		String url = getUrl();
		if (!TextUtils.isEmpty(url)) {
			return Uri.parse(url);
		}
		return null;
	}
	
	// smb://[[<domain>;]<username>[:<password>]@]<server>/<share>/<path>
	// smb://[[<domain>;]<username>[:<password>]@]<server>[/<share>[/<path>]]
	// smb://[[<domain>;]<username>[:<password>]@]<server>[:<port>][/[<share>[/[<path>]]][?[<param>=<value>[<param2>=<value2>[...]]]]]
	// smb://[<user>@]<workgroup>[:<port>][/]
	// smb://[<user>@]<host>[:<port>][/[<path>]][?<param1>=<value1>[;<param2>=<value2>]]
	@Override
	public String getUrl() {
		String value = FileEntryConstants.SMB_PROTOCOL;
		if (!TextUtils.isEmpty(getUserName())) {
			if (!TextUtils.isEmpty(getDomain())) {
				value += getDomain() + ";";
			}
			value += getUserName() + ":";
			value += getPassword() + "@";
		}
		value += getServer();
		
		if (!TextUtils.isEmpty(getShare())) {
			value += "/" + getShare();
		}
		
		String path = getPath();
		if (!TextUtils.isEmpty(path)) {
			value += path.charAt(0) == '/' ? path : "/" + path;
		}
		
		return value;
	}
	
	private static Bundle parseUrl(String smbUrlString) {
		if (!TextUtils.isEmpty(smbUrlString)) {
			if (!smbUrlString.startsWith(FileEntryConstants.SMB_PROTOCOL)) return Bundle.EMPTY;
			
			Bundle retValue = new Bundle();
			Uri uri = Uri.parse(smbUrlString);
			String authentication = uri.getUserInfo();
			if (!TextUtils.isEmpty(authentication)) {
				int idx = authentication.indexOf(';');
				if (idx > 0) {
					retValue.putString(FileEntryConstants.KEY_SMB_DOMAIN, authentication.substring(0, idx));
					authentication = authentication.substring(idx + 1);
				}
				idx = authentication.indexOf(':');
				if (idx > 0) {
					retValue.putString(FileEntryConstants.KEY_SMB_USER_NAME, authentication.substring(0, idx));
					retValue.putString(FileEntryConstants.KEY_SMB_PASSWORD, authentication.substring(idx + 1));
				} else {
					retValue.putString(FileEntryConstants.KEY_SMB_USER_NAME, authentication);
				}
			}
			retValue.putString(FileEntryConstants.KEY_SMB_SERVER, uri.getHost());
			String path = uri.getPath();
			if (!TextUtils.isEmpty(path)) {
				retValue.putString(FileEntryConstants.KEY_SMB_PATH, path.charAt(0) == '/' ? path : "/" + path);
			}
			
			return retValue;
		}
		return Bundle.EMPTY;
	}

	@Override
	public void listFiles(List<FileEntry> list) {
		listFiles(list, null);
	}

	@Override
	public void listFiles(List<FileEntry> list, FileEntryFilter filter) {
		if (isDirectory()) {
			list.clear();
			if (getSettings().isShowAliasUp()) {
				list.add(new AliasUpFileEntry(getSettings(), getParent()));
			}
			if (mFile != null) {
				try {
					if (mFile.canRead()) {
						boolean showHidden = getSettings().isShowHidden();
						SmbFile[] files = mFile.listFiles();
						Arrays.sort(files, new FileEntryComparator<SmbFile>(getSettings()));
						for(SmbFile f : files) {
                            if (!showHidden && f.isHidden()) {
                                continue;
                            }
							list.add(new SmbFileEntry(getSettings(), this, -1, f));
						}
					}
				} catch (SmbException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

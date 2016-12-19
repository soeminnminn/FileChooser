package com.s16.filechooser.entries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import com.s16.filechooser.FileSettings;
import com.s16.filechooser.utils.FileEntryConstants;
import com.s16.filechooser.utils.FileEntryFilter;
import com.s16.filechooser.utils.FileEntryTypes;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class HomeFileEntry extends RootFileEntry {

	public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {

		public FileEntry createFromParcel(Parcel data) {
			return new HomeFileEntry(data);
		}

		public FileEntry[] newArray(int size) {
			return new HomeFileEntry[size];
		}

	};
	
	public HomeFileEntry(FileSettings settings, FileEntry parent) {
		super(settings, parent, FileEntryConstants.HOME_ID);
		mEntryType = FileEntryTypes.HOME;
	}

    public HomeFileEntry(Parcel source) {
        super(source);
        mEntryType = FileEntryTypes.HOME;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

	@Override
	public String getName() {
		return FileEntryConstants.HOME;
	}

	@Override
	public FileEntryTypes getEntryType() {
		return FileEntryTypes.HOME;
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
	public void listFiles(List<FileEntry> list) {
		listFiles(list, null);
	}

	@Override
	public void listFiles(List<FileEntry> list, FileEntryFilter filter) {
		getStorageList(list);
		if (getSettings().isShowNetwork()) {
			list.add(new NetworkFileEntry(getSettings(), this, -1));
		}
	}
	
	protected String getName(boolean removable, int number) {
		String name = "";
		if (!removable) {
			name = "Internal Storage";
        } else if (number > 1) {
        	name = "SD card " + number;
        } else {
        	name = "SD card";
        }
		return name;
	}
	
	protected void getStorageList(List<FileEntry> list) {
		String defPath = Environment.getExternalStorageDirectory().getPath();
        boolean defPathRemovable = Environment.isExternalStorageRemovable();
        String defPathState = Environment.getExternalStorageState();
        boolean defPathAvailable = defPathState.equals(Environment.MEDIA_MOUNTED)
                                    || defPathState.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean defPathReadonly = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);

        HashSet<String> paths = new HashSet<String>();
        long id = -16;
        int curRemovableNumber = 1;

        if (defPathAvailable) {
            paths.add(defPath);
            String name = getName(defPathRemovable, curRemovableNumber);
            list.add(new StorageFileEntry(getSettings(), this, --id, defPath, name, defPathReadonly, defPathRemovable));
        }
        
        String secPath = System.getenv("SECONDARY_STORAGE");
        if (!TextUtils.isEmpty(secPath) && !paths.contains(secPath)) {
        	paths.add(secPath);
        	String name = getName(defPathRemovable, curRemovableNumber);
            list.add(new StorageFileEntry(getSettings(), this, --id, secPath, name, defPathReadonly, defPathRemovable));
        }

        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            //Log.d(TAG, "/proc/mounts");
            while ((line = bufReader.readLine()) != null) {
                //Log.d(TAG, line);
                if (line.contains("vfat") || line.contains("/mnt")) {
                    StringTokenizer tokens = new StringTokenizer(line, " ");
                    @SuppressWarnings("unused")
					String unused = tokens.nextToken(); //device
                    String mount_point = tokens.nextToken(); //mount point
                    if (paths.contains(mount_point)) {
                        continue;
                    }
                    unused = tokens.nextToken(); //file system
                    List<String> flags = Arrays.asList(tokens.nextToken().split(",")); //flags
                    boolean readonly = flags.contains("ro");

                    if (line.contains("/dev/block/vold")) {
                        if (!line.contains("/mnt/secure")
                            && !line.contains("/mnt/asec")
                            && !line.contains("/mnt/obb")
                            && !line.contains("/dev/mapper")
                            && !line.contains("tmpfs")) {
                            paths.add(mount_point);
                            
                            String name = getName(defPathRemovable, curRemovableNumber++);
                            list.add(new StorageFileEntry(getSettings(), this, --id, mount_point, name, readonly, true));
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException ex) {
                	ex.printStackTrace();
                }
            }
        }
	}
}

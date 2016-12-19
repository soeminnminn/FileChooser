package com.s16.filechooser.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.provider.BaseColumns;

public class FileEntryConstants {

	public static final String ALIAS_UP = "..";
	public static final String ALIAS_ADD_NETWORK = "Add Network";
	public static final String HOME = "Home";
	public static final String NETWORK = "Network";
	
	public static final long ROOT_ID = -1;
	public static final long HOME_ID = -3;
	public static final long ALIAS_UP_ID = -2;
	public static final long ALIAS_SMB_ADD_ID = -8;
	
	public static final String ROOT = "/";
	public static final String NETWORK_PATH = "/mnt/smb";
	public static final String NETWORK_ADD = "/mnt/smb/add";
	public static final String STORAGE_EMULATED = "/storage/emulated";
	public static final String SMB_PROTOCOL = "smb://";
	
	public static final String FILE_PROTOCOL = "file://";
	
	public static final String SMB_SHARE_AUTHORITY = "com.s16.filechooser.SmbShareContentProvider";
	public static final String SCHEME = "content://";
	public static final Uri SMB_SHARE_CONTENT_URI = Uri.parse(SCHEME + SMB_SHARE_AUTHORITY);
	
	public static final String KEY_ID = "fs_id";
	public static final String KEY_PATH = "fs_path";
	public static final String KEY_IS_EXISTS = "fs_exists";
	public static final String KEY_IS_ABSOLUTE = "fs_absolute";
	public static final String KEY_IS_DIRECTORY = "fs_is_directory";
	public static final String KEY_IS_FILE = "fs_is_file";
	public static final String KEY_IS_HIDDEN = "fs_is_hidden";
	public static final String KEY_IS_READONLY = "fs_is_readonly";
	public static final String KEY_IS_REMOVABLE = "fs_is_removable";
	public static final String KEY_HAS_CHILD = "fs_has_child";
	public static final String KEY_CAN_EXECUTE = "fs_can_execute";
	public static final String KEY_CAN_READ = "fs_can_read";
	public static final String KEY_CAN_WRITE = "fs_can_write";
	public static final String KEY_PARENT = "fs_parent";
	public static final String KEY_ABSOLUTE_PATH = "fs_absolute_path";
	public static final String KEY_CANONICAL_PATH = "fs_canonical_path";
	public static final String KEY_NAME = "fs_name";
	public static final String KEY_FREE_SPACE = "fs_free_space";
	public static final String KEY_TOTAL_SPACE = "fs_total_space";
	public static final String KEY_USABLE_SPACE = "fs_usable_space";
	public static final String KEY_LAST_MODIFIED = "fs_last_modified";
	public static final String KEY_LENGTH = "fs_length";
	public static final String KEY_ENTRY_TYPE = "fs_entry_type";
	
	public static final String KEY_SMB_DISPLAY_NAME = "fs_smb_name";
	public static final String KEY_SMB_SERVER = "fs_smb_server";
	public static final String KEY_SMB_PATH = "fs_smb_path";
	public static final String KEY_SMB_SHARE = "fs_smb_share";
	public static final String KEY_SMB_DOMAIN = "fs_smb_domain";
	public static final String KEY_SMB_USER_NAME = "fs_smb_user";
	public static final String KEY_SMB_PASSWORD = "fs_smb_password";
	
	public static final String[] SMB_SHARE_FIELDS = new String[] { 
		BaseColumns._ID, 
		FileEntryConstants.KEY_SMB_DISPLAY_NAME, 
		FileEntryConstants.KEY_SMB_SERVER, 
		FileEntryConstants.KEY_SMB_DOMAIN, 
		FileEntryConstants.KEY_SMB_USER_NAME,
		FileEntryConstants.KEY_SMB_PASSWORD 
	};
	
	public static Map<String, String> MAP_KEY_METHOD = new HashMap<String, String>();
	static {
		MAP_KEY_METHOD.put(KEY_ID, "getId");
		MAP_KEY_METHOD.put(KEY_PATH, "getPath");
		MAP_KEY_METHOD.put(KEY_IS_EXISTS, "exists");
		MAP_KEY_METHOD.put(KEY_IS_ABSOLUTE, "isAbsolute");
		MAP_KEY_METHOD.put(KEY_IS_DIRECTORY, "isDirectory");
		MAP_KEY_METHOD.put(KEY_IS_FILE, "isFile");
		MAP_KEY_METHOD.put(KEY_IS_HIDDEN, "isHidden");
		MAP_KEY_METHOD.put(KEY_IS_READONLY, "isReadonly");
		MAP_KEY_METHOD.put(KEY_IS_REMOVABLE, "isRemovable");
		MAP_KEY_METHOD.put(KEY_HAS_CHILD, "hasChild");
		MAP_KEY_METHOD.put(KEY_CAN_EXECUTE, "canExecute");
		MAP_KEY_METHOD.put(KEY_CAN_READ, "canRead");
		MAP_KEY_METHOD.put(KEY_CAN_WRITE, "canWrite");
		MAP_KEY_METHOD.put(KEY_PARENT, "getParent");
		MAP_KEY_METHOD.put(KEY_ABSOLUTE_PATH, "getAbsolutePath");
		MAP_KEY_METHOD.put(KEY_CANONICAL_PATH, "getCanonicalPath");
		MAP_KEY_METHOD.put(KEY_NAME, "getName");
		MAP_KEY_METHOD.put(KEY_FREE_SPACE, "getFreeSpace");
		MAP_KEY_METHOD.put(KEY_TOTAL_SPACE, "getTotalSpace");
		MAP_KEY_METHOD.put(KEY_USABLE_SPACE, "getUsableSpace");
		MAP_KEY_METHOD.put(KEY_LAST_MODIFIED, "lastModified");
		MAP_KEY_METHOD.put(KEY_LENGTH, "length");
		MAP_KEY_METHOD.put(KEY_ENTRY_TYPE, "getEntryType");
		
		MAP_KEY_METHOD.put(KEY_SMB_DISPLAY_NAME, "getName");
		MAP_KEY_METHOD.put(KEY_SMB_SERVER, "getServer");
		MAP_KEY_METHOD.put(KEY_SMB_SHARE, "getShare");
		MAP_KEY_METHOD.put(KEY_SMB_DOMAIN, "getDomain");
		MAP_KEY_METHOD.put(KEY_SMB_USER_NAME, "getUserName");
		MAP_KEY_METHOD.put(KEY_SMB_PASSWORD, "getPassword");
	}
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
	public static final SimpleDateFormat DATE_FORMAT_CURRENT_YEAR = new SimpleDateFormat("MMM dd");
	
}
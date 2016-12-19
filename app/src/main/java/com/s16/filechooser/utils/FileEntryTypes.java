package com.s16.filechooser.utils;

public enum FileEntryTypes {
	
	NONE (0),
	ROOT (1),
	ALIAS_UP (2),
	HOME (3),
	FILE (4),
	DIRECTORY (5),
	STORAGE (6),
	NETWORK (7),
	ALIAS_SMB_ADD (8),
	SMB_SHARE (9),
	SMB_FILE (10),
	SMB_DIRECTORY (11);
	
	private int nativeValue;
	FileEntryTypes(int nativeVal) {
		this.nativeValue = nativeVal;
	}
	
	public static FileEntryTypes fromNativeInt(int value) {
		for(FileEntryTypes et : FileEntryTypes.values()) {
			if (et.nativeValue == value) {
				return et;
			}
		}
		return FileEntryTypes.NONE;
	}
	
	public int getNativeValue() {
		return nativeValue;
	}
}

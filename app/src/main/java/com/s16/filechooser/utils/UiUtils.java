package com.s16.filechooser.utils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import com.s16.filechooser.entries.FileEntry;

public class UiUtils {

	public static int LOWORD(int val) { return val & 0xffff; }
	public static int HIWORD(int val) { return (val >> 0x10) & 0xffff; }
	public static int MAKELPARAM(int low, int high) { return ((high << 0x10) | (low & 0xffff)); }

	public static String getReadableLastModified(Context context, FileEntry entry) {
		if (entry == null) return "";
		if (entry.getEntryType().equals(FileEntryTypes.ALIAS_SMB_ADD) || entry.getEntryType().equals(FileEntryTypes.ALIAS_UP)) {
			return "";
		}
		
		long value = entry.lastModified();
		if (value > 0) {
			Date date = new Date(value);
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.setTime(date);
            if (calendar.get(Calendar.YEAR) == currentYear) {
                return FileEntryConstants.DATE_FORMAT_CURRENT_YEAR.format(date);
            }
			return FileEntryConstants.DATE_FORMAT.format(date);
		}
		return "";
	}
	
	public static String getReadableFileSize(FileEntry entry) {
		if (entry == null) return "";
		if (!entry.getEntryType().equals(FileEntryTypes.FILE) && !entry.getEntryType().equals(FileEntryTypes.SMB_FILE)) {
			return "";
		}
		
		long size = entry.length();
		if(size <= 0) return "0 B";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int)(Math.log10(size) / Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}

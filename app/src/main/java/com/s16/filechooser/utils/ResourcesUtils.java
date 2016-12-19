package com.s16.filechooser.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.TypedValue;

import com.s16.filechooser.R;
import com.s16.filechooser.drawables.DataUriThumbnailDrawable;
import com.s16.filechooser.entries.FileEntry;

public class ResourcesUtils {

    private static String ICON_COLOR = "#828282";
	private static final Map<String, Integer> mimeTypeToResourceMap = new HashMap<String, Integer>();
	
	static {
		int icon;

		// Package
        icon = R.drawable.ic_file_apk;
        add("application/vnd.android.package-archive", icon);

        // Audio
        icon = R.drawable.ic_file_audio;
        add("application/ogg", icon);
        add("application/x-flac", icon);

        // Certificate
        icon = R.drawable.ic_file_certificate;
        add("application/pgp-keys", icon);
        add("application/pgp-signature", icon);
        add("application/x-pkcs12", icon);
        add("application/x-pkcs7-certreqresp", icon);
        add("application/x-pkcs7-crl", icon);
        add("application/x-x509-ca-cert", icon);
        add("application/x-x509-user-cert", icon);
        add("application/x-pkcs7-certificates", icon);
        add("application/x-pkcs7-mime", icon);
        add("application/x-pkcs7-signature", icon);

        // Source code
        icon = R.drawable.ic_file_codes;
        add("application/rdf+xml", icon);
        add("application/rss+xml", icon);
        add("application/x-object", icon);
        add("application/xhtml+xml", icon);
        add("text/css", icon);
        add("text/html", icon);
        add("text/xml", icon);
        add("text/x-c++hdr", icon);
        add("text/x-c++src", icon);
        add("text/x-chdr", icon);
        add("text/x-csrc", icon);
        add("text/x-dsrc", icon);
        add("text/x-csh", icon);
        add("text/x-haskell", icon);
        add("text/x-java", icon);
        add("text/x-literate-haskell", icon);
        add("text/x-pascal", icon);
        add("text/x-tcl", icon);
        add("text/x-tex", icon);
        add("application/x-latex", icon);
        add("application/x-texinfo", icon);
        add("application/atom+xml", icon);
        add("application/ecmascript", icon);
        add("application/json", icon);
        add("application/javascript", icon);
        add("application/xml", icon);
        add("text/javascript", icon);
        add("application/x-javascript", icon);

        // Compressed
        icon = R.drawable.ic_file_compressed;
        add("application/mac-binhex40", icon);
        add("application/rar", icon);
        add("application/zip", icon);
        add("application/x-apple-diskimage", icon);
        add("application/x-debian-package", icon);
        add("application/x-gtar", icon);
        add("application/x-iso9660-image", icon);
        add("application/x-lha", icon);
        add("application/x-lzh", icon);
        add("application/x-lzx", icon);
        add("application/x-stuffit", icon);
        add("application/x-tar", icon);
        add("application/x-webarchive", icon);
        add("application/x-webarchive-xml", icon);
        add("application/gzip", icon);
        add("application/x-7z-compressed", icon);
        add("application/x-deb", icon);
        add("application/x-rar-compressed", icon);

        // Contact
        icon = R.drawable.ic_file_contact;
        add("text/x-vcard", icon);
        add("text/vcard", icon);

        // Event
        icon = R.drawable.ic_file_event;
        add("text/calendar", icon);
        add("text/x-vcalendar", icon);

        // Font
        icon = R.drawable.ic_file_font;
        add("application/x-font", icon);
        add("application/font-woff", icon);
        add("application/x-font-woff", icon);
        add("application/x-font-ttf", icon);

        // Image
        icon = R.drawable.ic_file_image;
        add("application/vnd.oasis.opendocument.graphics", icon);
        add("application/vnd.oasis.opendocument.graphics-template", icon);
        add("application/vnd.oasis.opendocument.image", icon);
        add("application/vnd.stardivision.draw", icon);
        add("application/vnd.sun.xml.draw", icon);
        add("application/vnd.sun.xml.draw.template", icon);

        // PDF
        icon = R.drawable.ic_file_pdf;
        add("application/pdf", icon);

        // Presentation
        icon = R.drawable.ic_file_presentation;
        add("application/vnd.stardivision.impress", icon);
        add("application/vnd.sun.xml.impress", icon);
        add("application/vnd.sun.xml.impress.template", icon);
        add("application/x-kpresenter", icon);
        add("application/vnd.oasis.opendocument.presentation", icon);

        // Spreadsheet
        icon = R.drawable.ic_file_spreadsheet;
        add("application/vnd.oasis.opendocument.spreadsheet", icon);
        add("application/vnd.oasis.opendocument.spreadsheet-template", icon);
        add("application/vnd.stardivision.calc", icon);
        add("application/vnd.sun.xml.calc", icon);
        add("application/vnd.sun.xml.calc.template", icon);
        add("application/x-kspread", icon);

        // Text
        icon = R.drawable.ic_file_text;
        add("application/vnd.oasis.opendocument.text", icon);
        add("application/vnd.oasis.opendocument.text-master", icon);
        add("application/vnd.oasis.opendocument.text-template", icon);
        add("application/vnd.oasis.opendocument.text-web", icon);
        add("application/vnd.stardivision.writer", icon);
        add("application/vnd.stardivision.writer-global", icon);
        add("application/vnd.sun.xml.writer", icon);
        add("application/vnd.sun.xml.writer.global", icon);
        add("application/vnd.sun.xml.writer.template", icon);
        add("application/x-abiword", icon);
        add("application/x-kword", icon);

        // Video
        icon = R.drawable.ic_file_video;
        add("application/x-quicktimeplayer", icon);
        add("application/x-shockwave-flash", icon);

        // Word
        icon = R.drawable.ic_file_word;
        add("application/msword", icon);
        add("application/vnd.openxmlformats-officedocument.wordprocessingml.document", icon);
        add("application/vnd.openxmlformats-officedocument.wordprocessingml.template", icon);

        // Excel
        icon = R.drawable.ic_file_excel;
        add("application/vnd.ms-excel", icon);
        add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", icon);
        add("application/vnd.openxmlformats-officedocument.spreadsheetml.template", icon);

        // Powerpoint
        icon = R.drawable.ic_file_powerpoint;
        add("application/vnd.ms-powerpoint", icon);
        add("application/vnd.openxmlformats-officedocument.presentationml.presentation", icon);
        add("application/vnd.openxmlformats-officedocument.presentationml.template", icon);
        add("application/vnd.openxmlformats-officedocument.presentationml.slideshow", icon);
        
        // Model
        icon = R.drawable.ic_file_model;
        add("model/iges", icon);
        add("model/iges", icon);
        add("model/mesh", icon);
        add("model/mesh", icon);
        add("model/mesh", icon);
	}
	
	private static void add(String mimeType, int resId) {
		if (!mimeTypeToResourceMap.containsKey(mimeType)) {
			mimeTypeToResourceMap.put(mimeType, Integer.valueOf(resId));
		}
	}
	
	private ResourcesUtils() {}
	
	public static int guessResourceFromMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
        	return R.drawable.ic_file_generic;
        }
        Integer resId = mimeTypeToResourceMap.get(mimeType);
        if (resId != null) {
        	return resId.intValue();
        }
        
        if (mimeType.startsWith("audio/")) {
        	return R.drawable.ic_file_audio;
        	
        } else if (mimeType.startsWith("image/")) {
        	return R.drawable.ic_file_image;
        	
        } else if (mimeType.startsWith("video/")) {
			return R.drawable.ic_file_video;
			
        } else if (mimeType.startsWith("model/")) {
			return R.drawable.ic_file_model;
			
        } else if (mimeType.startsWith("text/")) {
        	return R.drawable.ic_file_text;
        }
        return R.drawable.ic_file_generic;
    }

    private static boolean hasThumbnail(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) return false;
        if (mimeType.startsWith("image/")) {
            return true;

        } else if (mimeType.startsWith("video/")) {
            return true;
        }
        return false;
    }

	public static Drawable getFileIconDrawable(Context context, FileEntry entry) {
		boolean isFile = false;
        Drawable iconDrawable = null;
		FileEntryTypes entryType = entry.getEntryType();
		if (entryType == FileEntryTypes.ALIAS_UP) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_folder_up);
		} else if (entryType == FileEntryTypes.ALIAS_SMB_ADD) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_add);
		} else if (entryType == FileEntryTypes.HOME) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_storage_home);
		} else if (entryType == FileEntryTypes.FILE) {
			isFile = true;
		} else if (entryType == FileEntryTypes.DIRECTORY) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_file_folder);
		} else if (entryType == FileEntryTypes.STORAGE) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_storage_sd);
		} else if (entryType == FileEntryTypes.NETWORK) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_storage_lan);
		} else if (entryType == FileEntryTypes.SMB_SHARE) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_folder_share);
		} else if (entryType == FileEntryTypes.SMB_FILE) {
			isFile = true;
		} else if (entryType == FileEntryTypes.SMB_DIRECTORY) {
            iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_file_folder);
		}
		
		if (isFile) {
            String mime = null;
			String fileName = entry.getName();
			Pattern pattern = Pattern.compile("^.+\\.([^\\\\]+)$"); 
			if (!TextUtils.isEmpty(fileName)) {
                Matcher matcher = pattern.matcher(fileName);
                if (matcher.matches()) {
                    String extension = null;
                    try {
                        extension = matcher.group(1);
                    } catch (Exception e) {
                    }
                    if (extension != null) {
                        extension = extension.toLowerCase();
                        mime = MimeUtils.guessMimeTypeFromExtension(extension);
                    }
                }
			}

            String filePath = entry.getPath();
            int resId = guessResourceFromMimeType(mime);
            Drawable drawable;
            iconDrawable = ContextCompat.getDrawable(context, resId);
            DrawableCompat.setTint(iconDrawable, Color.parseColor(ICON_COLOR));

            if (hasThumbnail(mime)) {
                DataUriThumbnailDrawable thumbnailDrawable = new DataUriThumbnailDrawable(context, iconDrawable);
                int imageSize = (int)getPxFromDp(context, 48);
                thumbnailDrawable.setImage(filePath, imageSize, imageSize);
                drawable = thumbnailDrawable;

            } else {
                drawable = iconDrawable;
            }
            return drawable;
		}

        if (iconDrawable != null) {
            DrawableCompat.setTint(iconDrawable, Color.parseColor(ICON_COLOR));
        }
		return iconDrawable;
	}
	
	protected static Bitmap getThumbnailBitmap(Context context, Uri uri){
	    String[] projection = { MediaStore.Images.Media.DATA };
	    // This method was deprecated in API level 11
	    // Cursor cursor = managedQuery(contentUri, proj, null, null, null);

	    CursorLoader cursorLoader = new CursorLoader(context, uri, projection, null, null, null);
	    Cursor cursor = cursorLoader.loadInBackground();

	    int columnIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
	    if (cursor.moveToFirst()) {
		    long imageId = cursor.getLong(columnIdx);
		    Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
		    		context.getContentResolver(), imageId,
		            MediaStore.Images.Thumbnails.MINI_KIND,
		            (BitmapFactory.Options)null);
		    
		    cursor.close();
		    return bitmap;
	    }
	    return null;
	}

    public static float getPxFromDp(Context context, int dp) {
        Resources rs = context.getResources();
        float pxConverter = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, rs.getDisplayMetrics());
        float px = pxConverter * dp;
        return px;
    }
}

package com.s16.filechooser;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.s16.filechooser.utils.FileEntryConstants;

/**
 * Created by SMM on 9/12/2016.
 */
public class FileSettings extends ContextWrapper
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_SORT_MODE = "fs_settings_sort_mode";
    public static final String KEY_VIEW_MODE = "fs_settings_view_mode";
    public static final String KEY_SHOW_HIDDEN = "fs_settings_show_hidden";
    public static final String KEY_SHOW_NETWORK = "fs_settings_show_network";
    public static final String KEY_SHOW_ALIAS_UP = "fs_settings_show_alias_up";
    public static final String KEY_SHOW_ROOT = "fs_settings_show_root";

    public static final int SORT_BY_NAME = 0;
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_SIZE = 2;

    public static final int VIEW_LIST = 0;
    public static final int VIEW_GRID = 1;

    private final SharedPreferences mDefaultPreferences;

    private static FileSettings INSTANCE;
    public static FileSettings getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FileSettings(context);
        }
        return INSTANCE;
    }

    private boolean mIsShowHidden = true;
    private boolean mIsShowNetwork = true;
    private boolean mIsShowAliasUp = true;
    private boolean mIsShowFromRoot = false;
    private int mSortByMode = SORT_BY_NAME;
    private int mViewMode = VIEW_LIST;

    private FileSettings(Context context) {
        super(context);
        mDefaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        getDefaultSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        initialize();
    }

    protected void initialize() {
        SharedPreferences preferences = getDefaultSharedPreferences();
        mIsShowNetwork = preferences.getBoolean(KEY_SHOW_NETWORK, true);
        mIsShowAliasUp = preferences.getBoolean(KEY_SHOW_ALIAS_UP, true);
        mIsShowFromRoot = preferences.getBoolean(KEY_SHOW_ROOT, false);
        mSortByMode = preferences.getInt(KEY_SORT_MODE, SORT_BY_NAME);
        mViewMode = preferences.getInt(KEY_VIEW_MODE, VIEW_LIST);
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    private Context getContext() {
        return getBaseContext();
    }

    public SharedPreferences getDefaultSharedPreferences() {
        return mDefaultPreferences;
    }

    public boolean isShowHidden() { return mIsShowHidden; }

    public boolean isShowNetwork() {
        return mIsShowNetwork;
    }

    public boolean isShowAliasUp() {
        return mIsShowAliasUp;
    }

    public boolean isShowFromRoot() {
        return mIsShowFromRoot;
    }

    public int getSortBy() {
        return mSortByMode;
    }

    public void setSortBy(int sortByMode) {
        mSortByMode = sortByMode;
    }

    public int getViewMode() {
        return mViewMode;
    }

    public void setViewMode(int viewMode) {
        mViewMode = viewMode;
    }

    public boolean hasSmbShareProvider() {
        Uri uri = FileEntryConstants.SMB_SHARE_CONTENT_URI;
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager != null) {
            for (PackageInfo pack : packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS)) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (uri.getAuthority().equals(provider.authority) && provider.enabled) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int deleteSmbShare(String selection, String[] selectionArgs) {
        if (hasSmbShareProvider()) {
            return getContext().getContentResolver()
                    .delete(FileEntryConstants.SMB_SHARE_CONTENT_URI, selection, selectionArgs);
        }
        return 0;
    }

    public Cursor querySmbShareList(String sortOrder) {
        if (hasSmbShareProvider()) {
            return getContext().getContentResolver().query(FileEntryConstants.SMB_SHARE_CONTENT_URI,
                    FileEntryConstants.SMB_SHARE_FIELDS, null, null, sortOrder);
        }
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (KEY_SHOW_HIDDEN.equals(key)) {
            mIsShowHidden = sharedPreferences.getBoolean(key, true);

        } else if (KEY_SHOW_NETWORK.equals(key)) {
            mIsShowNetwork = sharedPreferences.getBoolean(key, true);

        } else if (KEY_SHOW_ALIAS_UP.equals(key)) {
            mIsShowAliasUp = sharedPreferences.getBoolean(key, true);

        } else if (KEY_SHOW_ROOT.equals(key)) {
            mIsShowFromRoot = sharedPreferences.getBoolean(key, false);

        } else if (KEY_SORT_MODE.equals(key)) {
            mSortByMode = sharedPreferences.getInt(key, SORT_BY_NAME);

        } else if (KEY_VIEW_MODE.equals(key)) {
            mViewMode = sharedPreferences.getInt(key, VIEW_LIST);
        }
    }

    public void close() {
        getDefaultSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public static <T> void saveSharedPreference(Context context, @NonNull String key, T value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        if (value instanceof  Boolean) {
            editor.putBoolean(key, (Boolean)value);

        } else if (value instanceof  Long) {
            editor.putLong(key, (Long)value);

        } else if (value instanceof  Float) {
            editor.putFloat(key, (Float)value);

        } else if (value instanceof  Integer) {
            editor.putInt(key, (Integer) value);

        } else if (value instanceof  String) {
            editor.putString(key, (String) value);
        }
        editor.commit();
    }
}

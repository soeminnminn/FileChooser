package com.s16.filechooser.providers;

import android.content.Context;
import android.util.Log;
import android.support.v4.view.ActionProvider;
import android.view.View;

import com.s16.filechooser.entries.FileEntry;

/**
 * Created by SMM on 11/8/2016.
 */

public class NavigationItemActionProvider extends ActionProvider {

    protected static final String TAG = NavigationItemActionProvider.class.getSimpleName();

    private final FileEntry mFileEntry;

    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public NavigationItemActionProvider(Context context, FileEntry entry) {
        super(context);
        mFileEntry = entry;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public boolean onPerformDefaultAction() {
        Log.i(TAG, "onPerformDefaultAction");
        return false;
    }

    public FileEntry getFileEntry() {
        return mFileEntry;
    }

    public String getMD5hash() {
        if (mFileEntry != null) {
            return mFileEntry.getMD5hash();
        }
        return null;
    }
}

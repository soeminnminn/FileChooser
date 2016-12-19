package com.s16.filechooser.drawables;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by SMM on 9/8/2016.
 */
public class ResourceDrawable extends Drawable {

    static class OpenResourceIdResult {
        Resources r;
        int id;
    }

    private final Context mContext;
    private Drawable mDrawable = null;
    private int mResource = 0;
    private Uri mUri;

    public ResourceDrawable(Context context) {
        mContext = context;
    }

    public ResourceDrawable(Context context, Uri uri) {
        this(context);
        mUri = uri;
        resolveUri();
    }

    public ResourceDrawable(Context context, int resid) {
        this(context);
        mResource = resid;
        mUri = null;
        resolveUri();
    }

    public ResourceDrawable(Context context, Drawable drawable) {
        this(context);
        mDrawable = drawable;
        mResource = 0;
        mUri = null;
    }

    public ResourceDrawable(Context context, Bitmap bitmap) {
        this(context);
        mDrawable = new BitmapDrawable(context.getResources(), bitmap);
        mResource = 0;
        mUri = null;
    }

    private void resolveUri() {
        if (mUri != null) {
            String scheme = mUri.getScheme();
            if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
                // android.resource://[package]/[res id]
                // android.resource://[package]/[res type]/[res name]
                try {
                    // Load drawable through Resources, to get the source density information
                    OpenResourceIdResult r = getResourceId(mContext, mUri);
                    mDrawable = r.r.getDrawable(r.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)
                    || ContentResolver.SCHEME_FILE.equals(scheme)) {
                InputStream stream = null;
                try {
                    stream = mContext.getContentResolver().openInputStream(mUri);
                    mDrawable = Drawable.createFromStream(stream, null);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                mDrawable = Drawable.createFromPath(mUri.toString());
            }

            if (mDrawable == null) {
                System.out.println("resolveUri failed on bad bitmap uri: " + mUri);
                // Don't try again.
                mUri = null;
            }
        } else if (mResource != 0) {
            mDrawable = ContextCompat.getDrawable(mContext, mResource);
        }
    }

    private OpenResourceIdResult getResourceId(Context context, Uri uri)
            throws FileNotFoundException {

        String authority = uri.getAuthority();
        Resources r;
        if (TextUtils.isEmpty(authority)) {
            throw new FileNotFoundException("No authority: " + uri);
        } else {
            try {
                r = context.getPackageManager().getResourcesForApplication(authority);
            } catch (PackageManager.NameNotFoundException ex) {
                throw new FileNotFoundException("No package found for authority: " + uri);
            }
        }
        List<String> path = uri.getPathSegments();
        if (path == null) {
            throw new FileNotFoundException("No path: " + uri);
        }
        int len = path.size();
        int id;
        if (len == 1) {
            try {
                id = Integer.parseInt(path.get(0));
            } catch (NumberFormatException e) {
                throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
            }
        } else if (len == 2) {
            id = r.getIdentifier(path.get(1), path.get(0), authority);
        } else {
            throw new FileNotFoundException("More than two path segments: " + uri);
        }
        if (id == 0) {
            throw new FileNotFoundException("No resource found for: " + uri);
        }
        OpenResourceIdResult res = new OpenResourceIdResult();
        res.r = r;
        res.id = id;
        return res;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (mDrawable != null) {
            mDrawable.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mDrawable != null) {
            mDrawable.setColorFilter(cf);
        }
    }

    @Override
    public int getOpacity() {
        if (mDrawable != null) {
            return mDrawable.getOpacity();
        }
        return 0;
    }

    public void setImageResource(int resId) {
        if (mUri != null || mResource != resId) {
            mResource = resId;
            mUri = null;
            resolveUri();
            invalidateSelf();
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
    }

    public void setImageDrawable(Drawable drawable) {
        if (mDrawable != drawable) {
            mResource = 0;
            mUri = null;
            mDrawable = drawable;
            invalidateSelf();
        }
    }

    public void setImageURI(Uri uri) {
        if (mResource != 0 ||
                (mUri != uri &&
                        (uri == null || mUri == null || !uri.equals(mUri)))) {
            mResource = 0;
            mUri = uri;
            resolveUri();
            invalidateSelf();
        }
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public boolean isEmpty() {
        return mDrawable == null;
    }
}


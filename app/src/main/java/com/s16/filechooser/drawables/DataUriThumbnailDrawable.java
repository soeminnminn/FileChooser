/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.s16.filechooser.drawables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DataUriThumbnailDrawable extends AutoThumbnailDrawable<String> {

    public DataUriThumbnailDrawable(Context context) {
        super(context);
    }

    public DataUriThumbnailDrawable(Context context, Drawable placeHolder) {
        super(context, placeHolder);
    }

    @Override
    protected byte[] getPreferredImageBytes(String data) {
        byte[] thumbnail = null;
        try {
            ExifInterface exif = new ExifInterface(data);
            if (exif.hasThumbnail()) {
                thumbnail = exif.getThumbnail();
             }
        } catch (IOException e) { }
        return thumbnail;
    }

    @Override
    protected InputStream getFallbackImageStream(String data) {
        try {
            return new FileInputStream(data);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    protected boolean dataChangedLocked(String data) {
        return !TextUtils.equals(mData, data);
    }
}
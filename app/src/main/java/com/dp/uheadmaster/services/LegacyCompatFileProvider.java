package com.dp.uheadmaster.services;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.FileProvider;

/**
 * Created by DELL on 03/10/2017.
 */

public class LegacyCompatFileProvider extends FileProvider {
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return(new LegacyCompatCursorWrapper(super.query(uri, projection, selection, selectionArgs, sortOrder)));
    }
}

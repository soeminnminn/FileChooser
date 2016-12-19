package com.s16.filechooser.utils;

import java.io.FileFilter;

import jcifs.smb.SmbFileFilter;

public abstract interface FileEntryFilter extends FileFilter, SmbFileFilter {

}

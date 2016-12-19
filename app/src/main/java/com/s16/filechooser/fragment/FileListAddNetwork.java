package com.s16.filechooser.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.AlertDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.s16.filechooser.R;
import com.s16.filechooser.entries.FileEntry;
import com.s16.filechooser.entries.SmbShareFileEntry;

/**
 * Created by SMM on 9/12/2016.
 */
public class FileListAddNetwork extends AlertDialogFragment {

    protected static final String TAG = FileListAddNetwork.class.getSimpleName();

    public interface OnAcceptListener {
        public void onAccept(FileListAddNetwork sender);
    }

    private TextInputEditText mTxtDisplayName;
    private TextInputEditText mTxtDomainName;
    private TextInputEditText mTxtServer;
    private TextInputEditText mTxtUserName;
    private TextInputEditText mTxtPassword;
    private CheckBox mCheckAnonymous;

    private FileEntry mEntry;
    private OnAcceptListener mOnAcceptListener;

    public FileListAddNetwork() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.fs_smb_server);
        if (mEntry != null && mEntry instanceof SmbShareFileEntry) {
            setTitle(R.string.fs_title_smb_edit);
        }

        setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnAcceptListener != null) {
                    mOnAcceptListener.onAccept(FileListAddNetwork.this);
                }
                dialog.dismiss();
            }
        });
        setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_add_network, container, false);

        mTxtDisplayName = (TextInputEditText)rootView.findViewById(R.id.fs_txt_display_name);
        mTxtDomainName = (TextInputEditText)rootView.findViewById(R.id.fs_txt_domain_name);
        mTxtServer = (TextInputEditText)rootView.findViewById(R.id.fs_txt_server);
        mTxtUserName = (TextInputEditText)rootView.findViewById(R.id.fs_txt_user_name);
        mTxtPassword = (TextInputEditText)rootView.findViewById(R.id.fs_txt_password);
        mCheckAnonymous = (CheckBox)rootView.findViewById(R.id.fs_check_anonymous);
        mCheckAnonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCheckAnonymous.requestFocus();
                mTxtUserName.setEnabled(!isChecked);
                mTxtPassword.setEnabled(!isChecked);
            }
        });

        if (mEntry != null && mEntry instanceof SmbShareFileEntry) {
            SmbShareFileEntry entry = (SmbShareFileEntry)mEntry;
            mTxtDisplayName.setText(entry.getName());
            mTxtServer.setText(entry.getServer());
            if (!TextUtils.isEmpty(entry.getUserName())) {
                mTxtDomainName.setText(entry.getDomain());
                mTxtUserName.setText(entry.getUserName());
            } else {
                mCheckAnonymous.setChecked(true);
                mTxtUserName.setEnabled(false);
                mTxtPassword.setEnabled(false);
            }
        }

        return rootView;
    }

    public void setEntry(FileEntry entry) {
        mEntry = entry;
    }

    public void setOnAcceptListener(OnAcceptListener listener) {
        mOnAcceptListener = listener;
    }

    public CharSequence getDisplayName() {
        if (mTxtDisplayName != null) {
            return mTxtDisplayName.getText();
        }
        return null;
    }

    public CharSequence getDomainName() {
        if (mTxtDomainName != null) {
            return mTxtDomainName.getText();
        }
        return null;
    }

    public CharSequence getServer() {
        if (mTxtServer != null) {
            return mTxtServer.getText();
        }
        return null;
    }

    public boolean isAnonymous() {
        if (mCheckAnonymous != null) {
            return mCheckAnonymous.isChecked();
        }
        return false;
    }

    public CharSequence getUserName() {
        if (mCheckAnonymous != null && mCheckAnonymous.isChecked()) {
            return null;
        }
        if (mTxtUserName != null) {
            return mTxtUserName.getText();
        }
        return null;
    }

    public CharSequence getPassword() {
        if (mCheckAnonymous != null && mCheckAnonymous.isChecked()) {
            return null;
        }
        if (mTxtPassword != null) {
            return mTxtPassword.getText();
        }
        return null;
    }
}

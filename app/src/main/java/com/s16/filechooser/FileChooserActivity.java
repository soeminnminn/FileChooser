package com.s16.filechooser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.s16.filechooser.entries.FileEntry;
import com.s16.filechooser.fragment.FileListFragment;
import com.s16.filechooser.providers.NavigationItemActionProvider;
import com.s16.filechooser.utils.FileEntryTypes;
import com.s16.filechooser.utils.UiUtils;
import com.s16.filechooser.widget.ExtendedToolbar;

import java.util.ArrayList;
import java.util.List;

public class FileChooserActivity extends BackStackActivity<FileListFragment>
        implements FileListFragment.FileListInteractionCallback,
        Toolbar.OnMenuItemClickListener,
        ExtendedToolbar.OnSearchListener {

    protected static final String TAG = FileChooserActivity.class.getSimpleName();

    private static final String FRAGMENT_NAME_PREFIX = "fragment_";
    private static final int NAV_MENU_ID = 0x100;
    private static final int PERMISSION_ACCESS_STORAGE = 0x1121;

    private View.OnClickListener mEditNavigationClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            performEditNavigationBack(view);
        }
    };

    private FileSettings mSettings;
    private ExtendedToolbar mToolbarMain;
    private Toolbar mToolbarEdit;
    private MenuItem mViewAsGrid;
    private MenuItem mViewAsList;

    protected Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        mToolbarMain = (ExtendedToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbarMain.setSearchHint(R.string.fs_hint_search);
        mToolbarMain.setOnSearchListener(this);
        mToolbarMain.setSearchMenuItem(R.id.action_search);
        mToolbarMain.setSearchClearMenuItem(R.id.action_search_clear);
        mToolbarMain.setNavigationMenuItemClickListener(this);
        mToolbarMain.setTitle(getText(R.string.app_name));

        requestPermission();

        mToolbarEdit = (Toolbar)findViewById(R.id.toolbarEdit);
        mToolbarEdit.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolbarEdit.inflateMenu(R.menu.fs_edit_menu);
        mToolbarEdit.setNavigationOnClickListener(mEditNavigationClick);
        mToolbarEdit.setOnMenuItemClickListener(this);
        mToolbarEdit.setVisibility(View.GONE);

        mSettings = FileSettings.getInstance(getContext());

        if (savedInstanceState == null) {
            FileListFragment fragment = new FileListFragment();
            FileEntry entry = fragment.setSettings(mSettings);
            String name = FRAGMENT_NAME_PREFIX + entry.getMD5hash();
            createNewFragment(R.id.fragmentContainer, name, fragment);
        }
        updateNavigation(null, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ACCESS_STORAGE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Not access to storage !
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fs_main_menu, menu);
        mToolbarMain.createNavigationMenu(getMenuInflater());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mViewAsGrid = menu.findItem(R.id.action_grid);
        mViewAsList = menu.findItem(R.id.action_list);

        if (mSettings.getViewMode() == FileSettings.VIEW_GRID) {
            mViewAsGrid.setVisible(false);
            mViewAsList.setVisible(true);
        } else {
            mViewAsGrid.setVisible(true);
            mViewAsList.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            performHomeClick();
            return true;

        } else if (id == R.id.action_by_name) {
            performSortMode(FileSettings.SORT_BY_NAME);
            return true;

        } else if (id == R.id.action_by_date) {
            performSortMode(FileSettings.SORT_BY_DATE);
            return true;

        } else if (id == R.id.action_by_size) {
            performSortMode(FileSettings.SORT_BY_SIZE);
            return true;

        } else if (id == R.id.action_grid) {
            performViewMode(FileSettings.VIEW_GRID);
            return true;


        } else if (id == R.id.action_list) {
            performViewMode(FileSettings.VIEW_LIST);
            return true;


        } else if (id == R.id.action_settings) {
            performSettings();
            return true;

        } else if (id == R.id.action_search) {
            performShowSearch(true);
            return true;

        } else if (id == R.id.action_search_clear) {
            performClearSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            return true;

        } else if (id == R.id.action_share) {
            performShare();
            return true;

        } else if (id == R.id.action_delete) {
            performDelete();
            return true;

        } else if (id == R.id.action_select_all) {
            performSelectAll();
            return true;

        } else if (id == R.id.action_copy_to) {
            performCopyTo();
            return true;

        } else if (id > NAV_MENU_ID) {
            performNavigationMenuClick(item);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        FileListFragment currentFragment = getCurrentFragment();
        if (mToolbarMain.isSearchViewShowing()) {
            performShowSearch(false);

        } else if (currentFragment != null && currentFragment.isSelectMode()) {
            setEditMode(false);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    PERMISSION_ACCESS_STORAGE);
        }
    }

    private void performSettings() {
        Intent intent = new Intent(this, FileSettingsActivity.class);
        startActivity(intent);
    }

    private void performHomeClick() {
        if (mToolbarMain.isSearchViewShowing()) {
            performShowSearch(false);
        }
    }

    private void performShowSearch(boolean show) {
        if (show) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbarMain.setSearchVisible(true);
        } else {
            mToolbarMain.setSearchVisible(false);
        }
    }

    private void performClearSearch() {
        mToolbarMain.clearSearchText();
    }

    @Override
    public void onSearchTextChanged(CharSequence searchText) {

    }

    @Override
    public void onSearchSubmit(CharSequence searchText) {

    }

    @Override
    public void onClearSearch() {

    }

    @Override
    public void onSearchViewVisibleChanged(View view, boolean visible) {
        if (!visible) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void performEditNavigationBack(View view) {
        setEditMode(false);
    }

    @Override
    public void onBackStackChanged(Fragment fragment) {
        int entryCount = getBackStackEntryCount();
        setEditMode(false);
        if (fragment != null && fragment instanceof  FileListFragment) {
            FileEntry entry = ((FileListFragment)fragment).getFileEntry();
            if (entry != null) {
                updateTitle(entry);
                updateNavigation(entry, entryCount);
            }
        }
    }

    @Override
    public void onOpenNewEntry(FileEntry entry) {
        int entryCount = getBackStackEntryCount();
        FileListFragment fragment = new FileListFragment();
        String name = FRAGMENT_NAME_PREFIX + entry.getMD5hash();
        fragment.setFileEntry(entry);

        addNewFragment(R.id.fragmentContainer, name, fragment);

        setEditMode(false);
        updateTitle(entry);
        updateNavigation(entry, entryCount);
    }

    @Override
    public void onEntryCheckedChanged(FileEntry entry, boolean isChecked, boolean isSelectMode, int selectedCount) {
        if (isSelectMode && mToolbarEdit.getVisibility() == View.GONE) {
            setEditMode(true);
        } else if (!isSelectMode && mToolbarEdit.getVisibility() == View.VISIBLE) {
            setEditMode(false);
        }
        updateEditTitle(selectedCount);
    }

    @Override
    public void onEntrySelectAll(int selectedCount) {
        updateEditTitle(selectedCount);
    }

    private void updateTitle(FileEntry entry) {
        if (entry != null) {
            if (entry.getEntryType() == FileEntryTypes.HOME) {
                mToolbarMain.setTitle(getString(R.string.app_name));

            } else {
                String fileName = entry.getName();
                mToolbarMain.setTitle(fileName);
            }
        }
    }

    private void updateEditTitle(int selectedCount) {
        mToolbarEdit.setTitle(String.format("%d selected", selectedCount));
    }

    private void updateNavigation(FileEntry entry, int entryCount) {
        boolean hasNavigation = entry != null && entry.getParent() != null &&
                entry.getEntryType() != FileEntryTypes.HOME && entry.getEntryType() != FileEntryTypes.ROOT;

        if (hasNavigation) {
            createNavigationMenu(entry, entryCount);
            mToolbarMain.setNavigationVisibility(View.VISIBLE);
        } else {
            mToolbarMain.setNavigationVisibility(View.GONE);
        }
    }

    private void createNavigationMenu(FileEntry entry, int entryCount) {
        if (entry == null) return;
        SubMenu menu = mToolbarMain.getNavigationDropDownMenu();
        if (menu == null) return;
        menu.clear();

        int i = entryCount;
        List<Pair<Integer, FileEntry>> itemList = new ArrayList<Pair<Integer, FileEntry>>();
        itemList.add(new Pair<Integer, FileEntry>(UiUtils.MAKELPARAM(NAV_MENU_ID, i--), entry));
        FileEntry item = entry.getParent();
        while (item != null) {
            itemList.add(new Pair<Integer, FileEntry>(UiUtils.MAKELPARAM(NAV_MENU_ID, i--), item));
            item = item.getParent();
        }

        for(i=(itemList.size() - 1); i>=0; i--) {
            Pair<Integer, FileEntry> itemData = itemList.get(i);
            MenuItem menuItem = menu.add(0, itemData.first.intValue(), 0, itemData.second.getName());
            NavigationItemActionProvider provider = new NavigationItemActionProvider(getContext(), itemData.second);
            MenuItemCompat.setActionProvider(menuItem, provider);
            if (i < (itemList.size() - 1)) {
                menuItem.setIcon(R.drawable.ic_subdirectory_arrow_right_gray);
            } else {
                menuItem.setIcon(R.drawable.ic_home_grey);
            }
        }
    }

    private void performNavigationMenuClick(MenuItem item) {
        ActionProvider provider = MenuItemCompat.getActionProvider(item);
        if (provider != null && provider instanceof NavigationItemActionProvider) {
            NavigationItemActionProvider itemActionProvider = (NavigationItemActionProvider)provider;
            String md5Hash = itemActionProvider.getMD5hash();
            if (!TextUtils.isEmpty(md5Hash)) {
                String name = FRAGMENT_NAME_PREFIX + itemActionProvider.getMD5hash();
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStackImmediate(name, 0);
            }
        }
    }

    private void performSortMode(final int mode) {
        runOnFragments(new RunOnFragment() {
            @Override
            public void run(Fragment fragment) {
                if (fragment != null && fragment instanceof FileListFragment) {
                    ((FileListFragment)fragment).setSortMode(mode);
                }
            }
        });
        FileSettings.saveSharedPreference(getContext(), FileSettings.KEY_SORT_MODE, mode);
    }

    private void performViewMode(final int mode) {
        runOnFragments(new RunOnFragment() {
            @Override
            public void run(Fragment fragment) {
                if (fragment != null && fragment instanceof FileListFragment) {
                    ((FileListFragment)fragment).setViewMode(mode);
                }
            }
        });
        if (mode == FileSettings.VIEW_GRID) {
            mViewAsList.setVisible(true);
            mViewAsGrid.setVisible(false);
        } else {
            mViewAsList.setVisible(false);
            mViewAsGrid.setVisible(true);
        }
        FileSettings.saveSharedPreference(getContext(), FileSettings.KEY_VIEW_MODE, mode);
    }

    private void setEditMode(boolean isEditMode) {
        if (isEditMode) {
            getSupportActionBar().hide();
            mToolbarEdit.setVisibility(View.VISIBLE);

        } else {
            runOnFragments(new RunOnFragment() {
                @Override
                public void run(Fragment fragment) {
                    if (fragment != null && fragment instanceof FileListFragment) {
                        ((FileListFragment)fragment).setSelectMode(false);
                    }
                }
            });

            getSupportActionBar().show();
            mToolbarEdit.setVisibility(View.GONE);
        }
    }

    private void performShare() {
        //
    }

    private void performDelete() {
        //
    }

    private void performSelectAll() {
        FileListFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.selectAll();
        }
    }

    private void performCopyTo() {
        //
    }
}

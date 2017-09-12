package com.s16.filechooser.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s16.filechooser.R;

/**
 * Created by SMM on 9/28/2016.
 */
public class ExtendedToolbar extends Toolbar
        implements TextWatcher, View.OnKeyListener,
        TextView.OnEditorActionListener {

    protected static final String TAG = ExtendedToolbar.class.getSimpleName();

    public interface OnSearchListener {
        public void onSearchTextChanged(CharSequence searchText);
        public void onSearchSubmit(CharSequence searchText);
        public void onClearSearch();
        public void onSearchViewVisibleChanged(View view, boolean visible);
    }

    private static final String EMPTY_STRING = "";

    private ActionMenuView mNavigationMenuView;
    private MenuItem mNavTitleItem;
    private MenuItem mNavDropDownItem;
    private OnMenuItemClickListener mNavMenuItemClickListener;

    private ActionMenuView.OnMenuItemClickListener mNavMenuItemClick = new ActionMenuView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return onNavMenuItemClick(item);
        }
    };

    private LinearLayout mSearchLayout;
    private EditText mTextSearchView;
    private MenuItem mMenuItemSearch;
    private int mMenuItemSearchId = 0;
    private MenuItem mMenuItemSearchClear;
    private int mMenuItemSearchClearId = 0;
    private OnSearchListener mSearchListener;
    private CharSequence mTitle;
    private CharSequence mSearchText;
    private TextView mNavTitleView;
    private boolean mIsSearching;

    public ExtendedToolbar(Context context) {
        super(context);
        initialize(context, null);
    }

    public ExtendedToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public ExtendedToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        createNavigationMenuView(context);
        createSearchView(context);
    }

    private void createNavigationMenuView(Context context) {
        mNavigationMenuView = new ActionMenuView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mNavigationMenuView.setLayoutParams(layoutParams);
        mNavigationMenuView.setPopupTheme(getPopupTheme());
        mNavigationMenuView.setOnMenuItemClickListener(mNavMenuItemClick);
        super.addView(mNavigationMenuView);
    }

    private void createSearchView(Context context) {
        mSearchLayout = new LinearLayout(context);
        mSearchLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mSearchLayout.setLayoutParams(layoutParams);

        mTextSearchView = new EditText(context);
        mTextSearchView.setSingleLine();
        mTextSearchView.setMaxLines(1);
        mTextSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
        mTextSearchView.setGravity(Gravity.CENTER_VERTICAL);
        mTextSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mTextSearchView.setBackgroundColor(Color.TRANSPARENT);
        mTextSearchView.setPadding(getPxFromDp(2), 0, 0, 0);
        mTextSearchView.setFocusableInTouchMode(true);
        mTextSearchView.setSaveEnabled(true);
        mTextSearchView.addTextChangedListener(this);
        mTextSearchView.setOnKeyListener(this);
        mTextSearchView.setOnEditorActionListener(this);

        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsText.weight = 1;
        mTextSearchView.setLayoutParams(layoutParamsText);

        mSearchLayout.addView(mTextSearchView);
        mSearchLayout.setVisibility(GONE);
        super.addView(mSearchLayout);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mNavigationMenuView != null && mNavTitleItem != null) {
            TextView itemView = (TextView)mNavigationMenuView.findViewById(R.id.action_nav_title);
            itemView.setAllCaps(false);
            itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            itemView.setTextColor(Color.parseColor("#fff3f3f3"));
            itemView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            itemView.setMaxLines(1);
            if (!TextUtils.isEmpty(mTitle)) {
                itemView.setText(mTitle);
            }
            mNavTitleView = itemView;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        super.setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch(actionId) {
            case EditorInfo.IME_ACTION_GO:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_SEARCH:
            case EditorInfo.IME_ACTION_SEND:
            case EditorInfo.IME_ACTION_UNSPECIFIED:
                return onQuerySubmit();
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    return onQuerySubmit();
                case KeyEvent.KEYCODE_ESCAPE:
                    clearSearchText();
                    return true;
            }
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mSearchText = s;
        if (!mIsSearching && mSearchListener != null) {
            mSearchListener.onSearchTextChanged(mSearchText);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (getMenuItemSearchClear() != null) {
            if (TextUtils.isEmpty(mTextSearchView.getText())) {
                getMenuItemSearchClear().setVisible(false);
            } else {
                getMenuItemSearchClear().setVisible(true);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mNavTitleItem != null) {
            mNavTitleItem.setTitle(title);
        } else {
            super.setTitle(title);
        }
    }

    protected boolean onQuerySubmit() {
        if (!mIsSearching && mSearchListener != null) {
            mSearchListener.onSearchSubmit(mSearchText);
        }
        return true;
    }

    private int getPxFromDp(int dp) {
        Resources rs = getResources();
        int pxConverter = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, rs.getDisplayMetrics());
        int px = pxConverter * dp;
        return px;
    }

    private MenuItem getMenuItemSearch() {
        if (mMenuItemSearch == null && mMenuItemSearchId != 0 && getMenu() != null) {
            mMenuItemSearch = getMenu().findItem(mMenuItemSearchId);
        }
        return mMenuItemSearch;
    }

    private MenuItem getMenuItemSearchClear() {
        if (mMenuItemSearchClear == null && mMenuItemSearchClearId != 0 && getMenu() != null) {
            mMenuItemSearchClear = getMenu().findItem(mMenuItemSearchClearId);
        }
        return mMenuItemSearchClear;
    }

    private void showSearch() {
        if (!mIsSearching && mSearchLayout != null) {
            mNavigationMenuView.setVisibility(GONE);
            if (getMenuItemSearch() != null) {
                getMenuItemSearch().setVisible(false);
            }
            if (getMenuItemSearchClear() != null) {
                if (TextUtils.isEmpty(mTextSearchView.getText())) {
                    getMenuItemSearchClear().setVisible(false);
                } else {
                    getMenuItemSearchClear().setVisible(true);
                }
            }
            mSearchLayout.setVisibility(View.VISIBLE);
            if (mSearchListener != null) {
                mSearchListener.onSearchViewVisibleChanged(mSearchLayout, true);
            }

            mTextSearchView.requestFocus();
            // Pop up the soft keyboard
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mTextSearchView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                    mTextSearchView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                }
            }, 100);
        }
    }

    private void hideSearch() {
        if (!mIsSearching && mSearchLayout != null) {
            mTextSearchView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextSearchView.setText(EMPTY_STRING);
                    mSearchLayout.setVisibility(View.GONE);
                    if (getMenuItemSearchClear() != null) {
                        getMenuItemSearchClear().setVisible(false);
                    }
                    if (getMenuItemSearch() != null) {
                        getMenuItemSearch().setVisible(true);
                    }
                    mNavigationMenuView.setVisibility(VISIBLE);

                    if (mSearchListener != null) {
                        mSearchListener.onSearchViewVisibleChanged(mSearchLayout, false);
                    }
                }
            }, 100);

            final Context context = mTextSearchView.getContext();
            // Hide the keyboard because the search box has been hidden
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mTextSearchView.getWindowToken(), 0);
        }
    }

    public void setOnSearchListener(OnSearchListener listener) {
        mSearchListener = listener;
    }

    public void setSearchHint(CharSequence searchHint) {
        mTextSearchView.setText(searchHint);
    }

    public void setSearchHint(@StringRes int resId) {
        mTextSearchView.setHint(resId);
    }

    public void setSearchMenuItem(MenuItem menuItem) {
        mMenuItemSearch = menuItem;
        if (mMenuItemSearch != null) {
            mMenuItemSearchId = mMenuItemSearch.getItemId();
        }
    }

    public void setSearchMenuItem(int menuItemId) {
        mMenuItemSearchId = menuItemId;
    }

    public void setSearchClearMenuItem(MenuItem menuItem) {
        mMenuItemSearchClear = menuItem;
        if (mMenuItemSearchClear != null) {
            mMenuItemSearchClearId = mMenuItemSearchClear.getItemId();
            mMenuItemSearchClear.setVisible(false);
        }
    }

    public void setSearchClearMenuItem(int menuItemId) {
        mMenuItemSearchClearId = menuItemId;
    }

    public void setSearchVisible(boolean visible) {
        if (visible == isSearchViewShowing()) return;
        if (visible) {
            showSearch();
        } else {
            hideSearch();
        }
    }

    public CharSequence getSearchText() {
        if (mTextSearchView != null) {
            mSearchText = mTextSearchView.getText();
        }
        return mSearchText;
    }

    public void setSearchText(CharSequence text) {
        mSearchText = text;
        if (mTextSearchView != null) {
            mTextSearchView.setText(text);
        }
    }

    public void setSearchText(@StringRes int resId) {
        mSearchText = getContext().getText(resId);
        if (mTextSearchView != null) {
            mTextSearchView.setText(resId);
        }
    }

    public boolean isSearchViewShowing() {
        if (mSearchLayout != null) {
            return (mSearchLayout.getVisibility() == View.VISIBLE);
        }
        return false;
    }

    public boolean isSearching() {
        return mIsSearching;
    }

    public void setIsSearching(boolean value) {
        mIsSearching = value;
    }

    public void clearSearchText() {
        if (mTextSearchView != null) {
            mTextSearchView.clearComposingText();
            mTextSearchView.setText(EMPTY_STRING);
            mSearchText = EMPTY_STRING;

            if (mSearchListener != null) {
                mSearchListener.onClearSearch();
            }
        }
    }

    public void createNavigationMenu(MenuInflater menuInflater) {
        Menu menu = mNavigationMenuView.getMenu();
        menuInflater.inflate(R.menu.fs_drop_down_menu, menu);
        mNavTitleItem = menu.findItem(R.id.action_nav_title);
        if (mNavTitleItem != null) {
            if (!TextUtils.isEmpty(mTitle)) {
                mNavTitleItem.setTitle(mTitle);
            } else {
                mNavTitleItem.setTitle(getTitle());
            }
            super.setTitle(null);
        }
        mNavDropDownItem = menu.findItem(R.id.action_nav_drop_down);
        if (mNavDropDownItem != null) {
            mNavDropDownItem.setVisible(false);
        }
        if (mNavTitleItem != null) {
            mNavTitleItem.setEnabled(false);
        }
    }

    private boolean onNavMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_nav_title) {
            return false;
        } else if (itemId == R.id.action_nav_drop_down) {
            showNavDropDown();
            return true;
        } else if (mNavMenuItemClickListener != null) {
            return mNavMenuItemClickListener.onMenuItemClick(item);
        }
        return false;
    }

    private void showNavDropDown() {
        if (mNavTitleView != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mNavTitleView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                    mNavTitleView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                }
            }, 100);
        }
    }

    public void setNavigationVisibility(int visibility) {
        boolean visible = visibility == View.VISIBLE;
        if (mNavDropDownItem != null) {
            mNavDropDownItem.setVisible(visible);
        }
        if (mNavTitleItem != null) {
            mNavTitleItem.setEnabled(visible);
        }
    }

    public Menu getNavigationMenu() {
        return mNavigationMenuView.getMenu();
    }

    public SubMenu getNavigationDropDownMenu() {
        if (mNavTitleItem != null) {
            SubMenu subMenu = mNavTitleItem.getSubMenu();
            if (subMenu == null) {
                subMenu = mNavigationMenuView.getMenu().addSubMenu(Menu.NONE, R.id.action_nav_title, Menu.NONE, "Navigation");
            }
            return subMenu;
        }
        return null;
    }

    public void setNavigationMenuItemClickListener(OnMenuItemClickListener listener) {
        mNavMenuItemClickListener = listener;
    }
}

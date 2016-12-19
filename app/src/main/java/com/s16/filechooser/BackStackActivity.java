package com.s16.filechooser;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by SMM on 11/30/2016.
 */

public abstract class BackStackActivity<T extends Fragment> extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener {

    public interface RunOnFragment {
        public void run(Fragment fragment);
    }

    protected void runOnFragments(final RunOnFragment runner) {
        FragmentManager manager = getSupportFragmentManager();
        int entryCount = manager.getBackStackEntryCount();
        for (int i=(entryCount - 1); i>=0; i--) {
            String name = manager.getBackStackEntryAt(i).getName();
            Fragment fragment = manager.findFragmentByTag(name);
            if (fragment != null) {
                runner.run(fragment);
            }
        }
    }

    protected Context getContext() {
        return this;
    }

    protected void createNewFragment(@IdRes int containerId, String fragmentName, T fragment) {
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .add(containerId, fragment, fragmentName)
                .addToBackStack(fragmentName)
                .commit();
    }

    protected void addNewFragment(@IdRes int containerId, String fragmentName, T fragment) {
        FragmentManager manager = getSupportFragmentManager();
        T currentFragment = getCurrentFragment();

        manager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                .add(containerId, fragment, fragmentName)
                .hide(currentFragment)
                .addToBackStack(fragmentName)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this);
    }

    @Override
    protected void onStop() {
        FragmentManager manager = getSupportFragmentManager();
        manager.removeOnBackStackChangedListener(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager manager = getSupportFragmentManager();
        int entryCount = manager.getBackStackEntryCount();
        String name = manager.getBackStackEntryAt(entryCount - 1).getName();

        Fragment fragment = manager.findFragmentByTag(name);
        onBackStackChanged(fragment);
    }

    public void onBackStackChanged(Fragment activeFragment) {

    }

    protected int getBackStackEntryCount() {
        FragmentManager manager = getSupportFragmentManager();
        return manager.getBackStackEntryCount();
    }

    protected T getCurrentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        int entryCount = manager.getBackStackEntryCount();
        String name = manager.getBackStackEntryAt(entryCount - 1).getName();

        Fragment fragment = manager.findFragmentByTag(name);
        if (fragment != null) {
            return  (T) fragment;
        }
        return null;
    }
}

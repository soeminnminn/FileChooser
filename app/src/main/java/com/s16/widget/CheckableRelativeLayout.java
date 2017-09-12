package com.s16.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * Created by SMM on 9/12/2016.
 */
public class CheckableRelativeLayout extends RelativeLayout
        implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private static final int[] ATTR_CHECKED = new int[] {
            android.R.attr.checked
    };
    private static final int[] ATTR_BUTTON = new int[] {
            android.R.attr.button
    };

    private boolean mChecked;
    private boolean mBroadcasting;

    private Drawable mButtonDrawable;
    private ColorStateList mButtonTintList = null;
    private PorterDuff.Mode mButtonTintMode = null;
    private boolean mHasButtonTint = false;
    private boolean mHasButtonTintMode = false;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a layout view changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a layout view has changed.
         *
         * @param view The layout view whose state has changed.
         * @param isChecked  The new checked state of layout view.
         */
        void onCheckedChanged(CheckableRelativeLayout view, boolean isChecked);
    }

    static class SavedState extends BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link CheckableRelativeLayout#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean)in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "CheckableImageButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public CheckableRelativeLayout(Context context) {
        super(context);
        initialize(context, null);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, ATTR_CHECKED);
        boolean checked = a.getBoolean(0, false);
        a.recycle();

        a = context.obtainStyledAttributes(attrs, ATTR_BUTTON);
        final Drawable d = a.getDrawable(0);
        if (d != null) {
            setButtonDrawable(d);
        }
        a.recycle();

        setChecked(checked);
        applyButtonTint();
    }

    protected boolean isLayoutRtl() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Sets a drawable as the compound button image given its resource
     * identifier.
     *
     * @param resId the resource identifier of the drawable
     * @attr ref android.R.styleable#CompoundButton_button
     */
    public void setButtonDrawable(@DrawableRes int resId) {
        final Drawable d;
        if (resId != 0) {
            d = ContextCompat.getDrawable(getContext(), resId);
        } else {
            d = null;
        }
        setButtonDrawable(d);
    }

    /**
     * Sets a drawable as the compound button image.
     *
     * @param drawable the drawable to set
     * @attr ref android.R.styleable#CompoundButton_button
     */
    @Nullable
    public void setButtonDrawable(@Nullable Drawable drawable) {
        if (mButtonDrawable != drawable) {
            if (mButtonDrawable != null) {
                mButtonDrawable.setCallback(null);
                unscheduleDrawable(mButtonDrawable);
            }

            mButtonDrawable = drawable;

            if (drawable != null) {
                drawable.setCallback(this);
                int layoutDirection = ViewCompat.getLayoutDirection(this);
                DrawableCompat.setLayoutDirection(drawable, layoutDirection);
                if (drawable.isStateful()) {
                    drawable.setState(getDrawableState());
                }
                drawable.setVisible(getVisibility() == VISIBLE, false);
                // setMinHeight(drawable.getIntrinsicHeight());
            }
        }
    }

    /**
     * @return the drawable used as the compound button image
     * @see #setButtonDrawable(Drawable)
     * @see #setButtonDrawable(int)
     */
    @Nullable
    public Drawable getButtonDrawable() {
        return mButtonDrawable;
    }

    /**
     * Applies a tint to the button drawable. Does not modify the current tint
     * mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setButtonDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and tint
     * mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint the tint to apply, may be {@code null} to clear tint
     *
     * @attr ref android.R.styleable#CompoundButton_buttonTint
     * @see #setButtonTintList(ColorStateList)
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setButtonTintList(@Nullable ColorStateList tint) {
        mButtonTintList = tint;
        mHasButtonTint = true;

        applyButtonTint();
    }

    /**
     * @return the tint applied to the button drawable
     * @attr ref android.R.styleable#CompoundButton_buttonTint
     * @see #setButtonTintList(ColorStateList)
     */
    @Nullable
    public ColorStateList getButtonTintList() {
        return mButtonTintList;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setButtonTintList(ColorStateList)}} to the button drawable. The
     * default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode the blending mode used to apply the tint, may be
     *                 {@code null} to clear tint
     * @attr ref android.R.styleable#CompoundButton_buttonTintMode
     * @see #getButtonTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setButtonTintMode(@Nullable PorterDuff.Mode tintMode) {
        mButtonTintMode = tintMode;
        mHasButtonTintMode = true;

        applyButtonTint();
    }

    /**
     * @return the blending mode used to apply the tint to the button drawable
     * @attr ref android.R.styleable#CompoundButton_buttonTintMode
     * @see #setButtonTintMode(PorterDuff.Mode)
     */
    @Nullable
    public PorterDuff.Mode getButtonTintMode() {
        return mButtonTintMode;
    }

    private void applyButtonTint() {
        if (mButtonDrawable != null && (mHasButtonTint || mHasButtonTintMode)) {
            mButtonDrawable = mButtonDrawable.mutate();

            if (mHasButtonTint) {
                DrawableCompat.setTintList(mButtonDrawable, mButtonTintList);
            }

            if (mHasButtonTintMode) {
                DrawableCompat.setTintMode(mButtonDrawable, mButtonTintMode);
            }

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mButtonDrawable.isStateful()) {
                mButtonDrawable.setState(getDrawableState());
            }
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }

            mBroadcasting = false;
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mButtonDrawable != null) {
            int[] myDrawableState = getDrawableState();

            // Set the state of the Drawable
            mButtonDrawable.setState(myDrawableState);
        }
        invalidate();
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);

        if (mButtonDrawable != null) {
            DrawableCompat.setHotspot(mButtonDrawable, x, y);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mButtonDrawable;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mButtonDrawable != null) mButtonDrawable.jumpToCurrentState();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CheckableRelativeLayout.class.getName());
        event.setChecked(mChecked);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CheckableRelativeLayout.class.getName());
        info.setCheckable(true);
        info.setChecked(mChecked);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final Drawable buttonDrawable = mButtonDrawable;
        if (buttonDrawable != null) {
            final int drawableHeight = buttonDrawable.getIntrinsicHeight();
            final int drawableWidth = buttonDrawable.getIntrinsicWidth();

            final int top = (getHeight() - drawableHeight) / 2;
            final int bottom = top + drawableHeight;
            final int left = isLayoutRtl() ? getWidth() - drawableWidth : 0;
            final int right = isLayoutRtl() ? getWidth() : drawableWidth;

            buttonDrawable.setBounds(left, top, right, bottom);

            final Drawable background = getBackground();
            if (background != null) {
                DrawableCompat.setHotspotBounds(background, left, top, right, bottom);
            }
        }

        super.dispatchDraw(canvas);

        if (buttonDrawable != null) {
            final int scrollX = getScrollX();
            final int scrollY = getScrollY();
            if (scrollX == 0 && scrollY == 0) {
                buttonDrawable.draw(canvas);
            } else {
                canvas.translate(scrollX, scrollY);
                buttonDrawable.draw(canvas);
                canvas.translate(-scrollX, -scrollY);
            }
        }
    }

    /**
     * Register a callback to be invoked when the checked state of this layout view
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }
}

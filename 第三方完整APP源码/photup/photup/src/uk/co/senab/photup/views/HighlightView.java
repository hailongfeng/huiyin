/*
 * Copyright 2013 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.senab.photup.views;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.view.View;

import uk.co.senab.photup.R;
import uk.co.senab.photup.platform.Platform;

public class HighlightView {

    static enum ModifyMode {
        None, Move, Grow
    }

    View mContext; // The View displaying the image.

    public static final int GROW_NONE = (1 << 0);
    public static final int GROW_LEFT_EDGE = (1 << 1);
    public static final int GROW_RIGHT_EDGE = (1 << 2);
    public static final int GROW_TOP_EDGE = (1 << 3);
    public static final int GROW_BOTTOM_EDGE = (1 << 4);

    public static final int MOVE = (1 << 5);

    private ModifyMode mMode = ModifyMode.None;

    private Rect mDrawRect; // in screen space
    private RectF mImageRect; // in image space
    private RectF mCropRect; // in image space

    Matrix mMatrix;

    private boolean mMaintainAspectRatio = false;
    private float mInitialAspectRatio;

    private Drawable mResizeDrawableWidth;
    private Drawable mResizeDrawableHeight;

    private final Paint mFocusPaint = new Paint();
    private final Paint mOutlinePaint = new Paint();

    private float mTouchHystersis;

    private final Path mOutlinePath = new Path();

    public HighlightView(View ctx) {
        mContext = ctx;
    }

    // Returns the cropping rectangle in image space.
    public RectF getCropRect() {
        final float width = mImageRect.width();
        final float height = mImageRect.height();

        return new RectF(mCropRect.left / width, mCropRect.top / height, mCropRect.right / width,
                mCropRect.bottom
                        / height);
    }

    // Determines which edges are hit by touching at (x, y).
    public int getHit(float x, float y) {
        Rect r = computeLayout();
        int retval = GROW_NONE;
        final float hysteresis = mTouchHystersis;

        // verticalCheck makes sure the position is between the top and
        // the bottom edge (with some tolerance). Similar for horizCheck.
        boolean verticalCheck = (y >= r.top - hysteresis) && (y < r.bottom + hysteresis);
        boolean horizCheck = (x >= r.left - hysteresis) && (x < r.right + hysteresis);

        // Check whether the position is near some edge(s).
        if ((Math.abs(r.left - x) < hysteresis) && verticalCheck) {
            retval |= GROW_LEFT_EDGE;
        }
        if ((Math.abs(r.right - x) < hysteresis) && verticalCheck) {
            retval |= GROW_RIGHT_EDGE;
        }
        if ((Math.abs(r.top - y) < hysteresis) && horizCheck) {
            retval |= GROW_TOP_EDGE;
        }
        if ((Math.abs(r.bottom - y) < hysteresis) && horizCheck) {
            retval |= GROW_BOTTOM_EDGE;
        }

        // Not near any edge but inside the rectangle: move.
        if (retval == GROW_NONE && r.contains((int) x, (int) y)) {
            retval = MOVE;
        }

        return retval;
    }

    public ModifyMode getMode() {
        return mMode;
    }

    public void invalidate() {
        mDrawRect = computeLayout();
    }

    public void setMode(ModifyMode mode) {
        if (mode != mMode) {
            mMode = mode;
            mContext.invalidate();
        }
    }

    public void setup(Matrix m, Rect imageRect, RectF cropRect, boolean maintainAspectRatio) {
        mMatrix = new Matrix(m);

        mCropRect = cropRect;
        mImageRect = new RectF(imageRect);
        mMaintainAspectRatio = maintainAspectRatio;

        mInitialAspectRatio = mCropRect.width() / mCropRect.height();
        mDrawRect = computeLayout();

        mFocusPaint.setColor(mContext.getResources().getColor(R.color.crop_rectangle_area));

        mOutlinePaint.setColor(mContext.getResources().getColor(R.color.crop_rectangle_line));
        mOutlinePaint.setStrokeWidth(3F);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setAntiAlias(true);

        mMode = ModifyMode.None;
        init();
    }

    // Grows the cropping rectange by (dx, dy) in image space.
    void growBy(float dx, float dy) {
        if (mMaintainAspectRatio) {
            if (dx != 0) {
                dy = dx / mInitialAspectRatio;
            } else if (dy != 0) {
                dx = dy * mInitialAspectRatio;
            }
        }

        // Don't let the cropping rectangle grow too fast.
        // Grow at most half of the difference between the image rectangle and
        // the cropping rectangle.
        RectF r = new RectF(mCropRect);
        if (dx > 0F && r.width() + 2 * dx > mImageRect.width()) {
            float adjustment = (mImageRect.width() - r.width()) / 2F;
            dx = adjustment;
            if (mMaintainAspectRatio) {
                dy = dx / mInitialAspectRatio;
            }
        }
        if (dy > 0F && r.height() + 2 * dy > mImageRect.height()) {
            float adjustment = (mImageRect.height() - r.height()) / 2F;
            dy = adjustment;
            if (mMaintainAspectRatio) {
                dx = dy * mInitialAspectRatio;
            }
        }

        r.inset(-dx, -dy);

        // Don't let the cropping rectangle shrink too fast.
        final float widthCap = 25F;
        if (r.width() < widthCap) {
            r.inset(-(widthCap - r.width()) / 2F, 0F);
        }
        float heightCap = mMaintainAspectRatio ? (widthCap / mInitialAspectRatio) : widthCap;
        if (r.height() < heightCap) {
            r.inset(0F, -(heightCap - r.height()) / 2F);
        }

        // Put the cropping rectangle inside the image rectangle.
        if (r.left < mImageRect.left) {
            r.offset(mImageRect.left - r.left, 0F);
        } else if (r.right > mImageRect.right) {
            r.offset(-(r.right - mImageRect.right), 0);
        }
        if (r.top < mImageRect.top) {
            r.offset(0F, mImageRect.top - r.top);
        } else if (r.bottom > mImageRect.bottom) {
            r.offset(0F, -(r.bottom - mImageRect.bottom));
        }

        mCropRect.set(r);
        mDrawRect = computeLayout();
        mContext.invalidate();
    }

    // Handles motion (dx, dy) in screen space.
    // The "edge" parameter specifies which edges the user is dragging.
    void handleMotion(int edge, float dx, float dy) {
        Rect r = computeLayout();
        if (edge == GROW_NONE) {
            return;
        } else if (edge == MOVE) {
            // Convert to image space before sending to moveBy().
            moveBy(dx * (mCropRect.width() / r.width()), dy * (mCropRect.height() / r.height()));
        } else {
            if (((GROW_LEFT_EDGE | GROW_RIGHT_EDGE) & edge) == 0) {
                dx = 0;
            }

            if (((GROW_TOP_EDGE | GROW_BOTTOM_EDGE) & edge) == 0) {
                dy = 0;
            }

            // Convert to image space before sending to growBy().
            float xDelta = dx * (mCropRect.width() / r.width());
            float yDelta = dy * (mCropRect.height() / r.height());
            growBy((((edge & GROW_LEFT_EDGE) != 0) ? -1 : 1) * xDelta,
                    (((edge & GROW_TOP_EDGE) != 0) ? -1 : 1)
                            * yDelta);
        }
    }

    // Grows the cropping rectange by (dx, dy) in image space.
    void moveBy(float dx, float dy) {
        Rect invalRect = new Rect(mDrawRect);

        mCropRect.offset(dx, dy);

        // Put the cropping rectangle inside image rectangle.
        mCropRect.offset(Math.max(0, mImageRect.left - mCropRect.left),
                Math.max(0, mImageRect.top - mCropRect.top));

        mCropRect.offset(Math.min(0, mImageRect.right - mCropRect.right),
                Math.min(0, mImageRect.bottom - mCropRect.bottom));

        mDrawRect = computeLayout();
        invalRect.union(mDrawRect);
        invalRect.inset(-10, -10);
        mContext.invalidate(invalRect);
    }

    protected void draw(Canvas canvas) {
        canvas.save();

        mOutlinePath.reset();
        mOutlinePath.addRect(mDrawRect.left, mDrawRect.top, mDrawRect.right, mDrawRect.bottom,
                Path.Direction.CW);

        if (!Platform.isCanvasHardwareAccelerated(canvas)) {
            canvas.clipPath(mOutlinePath, Region.Op.DIFFERENCE);
            canvas.drawPaint(mFocusPaint);
        }

        canvas.restore();

        canvas.drawPath(mOutlinePath, mOutlinePaint);

        /**
         * Draw Handle Drawables
         */
        int left = mDrawRect.left + 1;
        int right = mDrawRect.right + 1;
        int top = mDrawRect.top + 4;
        int bottom = mDrawRect.bottom + 3;

        int widthWidth = mResizeDrawableWidth.getIntrinsicWidth() / 2;
        int widthHeight = mResizeDrawableWidth.getIntrinsicHeight() / 2;
        int heightHeight = mResizeDrawableHeight.getIntrinsicHeight() / 2;
        int heightWidth = mResizeDrawableHeight.getIntrinsicWidth() / 2;

        int xMiddle = mDrawRect.left + ((mDrawRect.right - mDrawRect.left) / 2);
        int yMiddle = mDrawRect.top + ((mDrawRect.bottom - mDrawRect.top) / 2);

        mResizeDrawableWidth
                .setBounds(left - widthWidth, yMiddle - widthHeight, left + widthWidth, yMiddle
                        + widthHeight);
        mResizeDrawableWidth.draw(canvas);

        mResizeDrawableWidth
                .setBounds(right - widthWidth, yMiddle - widthHeight, right + widthWidth, yMiddle
                        + widthHeight);
        mResizeDrawableWidth.draw(canvas);

        mResizeDrawableHeight
                .setBounds(xMiddle - heightWidth, top - heightHeight, xMiddle + heightWidth, top
                        + heightHeight);
        mResizeDrawableHeight.draw(canvas);

        mResizeDrawableHeight
                .setBounds(xMiddle - heightWidth, bottom - heightHeight, xMiddle + heightWidth,
                        bottom
                                + heightHeight);
        mResizeDrawableHeight.draw(canvas);
    }

    // Maps the cropping rectangle from image space to screen space.
    private Rect computeLayout() {
        RectF r = new RectF(mCropRect.left, mCropRect.top, mCropRect.right, mCropRect.bottom);
        mMatrix.mapRect(r);
        return new Rect(Math.round(r.left), Math.round(r.top), Math.round(r.right),
                Math.round(r.bottom));
    }

    private void init() {
        android.content.res.Resources resources = mContext.getResources();
        mResizeDrawableWidth = resources.getDrawable(R.drawable.crop_handle_x);
        mResizeDrawableHeight = resources.getDrawable(R.drawable.crop_handle_y);
        mTouchHystersis = resources.getDimensionPixelSize(R.dimen.crop_hysteresis);
    }
}
package com.kaelli.niceratingbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KaelLi on 2019/11/19.
 */
public class NiceRatingBar extends LinearLayout {
    private RatingStatus mRatingStatus;
    private Drawable mStarFullDrawable;
    private Drawable mStarEmptyDrawable;
    private Drawable mStarHalfDrawable;
    private float mStarWidth;
    private float mStarHeight;
    private float mStarPadding;
    private int mStarTotal;
    private float mRating;
    private OnRatingChangedListener mOnRatingChangedListener;
    private List<Integer> mBoundaryList = new ArrayList<>(5);

    public NiceRatingBar(Context context) {
        this(context, null);
    }

    public NiceRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NiceRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs == null) {
            throw new RuntimeException("NiceRatingBar Error: You must use NiceRatingBar in layout file!");
        }
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);

        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NiceRatingBar);
        mRatingStatus = RatingStatus.getStatus(mTypedArray.getInt(R.styleable.NiceRatingBar_nrb_ratingStatus, RatingStatus.Disable.mStatus));
        mStarFullDrawable = mTypedArray.getDrawable(R.styleable.NiceRatingBar_nrb_starFullResource);
        mStarEmptyDrawable = mTypedArray.getDrawable(R.styleable.NiceRatingBar_nrb_starEmptyResource);
        mStarHalfDrawable = mTypedArray.getDrawable(R.styleable.NiceRatingBar_nrb_starHalfResource);
        if (mStarFullDrawable == null || mStarEmptyDrawable == null)
            throw new IllegalArgumentException("NiceRatingBar Error: You must declare starFullResource and starEmptyResource!");
        mStarWidth = mTypedArray.getDimension(R.styleable.NiceRatingBar_nrb_starImageWidth, 24f);
        mStarHeight = mTypedArray.getDimension(R.styleable.NiceRatingBar_nrb_starImageHeight, 24f);
        mStarPadding = mTypedArray.getDimension(R.styleable.NiceRatingBar_nrb_starImagePadding, 4f);
        mStarTotal = mTypedArray.getInt(R.styleable.NiceRatingBar_nrb_starTotal, 5);
        if (mStarTotal <= 0)
            throw new IllegalArgumentException("NiceRatingBar Error: starTotal must be positive!");
        mRating = mTypedArray.getFloat(R.styleable.NiceRatingBar_nrb_rating, 5f);
        mTypedArray.recycle();

        for (int i = 0; i < mStarTotal; i ++) {
            addView(createStarImageView(i == mStarTotal - 1));
        }

        setRating(mRating);
    }

    private ImageView createStarImageView(boolean isLast) {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(Math.round(mStarWidth), Math.round(mStarHeight));
        layoutParams.setMargins(0, 0, isLast ? 0 : Math.round(mStarPadding), 0);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    public void setRating(float rating) {
        if (rating > mStarTotal) {
            rating = mStarTotal;
        }
        this.mRating = rating;
        if (mOnRatingChangedListener != null) {
            mOnRatingChangedListener.onRatingChanged(rating);
        }

        int partInteger = (int) Math.floor(rating);
        float partDecimal = new BigDecimal(String.valueOf(rating))
                .subtract(new BigDecimal(String.valueOf(partInteger)))
                .floatValue();

        for (int i = 0; i < partInteger; i++) {
            ((ImageView)getChildAt(i)).setImageDrawable(mStarFullDrawable);
        }

        for (int i = partInteger; i < mStarTotal; i ++) {
            ((ImageView)getChildAt(i)).setImageDrawable(mStarEmptyDrawable);
        }

        if (partDecimal >= 0.25) {
            if (partDecimal < 0.75 && mStarHalfDrawable != null) {
                ((ImageView)getChildAt(partInteger)).setImageDrawable(mStarHalfDrawable);
            } else if (partDecimal >= 0.75){
                ((ImageView)getChildAt(partInteger)).setImageDrawable(mStarFullDrawable);
            }
        }
    }

    private float calculateRating(float touchX) {
        float result = 0;
        for (int i = 0; i < mStarTotal - 1; i ++) {
            if (touchX >= mBoundaryList.get(i) && touchX <= mBoundaryList.get(i + 1)) {
                if (mStarHalfDrawable != null && touchX < (mBoundaryList.get(i) + mBoundaryList.get(i + 1)) / 2)
                    result = i + 0.5f;
                else
                    result = i + 1;
                break;
            }
        }
        if (result == 0f && touchX >= mBoundaryList.get(mBoundaryList.size() - 1)) {
            result = touchX < mBoundaryList.get(mBoundaryList.size() - 1) + mStarWidth / 2 ? mStarTotal - 0.5f : mStarTotal;
        }
        if (result == 0f) {
            result = mStarHalfDrawable != null ? 0.5f : 1;
        }
        return result;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRatingStatus == RatingStatus.Enable && !mBoundaryList.isEmpty()) {
            setRating(calculateRating(event.getX()));
            if (event.getAction() == MotionEvent.ACTION_DOWN) return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mBoundaryList.isEmpty()) {
            for (int index = 0; index < mStarTotal; index++) {
                mBoundaryList.add(index == 0 ? 0 : mBoundaryList.get(index - 1) + Math.round(mStarWidth) + Math.round(mStarPadding));
            }
        }
    }

    public void setOnRatingChangedListener(OnRatingChangedListener listener) {
        this.mOnRatingChangedListener = listener;
    }

    public void setRatingStatus(RatingStatus status) {
        this.mRatingStatus = status;
    }
}

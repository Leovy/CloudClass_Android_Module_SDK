package com.allin.basicres.widget.label;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.allin.basicres.utils.ViewUtils;
import com.allin.commlibrary.CollectionUtils;
import com.example.xianxueliang.uicomponent.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签控件
 * Create by xianxueliang
 */
public class LabelView extends ViewGroup {

  private static final boolean TEST = false;
  private @Nullable
  OnLabelClickListener<Label> onLabelClickListener;
  private int mGravity = Gravity.START;
  private final List<List<View>> mLines = new ArrayList<>();
  private List<View> mLineViews = new ArrayList<>();
  private final List<Integer> mLineHeights = new ArrayList<>();
  private final List<Integer> mLineMargins = new ArrayList<>();
  private float mMinWidthRatio = -1;
  private float mMaxWidthRatio = -1;
  private float mMinChildWidth = 0;
  private float mMaxChildWidth = 0;
  private int mDividerSpacingHorizontal = 0;
  private int mDividerSpacingVertical = 0;
  private int mMaxLines = Integer.MAX_VALUE;
  private final static float DELTA = 0.001f;
  private final static float MAX_RATIO = 1.001f;
  private List<View> mHiddenViews = new ArrayList<>();

  public LabelView(Context context) {
    this(context, null);
  }

  public LabelView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    ViewUtils.obtainStyledAttributes(context, attrs, R.styleable.LabelView, defStyleAttr, 0,
        typedArray -> {
          int index = typedArray.getInt(R.styleable.LabelView_android_gravity, -1);
          if (index > 0) {
            setGravity(index);
          }

          mMinChildWidth = typedArray.getDimension(R.styleable.LabelView_lv_childMinWidth, 0);
          mMaxChildWidth = typedArray.getDimension(R.styleable.LabelView_lv_childMaxWidth, 0);

          mMinWidthRatio = typedArray.getFloat(R.styleable.LabelView_lv_childMinWidthRatio, -1);
          mMaxWidthRatio = typedArray.getFloat(R.styleable.LabelView_lv_childMaxWidthRatio, -1);

          mDividerSpacingHorizontal = Math
              .round(typedArray.getDimension(R.styleable.LabelView_lv_dividerSpacingHorizontal, 0));
          mDividerSpacingVertical = Math
              .round(typedArray.getDimension(R.styleable.LabelView_lv_dividerSpacingVertical, 0));

          mMaxLines = typedArray.getInt(R.styleable.LabelView_lv_maxLines, Integer.MAX_VALUE);
        });
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
    int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

    int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
    int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

    if (mMinWidthRatio >= 0 && mMinWidthRatio <= MAX_RATIO) {
      mMinChildWidth = sizeWidth * mMinWidthRatio;
    }
    if (mMaxWidthRatio >= 0 && mMaxWidthRatio <= MAX_RATIO) {
      mMaxChildWidth = sizeWidth * mMaxWidthRatio;
    }

    if (mMaxWidthRatio < 0 && mMaxChildWidth == 0) {
      mMaxChildWidth = sizeWidth;
    }

    mMinChildWidth -= DELTA;
    mMaxChildWidth += DELTA;

    int width = 0;
    int height = getPaddingTop() + getPaddingBottom();

    int lineWidth = 0;
    int lineHeight = 0;
    int lineCount = 1;

    int childCount = getChildCount();
    if (mMaxLines == 0) {
      setVisibility(View.GONE);
    }
    for (View view : mHiddenViews) {
      view.setVisibility(View.VISIBLE);
    }
    mHiddenViews.clear();
    int positionInLine = 0;
    for (int i = 0; i < childCount; i++) {
      View child = getChildAt(i);
      boolean lastChild = i == childCount - 1;
      if (child.getVisibility() == View.GONE) {
        if (lastChild) {
          width = Math.max(width, lineWidth);
          height += lineHeight;
          lineCount++;
        }
        continue;
      }
      measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height);
      LabelView.LayoutParams lp = (LabelView.LayoutParams) child.getLayoutParams();

      int childWidthMode = MeasureSpec.AT_MOST;
      int childWidthSize = sizeWidth;

      int childHeightMode = MeasureSpec.AT_MOST;
      int childHeightSize = sizeHeight;

      if (lp.width == LabelView.LayoutParams.MATCH_PARENT) {
        childWidthMode = MeasureSpec.EXACTLY;
        childWidthSize -= lp.leftMargin + lp.rightMargin;
      } else if (lp.width >= 0) {
        childWidthMode = MeasureSpec.EXACTLY;
        childWidthSize = lp.width;
      }

      if (lp.height >= 0) {
        childHeightMode = MeasureSpec.EXACTLY;
        childHeightSize = lp.height;
      } else if (modeHeight == MeasureSpec.UNSPECIFIED) {
        childHeightMode = MeasureSpec.UNSPECIFIED;
        childHeightSize = 0;
      }

      child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
          MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));

      int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

      boolean shouldReMeasureWidth = false;
      if (childWidth < mMinChildWidth) {
        childWidth = Math.round(mMinChildWidth);
        shouldReMeasureWidth = true;
      }
      if (childWidth > mMaxChildWidth) {
        childWidth = Math.round(mMaxChildWidth);
        shouldReMeasureWidth = true;
      }
      if (shouldReMeasureWidth) {
        child.measure(MeasureSpec.makeMeasureSpec(childWidth, childWidthMode),
            MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));
      }

      int hGap = getDividerSpacing(mDividerSpacingHorizontal, i - positionInLine);
      int vGap = getDividerSpacing(mDividerSpacingVertical, i - positionInLine);

      if (lineWidth + childWidth + hGap > sizeWidth) {
        width = Math.max(width, lineWidth);
        height += lineHeight;
        lineCount++;

        if (lineCount > mMaxLines) {
          for (int j = i; j < childCount; j++) {
            View childTmp = getChildAt(j);
            childTmp.setVisibility(View.GONE);
            mHiddenViews.add(childTmp);
          }
          break;
        }

        lineWidth = childWidth;
        lineHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin + vGap;
        positionInLine = i;
      } else {
        lineWidth = lineWidth + childWidth + hGap;
        lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + lp.topMargin + lp
            .bottomMargin);
      }

      if (lastChild) {
        width = Math.max(width, lineWidth);
        height += lineHeight;
        lineCount++;
      }
    }

    width += getPaddingLeft() + getPaddingRight();
    setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight
        == MeasureSpec.EXACTLY) ? sizeHeight : height);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    mLines.clear();
    mLineHeights.clear();
    mLineMargins.clear();
    mLineViews.clear();

    int width = getWidth();
    int height = getHeight();

    int linesSum = getPaddingTop();

    int lineWidth = 0;
    int lineHeight = 0;

    float horizontalGravityFactor;
    switch ((mGravity & Gravity.HORIZONTAL_GRAVITY_MASK)) {
      case Gravity.START:
      default:
        horizontalGravityFactor = 0;
        break;
      case Gravity.CENTER_HORIZONTAL:
        horizontalGravityFactor = .5f;
        break;
      case Gravity.END:
        horizontalGravityFactor = 1;
        break;
    }

    int lineNumber = 0;
    int positionInLine = 0;

    for (int i = 0; i < getChildCount(); i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == View.GONE) {
        continue;
      }

      LabelView.LayoutParams lp = (LabelView.LayoutParams) child.getLayoutParams();

      int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
      int childHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

      int hGap = getDividerSpacing(mDividerSpacingHorizontal, i - positionInLine);
      int vGap = getDividerSpacing(mDividerSpacingVertical, i - positionInLine);
      if (lineWidth + childWidth + hGap > width) {
        mLineHeights.add(lineHeight + vGap);
        mLines.add(mLineViews);
        mLineMargins.add((int) ((width - lineWidth) * horizontalGravityFactor) +
            getPaddingLeft());
        linesSum += lineHeight + vGap;

        lineHeight = 0;
        lineWidth = 0;
        mLineViews = new ArrayList<>();
        lineNumber++;
        positionInLine = i;
      }

      lineWidth += childWidth + hGap;
      lineHeight = Math.max(lineHeight, childHeight);
      mLineViews.add(child);
    }

    int vGap = getDividerSpacing(mDividerSpacingVertical, lineNumber);
    mLineHeights.add(lineHeight + vGap);
    mLines.add(mLineViews);
    mLineMargins.add((int) ((width - lineWidth) * horizontalGravityFactor) + getPaddingLeft());

    linesSum += lineHeight;

    int verticalGravityMargin = 0;
    switch ((mGravity & Gravity.VERTICAL_GRAVITY_MASK)) {
      case Gravity.TOP:
      default:
        break;
      case Gravity.CENTER_VERTICAL:
        verticalGravityMargin = (height - linesSum) / 2;
        break;
      case Gravity.BOTTOM:
        verticalGravityMargin = height - linesSum;
        break;
    }

    int numLines = mLines.size();

    int left;
    int top = getPaddingTop();
    for (int i = 0; i < numLines; i++) {
      lineHeight = mLineHeights.get(i);
      mLineViews = mLines.get(i);
      left = mLineMargins.get(i);

      int children = mLineViews.size();

      for (int j = 0; j < children; j++) {

        View child = mLineViews.get(j);

        if (child.getVisibility() == View.GONE) {
          continue;
        }

        LabelView.LayoutParams lp = (LabelView.LayoutParams) child.getLayoutParams();

        // if height is match_parent we need to remeasure child to line height
        if (lp.height == LabelView.LayoutParams.MATCH_PARENT) {
          int childWidthMode = MeasureSpec.AT_MOST;
          int childWidthSize = lineWidth;

          if (lp.width == LabelView.LayoutParams.MATCH_PARENT) {
            childWidthMode = MeasureSpec.EXACTLY;
          } else if (lp.width >= 0) {
            childWidthMode = MeasureSpec.EXACTLY;
            childWidthSize = lp.width;
          }

          child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
              MeasureSpec.makeMeasureSpec(lineHeight - lp.topMargin - lp
                  .bottomMargin, MeasureSpec.EXACTLY));
        }

        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();

        int gravityMargin = 0;

        if (Gravity.isVertical(lp.gravity)) {
          switch (lp.gravity) {
            case Gravity.TOP:
            default:
              break;
            case Gravity.CENTER_VERTICAL:
            case Gravity.CENTER:
              gravityMargin = (lineHeight - childHeight - lp.topMargin - lp
                  .bottomMargin) / 2;
              break;
            case Gravity.BOTTOM:
              gravityMargin = lineHeight - childHeight - lp.topMargin - lp
                  .bottomMargin;
              break;
          }
        }
        child.layout(left + lp.leftMargin, top + lp.topMargin + gravityMargin +
            verticalGravityMargin, left + childWidth + lp.leftMargin, top +
            childHeight + lp.topMargin + gravityMargin + verticalGravityMargin);
        left += childWidth + lp.leftMargin + lp.rightMargin + getDividerSpacing(
            mDividerSpacingHorizontal, j + 1);
      }

      top += lineHeight;
    }
  }

  @Override
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    return new LayoutParams(p);
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new LayoutParams(getContext(), attrs);
  }

  @Override
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
  }

  public void setGravity(int gravity) {
    if (mGravity != gravity) {
      if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
        gravity |= Gravity.START;
      }

      if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
        gravity |= Gravity.TOP;
      }

      mGravity = gravity;
      requestLayout();
      invalidate();
    }
  }

  public int getGravity() {
    return mGravity;
  }


  public void clearAll() {
    if (getChildCount() > 0) {
      removeAllViews();
    }
  }

  public void addLabels(@Nullable ArrayList<Label> labels) {
    if (!CollectionUtils.isEmpty(labels)) {
      int index = 0;
      for (Label label : labels) {
        if (label != null) {
          LayoutInflater factory = null;
          try {
            factory = LayoutInflater.from(getContext());
          } catch (AssertionError ignored) {
          }

          if (factory != null) {
            View labelView = label.onCreateView(factory, this);
            if (labelView != null) {
              label.onViewCreated(labelView);
              final int finalIndex = index;
              labelView.setOnClickListener(v -> {
                if (onLabelClickListener != null) {
                  onLabelClickListener.onClick(v, label, finalIndex);
                }
              });
              if (TEST) {
                labelView.setBackgroundColor(Color.YELLOW);
              }
              addView(labelView, label.generateLayoutParams());
            }
          }
        }
        index++;
      }
    }
  }

  public void setOnLabelClickListener(
      @Nullable OnLabelClickListener<Label> onLabelClickListener) {
    this.onLabelClickListener = onLabelClickListener;
  }

  public void setMaxLines(int mMaxLines) {
    this.mMaxLines = mMaxLines;
    requestLayout();
    invalidate();
  }

  public void setChildMaxWidth(float maxWidth) {
    this.mMaxChildWidth = maxWidth;
    requestLayout();
    invalidate();
  }

  public void setChildMinWidth(float minWidth) {
    this.mMinChildWidth = minWidth;
    requestLayout();
    invalidate();
  }

  public void setDividerSpacingHorizontal(int spacing) {
    this.mDividerSpacingHorizontal = spacing;
    requestLayout();
    invalidate();
  }

  public void setDividerSpacingVertical(int spacing) {
    this.mDividerSpacingVertical = spacing;
    requestLayout();
    invalidate();
  }

  private int getDividerSpacing(int dividerSpacing, int position) {
    return position == 0 ? 0 : dividerSpacing;
  }

  public static class LayoutParams extends MarginLayoutParams {

    public int gravity = -1;

    LayoutParams(int width, int height) {
      super(width, height);
    }

    LayoutParams(ViewGroup.LayoutParams source) {
      super(source);
    }

    LayoutParams(Context context, AttributeSet attrs) {
      super(context, attrs);
      ViewUtils.obtainStyledAttributes(context, attrs, R.styleable.LabelView, 0, 0,
          typedArray -> gravity = typedArray
              .getInt(R.styleable.LabelView_android_layout_gravity, -1));
    }
  }
}

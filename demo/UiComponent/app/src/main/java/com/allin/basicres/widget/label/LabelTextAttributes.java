package com.allin.basicres.widget.label;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils.TruncateAt;
import com.allin.basicres.utils.ViewUtils;
import java.util.Objects;

public class LabelTextAttributes {
  final int typefaceStyle;
  //text size in sp
  final float textSize;
  final @ColorInt int textColor;
  final int textMaxLines;
  final TruncateAt textEllipsize;
  final boolean textIncludeFrontPadding;
  final @Nullable Drawable compoundDrawableLeft;
  final @Nullable Drawable compoundDrawableTop;
  final @Nullable Drawable compoundDrawableRight;
  final @Nullable Drawable compoundDrawableBottom;
  final int compoundDrawablePadding;

  private LabelTextAttributes(Builder builder) {
    this.typefaceStyle = builder.typefaceStyle;
    this.textSize = builder.textSize;
    this.textColor = builder.textColor;
    this.textMaxLines = builder.textMaxLines;
    this.textEllipsize = builder.textEllipsize;
    this.textIncludeFrontPadding = builder.textIncludeFrontPadding;
    this.compoundDrawableLeft = builder.compoundDrawableLeft;
    this.compoundDrawableTop = builder.compoundDrawableTop;
    this.compoundDrawableRight = builder.compoundDrawableRight;
    this.compoundDrawableBottom = builder.compoundDrawableBottom;
    this.compoundDrawablePadding = builder.compoundDrawablePadding;
  }

  public static Builder newAttrsBuilder(){
    return new Builder();
  }

  public static class Builder {
    private int typefaceStyle;
    //text size in sp
    private float textSize;
    private @ColorInt int textColor;
    private int textMaxLines;
    private TruncateAt textEllipsize;
    private boolean textIncludeFrontPadding;
    private @Nullable Drawable compoundDrawableLeft;
    private @Nullable Drawable compoundDrawableTop;
    private @Nullable Drawable compoundDrawableRight;
    private @Nullable Drawable compoundDrawableBottom;
    private int compoundDrawablePadding;

    private Builder() {
      this.typefaceStyle = Typeface.NORMAL;
      this.textSize = 15;
      this.textColor = Color.BLACK;
      this.textMaxLines = Integer.MAX_VALUE;
      this.textEllipsize = null;
      this.textIncludeFrontPadding = true;
    }

    /**
     * {@link Typeface#NORMAL} OR {@link Typeface#BOLD} OR {@link Typeface#ITALIC}
     * OR {@link Typeface#BOLD_ITALIC}
     * @see Typeface
     */
    public Builder setTypefaceStyle(int style) {
      this.typefaceStyle = ViewUtils.requiredTypefaceStyle(style);
      return this;
    }

    /**
     * text size in sp
     * {@link android.widget.TextView#setTextSize(float)}
     */
    public Builder setTextSize(float textSize) {
      this.textSize = textSize;
      return this;
    }

    /**
     * {@link android.widget.TextView#setTextColor(int)}
     */
    public Builder setTextColor(@ColorInt int textColor) {
      this.textColor = textColor;
      return this;
    }

    /**
     * {@link android.widget.TextView#setMaxLines(int)}
     */
    public Builder setMaxLines(@IntRange(from = 1) int maxLines) {
      if (maxLines <= 0) {
        throw new IllegalArgumentException("line count must >= 0");
      }
      this.textMaxLines = maxLines;
      return this;
    }

    /**
     * {@link android.widget.TextView#setEllipsize(TruncateAt)}
     */
    public Builder setEllipsize(@NonNull TruncateAt where) {
      this.textEllipsize = Objects.requireNonNull(where);
      return this;
    }

    /**
     * {@link android.widget.TextView#setCompoundDrawables(Drawable, Drawable, Drawable, Drawable)}
     */
    public Builder setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top,
        @Nullable Drawable right, @Nullable Drawable bottom) {
      this.compoundDrawableLeft = left;
      this.compoundDrawableTop = top;
      this.compoundDrawableRight = right;
      this.compoundDrawableBottom = bottom;
      return this;
    }

    /**
     * {@link android.widget.TextView#setCompoundDrawablePadding(int)}
     */
    public Builder setCompoundDrawablePadding(int pad) {
      this.compoundDrawablePadding = pad;
      return this;
    }

    /**
     * {@link android.widget.TextView#setIncludeFontPadding(boolean)}
     */
    public Builder setIncludeFontPadding(boolean includeFrontPadding) {
      this.textIncludeFrontPadding = includeFrontPadding;
      return this;
    }

    public LabelTextAttributes build() {
      return new LabelTextAttributes(this);
    }
  }
}

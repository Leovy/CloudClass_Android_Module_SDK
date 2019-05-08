package com.allin.basicres.widget.label;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public class LabelBackgroundAttributes {
  static final int COLOR_INVALID = -1;

  final int paddingStart;
  final int paddingTop;
  final int paddingEnd;
  final int paddingBottom;

  final int strokeWidth;
  final @ColorInt int strokeColor;
  final float strokeDashWidth;
  final float strokeDashGap;

  final @ColorInt int pressedBackgroundColor;
  final @ColorInt int backgroundColor;
  final @BackgroundStyle
  int backgroundStyle;

  final float topLeftCornerRadius;
  final float topRightCornerRadius;
  final float bottomRightCornerRadius;
  final float bottomLeftCornerRadius;

  private LabelBackgroundAttributes(Builder builder) {
    this.paddingStart = builder.paddingStart;
    this.paddingTop = builder.paddingTop;
    this.paddingEnd = builder.paddingEnd;
    this.paddingBottom = builder.paddingBottom;

    this.strokeWidth = builder.strokeWidth;
    this.strokeColor = builder.strokeColor;
    this.strokeDashWidth = builder.strokeDashWidth;
    this.strokeDashGap = builder.strokeDashGap;

    this.pressedBackgroundColor = builder.pressedBackgroundColor;
    this.backgroundColor = builder.backgroundColor;
    this.backgroundStyle = builder.backgroundStyle;

    this.topLeftCornerRadius = builder.topLeftCornerRadius;
    this.topRightCornerRadius = builder.topRightCornerRadius;
    this.bottomRightCornerRadius = builder.bottomRightCornerRadius;
    this.bottomLeftCornerRadius = builder.bottomLeftCornerRadius;
  }

  public static Builder newAttrsBuilder() {
    return new Builder();
  }

  public static class Builder {
    private int paddingStart;
    private int paddingTop;
    private int paddingEnd;
    private int paddingBottom;

    private int strokeWidth;
    private @ColorInt int strokeColor;
    private float strokeDashWidth;
    private float strokeDashGap;

    private @ColorInt int pressedBackgroundColor;
    private @ColorInt int backgroundColor;
    private @BackgroundStyle
    int backgroundStyle;

    private float topLeftCornerRadius;
    private float topRightCornerRadius;
    private float bottomRightCornerRadius;
    private float bottomLeftCornerRadius;

    private Builder() {
      this.pressedBackgroundColor = COLOR_INVALID;
      this.backgroundColor = Color.WHITE;
      this.backgroundStyle = BackgroundStyle.STROKE;
    }

    /**
     * @param start in px
     * @param top in px
     * @param end in px
     * @param bottom in px
     */
    public Builder setPadding(int start, int top, int end, int bottom) {
      this.paddingStart = start;
      this.paddingTop = top;
      this.paddingEnd = end;
      this.paddingBottom = bottom;
      return this;
    }

    public Builder setStroke(int width, @ColorInt int color, float dashWidth, float dashGap) {
      this.strokeWidth = width;
      this.strokeColor = color;
      this.strokeDashWidth = dashWidth;
      this.strokeDashGap = dashGap;
      return this;
    }

    /**
     * @param topLeft in px
     * @param topRight in px
     * @param bottomRight in px
     * @param bottomLeft in px
     */
    public Builder setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
      this.topLeftCornerRadius = topLeft;
      this.topRightCornerRadius = topRight;
      this.bottomRightCornerRadius = bottomRight;
      this.bottomLeftCornerRadius = bottomLeft;
      return this;
    }

    public Builder setPressedBackgroundColor(@ColorInt int color) {
      this.pressedBackgroundColor = color;
      return this;
    }

    public Builder setBackgroundColor(@ColorInt int color) {
      this.backgroundColor = color;
      return this;
    }

    public Builder setBackgroundStyle(@BackgroundStyle int style) {
      this.backgroundStyle = style;
      return this;
    }

    public LabelBackgroundAttributes build() {
      return new LabelBackgroundAttributes(this);
    }
  }
}

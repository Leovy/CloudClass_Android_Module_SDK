package com.allin.basicres.widget.label;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Objects;

/**
 * 标签配置
 * @see TextLabel
 */
public interface Label {

  @Nullable
  View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container);

  void onViewCreated(@NonNull View labelView);

  default LabelView.LayoutParams generateLayoutParams() {
    return new LabelView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  }

  class TextLabel implements Label {
    private final CharSequence text;
    private final @NonNull LabelView.LayoutParams mLayoutParams;
    private final @NonNull
    LabelBackgroundAttributes backgroundAttrs;
    private final @NonNull
    LabelTextAttributes textAttrs;

    private TextLabel(CharSequence text,
        @NonNull LabelView.LayoutParams layoutParams,
        @NonNull LabelBackgroundAttributes backgroundAttrs,
        @NonNull LabelTextAttributes textAttrs) {
      this.text = text;
      this.mLayoutParams = layoutParams;
      this.backgroundAttrs = Objects.requireNonNull(backgroundAttrs);
      this.textAttrs = Objects.requireNonNull(textAttrs);
    }

    @Override
    public LabelView.LayoutParams generateLayoutParams() {
      return mLayoutParams;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
      Context context = inflater.getContext();

      FrameLayout labelView = new FrameLayout(context);

      FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(
          LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      textLayoutParams.gravity = Gravity.CENTER;
      textLayoutParams.setMarginStart(backgroundAttrs.paddingStart);
      textLayoutParams.topMargin = backgroundAttrs.paddingTop;
      textLayoutParams.setMarginEnd(backgroundAttrs.paddingEnd);
      textLayoutParams.bottomMargin = backgroundAttrs.paddingBottom;

      labelView.addView(new TextView(context), textLayoutParams);
      return labelView;
    }

    @Override
    public void onViewCreated(@NonNull View labelView) {
      ViewGroup labelViewGroup = (ViewGroup) labelView;
      labelViewGroup.setBackground(getBackground());

      TextView textView = (TextView) labelViewGroup.getChildAt(0);
      textView.setText(text);
      textView.setTypeface(Typeface.defaultFromStyle(textAttrs.typefaceStyle));
      textView.setTextColor(textAttrs.textColor);
      textView.setTextSize(textAttrs.textSize);
      textView.setMaxLines(textAttrs.textMaxLines);
      if (textAttrs.textEllipsize != null) {
        textView.setEllipsize(textAttrs.textEllipsize);
      }
      textView.setCompoundDrawables(textAttrs.compoundDrawableLeft, textAttrs.compoundDrawableTop,
          textAttrs.compoundDrawableRight,
          textAttrs.compoundDrawableBottom);
      textView.setCompoundDrawablePadding(textAttrs.compoundDrawablePadding);
      textView.setIncludeFontPadding(textAttrs.textIncludeFrontPadding);
    }

    private Drawable getBackground() {
      StateListDrawable sld = new StateListDrawable();

      GradientDrawable nd = new GradientDrawable();
      if (backgroundAttrs.backgroundStyle == BackgroundStyle.FILL) {
        nd.setColor(backgroundAttrs.backgroundColor);
      }

      //top-left, top-right, bottom-right, bottom-left
      nd.setCornerRadii(new float[]{backgroundAttrs.topLeftCornerRadius,
          backgroundAttrs.topLeftCornerRadius,
          backgroundAttrs.topRightCornerRadius,
          backgroundAttrs.topRightCornerRadius,
          backgroundAttrs.bottomRightCornerRadius,
          backgroundAttrs.bottomRightCornerRadius,
          backgroundAttrs.bottomLeftCornerRadius,
          backgroundAttrs.bottomLeftCornerRadius
      });

      //If width is zero, then no stroke is drawn
      nd.setStroke(backgroundAttrs.strokeWidth, backgroundAttrs.strokeColor,
          backgroundAttrs.strokeDashWidth, backgroundAttrs.strokeDashGap);

      if (backgroundAttrs.pressedBackgroundColor != LabelBackgroundAttributes.COLOR_INVALID) {
        GradientDrawable pd = new GradientDrawable();
        pd.setColor(backgroundAttrs.pressedBackgroundColor);
        //top-left, top-right, bottom-right, bottom-left
        pd.setCornerRadii(new float[]{backgroundAttrs.topLeftCornerRadius,
            backgroundAttrs.topLeftCornerRadius,
            backgroundAttrs.topRightCornerRadius,
            backgroundAttrs.topRightCornerRadius,
            backgroundAttrs.bottomRightCornerRadius,
            backgroundAttrs.bottomRightCornerRadius,
            backgroundAttrs.bottomLeftCornerRadius,
            backgroundAttrs.bottomLeftCornerRadius
        });
        //If width is zero, then no stroke is drawn
        pd.setStroke(backgroundAttrs.strokeWidth, backgroundAttrs.pressedBackgroundColor,
            backgroundAttrs.strokeDashWidth, backgroundAttrs.strokeDashGap);

        sld.addState(new int[]{android.R.attr.state_pressed}, pd);
      }

      sld.addState(new int[]{}, nd);

      return sld;
    }

    public static Builder attrsBuilder(CharSequence text){
      return new Builder(text);
    }

    public static class Builder {
      private final CharSequence text;
      private @NonNull LabelView.LayoutParams mLayoutParams;
      private @NonNull
      LabelBackgroundAttributes mLabelBackgroundAttributes;
      private @NonNull
      LabelTextAttributes mLabelTextAttributes;

      private Builder(CharSequence text) {
        this.text = text;
        this.mLayoutParams = new LabelView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.mLabelBackgroundAttributes = LabelBackgroundAttributes.newAttrsBuilder().build();
        this.mLabelTextAttributes = LabelTextAttributes.newAttrsBuilder().build();
      }

      public Builder setLayoutParams(@NonNull LabelView.LayoutParams params) {
        this.mLayoutParams = Objects.requireNonNull(params);
        return this;
      }

      public Builder backgroundAttrs(@NonNull LabelBackgroundAttributes attrs) {
        this.mLabelBackgroundAttributes = Objects.requireNonNull(attrs);
        return this;
      }

      public Builder textAttrs(@NonNull LabelTextAttributes attrs) {
        this.mLabelTextAttributes = Objects.requireNonNull(attrs);
        return this;
      }

      public TextLabel build() {
        return new TextLabel(text, mLayoutParams, mLabelBackgroundAttributes, mLabelTextAttributes);
      }
    }
  }
}

package com.allin.basicres.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.annotation.StyleableRes;
import android.support.v4.util.Consumer;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.util.Objects;

/**
 * {@link View} 相关操作
 * Create by xianxueliang
 */
public final class ViewUtils {

  public static void runIfLaidOut(@Nullable View view, @NonNull Runnable action) {
    Objects.requireNonNull(action);
    if (view != null) {
      if (ViewCompat.isLaidOut(view)) {
        action.run();
      } else {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            action.run();
          }
        });
      }
    }
  }

  public static void setVisibility(@Nullable View view, int visibility) {
    if (visibility != View.VISIBLE && visibility != View.INVISIBLE && visibility != View.GONE) {
      throw new IllegalArgumentException(
          "unKnow visibility " + visibility + "must be one of " + View.VISIBLE + ", "
              + View.INVISIBLE + ", and " + View.GONE);
    }

    if (view != null && view.getVisibility() != visibility) {
      view.setVisibility(visibility);
    }
  }

  @Nullable
  public static View safeInflateLayout(@NonNull Context context, @LayoutRes int resource,
      @Nullable ViewGroup root) {
    return safeInflateLayout(context, resource, root, root != null);
  }

  @Nullable
  public static View safeInflateLayout(@NonNull Context context, @LayoutRes int resource,
      @Nullable ViewGroup root, boolean attachToRoot) {
    Objects.requireNonNull(context);
    LayoutInflater inflater = null;
    try {
      inflater = LayoutInflater.from(context);
    } catch (AssertionError error) {
      //throw if LayoutInflater not found
    }
    if (inflater != null) {
      try {
        return inflater.inflate(resource, root, attachToRoot);
      } catch (InflateException ex) {
        //throw if there is an error occurred
      }
    }
    return null;
  }

  public static int requiredTypefaceStyle(int style) {
    if (style != Typeface.NORMAL && style != Typeface.BOLD && style != Typeface.ITALIC
        && style != Typeface.BOLD_ITALIC) {
      throw new IllegalArgumentException("illegal typeface style " + style);
    }
    return style;
  }

  public static void obtainStyledAttributes(@NonNull Context context, AttributeSet set,
      @StyleableRes int[] attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes,
      @NonNull Consumer<TypedArray> consumer) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(consumer);
    TypedArray a = context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
    try {
      consumer.accept(a);
    } finally {
      a.recycle();
    }
  }
}

package com.allin.basicres.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;
import android.text.TextUtils;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Reflection 相关操作 Create by xianxueliang
 */
public class ReflectionUtil {

  @Nullable
  public static Class<?> tryGetClassForName(String className) {
    if (!TextUtils.isEmpty(className)) {
      try {
        return Class.forName(className);
      } catch (ClassNotFoundException ignored) {
      }
    }
    return null;
  }

  @Nullable
  public static Field tryGetDeclaredField(@Nullable Class<?> theCls, @Nullable String fieldName) {
    if (theCls != null && !TextUtils.isEmpty(fieldName)) {
      try {
        assert fieldName != null;
        return theCls.getDeclaredField(fieldName);
      } catch (NoSuchFieldException ignored) {
      }
    }
    return null;
  }

  private static boolean accessibleField(@NonNull Field tField) {
    Objects.requireNonNull(tField);
    boolean isAccessible = tField.isAccessible();
    if (!isAccessible) {
      try {
        tField.setAccessible(true);
        isAccessible = true;
      } catch (SecurityException ignored) {
      }
    }
    return isAccessible;
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public static <T> T tryGetFieldValue(@Nullable Field tField, @Nullable Object target) {
    if (tField != null && target != null) {
      if (accessibleField(tField)) {
        Object fv = null;
        try {
          fv = tField.get(target);
        } catch (IllegalAccessException | IllegalArgumentException | NullPointerException
            | ExceptionInInitializerError ignored) {
        }

        if (fv != null) {
          try {
            return ((T) fv);
          } catch (ClassCastException ignored) {
          }
        }
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static int tryGetFieldIntValue(@Nullable Field tField, @Nullable Object target,
      int defaultValue) {
    if (tField != null && target != null) {
      if (accessibleField(tField)) {
        try {
          return tField.getInt(target);
        } catch (IllegalAccessException | IllegalArgumentException | NullPointerException
            | ExceptionInInitializerError ignored) {
        }
      }
    }
    return defaultValue;
  }
}

package com.allin.commlibrary;

import java.util.Collection;

public class CollectionUtils {
  private CollectionUtils() {
    throw new AssertionError();
  }

  public static <V> boolean isEmpty(Collection<V> c) {
    return c == null || c.size() == 0;
  }
}

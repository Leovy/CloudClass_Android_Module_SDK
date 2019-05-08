package com.allin.basicres.widget.label;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({BackgroundStyle.FILL, BackgroundStyle.STROKE})
@Retention(RetentionPolicy.SOURCE)
public @interface BackgroundStyle {
  int FILL = 0;
  int STROKE = 1;
}

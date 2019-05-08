package com.allin.basicres.widget.label;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

public interface OnLabelClickListener<T> {

  void onClick(@NonNull View labelView, @NonNull T label, @IntRange(from = 0) int position);
}

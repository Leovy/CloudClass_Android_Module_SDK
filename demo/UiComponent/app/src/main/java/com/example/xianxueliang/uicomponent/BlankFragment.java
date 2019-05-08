package com.example.xianxueliang.uicomponent;


import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.allin.basicres.widget.label.BackgroundStyle;
import com.allin.basicres.widget.label.Label;
import com.allin.basicres.widget.label.Label.TextLabel;
import com.allin.basicres.widget.label.LabelBackgroundAttributes;
import com.allin.basicres.widget.label.LabelTextAttributes;
import com.allin.basicres.widget.label.LabelView;
import java.util.ArrayList;
import me.jessyan.autosize.utils.AutoSizeUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

  private static final String ARG_BG_COLOR = "backgroundColor";
  private int mBgColor;

  public static BlankFragment newInstance(@ColorInt int bgColor) {
    BlankFragment f = new BlankFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(ARG_BG_COLOR, bgColor);
    f.setArguments(bundle);
    return f;
  }

  public BlankFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      mBgColor = args.getInt(ARG_BG_COLOR);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_blank, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    view.setBackgroundColor(mBgColor);
    LabelView labelView = view.findViewById(R.id.lv);
    labelView.setOnLabelClickListener((labelView1, label, position) -> LinkageDialog.newInstance().show(getChildFragmentManager(), "LinkageDialog"));

    ArrayList<Label> labels = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      labels.add(
          TextLabel.attrsBuilder("Label_" + i)
              .textAttrs(LabelTextAttributes.newAttrsBuilder()
                  .setTypefaceStyle(Typeface.NORMAL)
                  .setTextColor(Color.RED)
                  .setTextSize(25)
                  .setCompoundDrawables(compoundDrawable(), compoundDrawable(), compoundDrawable(), compoundDrawable())
                  .setCompoundDrawablePadding(AutoSizeUtils.dp2px(getContext(), 10))
                  .setIncludeFontPadding(false)
                  .build())
              .backgroundAttrs(LabelBackgroundAttributes.newAttrsBuilder()
                  .setBackgroundStyle(BackgroundStyle.FILL)
                  .setBackgroundColor(Color.BLACK)
                  .setPressedBackgroundColor(Color.YELLOW)
                  .setCornerRadius(AutoSizeUtils.dp2px(getContext(), 3), AutoSizeUtils.dp2px(getContext(), 3), AutoSizeUtils.dp2px(getContext(), 3), AutoSizeUtils.dp2px(getContext(), 3))
                  .setPadding(AutoSizeUtils.dp2px(getContext(), 10), AutoSizeUtils.dp2px(getContext(), 10), AutoSizeUtils.dp2px(getContext(), 10), AutoSizeUtils.dp2px(getContext(), 10))
                  .setStroke(AutoSizeUtils.dp2px(getContext(), 5), Color.WHITE, 0, 0)
                  .build())
              .build()
      );
    }
    labelView.addLabels(labels);
  }

  @NonNull
  private Drawable compoundDrawable() {
    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background);
    drawable.setBounds(0, 0, AutoSizeUtils.dp2px(getContext(), 5),
        AutoSizeUtils.dp2px(getContext(), 5));
    return drawable;
  }
}

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
import android.widget.TextView;
import android.widget.Toast;
import com.allin.basicres.widget.label.BackgroundStyle;
import com.allin.basicres.widget.label.Label;
import com.allin.basicres.widget.label.Label.TextLabel;
import com.allin.basicres.widget.label.LabelBackgroundAttributes;
import com.allin.basicres.widget.label.LabelTextAttributes;
import com.allin.basicres.widget.label.LabelView;
import java.util.ArrayList;
import kotlin.jvm.functions.Function1;
import me.jessyan.autosize.utils.AutoSizeUtils;
import org.json.JSONException;
import org.json.JSONObject;


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
    TextView testTextView = (TextView) view.findViewById(R.id.testTextView);

    LabelView labelView = view.findViewById(R.id.lv);
    labelView.setOnLabelClickListener((labelView1, label, position) -> {
      try {

        //解析数据
        ArrayList<CampaignTree> trees = CampaignLinkagesKt
            .parseTree(new JSONObject(TEST_JSON).getJSONArray("data_list"), jo -> {
              CampaignTree tree = new CampaignTree();
              tree.setPropertyFullPath(jo.optString("propertyFullPath"));
              tree.setPropertyId(jo.optString("propertyId"));
              return tree;
            });
        if (trees == null || trees.isEmpty()) {
          Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
        } else {
          LinkageDialog.show(getChildFragmentManager(), trees);
        }

      } catch (JSONException ignored) {
      }
    });

    ArrayList<Label> labels = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      labels.add(
          TextLabel.attrsBuilder("Label_" + i)
              .textAttrs(LabelTextAttributes.newAttrsBuilder()
                  .setTypefaceStyle(Typeface.NORMAL)
                  .setTextColor(Color.RED)
                  .setTextSize(25)
                  .setCompoundDrawables(compoundDrawable(), compoundDrawable(), compoundDrawable(),
                      compoundDrawable())
                  .setCompoundDrawablePadding(AutoSizeUtils.dp2px(getContext(), 10))
                  .setIncludeFontPadding(false)
                  .build())
              .backgroundAttrs(LabelBackgroundAttributes.newAttrsBuilder()
                  .setBackgroundStyle(BackgroundStyle.FILL)
                  .setBackgroundColor(Color.BLACK)
                  .setPressedBackgroundColor(Color.YELLOW)
                  .setCornerRadius(AutoSizeUtils.dp2px(getContext(), 3),
                      AutoSizeUtils.dp2px(getContext(), 3), AutoSizeUtils.dp2px(getContext(), 3),
                      AutoSizeUtils.dp2px(getContext(), 3))
                  .setPadding(AutoSizeUtils.dp2px(getContext(), 10),
                      AutoSizeUtils.dp2px(getContext(), 10), AutoSizeUtils.dp2px(getContext(), 10),
                      AutoSizeUtils.dp2px(getContext(), 10))
                  .setStroke(AutoSizeUtils.dp2px(getContext(), 5), Color.WHITE, 0, 0)
                  .build())
              .build()
      );
    }
    labelView.addLabels(labels);

    testTextView.setText("防盗锁了房间四道口路附/加赛的浪/费jsld/kfjlsdkjfsdl减肥的四六级防盗锁了");
  }

  @NonNull
  private Drawable compoundDrawable() {
    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background);
    drawable.setBounds(0, 0, AutoSizeUtils.dp2px(getContext(), 5),
        AutoSizeUtils.dp2px(getContext(), 5));
    return drawable;
  }

  public static final String TEST_JSON = "{\"data_list\": [{\n" +
      "\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\"treeLevel\": 1,\n" +
      "\t\t\t\"propertyName\": \"北区\",\n" +
      "\t\t\t\"property_2\": [{\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [{\n" +
      "\t\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\t\"treeLevel\": 3,\n" +
      "\t\t\t\t\t\"propertyName\": \"骨肿瘤\",\n" +
      "\t\t\t\t\t\"propertyFullPath\": \"2074,2078,2104\",\n" +
      "\t\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\t\"property_4\": [{\n" +
      "\t\t\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\t\t\"treeLevel\": 4,\n" +
      "\t\t\t\t\t\t\"propertyName\": \"四肢矫形\",\n" +
      "\t\t\t\t\t\t\"propertyFullPath\": \"2074,2078,2104,2159\",\n" +
      "\t\t\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\t\t\"id\": 2159,\n" +
      "\t\t\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\t\t\"propertyId\": 4,\n" +
      "\t\t\t\t\t\t\"parentId\": 2104\n" +
      "\t\t\t\t\t}],\n" +
      "\t\t\t\t\t\"id\": 2104,\n" +
      "\t\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\t\"propertyId\": 816,\n" +
      "\t\t\t\t\t\"parentId\": 2078\n" +
      "\t\t\t\t}],\n" +
      "\t\t\t\t\"propertyName\": \"北京赛区\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2074,2078\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2078,\n" +
      "\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\"propertyId\": 1459415965351,\n" +
      "\t\t\t\t\"parentId\": 2074\n" +
      "\t\t\t}, {\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [{\n" +
      "\t\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\t\"treeLevel\": 3,\n" +
      "\t\t\t\t\t\"propertyName\": \"中西医结合\",\n" +
      "\t\t\t\t\t\"propertyFullPath\": \"2074,2079,2105\",\n" +
      "\t\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\t\"property_4\": [{\n" +
      "\t\t\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\t\t\"treeLevel\": 4,\n" +
      "\t\t\t\t\t\t\"propertyName\": \"四肢矫形\",\n" +
      "\t\t\t\t\t\t\"propertyFullPath\": \"2074,2079,2105,2160\",\n" +
      "\t\t\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\t\t\"id\": 2160,\n" +
      "\t\t\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\t\t\"propertyId\": 4,\n" +
      "\t\t\t\t\t\t\"parentId\": 2105\n" +
      "\t\t\t\t\t}],\n" +
      "\t\t\t\t\t\"id\": 2105,\n" +
      "\t\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\t\"propertyId\": 821,\n" +
      "\t\t\t\t\t\"parentId\": 2079\n" +
      "\t\t\t\t}],\n" +
      "\t\t\t\t\"propertyName\": \"天津赛区\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2074,2079\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2079,\n" +
      "\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\"propertyId\": 1459416163242,\n" +
      "\t\t\t\t\"parentId\": 2074\n" +
      "\t\t\t}, {\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [{\n" +
      "\t\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\t\"treeLevel\": 3,\n" +
      "\t\t\t\t\t\"propertyName\": \"四肢矫形\",\n" +
      "\t\t\t\t\t\"propertyFullPath\": \"2074,2080,2161\",\n" +
      "\t\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\t\"property_4\": [],\n" +
      "\t\t\t\t\t\"id\": 2161,\n" +
      "\t\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\t\"propertyId\": 4,\n" +
      "\t\t\t\t\t\"parentId\": 2080\n" +
      "\t\t\t\t}],\n" +
      "\t\t\t\t\"propertyName\": \"内蒙古赛区\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2074,2080\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2080,\n" +
      "\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\"propertyId\": 1462857398505,\n" +
      "\t\t\t\t\"parentId\": 2074\n" +
      "\t\t\t}],\n" +
      "\t\t\t\"propertyFullPath\": \"2074\",\n" +
      "\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\"id\": 2074,\n" +
      "\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\"propertyId\": 1460079778439,\n" +
      "\t\t\t\"parentId\": 0\n" +
      "\t\t}, {\n" +
      "\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\"treeLevel\": 1,\n" +
      "\t\t\t\"propertyName\": \"南区\",\n" +
      "\t\t\t\"property_2\": [{\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [],\n" +
      "\t\t\t\t\"propertyName\": \"南京赛区\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2075,2081\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2081,\n" +
      "\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\"propertyId\": 1459945317314,\n" +
      "\t\t\t\t\"parentId\": 2075\n" +
      "\t\t\t}, {\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [],\n" +
      "\t\t\t\t\"propertyName\": \"上海赛区\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2075,2082\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2082,\n" +
      "\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\"propertyId\": 1459416175609,\n" +
      "\t\t\t\t\"parentId\": 2075\n" +
      "\t\t\t}],\n" +
      "\t\t\t\"propertyFullPath\": \"2075\",\n" +
      "\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\"id\": 2075,\n" +
      "\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\"propertyId\": 1461123868694,\n" +
      "\t\t\t\"parentId\": 0\n" +
      "\t\t}, {\n" +
      "\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\"treeLevel\": 1,\n" +
      "\t\t\t\"propertyName\": \"西区\",\n" +
      "\t\t\t\"property_2\": [{\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [],\n" +
      "\t\t\t\t\"propertyName\": \"新疆赛区\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2076,2083\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2083,\n" +
      "\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\"propertyId\": 1463370312080,\n" +
      "\t\t\t\t\"parentId\": 2076\n" +
      "\t\t\t}, {\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [],\n" +
      "\t\t\t\t\"propertyName\": \"成都赛区\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2076,2084\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2084,\n" +
      "\t\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\t\"propertyId\": 1463370205491,\n" +
      "\t\t\t\t\"parentId\": 2076\n" +
      "\t\t\t}],\n" +
      "\t\t\t\"propertyFullPath\": \"2076\",\n" +
      "\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\"id\": 2076,\n" +
      "\t\t\t\"activityType\": 8,\n" +
      "\t\t\t\"propertyId\": 1461123881532,\n" +
      "\t\t\t\"parentId\": 0\n" +
      "\t\t}]\n" +
      "\t}";

  public static final String TEST_JSON2 = "{\n" +
      "\t\t\"data_list\": [{\n" +
      "\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\"treeLevel\": 1,\n" +
      "\t\t\t\"propertyName\": \"创伤\",\n" +
      "\t\t\t\"property_2\": [{\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [{\n" +
      "\t\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\t\"treeLevel\": 3,\n" +
      "\t\t\t\t\t\"propertyName\": \"足踝11\",\n" +
      "\t\t\t\t\t\"propertyFullPath\": \"2068,2071,2162\",\n" +
      "\t\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\t\"property_4\": [{\n" +
      "\t\t\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\t\t\"treeLevel\": 4,\n" +
      "\t\t\t\t\t\t\"propertyName\": \"四肢矫形\",\n" +
      "\t\t\t\t\t\t\"propertyFullPath\": \"2068,2071,2162,2163\",\n" +
      "\t\t\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\t\t\"id\": 2163,\n" +
      "\t\t\t\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\t\t\t\"propertyId\": 4,\n" +
      "\t\t\t\t\t\t\"parentId\": 2162\n" +
      "\t\t\t\t\t}],\n" +
      "\t\t\t\t\t\"id\": 2162,\n" +
      "\t\t\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\t\t\"propertyId\": 3,\n" +
      "\t\t\t\t\t\"parentId\": 2071\n" +
      "\t\t\t\t}],\n" +
      "\t\t\t\t\"propertyName\": \"显微修复\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2068,2071\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2071,\n" +
      "\t\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\t\"propertyId\": 820,\n" +
      "\t\t\t\t\"parentId\": 2068\n" +
      "\t\t\t}],\n" +
      "\t\t\t\"propertyFullPath\": \"2068\",\n" +
      "\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\"id\": 2068,\n" +
      "\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\"propertyId\": 9,\n" +
      "\t\t\t\"parentId\": 0\n" +
      "\t\t}, {\n" +
      "\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\"treeLevel\": 1,\n" +
      "\t\t\t\"propertyName\": \"康复\",\n" +
      "\t\t\t\"property_2\": [{\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [],\n" +
      "\t\t\t\t\"propertyName\": \"中西医结合\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2069,2072\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2072,\n" +
      "\t\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\t\"propertyId\": 821,\n" +
      "\t\t\t\t\"parentId\": 2069\n" +
      "\t\t\t}],\n" +
      "\t\t\t\"propertyFullPath\": \"2069\",\n" +
      "\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\"id\": 2069,\n" +
      "\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\"propertyId\": 817,\n" +
      "\t\t\t\"parentId\": 0\n" +
      "\t\t}, {\n" +
      "\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\"treeLevel\": 1,\n" +
      "\t\t\t\"propertyName\": \"运动医学\",\n" +
      "\t\t\t\"property_2\": [{\n" +
      "\t\t\t\t\"activityId\": 1548751774179,\n" +
      "\t\t\t\t\"treeLevel\": 2,\n" +
      "\t\t\t\t\"property_3\": [],\n" +
      "\t\t\t\t\"propertyName\": \"骨质疏松\",\n" +
      "\t\t\t\t\"propertyFullPath\": \"2070,2073\",\n" +
      "\t\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\t\"id\": 2073,\n" +
      "\t\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\t\"propertyId\": 818,\n" +
      "\t\t\t\t\"parentId\": 2070\n" +
      "\t\t\t}],\n" +
      "\t\t\t\"propertyFullPath\": \"2070\",\n" +
      "\t\t\t\"isValid\": 1,\n" +
      "\t\t\t\"id\": 2070,\n" +
      "\t\t\t\"activityType\": 7,\n" +
      "\t\t\t\"propertyId\": 8,\n" +
      "\t\t\t\"parentId\": 0\n" +
      "\t\t}]\n" +
      "\t}";
}

package com.example.xianxueliang.uicomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Consumer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.allin.basicres.utils.ReflectionUtil;
import com.allin.basicres.utils.ViewUtils;
import com.allin.commlibrary.CollectionUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import me.jessyan.autosize.utils.AutoSizeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 拓展{@link SlidingTabLayout} Create by xianxueliang
 */
public class SupportSlidingTabLayout extends SlidingTabLayout {

  public static final @IdRes int ID_RES_TAB_TEXT_VIEW = com.flyco.tablayout.R.id.tv_tab_title;

  private float mSelectedTextSize;
  private float mUnselectedTextSize;
  private int mSelectedTextStyle;
  private int mUnselectedTextStyle;
  private boolean mTextSingleLine;
  private boolean mTextIncludeFontPadding;
  private float mTextPaddingTop;
  private float mTextPaddingBottom;

  private OnTabAttributeUpdateListener onTabAttributeUpdateListener;

  public SupportSlidingTabLayout(Context context) {
    this(context, null);
  }

  public SupportSlidingTabLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SupportSlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    ViewUtils.obtainStyledAttributes(context, attrs, R.styleable.SupportSlidingTabLayout,
            defStyleAttr, 0,
            a -> {
              mSelectedTextSize = a
                      .getDimension(R.styleable.SupportSlidingTabLayout_tl_selectedTextSize,
                              AutoSizeUtils.sp2px(context, 16));
              mUnselectedTextSize = a
                      .getDimension(R.styleable.SupportSlidingTabLayout_tl_unselectedTextSize,
                              AutoSizeUtils.sp2px(context, 16));
              mSelectedTextStyle = a
                      .getInt(R.styleable.SupportSlidingTabLayout_tl_selectedTextStyle, Typeface.NORMAL);
              mUnselectedTextStyle = a
                      .getInt(R.styleable.SupportSlidingTabLayout_tl_unselectedTextStyle, Typeface.NORMAL);
              mTextSingleLine = a
                      .getBoolean(R.styleable.SupportSlidingTabLayout_tl_textSingleLine, false);
              mTextIncludeFontPadding = a
                      .getBoolean(R.styleable.SupportSlidingTabLayout_tl_textIncludeFontPadding, true);
              mTextPaddingTop = a
                      .getDimension(R.styleable.SupportSlidingTabLayout_tl_textPaddingTop, 0);
              mTextPaddingBottom = a
                      .getDimension(R.styleable.SupportSlidingTabLayout_tl_textPaddingBottom, 0);
            });

    ViewUtils.runIfLaidOut(this,
            () -> updateTabTextAttribute(getCurrentTab()));
  }

  public void setViewPager(@NonNull ViewPager vp, @NonNull String[] titles,
                           @NonNull FragmentManager fm, @NonNull List<Fragment> fragments) {
    vp.setAdapter(new DefaultPagerAdapter(fm, fragments, titles));
    setViewPager(vp, titles);
  }

  @Override
  public void notifyDataSetChanged() {
    super.notifyDataSetChanged();
    updateTabTextAttribute(getCurrentTab());
  }

  @Override
  public void onPageSelected(int position) {
    super.onPageSelected(position);
    updateTabTextAttribute(position);
  }

  /**
   * 获取当前选中的Fragment
   */
  @Nullable
  public <T extends Fragment> T getCurrentFragment() {
    return getItemFragmentAt(getCurrentTab());
  }

  /**
   * 获取指定位置的Fragment
   * @param index 指定位置
   */
  @Nullable
  @SuppressWarnings("unchecked")
  public <T extends Fragment> T getItemFragmentAt(@IntRange(from = 0) int index) {
    ViewPager viewPager = getViewPager();
    if (viewPager != null) {
      PagerAdapter adapter = viewPager.getAdapter();
      if (adapter != null && (index >= 0 && index < adapter.getCount())) {
        if (adapter instanceof FragmentPagerAdapter) {
          return ((T) ((FragmentPagerAdapter) adapter).getItem(index));
        } else if (adapter instanceof FragmentStatePagerAdapter) {
          return ((T) ((FragmentStatePagerAdapter) adapter).getItem(index));
        } else {
          throw new IllegalStateException("adapter is not instance of FragmentPagerAdapter or"
                  + " FragmentStatePagerAdapter");
        }
      }
    }
    return null;
  }

  public void removeAllTab() {
    for (int i = 0; i < getTabCount(); i++) {
      removeTabAt(i);
    }
  }

  public void addTab(int position, @NonNull String title, @NonNull Fragment fragment) {
    Objects.requireNonNull(title);
    Objects.requireNonNull(fragment);

    View tabView = ViewUtils
            .safeInflateLayout(getContext(), com.flyco.tablayout.R.layout.layout_tab, null);
    Objects.requireNonNull(tabView, "inflate com.flyco.tablayout.R.layout.layout_tab err");

    Field titlesField = requiredFieldByName("mTitles");
    ArrayList<String> titles = ReflectionUtil.tryGetFieldValue(titlesField, this);
    //noinspection Guava
    titles = Optional.fromNullable(titles).or(Lists.newArrayList());
    try {
      titles.add(position, title);
      titlesField.set(this, titles);
    } catch (IllegalAccessException ignored) {
    }

    try {
      Method addTabMethod = SlidingTabLayout.class
              .getDeclaredMethod("addTab", int.class, String.class, View.class);
      addTabMethod.setAccessible(true);
      addTabMethod.invoke(this, position, title, tabView);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException("invoke method(addTab(int, String, View) err)");
    }

    Field tabCountField = requiredFieldByName("mTabCount");
    int tabCount = ReflectionUtil.tryGetFieldIntValue(tabCountField, this, 0);
    try {
      tabCountField.setInt(this, ++tabCount);
    } catch (IllegalAccessException ignored) {
    }

    try {
      Method method = SlidingTabLayout.class.getDeclaredMethod("updateTabStyles");
      method.setAccessible(true);
      method.invoke(this);
    } catch (NoSuchMethodException | IllegalAccessException
            | InvocationTargetException ignored) {
      throw new IllegalStateException("invoke method(updateTabStyles) of SlidingTabLayout err");
    }

    updateTabTextAttribute(getCurrentTab());

    ViewPager viewPager = getViewPager();
    if (viewPager != null) {
      PagerAdapter adapter = viewPager.getAdapter();
      if (adapter instanceof DefaultPagerAdapter) {
        ((DefaultPagerAdapter)adapter).add(position, title, fragment);
      }
    }
  }

  public void addTab(@NonNull String title, @NonNull Fragment fragment) {
    Objects.requireNonNull(title);
    Objects.requireNonNull(fragment);

    addNewTab(title);
    updateTabTextAttribute(getCurrentTab());

    ViewPager viewPager = getViewPager();
    if (viewPager != null) {
      PagerAdapter adapter = viewPager.getAdapter();
      if (adapter instanceof DefaultPagerAdapter) {
        ((DefaultPagerAdapter)adapter).add(title, fragment);
      }
    }
  }

  public void removeTabAt(int index) {
    ViewGroup tabsContainer = getTabsContainer();
    if (tabsContainer != null && index >= 0 && index < tabsContainer.getChildCount()) {

      tabsContainer.removeViewAt(index);

      Field titlesField = requiredFieldByName("mTitles");
      ArrayList<String> titles = ReflectionUtil.tryGetFieldValue(titlesField, this);
      if (!CollectionUtils.isEmpty(titles) && index < titles.size()) {
        titles.remove(index);
        try {
          titlesField.set(this, titles);
        } catch (IllegalAccessException ignored) {
        }
      }


      Field tabCountField = requiredFieldByName("mTabCount");
      int tabCount = ReflectionUtil.tryGetFieldIntValue(tabCountField, this, 0);
      if (tabCount > 0) {
        try {
          tabCountField.setInt(this, --tabCount);
        } catch (IllegalAccessException ignored) {
        }
      }

      Field currentTabField = requiredFieldByName("mCurrentTab");
      int currentTab = ReflectionUtil.tryGetFieldIntValue(currentTabField, this, 0);
      if (currentTab > 0) {
        try {
          currentTabField.setInt(this, --currentTab);
          setCurrentTab(currentTab);
        } catch (IllegalAccessException ignored) {
        }
      } else if (currentTab == 0) {
        try {
          Method method = SlidingTabLayout.class.getDeclaredMethod("updateTabStyles");
          method.setAccessible(true);
          method.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException ignored) {
          throw new IllegalStateException("invoke method(updateTabStyles) of SlidingTabLayout err");
        }
        updateTabTextAttribute(currentTab);
      }

      ViewPager viewPager = getViewPager();
      if (viewPager != null) {
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter instanceof DefaultPagerAdapter) {
          ((DefaultPagerAdapter)adapter).remove(index);
        }
      }
    }
  }

  private void updateTabTextAttribute(@IntRange(from = 0) int currentIndex) {
    ViewGroup tabsContainer = getTabsContainer();
    int tc = getTabCount();
    if (tc > 0 && tabsContainer != null && tabsContainer.getChildCount() == tc) {
      for (int tabIndex = 0; tabIndex < tc; tabIndex++) {
        View child = tabsContainer.getChildAt(tabIndex);
        TextView tabTextView = child.findViewById(ID_RES_TAB_TEXT_VIEW);
        if (tabTextView != null) {
          boolean isSelected = currentIndex == tabIndex;
          tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                  isSelected ? mSelectedTextSize : mUnselectedTextSize);
          tabTextView.setTypeface(Typeface.DEFAULT,
                  isSelected ? mSelectedTextStyle : mUnselectedTextStyle);
          tabTextView.setSingleLine(mTextSingleLine);
          tabTextView.setIncludeFontPadding(mTextIncludeFontPadding);
          tabTextView.setPadding(tabTextView.getPaddingLeft(),
                  Math.round(mTextPaddingTop),
                  tabTextView.getPaddingRight(),
                  Math.round(mTextPaddingBottom));

          if (onTabAttributeUpdateListener != null) {
            onTabAttributeUpdateListener.onAttributeUpdate(tabTextView, currentIndex, tabIndex);
          }
        }
      }
    }
  }

  @Nullable
  public ViewPager getViewPager() {
    Field tField = requiredFieldByName("mViewPager");
    return ReflectionUtil.tryGetFieldValue(tField, this);
  }

  @Nullable
  private ViewGroup getTabsContainer() {
    Field tField = requiredFieldByName("mTabsContainer");
    return ReflectionUtil.tryGetFieldValue(tField, this);
  }

  @Nullable
  public TextView getTabTextViewAt(@IntRange(from = 0) int index) {
    ViewGroup tabsContainer = getTabsContainer();
    if (tabsContainer != null && index >= 0 && index < tabsContainer.getChildCount()) {
      View child = tabsContainer.getChildAt(index);
      if (child != null) {
        return child.findViewById(ID_RES_TAB_TEXT_VIEW);
      }
    }
    return null;
  }

  public void eachTabView(@NonNull Consumer2 consumer) {
    Objects.requireNonNull(consumer);
    ViewGroup tabsContainer = getTabsContainer();
    if (tabsContainer != null) {
      for (int i = 0; i < tabsContainer.getChildCount(); i++) {
        View tabView = tabsContainer.getChildAt(i);
        consumer.accept(i, tabView);
      }
    }
  }

  public void eachTabTextView(@NonNull Consumer<TextView> consumer) {
    Objects.requireNonNull(consumer);
    for (int i = 0; i < getTabCount(); i++) {
      TextView tabTextView = getTabTextViewAt(i);
      if (tabTextView != null) {
        consumer.accept(tabTextView);
      }
    }
  }

  private Field requiredFieldByName(String fieldName) {
    Field tField = ReflectionUtil.tryGetDeclaredField(SlidingTabLayout.class, fieldName);
    if (tField == null) {
      throw new IllegalStateException("not found field with name " + fieldName + ""
              + " in SlidingTabLayout,sdk update probably, please check it");
    }
    return tField;
  }

  /**
   * 设置选中状态下tab的文字的大小
   *
   * @param textSize in px
   */
  public void setSelectedTextSize(float textSize) {
    if (mSelectedTextSize != textSize) {
      mSelectedTextSize = textSize;
      updateTabTextAttribute(getCurrentTab());
    }
  }

  /**
   * 设置未选中状态下tab的文字的大小
   *
   * @param textSize in px
   */
  public void setUnselectedTextSize(float textSize) {
    if (mUnselectedTextSize != textSize) {
      mUnselectedTextSize = textSize;
      updateTabTextAttribute(getCurrentTab());
    }
  }

  /**
   * 设置选中状态下tab的文字的样式, {@link Typeface#NORMAL}, {@link Typeface#BOLD}, {@link Typeface#BOLD_ITALIC}
   *
   * @see Typeface
   */
  public void setSelectedTextStyle(int style) {
    ViewUtils.requiredTypefaceStyle(style);
    if (mSelectedTextStyle != style) {
      mSelectedTextStyle = style;
      updateTabTextAttribute(getCurrentTab());
    }
  }

  /**
   * 设置未选中状态下tab的文字的样式, {@link Typeface#NORMAL}, {@link Typeface#BOLD}, {@link
   * Typeface#BOLD_ITALIC}
   *
   * @see Typeface
   */
  public void setUnselectedTextStyle(int style) {
    ViewUtils.requiredTypefaceStyle(style);
    if (mUnselectedTextStyle != style) {
      mUnselectedTextStyle = style;
      updateTabTextAttribute(getCurrentTab());
    }
  }

  /**
   * 设置tab的文字是否单行显示
   */
  public void setTextSingleLine(boolean singleLine) {
    if (mTextSingleLine != singleLine) {
      mTextSingleLine = singleLine;
      updateTabTextAttribute(getCurrentTab());
    }
  }

  /**
   * 设置tab的文字的上内边距
   *
   * @param paddingTop in px
   */
  public void setTextPaddingTop(float paddingTop) {
    if (mTextPaddingTop != paddingTop) {
      mTextPaddingTop = paddingTop;
      updateTabTextAttribute(getCurrentTab());
    }
  }

  /**
   * 设置tab的文字的下内边距
   *
   * @param paddingBottom in px
   */
  public void setTextPaddingBottom(float paddingBottom) {
    if (mTextPaddingBottom != paddingBottom) {
      mTextPaddingBottom = paddingBottom;
      updateTabTextAttribute(getCurrentTab());
    }
  }

  public void setOnTabAttributeUpdateListener(@Nullable OnTabAttributeUpdateListener onTabAttributeUpdateListener) {
    this.onTabAttributeUpdateListener = onTabAttributeUpdateListener;
  }

  /**
   * TabView属性更新监听
   */
  public interface OnTabAttributeUpdateListener {

    /**
     * 当Tab属性被更新的时候调用
     * @param tabTextView TextView
     */
    void onAttributeUpdate(@NonNull TextView tabTextView, @IntRange(from = 0) int currentIndex, @IntRange(from = 0) int tabIndex);

  }

  public interface Consumer2 {
    void accept(@IntRange(from = 0) int index, @NonNull View view);
  }

  private class DefaultPagerAdapter extends FragmentPagerAdapter {

    private @NonNull final List<Fragment> fragments;
    private @NonNull String[] titles;

    private DefaultPagerAdapter(@NonNull FragmentManager fm, @NonNull List<Fragment> fragments,
                                @NonNull String[] titles) {
      super(fm);
      this.fragments = Objects.requireNonNull(fragments);
      this.titles = Objects.requireNonNull(titles);
      if (fragments.size() != titles.length) {
        throw new IllegalStateException("fragments size not eq pageTitle len");
      }
    }

    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }

    @Override
    public long getItemId(int position) {
      return fragments.get(position).hashCode();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return titles[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
      return PagerAdapter.POSITION_NONE;
    }

    public void add(int position, @NonNull String title, @NonNull Fragment fragment) {
      Objects.requireNonNull(title);
      Objects.requireNonNull(fragment);
      ArrayList<String> newTitles = Lists.newArrayList(titles);
      newTitles.add(position, title);
      titles = newTitles.toArray(new String[0]);
      fragments.add(position, fragment);
      notifyDataSetChanged();
    }

    public void add(@NonNull String title, @NonNull Fragment fragment) {
      Objects.requireNonNull(title);
      Objects.requireNonNull(fragment);
      ArrayList<String> newTitles = Lists.newArrayList(titles);
      newTitles.add(title);
      titles = newTitles.toArray(new String[0]);
      fragments.add(fragment);
      notifyDataSetChanged();
    }

    public void remove(int position) {
      if (position >= 0 && position < getCount()) {
        ArrayList<String> newTitles = Lists.newArrayList(titles);
        newTitles.remove(position);
        titles = newTitles.toArray(new String[0]);
        fragments.remove(position);
        notifyDataSetChanged();
      }
    }
  }
}

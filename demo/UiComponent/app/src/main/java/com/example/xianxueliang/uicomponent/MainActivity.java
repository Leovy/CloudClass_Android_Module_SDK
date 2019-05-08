package com.example.xianxueliang.uicomponent;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SupportSlidingTabLayout tabLayout = findViewById(R.id.tl);
    ViewPager vpContent = findViewById(R.id.vp_content);

    ArrayList<Fragment> fragments = new ArrayList<>();
    Collections.addAll(fragments, BlankFragment.newInstance(Color.RED),
        BlankFragment.newInstance(Color.YELLOW));
    tabLayout.setViewPager(vpContent,
        new String[]{"手机验证登录", "登录"},
        getSupportFragmentManager(),
        fragments
    );
//    tabLayout.setCurrentTab(0);
  }
}

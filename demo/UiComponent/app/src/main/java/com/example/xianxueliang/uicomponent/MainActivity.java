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
        BlankFragment.newInstance(Color.YELLOW),
        BlankFragment.newInstance(Color.YELLOW),
        BlankFragment.newInstance(Color.YELLOW),
        BlankFragment.newInstance(Color.YELLOW)
    );
    tabLayout.setViewPager(vpContent,
        new String[]{"已下载", "下载中", "下载中", "下载中", "下载中"},
        getSupportFragmentManager(),
        fragments
    );

//    ArrayList<Fragment> fragments = new ArrayList<>();
//    Collections.addAll(fragments, BlankFragment.newInstance(Color.RED));
//    tabLayout.setViewPager(vpContent,
//            new String[]{"已下载"},
//            getSupportFragmentManager(),
//            fragments
//    );
//    tabLayout.setCurrentTab(0);

    AlignBottomPlugin.applyTo(tabLayout);
//    SmoothScaleTitleSizePlugin.applyTo(tabLayout);

    DownloadTabPluginApi plugin = DownloadTabPlugin.applyTo(tabLayout);
    plugin.showDotAt(0);
    plugin.showSubscriptNumberAt(0, "19");
    plugin.showDotAt(1);
    plugin.showSubscriptNumberAt(1, "19000000000000000000");

//    tabLayout.addTab("BLACK", BlankFragment.newInstance(Color.BLACK));
//    tabLayout.addTab("CYAN", BlankFragment.newInstance(Color.CYAN));
  }
}

package com.scwang.refreshlayout.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.fragment.RefreshPractiveFragment;
import com.scwang.refreshlayout.fragment.RefreshStylesFragment;
import com.scwang.refreshlayout.fragment.RefreshUsingFragment;

public class IndexMainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private enum TabFragment {
        practice(R.id.navigation_practice,RefreshPractiveFragment.class),
        styles(R.id.navigation_style,RefreshStylesFragment.class),
        using(R.id.navigation_using,RefreshUsingFragment.class)
        ;

        private final int menuId;
        private final Class<? extends Fragment> clazz;
        private Fragment fragment;

        TabFragment(@IdRes int menuId, Class<? extends Fragment> clazz) {
            this.menuId = menuId;
            this.clazz = clazz;
        }

        @NonNull
        public Fragment fragment() {
            if (fragment == null) {
                try {
                    fragment = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new Fragment();
                }
            }
            return fragment;
        }

        public static TabFragment from(int itemId) {
            for (TabFragment fragment : values()) {
                if (fragment.menuId == itemId) {
                    return fragment;
                }
            }
            return styles;
        }

        public static void onDestroy() {
            for (TabFragment fragment : values()) {
                fragment.fragment = null;
            }
        }
    }

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_main);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TabFragment.values()[position].fragment();
            }
            @Override
            public int getCount() {
                return TabFragment.values().length;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(TabFragment.values()[position].menuId);
            }
        });

        navigation.setSelectedItemId(R.id.navigation_style);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TabFragment.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content,TabFragment.from(item.getItemId()).fragment())
//                .commit();
        mViewPager.setCurrentItem(TabFragment.from(item.getItemId()).ordinal());
        return true;
    }
}

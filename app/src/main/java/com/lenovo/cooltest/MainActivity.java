package com.lenovo.cooltest;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by liufeng23 on 2017/7/26.
 */

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private SearchView mSearchView;
    private RadioGroup mButtonBar;
    private RadioButton mButtonHome;
    private RadioButton mButtonClassify;
    private RadioButton mButtonCart;
    private RadioButton mButtonMyProfile;
    private ViewPager mMainViewPager;

    private MyFragmentPagerAdapter mAdapter;

    // 4 pages
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        mButtonHome.setChecked(true);
    }

    private void bindViews() {
        mSearchView = (SearchView) findViewById(R.id.search_edit_text);
        mButtonBar = (RadioGroup) findViewById(R.id.button_bar_group);
        mButtonHome = (RadioButton) findViewById(R.id.button_home);
        mButtonClassify = (RadioButton) findViewById(R.id.button_classify);
        mButtonCart = (RadioButton) findViewById(R.id.button_cart);
        mButtonMyProfile = (RadioButton) findViewById(R.id.button_myprofile);
        mButtonBar.setOnCheckedChangeListener(this);
        mSearchView.setOnSearchClickListener(new SearchView.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                Toast.makeText(MainActivity.this, "i'm going to seach", Toast.LENGTH_SHORT).show();
            }
        });
        mMainViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mMainViewPager.setAdapter(mAdapter);
        mMainViewPager.setCurrentItem(0);
        mMainViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button_home:
                mMainViewPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.button_classify:
                mMainViewPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.button_cart:
                mMainViewPager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.button_myprofile:
                mMainViewPager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state 3 states，0 nothing，1 Scrolling，2 done
        if (state == 2) {
            switch (mMainViewPager.getCurrentItem()) {
                case PAGE_ONE:
                    mButtonHome.setChecked(true);
                    break;
                case PAGE_TWO:
                    mButtonClassify.setChecked(true);
                    break;
                case PAGE_THREE:
                    mButtonCart.setChecked(true);
                    break;
                case PAGE_FOUR:
                    mButtonMyProfile.setChecked(true);
                    break;
            }
        }
    }
}

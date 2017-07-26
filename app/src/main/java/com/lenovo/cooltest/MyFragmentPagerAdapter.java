package com.lenovo.cooltest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by liufeng23 on 2017/7/26.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private MyFragmentHome fragmentHome = null;
    private MyFragmentClassify fragmentCalssify = null;
    private MyFragmentShoppingCart fragmentCart = null;
    private MyFragmentMyProfile fragmentProfile = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentHome = new MyFragmentHome();
        fragmentCalssify = new MyFragmentClassify();
        fragmentCart = new MyFragmentShoppingCart();
        fragmentProfile = new MyFragmentMyProfile();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = fragmentHome;
                break;
            case MainActivity.PAGE_TWO:
                fragment = fragmentCalssify;
                break;
            case MainActivity.PAGE_THREE:
                fragment = fragmentCart;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = fragmentProfile;
                break;
        }
        return fragment;
    }


}


package org.gnuton.newshub.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.gnuton.newshub.ArticleFragment;
import org.gnuton.newshub.ArticleListFragment;
import org.gnuton.newshub.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gnuton on 6/21/13.
 */

public class MainPageFragmentAdapter  extends FragmentPagerAdapter {
    List<Fragment> mFragments = new ArrayList<Fragment>();

    public MainPageFragmentAdapter(FragmentManager fm) {
        super(fm);

        // Get or Create articles list fragment
        mFragments.add(FragmentUtils.getFragment(fm, ArticleListFragment.class.getName(), null));

        // Create article detail fragment
        mFragments.add(FragmentUtils.getFragment(fm, ArticleFragment.class.getName(), null));
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


}
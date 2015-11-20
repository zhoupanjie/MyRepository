
package com.hy.superemsg.viewpager;

import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    private List<AbsFragment> fragments;

    public void setFragments(List<AbsFragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public AbsFragment getItem(int position) {
        if (fragments != null && position < fragments.size()) {
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fragments != null) {
            return fragments.size();
        }
        return 0;
    }

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

}

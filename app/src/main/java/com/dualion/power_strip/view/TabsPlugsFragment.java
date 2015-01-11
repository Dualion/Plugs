package com.dualion.power_strip.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.ui.BaseFragment;

import java.util.ArrayList;

public class TabsPlugsFragment extends BaseFragment {

	TabHost tabHost;
	ViewPager viewPager;
	TabsAdapter tabsAdapter;

	public final static String ARG_TAB = "tab";

	public static TabsPlugsFragment newInstance(int index, String pid, String component ) {
		TabsPlugsFragment f = new TabsPlugsFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt(ARG_INDEX, index);
		args.putString(ARG_PID, pid);
		args.putString(ARG_COMP,component);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist.  The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed.  Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.plugs_detail_tabs, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		tabHost = (TabHost) getActivity().findViewById(android.R.id.tabhost);

		if (tabHost == null){
			return;
		}

		tabHost.setup();

		viewPager = (ViewPager)getActivity().findViewById(R.id.pager);

		tabsAdapter = new TabsAdapter(getActivity(), tabHost, viewPager);

		tabsAdapter.addTab(tabHost.newTabSpec("dates").setIndicator("Dates"),
				DatesFragment.class, getArguments());

		if (savedInstanceState != null) {
			tabHost.setCurrentTabByTag(savedInstanceState.getString(ARG_TAB));
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (tabHost != null)
			outState.putString(ARG_TAB, tabHost.getCurrentTabTag());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		tabHost = null;
		viewPager = null;
		tabsAdapter = null;

	}

	public int getShownIndex() {
		return getArguments().getInt(ARG_INDEX, 0);
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost.  It relies on a
	 * trick.  Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show.  This is not sufficient for switching
	 * between pages.  So instead we make the content part of the tab host
	 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
	 * view to show as the tab content.  It listens to changes in tabs, and takes
	 * care of switch to the correct paged in the ViewPager whenever the selected
	 * tab changes.
	 */
	public static class TabsAdapter extends FragmentStatePagerAdapter
			implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

		private final Context context;
		private final TabHost tabHost;
		private final ViewPager viewPager;
		private final ArrayList<TabInfo> tabs = new ArrayList<>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			context = activity;
			this.tabHost = tabHost;
			viewPager = pager;
			this.tabHost.setOnTabChangedListener(this);
			viewPager.setAdapter(this);
			viewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(context));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			tabs.add(info);
			tabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return tabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = tabs.get(position);
			return Fragment.instantiate(context, info.clss.getName(), info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = tabHost.getCurrentTab();
			viewPager.setCurrentItem(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = tabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			tabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

}

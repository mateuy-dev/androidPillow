//package com.mateuyabar.android.pillow.view.extras;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.mateuyabar.android.cleanbase.BaseComponent;
//import com.mateuyabar.android.cleanbase.Component;
//import com.mateuyabar.android.pillow.data.models.IdentificableModel;
//import com.mateuyabar.android.pillow.util.BundleUtils;
//import com.mateuyabar.android.pillow.view.fragments.PillowBaseFragment;
//import com.mateuyabar.android.pillow.views.R;
//
//
///**
// * Tabs with filtered lists
// */
//public class PillowListTabsFragment<T extends IdentificableModel> extends PillowBaseFragment {
//    TabLayout tabLayout;
//    ViewPager viewPager;
//    Class<T> modelClass;
//
//
//    protected void loadDataFromBundle(){
//        modelClass = BundleUtils.getModelClass(getArguments());
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.tabs_list_fragment, container, false);
//
//
//        // Get the ViewPager and set it's PagerAdapter so that it can display items
//        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        viewPager.setAdapter(new SampleFragmentPagerAdapter(getFragmentManager(), getContext()));
//
//        // Give the TabLayout the ViewPager
//        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);
//
//        return view;
//    }
//
//    @Override
//    public Component getPresenter() {
//        return new BaseComponent(getContext());
//    }
//
//
//    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
//        final int PAGE_COUNT = 5;
//        private String tabTitles[] = new String[] { "All", "14th", "15th", "16th", "17th"};
//        private Context context;
//
//        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
//            super(fm);
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            return PAGE_COUNT;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return getNavigation().getListFragment(modelClass);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            // Generate title based on item position
//            return tabTitles[position];
//        }
//    }
//
//}


package org.denny.myimageloader.fragments;

import java.util.ArrayList;
import java.util.List;

import org.denny.myimageloader.R;
import org.denny.myimageloader.manager.ImageEntry;
import org.denny.myimageloader.util.Constants;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DetailFragment extends Fragment {
    private Context mContext = null;
    private ViewPager mDetailViewPager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View detailView = inflater.inflate(R.layout.detail_image_fragment, null);
        mDetailViewPager = (ViewPager) detailView.findViewById(R.id.detail_view_pager);
        List<ImageEntry> imageEntryList = getArguments().getParcelableArrayList(
                Constants.Extras.IMAGE_ENTRY_LIST);
        int viewPagerPosition = getArguments().getInt(Constants.Extras.VIEW_PAGER_POSITION);
        mDetailViewPager.setAdapter(new DetailPagerFragmentStateAdapter(getFragmentManager(),
                imageEntryList, mContext));
        mDetailViewPager.setCurrentItem(viewPagerPosition);
        return detailView;
    }

    public static class DetailPagerFragmentStateAdapter extends FragmentStatePagerAdapter {
        private final FragmentManager mFm;
        private final List<ImageEntry> mImageEntryList;
        private final Context mContext;

        public DetailPagerFragmentStateAdapter(FragmentManager fm, List<ImageEntry> imageEntryList,
                Context context) {
            super(fm);
            this.mFm = fm;
            this.mImageEntryList = imageEntryList;
            this.mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ViewPagerItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.Extras.VIEW_PAGER_POSITION, position);
            bundle.putParcelableArrayList(Constants.Extras.IMAGE_ENTRY_LIST,
                    (ArrayList<ImageEntry>) mImageEntryList);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mImageEntryList.size();
        }

    }

    public static class ViewPagerItemFragment extends Fragment {
        private List<ImageEntry> mImageEntryList;
        private int mPos;
        private DisplayImageOptions mDisplayOptions = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View detailView = inflater.inflate(R.layout.detail_image_item, null);
            ImageView detailImageView = (ImageView) detailView.findViewById(R.id.detail_image);
            getArgs();
            ImageLoader.getInstance().displayImage("file://" + mImageEntryList.get(mPos).getPath(),
                    detailImageView, getDisplayOptions());
            return detailView;
        }

        private void getArgs() {
            mPos = getArguments().getInt(Constants.Extras.VIEW_PAGER_POSITION);
            mImageEntryList = getArguments().getParcelableArrayList(
                    Constants.Extras.IMAGE_ENTRY_LIST);
        }

        private DisplayImageOptions getDisplayOptions() {
            if (mDisplayOptions == null) {
                mDisplayOptions = new DisplayImageOptions.Builder().cacheOnDisc()
                        .showStubImage(R.drawable.empty_photo).imageScaleType(ImageScaleType.NONE)
                        .build();
            }
            return mDisplayOptions;
        }

    }

}


package org.denny.myimageloader.fragments;

import java.util.List;

import org.denny.myimageloader.R;
import org.denny.myimageloader.manager.ImageEntry;
import org.denny.myimageloader.util.Constants;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class DetailFragment extends Fragment {
    private Context mContext = null;
    private ViewPager mDetailViewPager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.detail_image_fragment, null);
        mDetailViewPager = (ViewPager) detailView.findViewById(R.id.detail_view_pager);
        List<ImageEntry> imageEntryList = getArguments().getParcelableArrayList(
                Constants.Extras.IMAGE_ENTRY_LIST);
        int viewPagerPosition = getArguments().getInt(Constants.Extras.VIEW_PAGER_POSITION);
        mDetailViewPager.setAdapter(new DetailViewPagerAdapter(imageEntryList, mContext));
        mDetailViewPager.setCurrentItem(viewPagerPosition);
        return detailView;
    }

    private static class DetailViewPagerAdapter extends PagerAdapter {
        private final List<ImageEntry> mImageEntryList;
        private final Context mContext;

        public DetailViewPagerAdapter(List<ImageEntry> imageEntryList, Context context) {
            this.mImageEntryList = imageEntryList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mImageEntryList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ImageLoader.getInstance().displayImage(
                    "file://" + mImageEntryList.get(position).getPath(), imageView);
            return imageView;
        }

    }

}

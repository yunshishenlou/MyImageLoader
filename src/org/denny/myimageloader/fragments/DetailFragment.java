
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
            return true;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View detailView = inflater.inflate(R.layout.detail_image_item, null);
            ImageView detailImageView = (ImageView) detailView.findViewById(R.id.detail_image);
            ImageLoader.getInstance().displayImage(
                    "file://" + mImageEntryList.get(position).getPath(), detailImageView);
            container.addView(detailView);
            return detailView;
        }

    }

}

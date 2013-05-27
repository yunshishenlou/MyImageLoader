
package org.denny.myimageloader.fragments;

import java.util.ArrayList;
import java.util.List;

import org.denny.myimageloader.R;
import org.denny.myimageloader.manager.ImageEntry;
import org.denny.myimageloader.manager.ImageLoaderFactory;
import org.denny.myimageloader.manager.ThumbnailLoader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LocalImageFragment extends Fragment implements LoaderCallbacks<List<ImageEntry>> {
    private ProgressBar mEmptyProgressBar = null;
    private GridView mGridView = null;
    private ImageView mBigImageView = null;
    GridViewAdapter mGridViewAdapter = null;
    LocalImageLoader mLoader = null;
    private List<ImageEntry> mImageEntryList = new ArrayList<ImageEntry>();

    private static int LOCAL_IMAGE_LOADER = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.local_image_fragment, null);
        mEmptyProgressBar = (ProgressBar) contentView.findViewById(R.id.empty_view_pogress_bar);
        mGridView = (GridView) contentView.findViewById(R.id.local_image_gird_view);
        mGridViewAdapter = new GridViewAdapter(getActivity(), mImageEntryList);
        mGridView.setAdapter(mGridViewAdapter);
        mBigImageView = (ImageView) contentView.findViewById(R.id.big_image_image_view);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOCAL_IMAGE_LOADER, null, this);
    }

    private static class GridViewAdapter extends BaseAdapter {
        private List<ImageEntry> mImageList = null;
        private Context mContext = null;

        public GridViewAdapter(Context context, List<ImageEntry> imageList) {
            this.mImageList = imageList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(mContext);
            }
            ImageEntry imageEntry = mImageList.get(position);
            ImageLoaderFactory.getImageLoader(ThumbnailLoader.class, mContext).loadLocalImage(
                    imageEntry, (ImageView) convertView);
            return convertView;
        }

    }

    @Override
    public Loader<List<ImageEntry>> onCreateLoader(int arg0, Bundle arg1) {
        mLoader = new LocalImageLoader(getActivity());
        mLoader.forceLoad();
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ImageEntry>> loader, List<ImageEntry> imageEntryList) {
        mEmptyProgressBar.setVisibility(View.INVISIBLE);
        mImageEntryList.addAll(imageEntryList);
        mGridViewAdapter.notifyDataSetChanged();
        mGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<ImageEntry>> loader) {
        // TODO Auto-generated method stub

    }

    private static class LocalImageLoader extends AsyncTaskLoader<List<ImageEntry>> {
        private final Context mContext;
        private static String[] PROJECTION = new String[] {
                MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media._ID, MediaStore.Images.Media.SIZE
        };

        public LocalImageLoader(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        public List<ImageEntry> loadInBackground() {
            List<ImageEntry> imageEntryList = null;
            ContentResolver contentResolver = mContext.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        PROJECTION, null, null, null);
                if (cursor != null) {
                    imageEntryList = new ArrayList<ImageEntry>();
                    while (cursor.moveToNext()) {
                        ImageEntry imageEntry = new ImageEntry();
                        imageEntry.setPath(cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA)));
                        imageEntry.setName(cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                        imageEntry.setId(cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media._ID)));
                        imageEntry.setSize(cursor.getLong(cursor
                                .getColumnIndex(MediaStore.Images.Media.SIZE)));

                        imageEntryList.add(imageEntry);
                    }
                }
                return imageEntryList;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

}

package org.denny.myimageloader.fragments;

import java.util.ArrayList;
import java.util.List;

import org.denny.myimageloader.DetailActivity;
import org.denny.myimageloader.R;
import org.denny.myimageloader.manager.ImageEntry;
import org.denny.myimageloader.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;

public class LocalImageFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private ProgressBar mEmptyProgressBar = null;

    private GridView mGridView = null;

    private ImageView mBigImageView = null;

    GridViewAdapter mGridViewAdapter = null;

    private Handler mUiHandler = new Handler();

    private int mGridThumbnailWidth;

    private int mGridThumbnailSpaceing;

    private static String[] PROJECTION = new String[] {
            MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media._ID, MediaStore.Images.Media.SIZE
    };

    private ArrayList<ImageEntry> mImageEntryList = new ArrayList<ImageEntry>();

    private static int LOCAL_IMAGE_LOADER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridThumbnailWidth = getResources().getDimensionPixelSize(R.dimen.grid_view_width);
        mGridThumbnailSpaceing = getResources().getDimensionPixelSize(R.dimen.grid_view_spaceing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.local_image_fragment, null);
        mEmptyProgressBar = (ProgressBar) contentView.findViewById(R.id.empty_view_pogress_bar);
        mGridView = (GridView) contentView.findViewById(R.id.local_image_gird_view);
        mGridViewAdapter = new GridViewAdapter(getActivity(), mImageEntryList);
        mGridView.setAdapter(mGridViewAdapter);
        mBigImageView = (ImageView) contentView.findViewById(R.id.big_image_image_view);
        setGridViewLayoutListener();
        setGridViewItemClickListener();
        return contentView;
    }

    private void setGridViewItemClickListener() {
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                startDetailActivity(position);
            }
        });
    }

    private void startDetailActivity(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.Extras.VIEW_PAGER_POSITION, position);
        bundle.putParcelableArrayList(Constants.Extras.IMAGE_ENTRY_LIST, mImageEntryList);
        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOCAL_IMAGE_LOADER, null, this);
    }

    private void setGridViewLayoutListener() {
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mGridViewAdapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(mGridView.getWidth()
                                    / (mGridThumbnailWidth + mGridThumbnailSpaceing));
                            if (numColumns > 0) {
                                final int columnWidth = (mGridView.getWidth() / numColumns)
                                        - mGridThumbnailSpaceing;
                                mGridViewAdapter.setNumColumns(numColumns);
                                mGridViewAdapter
                                        .setImageViewLayoutParas(new AbsListView.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT, columnWidth));
                            }
                        }
                    }
                });
    }

    private static class GridViewAdapter extends BaseAdapter {
        private List<ImageEntry> mImageList = null;

        private Context mContext = null;

        private int mNumColumns;

        private int mItemHeight;

        private AbsListView.LayoutParams mImageViewLayoutParas = null;

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

                ImageView imageView = new ImageView(mContext);
                setImageViewAttrs(imageView);
                convertView = imageView;
            }
            ImageEntry imageEntry = (ImageEntry) getItem(position);
            // ImageLoaderFactory.getImageLoader(ThumbnailLoader.class,
            // mContext, mUiHandler)
            // .loadLocalImage(imageEntry, (ImageView) convertView);
            ImageLoader.getInstance().displayImage("file://" + imageEntry.getPath(),
                    (ImageView) convertView);
            return convertView;
        }

        private void setImageViewAttrs(ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(mImageViewLayoutParas);
        }

        public void setImageViewLayoutParas(AbsListView.LayoutParams paras) {
            mImageViewLayoutParas = paras;
            notifyDataSetChanged();
        }

        public int getNumColumns() {
            return mNumColumns;
        }

        public void setNumColumns(int mNumColumns) {
            this.mNumColumns = mNumColumns;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mEmptyProgressBar.setVisibility(View.INVISIBLE);
        mImageEntryList.addAll(getImageEntryList(cursor));
        mGridViewAdapter.notifyDataSetChanged();
        mGridView.setVisibility(View.VISIBLE);
    }

    private List<ImageEntry> getImageEntryList(Cursor cursor) {
        List<ImageEntry> imageEntryList = null;
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // TODO Auto-generated method stub

    }

}


package org.denny.myimageloader.manager;

import java.lang.ref.WeakReference;

import org.denny.myimageloader.util.AsyncTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<ImageEntry, Void, Bitmap> {
    private final WeakReference<ImageView> mImageViewWeakReference;
    private final ImageEntry mImageEntry;

    public BitmapWorkerTask(ImageView ivm, ImageEntry imageEntry) {
        this.mImageEntry = imageEntry;
        this.mImageViewWeakReference = new WeakReference<ImageView>(ivm);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromFile(final String filePath, final int reqWidth,
            final int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (isCancelled()) {
            bitmap = null;
        }

        if (mImageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = mImageViewWeakReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

    }

    @Override
    protected Bitmap doInBackground(ImageEntry... params) {
        return decodeSampledBitmapFromFile(mImageEntry.getPath(), mImageViewWeakReference.get()
                .getWidth(), mImageViewWeakReference.get().getHeight());

    }

    public ImageEntry getImageEntry() {
        return mImageEntry;
    }

    public static boolean cancelPotentialWork(ImageEntry imageEntry, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final ImageEntry loadedImageEntry = bitmapWorkerTask.getImageEntry();
            if (loadedImageEntry != null && !loadedImageEntry.equals(imageEntry)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

}

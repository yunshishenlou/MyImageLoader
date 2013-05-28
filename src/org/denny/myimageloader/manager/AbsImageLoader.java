
package org.denny.myimageloader.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

public abstract class AbsImageLoader {
    protected final Context mContext;
    protected final Handler mUiHandler;
    private static ImageLoaderWorker IMAGE_LOADER_WORKER = null;

    protected AbsImageLoader(Context context, Handler uiHandler) {
        this.mContext = context;
        this.mUiHandler = uiHandler;
    }

    protected ImageLoaderWorker getWorker(){
        synchronized (AbsImageLoader.class) {
            if( IMAGE_LOADER_WORKER == null ){
                IMAGE_LOADER_WORKER = new ImageLoaderWorker();
            }
            return IMAGE_LOADER_WORKER;
        }
    }

    public abstract void loadLocalImage(ImageEntry imageEntry, ImageView imv);

    protected static class ImageLoaderWorker extends AsyncTask<ImageEntry, Void, Bitmap> {
        private ImageView mImageView;

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
        

        private Bitmap decodeSampledBitmapFromFile(final ImageEntry imageEntry, final int reqWidth,
                final int reqHeight) {

            return decodeSampledBitmapFromFile(imageEntry.getPath(), reqWidth, reqHeight);
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

        private void setTargetImageView(ImageView imageView) {
            this.mImageView = imageView;
        }

        public void excute(ImageView imv, ImageEntry imageEntry) {
            setTargetImageView(imv);
            this.execute(imageEntry);
        }

        @Override
        protected Bitmap doInBackground(ImageEntry... params) {
            ImageEntry imageEntry = params[0];
            return decodeSampledBitmapFromFile(imageEntry.getPath(), imageEntry.getReqWidth(),
                    imageEntry.getReqHeight());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (mImageView != null) {
                mImageView.setImageBitmap(result);
            }
        }

    }

}

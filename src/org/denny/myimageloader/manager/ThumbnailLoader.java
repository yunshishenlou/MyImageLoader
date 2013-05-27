
package org.denny.myimageloader.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;

public class ThumbnailLoader extends AbsImageLoader {

    public ThumbnailLoader(Context context) {
        super(context);
    }

    @Override
    public void loadLocalImage(ImageEntry imageEntry, ImageView imv) {
        Bitmap imageBitmap = MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(),
                Long.valueOf(imageEntry.getId()), MediaStore.Images.Thumbnails.MINI_KIND, null);
        if(imageBitmap != null){
            imv.setImageBitmap(imageBitmap);
        }
    }

}

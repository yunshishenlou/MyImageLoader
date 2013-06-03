
package org.denny.myimageloader.manager;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageEntry implements Parcelable{
    private String name;
    private String path;
    private String id;
    private int width;
    private int height;
    private long size;

   

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ImageEntry)) {
            return false;
        } else {
            ImageEntry imageEntry = (ImageEntry) o;
            // TODO this just a simple check
            if (this.path.equals(imageEntry.path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO this is a bad implemention
        return 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeLong(size);
    }

}

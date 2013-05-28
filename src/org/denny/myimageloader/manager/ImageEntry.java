package org.denny.myimageloader.manager;

public class ImageEntry {
    private String name;
    private String path;
    private String id;
    private int width;
    private int height;
    private long size;
    
    private int reqWidth;
    public int getReqWidth() {
        return reqWidth;
    }
    public void setReqWidth(int reqWidth) {
        this.reqWidth = reqWidth;
    }
    private int reqHeight;
    public int getReqHeight() {
        return reqHeight;
    }
    public void setReqHeight(int reqHeight) {
        this.reqHeight = reqHeight;
    }
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
    
}

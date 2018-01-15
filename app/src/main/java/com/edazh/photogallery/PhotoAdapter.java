package com.edazh.photogallery;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edazh on 2018/1/15 0015.
 * e-mail:edazh@qq.com
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoViewHolder> mThumbnailDownloader;

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_gallery, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        GalleryItem item = mItems.get(position);
        mThumbnailDownloader.queueThumbnail(holder, item.getUrl());
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public void setGalleryItemList(List<GalleryItem> galleryItems) {
        mItems = galleryItems;
    }

    public void setThumbnailDownloader(ThumbnailDownloader<PhotoViewHolder> thumbnailDownloader) {
        mThumbnailDownloader = thumbnailDownloader;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            itemView = itemView.findViewById(R.id.item_image_view);
        }

        public void bindDraweable(Drawable drawable) {
            itemImageView.setImageDrawable(drawable);
        }
    }
}

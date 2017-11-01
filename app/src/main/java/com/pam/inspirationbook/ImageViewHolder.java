package com.pam.inspirationbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Pam on 10/31/2017.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private ImageView mPreviewImage;

    public ImageViewHolder(View itemView) {
        super(itemView);
        mPreviewImage = (ImageView) itemView.findViewById(R.id.gallery_preview_image);
    }

    public void setPreviewImage(int resourceId) {
        mPreviewImage.setImageResource(resourceId);
        mPreviewImage.setScaleType(ImageView.ScaleType.FIT_START);
        mPreviewImage.setAdjustViewBounds(true);
    }
}

package com.pam.inspirationbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Pam on 10/31/2017.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private TextView mPreviewText;

    public NoteViewHolder(View itemView) {
        super(itemView);
        mPreviewText = (TextView) itemView.findViewById(R.id.gallery_preview_text);
    }

    public void setPreviewText(String previewText) {
        mPreviewText.setText(previewText);
    }
}

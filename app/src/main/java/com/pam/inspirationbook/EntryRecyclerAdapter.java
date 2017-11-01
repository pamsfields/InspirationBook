package com.pam.inspirationbook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Pam on 10/31/2017.
 */

public class EntryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private EntryLoader mEntryLoader;
    public static final int ENTRYTYPE_TEXT = 0;
    public static final int ENTRYTYPE_IMAGE = 1;

    public EntryRecyclerAdapter(EntryLoader eLoader) {
        mEntryLoader = eLoader;
    }
    public int getItemCount() {
        return this.mEntryLoader.mEntries.size();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType)
        {
            case ENTRYTYPE_TEXT:
                View textView = layoutInflater.inflate(R.layout.fragment_gallery_note, parent, false);
                return new NoteViewHolder(textView);

            case ENTRYTYPE_IMAGE:
                View imageView = layoutInflater.inflate(R.layout.fragment_gallery_image, parent, false);
                return new ImageViewHolder(imageView);

            default:
                // Should not get to here
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Entry gridItem = mEntryLoader.mEntries.get(position);

        if (gridItem.getClass() == NoteEntry.class) {
            ((NoteViewHolder) holder).setPreviewText(gridItem.mText);

        } else if (gridItem.getClass() == ImageEntry.class) {
            ((ImageViewHolder) holder).setPreviewImage(((ImageEntry)gridItem).mResourceId);
        }
    }

    public void setEntries(List<Entry> entries) {
        // There will be some stuff in here
    }

    public int getItemViewType(int position) {

        Entry entry = mEntryLoader.mEntries.get(position);

        if (entry.getClass().getName().equals(NoteEntry.class.getName())) {
            return ENTRYTYPE_TEXT;

        } else if (entry.getClass().getName().equals(ImageEntry.class.getName())) {
            return ENTRYTYPE_IMAGE;

        } else {
            // This should not happen
            return 0;
        }
    }
}

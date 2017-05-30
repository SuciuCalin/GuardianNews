package com.example.android.guardiannews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JukUm on 5/30/2017.
 */

public class EditorialAdapter extends RecyclerView.Adapter<EditorialAdapter.ViewHolder> {

    private static final String LOG_TAG = EditorialAdapter.class.getSimpleName();

    private List<Editorial> mEditorial;
    private Context mContext;

    /**
     * Constructs a new {@link EditorialAdapter}
     *
     * @param context      is the current context that the adapter is being created in
     * @param editorials   is the list of editorials, which is the data source of the adapter
     */
    public EditorialAdapter(Context context, List<Editorial> editorials) {
        this.mEditorial = editorials;
        this.mContext   = context;
    }

    @Override
    public EditorialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View editorialRecyclerItem = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(editorialRecyclerItem);
    }

    @Override
    public void onBindViewHolder(EditorialAdapter.ViewHolder holder, int position) {
        final Editorial editorialListItem = mEditorial.get(position);

        holder.editorialSection.setText(editorialListItem.getEditorialSection());
        holder.editorialTitle.setText(editorialListItem.getEditorialTitle());
        holder.setClickListener(new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                Uri editorialUri = Uri.parse(editorialListItem.getEditorialUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, editorialUri);
                mContext.startActivity(websiteIntent);

                Log.v(LOG_TAG,"List item: " + editorialListItem + " with " + editorialUri + " was clicked.");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEditorial.size();
    }

    // View lookup cache
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView editorialTitle;
        private TextView editorialSection;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            editorialTitle = (TextView) itemView.findViewById(R.id.editorial_title);
            editorialSection = (TextView) itemView.findViewById(R.id.editorial_section);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

}
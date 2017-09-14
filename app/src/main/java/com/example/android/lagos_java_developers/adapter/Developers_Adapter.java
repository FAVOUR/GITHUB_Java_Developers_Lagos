package com.example.android.lagos_java_developers.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.lagos_java_developers.R;
import com.example.android.lagos_java_developers.model.Developer;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class Developers_Adapter extends RecyclerView.Adapter<Developers_Adapter.Developer_ViewHoler> {

    final private ListItemClickListiner mOnClickedListiner;
    private List<Developer> mDevelopers;
    private Context context;
    private int clickedPosition;


    public Developers_Adapter(Context context, List<Developer> developers, ListItemClickListiner listiner) {

        mDevelopers = developers;
        this.context = context;
        mOnClickedListiner = listiner;


    }

    @Override
    public Developers_Adapter.Developer_ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResourceId = R.layout.dev_row_item;
        Context getCurrentContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(getCurrentContext);


        View newViewHolder = layoutInflater.inflate(layoutResourceId, parent, false);

        return new Developer_ViewHoler(newViewHolder);
    }

    @Override
    public void onBindViewHolder(Developer_ViewHoler holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mDevelopers.size();
    }

    public void clear() {
        mDevelopers.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Developer> Developers) {
        mDevelopers.addAll(Developers);
        notifyDataSetChanged();
    }

    public Developer getItemPosition() {
        return mDevelopers.get(clickedPosition);
    }

    public interface ListItemClickListiner {
        void onListItemClicked(int clickditemindex);
    }

    public class Developer_ViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView devName;
        ImageView devImage;

        private Developer_ViewHoler(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

//           Calls the setOnClicklistiner on the itemView in the constructor to these

            devImage = (ImageView) itemView.findViewById(R.id.dev_image);
            devName = (TextView) itemView.findViewById(R.id.dev_name);


        }


        void bind(int listIndex) {
            Developer developers = mDevelopers.get(listIndex);
            Picasso.with(context).load(developers.getDevImg())
                    .transform(new CropCircleTransformation())
                    .error(R.drawable.image_download_error)
                    .placeholder(R.drawable.avata)
                    .into(devImage);
            devName.setText(developers.getDevName());

        }


        @Override
        public void onClick(View view) {
//             Set the body of the function to get the position which is the item that was clicked

            clickedPosition = getAdapterPosition();


//             This invokes the onclick listener of the other class by passing clickedPosition value


            mOnClickedListiner.onListItemClicked(clickedPosition);
        }
    }
}
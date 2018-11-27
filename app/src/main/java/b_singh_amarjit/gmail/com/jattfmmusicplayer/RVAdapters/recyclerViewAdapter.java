package b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.albumArt;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.pop_ups.popup_albums;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.pop_ups.popup_singles;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<albumArt> mData;

    public recyclerViewAdapter(Context context, List<albumArt> data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.artistSong.setText(mData.get(position).getArtistSong());
        Picasso.get().load(mData.get(position).getThumbnail()).into(holder.albumArt);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mData.get(position).getArtistSong().toLowerCase().contains("upcoming")){
                    Toast.makeText(mContext, "Upcoming...", Toast.LENGTH_LONG).show();
                }
                else if(mData.get(position).getType().toLowerCase().matches("single")){
                    Intent i = new Intent(mContext, popup_singles.class);
                    i.putExtra("WEB_PAGE_LINK", mData.get(position).getLink());
                    mContext.startActivity(i);
                }
                else if(mData.get(position).getType().toLowerCase().matches("album")){
                    Intent i = new Intent(mContext, popup_albums.class);
                    i.putExtra("WEB_PAGE_LINK", mData.get(position).getLink());
                    mContext.startActivity(i);
                }
                else if(mData.get(position).getType().toLowerCase().matches("loading")){
                    Toast.makeText(mContext, "Still Loading...", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mContext, "Something went wrong!!!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView artistSong;
        ImageView albumArt;
        CardView mCardView;

        public MyViewHolder(View itemView){
            super(itemView);
            artistSong = (TextView) itemView.findViewById(R.id.songArtist_textView);
            albumArt = (ImageView) itemView.findViewById(R.id.albumArt_imageView);
            mCardView = (CardView) itemView.findViewById(R.id.cardViewID);

        }
    }
}

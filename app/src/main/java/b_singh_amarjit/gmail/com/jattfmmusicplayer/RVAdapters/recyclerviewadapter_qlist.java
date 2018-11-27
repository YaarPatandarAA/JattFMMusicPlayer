package b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.Audio;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.pop_ups.popup_singles;


public class recyclerviewadapter_qlist extends RecyclerView.Adapter<recyclerviewadapter_qlist.myViewHolder> {

    private Context mContext;
    private List<Audio> mData;

    public static final String Broadcast_PlayQlist = "b_singh_amarjit.gmail.com.jattfmmusicplayer.PlayQList";

    public recyclerviewadapter_qlist(Context context, List<Audio> data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.qlist_item, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        holder.songName.setText(mData.get(position).getSongName());
        holder.artistName.setText(mData.get(position).getArtistName());

        Picasso.get().load(mData.get(position).getArtLink()).into(holder.albumImg);

        holder.infoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, popup_singles.class);
                i.putExtra("WEB_PAGE_LINK", mData.get(position).getWebPageLink());
                mContext.startActivity(i);
            }
        });

        holder.qListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent broadcastIntent = new Intent(Broadcast_PlayQlist);
                broadcastIntent.putExtra("playPosition", position);
                mContext.sendBroadcast(broadcastIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        CardView qListItem;

        TextView songName, artistName;
        ImageView albumImg;
        ImageButton infoBTN;


        public myViewHolder(View itemView) {
            super(itemView);
            qListItem = (CardView) itemView.findViewById(R.id.qlistItem);

            songName = (TextView) itemView.findViewById(R.id.sngName);
            artistName = (TextView) itemView.findViewById(R.id.artistName);
            albumImg = (ImageView) itemView.findViewById(R.id.albumARTIMG);
            infoBTN = (ImageButton) itemView.findViewById(R.id.infoButton);
        }
    }


}

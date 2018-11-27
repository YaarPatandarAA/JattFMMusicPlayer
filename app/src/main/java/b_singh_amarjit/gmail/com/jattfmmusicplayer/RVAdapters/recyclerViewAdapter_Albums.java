package b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.albumSongs;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;

public class recyclerViewAdapter_Albums extends RecyclerView.Adapter<recyclerViewAdapter_Albums.MyViewHolder> {

    public static final String Broadcast_addtoList = "b_singh_amarjit.gmail.com.jattfmmusicplayer.addtoList";
    public static final String Broadcast_instPlay = "b_singh_amarjit.gmail.com.jattfmmusicplayer.instPlay";
    private Uri webAdr;

    private Context mContext;
    private List<albumSongs> mData;

    public recyclerViewAdapter_Albums(Context context, List<albumSongs> data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public recyclerViewAdapter_Albums.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.albumsongs_item, parent, false);

        return new recyclerViewAdapter_Albums.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapter_Albums.MyViewHolder holder, final int position) {
        holder.sngName.setText(mData.get(position).getSongName() + " (" + mData.get(position).getArtistName() + ")");

        if(mData.get(position).getMp3Link().matches("")){
            holder.instPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "ERROR 404", Toast.LENGTH_SHORT).show();
                }
            });
            holder.add2Q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "ERROR 404", Toast.LENGTH_SHORT).show();
                }
            });
            holder.mp3Down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "ERROR 404", Toast.LENGTH_SHORT).show();
                }
            });
            holder.webpageShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "ERROR 404", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            holder.instPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent instPlay = new Intent(Broadcast_instPlay);

                    instPlay.putExtra("webLink", mData.get(position).getWebPageLink());
                    instPlay.putExtra("mp3link", mData.get(position).getMp3Link());
                    instPlay.putExtra("albumartlink", mData.get(position).getArtLink());
                    instPlay.putExtra("singerName", mData.get(position).getArtistName());
                    instPlay.putExtra("songName", mData.get(position).getSongName());

                    mContext.sendBroadcast(instPlay);
                }
            });
            holder.add2Q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addtoQBR = new Intent(Broadcast_addtoList);

                    addtoQBR.putExtra("webLink", mData.get(position).getWebPageLink());
                    addtoQBR.putExtra("mp3link", mData.get(position).getMp3Link());
                    addtoQBR.putExtra("albumartlink", mData.get(position).getArtLink());
                    addtoQBR.putExtra("singerName", mData.get(position).getArtistName());
                    addtoQBR.putExtra("songName", mData.get(position).getSongName());

                    mContext.sendBroadcast(addtoQBR);
                }
            });
            holder.mp3Down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mData.get(position).getHqMp3Link().contains("hd")) {
                        webAdr = Uri.parse(mData.get(position).getHqMp3Link());
                    }
                    else{
                        webAdr = Uri.parse(mData.get(position).getHqMp3Link());
                    }
                    Intent goToDownload = new Intent(Intent.ACTION_VIEW, webAdr);
                    if(goToDownload.resolveActivity(mContext.getPackageManager()) != null){
                        mContext.startActivity(goToDownload);
                    }
                    else{
                        Toast toast = Toast.makeText(mContext, "No Download For Some Reason!!!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
            holder.webpageShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String shareBodyText = mData.get(position).getWebPageLink();
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                    mContext.startActivity(Intent.createChooser(intent, "Choose sharing method"));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView sngName;
        ImageButton instPlay, add2Q, mp3Down, webpageShare;

        public MyViewHolder(View itemView){
            super(itemView);
            sngName = (TextView) itemView.findViewById(R.id.songName);

            instPlay     = (ImageButton) itemView.findViewById(R.id.instPlay);
            add2Q        = (ImageButton) itemView.findViewById(R.id.add2Q);
            mp3Down      = (ImageButton) itemView.findViewById(R.id.downloadNow);
            webpageShare = (ImageButton) itemView.findViewById(R.id.webpageShare);
        }
    }
}

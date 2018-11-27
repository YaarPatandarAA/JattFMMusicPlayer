package b_singh_amarjit.gmail.com.jattfmmusicplayer.musicPlayers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.MainActivity;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.Audio;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.BlurBitmap;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Services.MediaPlayerService;


/**
 * A simple {@link Fragment} subclass.
 */
public class music_player_mini extends Fragment {
    private Context mContext;

    private int currentPosition;
    private ImageView albumArt;
    private TextView singerName, songName;
    private ImageButton previous, next;
    private ToggleButton playPause;
    private ConstraintLayout miniPlayer;

    //List of Audio Links form user.
    ArrayList<Audio> audioListMP = MainActivity.audioList;

    public music_player_mini() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_music_player_mini, container, false);
        mContext = v.getContext();

        miniPlayer = (ConstraintLayout) v.findViewById(R.id.miniMusicPlayer);
        albumArt   = (ImageView) v.findViewById(R.id.albumArt);
        singerName = (TextView) v.findViewById(R.id.artist_tv);
        songName   = (TextView) v.findViewById(R.id.song_tv);
        previous   = (ImageButton) v.findViewById(R.id.prevTrackmini);
        playPause  = (ToggleButton) v.findViewById(R.id.playPause_Togglemini);
        next       = (ImageButton) v.findViewById(R.id.nextTrackmini);

        register_currentPos();
        register_updatePP();

        previous.setEnabled(false);
        next.setEnabled(false);

        playPause.setChecked(true);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music_player_main.prevTrack.callOnClick();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music_player_main.nextTrack.callOnClick();
            }
        });


        playPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    music_player_main.playPause.setChecked(true);
                } else {
                    music_player_main.playPause.setChecked(false);
                }
            }
        });

        return v;
    }

//=============This is used to get metadata that isn't in list======================================
    private BroadcastReceiver currentPos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the new media index form SharedPreferences
            try {
                //An audio file is passed to the service through putExtra();
                currentPosition = intent.getExtras().getInt("currentPositionMP");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            setMetaDataMP(currentPosition);
        }
    };

    private void register_currentPos() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(music_player_main.Broadcast_newMDmini);
        getActivity().registerReceiver(currentPos, filter);
    }

//================Use this to set MetaData on Music PLayer==========================================
    private void setMetaDataMP(int index){
        Picasso.get().load(audioListMP.get(index).getArtLink()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap blurredBitmap = BlurBitmap.blur( getActivity(), bitmap );

                miniPlayer.setBackground(new BitmapDrawable(mContext.getResources(), blurredBitmap));
                albumArt.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        songName.setText(audioListMP.get(index).getSongName());
        singerName.setText(audioListMP.get(index).getArtistName());

        previous.setEnabled(true);
        next.setEnabled(true);
        playPause.setEnabled(true);
    }

//=========================BR to update play or pause===============================================
    private BroadcastReceiver updatePP = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int i = 9;
            //Get the new media index form SharedPreferences
            try {
                //An audio file is passed to the service through putExtra();
                i = intent.getExtras().getInt("ppValue");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if(i == 1){
                playPause.setChecked(true);
            }
            else if(i == 0){
                playPause.setChecked(false);
            }
            else{
                Log.d("shPref","IDK WTF HAPPENED");
            }
        }
    };

    private void register_updatePP() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(MediaPlayerService.Broadcast_UPDATE_PP);
        getActivity().registerReceiver(updatePP, filter);
    }
}

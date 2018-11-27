package b_singh_amarjit.gmail.com.jattfmmusicplayer.musicPlayers;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.MainActivity;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.Audio;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.BlurBitmap;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters.recyclerviewadapter_qlist;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Services.MediaPlayerService;


/**
 * A simple {@link Fragment} subclass.
 */
public class music_player_main extends Fragment {
    //Instance of the Service
    private MediaPlayerService player;
    //Status of the Service, bound or not to the activity
    boolean serviceBound = false;
    //Context to be able to be used universal
    private Context mContext;

    //Buttons in the Layout
    public static ToggleButton playPause;
    public static ImageButton prevTrack, nextTrack;
    private ImageButton share;
    private ImageButton qListPane;

    //Fragments to be accessed
    private mp_qList mpQLIST;

    //To manipulate Layout
    private TextView songNameTV, artistNameTV, curDur, totDur;
    private SeekBar sngSeekBar;
    private ImageView albumArtIV;
    private ConstraintLayout mCont;

    String duration;
    String maxDuration;
    Handler handlerSong;
    Runnable runableSong;

    //List of Audio Links form user.
    ArrayList<Audio> audioListMP = MainActivity.audioList;

    //BR to let the MediaPlayerService know user wants to play new track
    public static final String Broadcast_PLAY_NEW_AUDIO = "b_singh_amarjit.gmail.com.jattfmmusicplayer.PlayNewAudio";
    public static final String Broadcast_newMDmini      = "b_singh_amarjit.gmail.com.jattfmmusicplayer.newMDmini";
    public static final String Broadcast_blurBitmap     = "b_singh_amarjit.gmail.com.jattfmmusicplayer.blurBitmap";
    //Current Position based on MPS
    public static int curPos;
    private Uri uri;

    View v;

    public music_player_main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_music_player_main, container, false);
        mContext = v.getContext();

        playPause = (ToggleButton) v.findViewById(R.id.playPause_Toggle);
        prevTrack = (ImageButton) v.findViewById(R.id.prevTrack);
        nextTrack = (ImageButton) v.findViewById(R.id.nextTrack);
        share     = (ImageButton) v.findViewById(R.id.shareButton);

        songNameTV = (TextView) v.findViewById(R.id.song_tv);
        artistNameTV = (TextView) v.findViewById(R.id.artist_tv);
        curDur = (TextView) v.findViewById(R.id.curentDuration);
        totDur = (TextView) v.findViewById(R.id.totalDuration);

        sngSeekBar = (SeekBar) v.findViewById(R.id.curentSong_seekBar);

        albumArtIV = (ImageView) v.findViewById(R.id.albumArt);
        qListPane = (ImageButton) v.findViewById(R.id.songList);

        mpQLIST = new mp_qList();

        mCont = (ConstraintLayout) v.findViewById(R.id.fullMusicPlayer);

        handlerSong = new Handler();

        register_playQlist();
        register_playMain();
        register_updatePP();
        register_metaData();

        qListPane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(mpQLIST, "qList", v);
            }
        });

        sngSeekBar.setEnabled(false);
        prevTrack.setEnabled(false);
        nextTrack.setEnabled(false);
        share.setEnabled(false);

        playPause.setChecked(true);

        prevTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serviceBound) {
                    Toast.makeText(mContext,"PREV",Toast.LENGTH_SHORT).show();
                    player.transportControls.skipToPrevious();
                    playPause.setEnabled(false);
                    sngSeekBar.setEnabled(false);
                    prevTrack.setEnabled(false);
                    nextTrack.setEnabled(false);
                    share.setEnabled(false);
                }
            }
        });

        nextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serviceBound) {
                    Toast.makeText(mContext,"NEXT",Toast.LENGTH_SHORT).show();
                    player.transportControls.skipToNext();
                    playPause.setEnabled(false);
                    sngSeekBar.setEnabled(false);
                    prevTrack.setEnabled(false);
                    nextTrack.setEnabled(false);
                    share.setEnabled(false);
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Share",Toast.LENGTH_SHORT).show();
                Intent shareURL = new Intent(android.content.Intent.ACTION_SEND);
                shareURL.setType("text/plain");
                String shareBodyText = audioListMP.get(curPos).getWebPageLink();
                shareURL.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                mContext.startActivity(Intent.createChooser(shareURL, "Choose sharing method"));
            }
        });

        playPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    if(serviceBound) {
                        player.transportControls.pause();
                    }
                } else {
                    // The toggle is disabled
                    if(serviceBound) {
                        player.transportControls.play();
                    }
                }
            }
        });

        sngSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input){
                    player.transportControls.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }

//==============Function will bind this activity to the service=====================================
//==============Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

//========The following function bounds the service, and sends a link to a media file===============
    private void playAudio(int curPos) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(mContext, MediaPlayerService.class);
            playerIntent.putExtra("sngPos", curPos);
            //playerIntent.putExtra("fullArray", audioListMP);

            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("sngPos", curPos);
            getActivity().sendBroadcast(broadcastIntent);
        }
    }

//===========save and restore the state of the serviceBound variable================================
//===========and unbind the Service when a user closes the app======================================
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            serviceBound = savedInstanceState.getBoolean("ServiceState");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            getActivity().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
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

//=============This is used to get metadata that isn't in list======================================
    private BroadcastReceiver metaData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalDur = 0;
            //Get the new media index form SharedPreferences
            try {
                //An audio file is passed to the service through putExtra();
                totalDur = intent.getExtras().getInt("totalDur");
                curPos = intent.getExtras().getInt("curSong");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            setMetaDataMP(curPos);
            maxDuration = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(totalDur), TimeUnit.MILLISECONDS.toSeconds(totalDur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDur)));
            sngSeekBar.setMax(totalDur);
            playCycle();
            totDur.setText(maxDuration);
        }
    };

    private void register_metaData() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(MediaPlayerService.Broadcast_MetaData);
        getActivity().registerReceiver(metaData, filter);
    }

//=============This is used to play from outside======================================
    private BroadcastReceiver playfrmOutside = new BroadcastReceiver() {
        int playPos;
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the new media index form SharedPreferences
            try {
                //An audio file is passed to the service through putExtra();
                playPos = intent.getExtras().getInt("playPosition");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            playAudio(playPos);
        }
    };

    private void register_playQlist() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(recyclerviewadapter_qlist.Broadcast_PlayQlist);
        getActivity().registerReceiver(playfrmOutside, filter);
    }

    private void register_playMain() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(MainActivity.Broadcast_newPlay);
        getActivity().registerReceiver(playfrmOutside, filter);
    }

//================Use this to set MetaData on Music PLayer==========================================
    private void setMetaDataMP(int index){
        Picasso.get().load(audioListMP.get(index).getArtLink()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap blurredBitmap = BlurBitmap.blur(getActivity(), bitmap);

                mCont.setBackground(new BitmapDrawable(mContext.getResources(), blurredBitmap));

                Intent sendBitmap = new Intent(Broadcast_blurBitmap);
                sendBitmap.putExtra("blurry", blurredBitmap);
                getActivity().sendBroadcast(sendBitmap);

                albumArtIV.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        songNameTV.setText(audioListMP.get(index).getSongName());
        artistNameTV.setText(audioListMP.get(index).getArtistName());

        Intent broadcastIntent = new Intent(Broadcast_newMDmini);
        broadcastIntent.putExtra("currentPositionMP", index);
        getActivity().sendBroadcast(broadcastIntent);
    }

//=================Used to generate the progress bar/seek bar=======================================
    public void playCycle(){
        try {
            sngSeekBar.setProgress(player.mediaCurPos());
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        duration = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(sngSeekBar.getProgress()), TimeUnit.MILLISECONDS.toSeconds(sngSeekBar.getProgress()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sngSeekBar.getProgress())));
        curDur.setText(duration);

        try {
            if (player.getMediaPlayer().isPlaying()) {
                playPause.setEnabled(true);
                sngSeekBar.setEnabled(true);
                prevTrack.setEnabled(true);
                nextTrack.setEnabled(true);
                share.setEnabled(true);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        if (player.getMediaPlayer()!=null){
            runableSong = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handlerSong.postDelayed(runableSong, 1000);
        }

    }

//===================Used for fragment transaction==================================================
    private void setFragment(Fragment myFragment, String tag, View view){
        Bundle bundle = new Bundle();

        myFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.musicPlayerFrame, myFragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}

package b_singh_amarjit.gmail.com.jattfmmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.Audio;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.BlurBitmap;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_about;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_albums;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_albumsHindi;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_home;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_singles;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_singlesHindi;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_top20Overall;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.fragment_topLSingles;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.settings;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.musicPlayers.music_player_main;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.musicPlayers.music_player_mini;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    public static NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    //sliding layout
    public static SlidingUpPanelLayout musicPlayer;
    //music player fragment
    private music_player_main mMusic_player_main;
    private music_player_mini mMusic_player_mini;
    //Main Screen fragments
    private fragment_home mHome;
    private fragment_singles mSingles;
    private fragment_singlesHindi mSinglesHindi;
    private fragment_albums mAlbums;
    private fragment_albumsHindi mAlbumsHindi;
    private fragment_about mAbout;
    private fragment_topLSingles mTopLSingles;
    private fragment_top20Overall mTop20Overall;
    private settings mSettings;
    //List of Audio Links form user.
    public static ArrayList<Audio> audioList = new ArrayList<Audio>();
    private boolean contains = false;
    private int location;

    //Variables needed;
    private String albumArtLink, singerName, songName, mp3link, weblink;
    private String whichFragfrmHome;
    private Context mContext;
    private CharSequence previousTitle;

    public static final String Broadcast_newPlay = "b_singh_amarjit.gmail.com.jattfmmusicplayer.newPlaySng";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioList.clear();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (musicPlayer.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            if(count==0){
                musicPlayer.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            else {
                getSupportFragmentManager().popBackStack();
            }
        } else {

            if(getSupportFragmentManager().getPrimaryNavigationFragment().equals(getSupportFragmentManager().findFragmentByTag("homePANE"))){
                //additional code
                this.moveTaskToBack(true);
            }
            else{
                setFragment(mHome, "homePANE");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DarkTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mHome = new fragment_home();
        mSingles = new fragment_singles();
        mSinglesHindi = new fragment_singlesHindi();
        mAlbums = new fragment_albums();
        mAlbumsHindi = new fragment_albumsHindi();
        mSettings = new settings();
        mAbout = new fragment_about();
        mTopLSingles = new fragment_topLSingles();
        mTop20Overall = new fragment_top20Overall();

        mNavigationView = (NavigationView) findViewById(R.id.drawerNav);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainPane);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        mMusic_player_main = new music_player_main();
        mMusic_player_mini = new music_player_mini();
        musicPlayer = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        if(audioList.size()>0){
            musicPlayer.setTouchEnabled(true);
        }
        else {
            musicPlayer.setTouchEnabled(false);
        }

        register_addtoList();
        register_instPlay();
        register_newFragfrmHome();
        register_blurry();
        setFragment(mHome, "homePANE");

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homePage:
                        setFragment(mHome, "homePANE");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.singleTracks:
                        setFragment(mSingles, "singleTacksPANE");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.singleHindiTracks:
                        setFragment(mSinglesHindi, "singleTacksHindiPANE");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.albums:
                        setFragment(mAlbums, "albumsPANE");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.albumsHindi:
                        setFragment(mAlbumsHindi, "albumsHindiPANE");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.top20:
                        setFragment(mTop20Overall, "top20Overall");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.top20S:
                        setFragment(mTopLSingles, "top20LSingles");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.settingsPage:
                        setFragment(mSettings, "settingsPANE");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.aboutPage:
                        setFragment(mAbout, "aboutPANE");
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Not yet Implemented...",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });

        setMusicPlayer("init");
        musicPlayer.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d("slidePanelCode","slideOffset: " + slideOffset);
                if(slideOffset >=0.5){
                    setMusicPlayer("main");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setTitle("Now Playing");
                }
                else if(slideOffset < 0.5){
                    setMusicPlayer("mini");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(previousTitle);
                    if(getSupportFragmentManager().getBackStackEntryCount() != 0){
                        getSupportFragmentManager().popBackStack();
                    }
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    setMusicPlayer("main");
                }
                else if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    setMusicPlayer("mini");
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//Setting the fragment of the music player
    private void setMusicPlayer(String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(tag == "init"){
            fragmentTransaction.add(R.id.musicPlayerFrame, mMusic_player_main, mMusic_player_main.getTag());
            fragmentTransaction.hide(mMusic_player_main);
            fragmentTransaction.add(R.id.musicPlayerFrame, mMusic_player_mini, mMusic_player_mini.getTag());
        }
        else if(tag == "main"){
            fragmentTransaction.hide(mMusic_player_mini);
            fragmentTransaction.show(mMusic_player_main);
        }
        else if(tag == "mini"){
            fragmentTransaction.hide(mMusic_player_main);
            fragmentTransaction.show(mMusic_player_mini);
        }
        fragmentTransaction.commit();
    }
//Setting the Main Showing Fragment
    private void setFragment(Fragment myFragment1, String tag1){
        switch (tag1){
            case "homePANE":
                getSupportActionBar().setTitle("Home");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "singleTacksPANE":
                getSupportActionBar().setTitle("Punjabi Latest Single Tracks");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "singleTacksHindiPANE":
                getSupportActionBar().setTitle("Hindi Latest Single Tracks");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "albumsPANE":
                getSupportActionBar().setTitle("Punjabi Latest Albums");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "albumsHindiPANE":
                getSupportActionBar().setTitle("Hindi Latest Albums");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "top20Overall":
                getSupportActionBar().setTitle("Top 20 Overall");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "top20LSingles":
                getSupportActionBar().setTitle("Top 20 Latest Punjabi Singles");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "settingsPANE":
                getSupportActionBar().setTitle("Settings");
                previousTitle = getSupportActionBar().getTitle();
                break;
            case "aboutPANE":
                getSupportActionBar().setTitle("About");
                previousTitle = getSupportActionBar().getTitle();
                break;
            default:
                getSupportActionBar().setTitle("JattFM Music Player");
                previousTitle = getSupportActionBar().getTitle();
                break;
        }


        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(tag1);
        if (fragment1 == null) {
            fragment1 = myFragment1;
            fragmentTransaction1.add(R.id.mainActFrame, fragment1, tag1);
        }
        else {
            fragmentTransaction1.show(fragment1);
        }

        Fragment curFrag1 = getSupportFragmentManager().getPrimaryNavigationFragment();
        if(curFrag1 == fragment1){
            fragmentTransaction1.hide(fragment1);
            fragmentTransaction1.show(curFrag1);
        }
        else if (curFrag1 != null) {
            fragmentTransaction1.hide(curFrag1);
        }

        fragmentTransaction1.setPrimaryNavigationFragment(fragment1);
        fragmentTransaction1.commit();
    }

//===========================Load Audio into the List===============================================
    private void loadAudio(String webPageLink, String mp3Link, String artLink, String artistName, String songName) {

        if(audioList.size() != 0) {
            for (int index = 0; index < audioList.size(); index++) {
                if (audioList.get(index).getSongName().matches(songName)) {
                    Toast.makeText(this, "This song is already in the Queue.", Toast.LENGTH_LONG).show();
                    location = index;
                    contains = true;
                    break;
                } else {
                    contains = false;
                }
            }
        }

        if(contains == false){
            // Save to audioList
            audioList.add(new Audio(webPageLink, mp3Link, artLink, artistName.replace(" , ", ", ").trim(), songName));

            location = (audioList.size()-1);
        }
    }

//=========================To Play new song fresh, regardless what is playing=======================
    private void playnewSong(){
        musicPlayer.setTouchEnabled(true);
        Intent broadcastIntent = new Intent(Broadcast_newPlay);
        broadcastIntent.putExtra("playPosition", location);
        sendBroadcast(broadcastIntent);

    }

//==============Set the background to blurry====================================
    private BroadcastReceiver newBack = new BroadcastReceiver() {
        Bitmap blurry;
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                blurry = intent.getExtras().getParcelable("blurry");
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
            mDrawerLayout.setBackground(new BitmapDrawable(mContext.getResources(), blurry));
        }
    };
    private void register_blurry(){
        IntentFilter filter = new IntentFilter(music_player_main.Broadcast_blurBitmap);
        registerReceiver(newBack, filter);
    }

//=================Function to access new audio link=============================
    private BroadcastReceiver addtoList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the new media index form SharedPreferences
            try {
                weblink = intent.getExtras().getString("webLink");
            } catch (NullPointerException e) {
                weblink = "";
            }
            try {
                mp3link = intent.getExtras().getString("mp3link");
            } catch (NullPointerException e){
                mp3link = "";
            }
            try {
                albumArtLink = intent.getExtras().getString("albumartlink");
            } catch (NullPointerException e) {
                albumArtLink = "";
            }
            try {
                singerName = intent.getExtras().getString("singerName");
            } catch (NullPointerException e){
                singerName = "";
            }
            try {
                songName = intent.getExtras().getString("songName");
            } catch (NullPointerException e) {
                songName = "";
            }

            loadAudio(weblink,mp3link,albumArtLink,singerName,songName);
        }
    };

    private void register_addtoList() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter("b_singh_amarjit.gmail.com.jattfmmusicplayer.addtoList");
        registerReceiver(addtoList, filter);
    }


//=================Function to access new audio link and play=============================
    private BroadcastReceiver instPlay = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the new media index form SharedPreferences
            try {
                weblink = intent.getExtras().getString("webLink");
            } catch (NullPointerException e) {
                weblink = "";
            }
            try {
                mp3link = intent.getExtras().getString("mp3link");
            } catch (NullPointerException e){
                mp3link = "";
            }
            try {
                albumArtLink = intent.getExtras().getString("albumartlink");
            } catch (NullPointerException e) {
                albumArtLink = "";
            }
            try {
                singerName = intent.getExtras().getString("singerName");
            } catch (NullPointerException e){
                singerName = "";
            }
            try {
                songName = intent.getExtras().getString("songName");
            } catch (NullPointerException e) {
                songName = "";
            }

            loadAudio(weblink,mp3link,albumArtLink,singerName,songName);
            playnewSong();
        }
    };

    private void register_instPlay() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter("b_singh_amarjit.gmail.com.jattfmmusicplayer.instPlay");
        registerReceiver(instPlay, filter);
    }

//======================BR for the home page to set fragments=======================================
    private BroadcastReceiver newFragfrmHome = new BroadcastReceiver() {
    @Override
        public void onReceive(Context context, Intent intent) {
            try {
                whichFragfrmHome = intent.getExtras().getString("fragFrm");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if(whichFragfrmHome.matches("mSingles")){
                setFragment(mSingles, "singleTacksPANE");
            }
            else if(whichFragfrmHome.matches("mSinglesHindi")){
                setFragment(mSinglesHindi, "singleTacksHindiPANE");
            }
            else if(whichFragfrmHome.matches("mAlbums")){
                setFragment(mAlbums, "albumsPANE");
            }
            else if(whichFragfrmHome.matches("mAlbumsHindi")){
                setFragment(mAlbumsHindi, "albumsHindiPANE");
            }
            else if(whichFragfrmHome.matches("mTopLSingles")){
                setFragment(mTopLSingles, "top20LSingles");
            }
            else if(whichFragfrmHome.matches("mTop20Overall")){
                setFragment(mTop20Overall, "top20Overall");
            }

        }
    };

    private void register_newFragfrmHome(){
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(fragment_home.Broadcast_switchFrag);
        registerReceiver(newFragfrmHome, filter);
    }
}


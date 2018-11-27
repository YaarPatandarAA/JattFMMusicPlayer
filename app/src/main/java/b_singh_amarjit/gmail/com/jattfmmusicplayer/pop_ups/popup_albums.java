package b_singh_amarjit.gmail.com.jattfmmusicplayer.pop_ups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.BlurBitmap;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.albumSongs;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters.recyclerViewAdapter_Albums;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.settings;

public class popup_albums extends AppCompatActivity {

    private Context mContext;

    //Items in layout
    private ImageView albumArtImages;
    private TextView singerN, albumN, musicN, lyricsN, labelN, releaseN, categoryN;
    private FloatingActionButton shareFloating, downloadFloating, addAllFloating, playAllFloating;
    private FloatingActionsMenu floatingMenu;
    private ConstraintLayout popAlbums;

    //Variables needed;
    private String albumArtLink, singerName, albumName, weblink, mp3link;
    private String musician, lyricist, rLabel, releaseDate, category;
    private String songMp3_48 = "", songMp3_128 = "", songMp3_192 = "", songMp3_320 = "";
    private String zip_48, zip_128, zip_320, musicQualityValue;
    private String webURL; // internal for the JSOUP;
    private String[] allAlbumSongsWebLink, allAlbumSongsMp3Link, allAlbumSongsArtLink, allAlbumSongsSinger, allAlbumSongsSong;
    private Uri webAdr;

    List<albumSongs> lstTracks;
    RecyclerView myRecyclerView;
    recyclerViewAdapter_Albums myAdapter;
    GridLayoutManager gManager;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_albums);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(settings.SHAREDPREF, Context.MODE_PRIVATE);
        musicQualityValue = sharedPreferences.getString(settings.boxVal, "MediumQuality");

        if(getIntent().hasExtra("WEB_PAGE_LINK")){
            webURL = getIntent().getExtras().getString("WEB_PAGE_LINK");
        }
        else{
            webURL = "";
        }

        lstTracks = new ArrayList<>();

        myRecyclerView = (RecyclerView) findViewById(R.id.albumSongs);
        myAdapter = new recyclerViewAdapter_Albums(mContext, lstTracks);
        gManager = new GridLayoutManager(mContext, 1);
        myRecyclerView.setLayoutManager(gManager);
        myRecyclerView.setAdapter(myAdapter);

        floatingMenu     = (FloatingActionsMenu) findViewById(R.id.floatingMenu);
        shareFloating    = (FloatingActionButton) findViewById(R.id.shareFloating);
        downloadFloating = (FloatingActionButton) findViewById(R.id.downloadFloating);
        addAllFloating   = (FloatingActionButton) findViewById(R.id.addAll2Q);
        playAllFloating  = (FloatingActionButton) findViewById(R.id.playAll);
        popAlbums        = (ConstraintLayout) findViewById(R.id.popAlbums);

        albumArtImages = (ImageView) findViewById(R.id.albumartImg);
        singerN = (TextView) findViewById(R.id.artistNamePOP);
        albumN = (TextView) findViewById(R.id.albumNamePOP);
        musicN = (TextView) findViewById(R.id.musicNamePOP);
        lyricsN = (TextView) findViewById(R.id.lyricsNamePOP);
        labelN = (TextView) findViewById(R.id.labelNamePOP);
        releaseN = (TextView) findViewById(R.id.releaseDatePOP);
        categoryN = (TextView) findViewById(R.id.categoryPOP);

        getWebsite();

        shareFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareText();
            }
        });
        downloadFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zip_320.contains("hq")) {
                    webAdr = Uri.parse(zip_320);
                }
                else{
                    webAdr = Uri.parse(zip_128);
                }
                Intent goToDownload = new Intent(Intent.ACTION_VIEW, webAdr);
                if(goToDownload.resolveActivity(getPackageManager()) != null){
                    startActivity(goToDownload);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "No Download For Some Reason!!!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        addAllFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < allAlbumSongsMp3Link.length; i++) {
                    if(allAlbumSongsMp3Link[i].matches("")){
                        Toast.makeText(mContext,"Found Song with 404, not added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent addtoQBR = new Intent("b_singh_amarjit.gmail.com.jattfmmusicplayer.addtoList");

                        addtoQBR.putExtra("webLink", allAlbumSongsWebLink[i]);
                        addtoQBR.putExtra("mp3link", allAlbumSongsMp3Link[i]);
                        addtoQBR.putExtra("albumartlink", allAlbumSongsArtLink[i]);
                        addtoQBR.putExtra("singerName", allAlbumSongsSinger[i]);
                        addtoQBR.putExtra("songName", allAlbumSongsSong[i]);

                        mContext.sendBroadcast(addtoQBR);
                    }
                }
                Toast.makeText(mContext,"Added all to Queue", Toast.LENGTH_SHORT).show();
            }
        });
        playAllFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean firstPlay = true;
                for(int i = 0; i < allAlbumSongsMp3Link.length; i++) {
                    if (allAlbumSongsMp3Link[i].matches("")) {
                        Toast.makeText(mContext, "Found Song with 404, not added", Toast.LENGTH_SHORT).show();
                    }
                    else if(firstPlay){
                        Intent instPlay = new Intent("b_singh_amarjit.gmail.com.jattfmmusicplayer.instPlay");

                        instPlay.putExtra("webLink", allAlbumSongsWebLink[i]);
                        instPlay.putExtra("mp3link", allAlbumSongsMp3Link[i]);
                        instPlay.putExtra("albumartlink", allAlbumSongsArtLink[i]);
                        instPlay.putExtra("singerName", allAlbumSongsSinger[i]);
                        instPlay.putExtra("songName", allAlbumSongsSong[i]);

                        mContext.sendBroadcast(instPlay);
                        firstPlay = false;
                    }
                    else if(!firstPlay){
                        Intent addtoQBR = new Intent("b_singh_amarjit.gmail.com.jattfmmusicplayer.addtoList");

                        addtoQBR.putExtra("webLink", allAlbumSongsWebLink[i]);
                        addtoQBR.putExtra("mp3link", allAlbumSongsMp3Link[i]);
                        addtoQBR.putExtra("albumartlink", allAlbumSongsArtLink[i]);
                        addtoQBR.putExtra("singerName", allAlbumSongsSinger[i]);
                        addtoQBR.putExtra("songName", allAlbumSongsSong[i]);

                        mContext.sendBroadcast(addtoQBR);
                    }
                }
                Toast.makeText(mContext,"Added all to Queue", Toast.LENGTH_SHORT).show();
            }
        });


        albumArtImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(weblink);
                Intent goToWebPage = new Intent(Intent.ACTION_VIEW, webpage);
                if(goToWebPage.resolveActivity(getPackageManager()) != null){
                    startActivity(goToWebPage);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "No Webpage, How is this possible?!?!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }

    public void shareText() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = weblink;
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(webURL).get();
                    Elements linkSongs = doc.select("a.touch");
                    Element albumArt = doc.select("div.albumCoverSmall img").first();
                    Elements albumName_txt = doc.select("p.style18");
                    Elements links_Zip = doc.select("a");

                    albumArtLink = albumArt.attr("src").replaceAll(" ", "%20");

                    allAlbumSongsWebLink = new String[linkSongs.size() - 1];
                    allAlbumSongsMp3Link = new String[linkSongs.size() - 1];
                    allAlbumSongsArtLink = new String[linkSongs.size() - 1];
                    allAlbumSongsSinger  = new String[linkSongs.size() - 1];
                    allAlbumSongsSong    = new String[linkSongs.size() - 1];

                    Boolean mp3ZipCheck = true;
                    for(int i = 0; i < links_Zip.size(); i++){
                        if(links_Zip.get(i).attr("href").contains("/zip/hq")){
                            zip_320 = links_Zip.get(i).attr("href");
                            mp3ZipCheck = false;
                        }
                        else if(links_Zip.get(i).attr("href").contains("/zip/mq")){
                            zip_128 = links_Zip.get(i).attr("href");
                            mp3ZipCheck = false;
                        }
                        else if(links_Zip.get(i).attr("href").contains("/zip/lq/")){
                            zip_48 = links_Zip.get(i).attr("href");
                        }
                        else if(mp3ZipCheck){
                            zip_48 = "";
                            zip_128 = "";
                            zip_320 = "";
                        }
                    }

                    Document doc2;
                    for(int j = 0; j < (linkSongs.size() - 1); j++) {
                        doc2 = Jsoup.connect(linkSongs.get(j).attr("href")).get();
                        Elements mp3Links = doc2.select("a.touch");
                        Element albumArtSingle = doc2.select("div.albumCoverSmall img").first();
                        Elements albumName_txtSingle = doc2.select("p.style18");
                        Boolean mp3Checker = true;

                        for(int i = 0; i < mp3Links.size(); i++) {
                            if(mp3Links.get(i).attr("href").startsWith("https://lq")) {
                                songMp3_48 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                                if(musicQualityValue.matches("LowQuality")){
                                    mp3Checker = false;
                                    mp3link = songMp3_48;
                                }
                            }
                            else if(mp3Links.get(i).attr("href").startsWith("https://cdn")) {
                                songMp3_128 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                                if(musicQualityValue.matches("MediumQuality")){
                                    mp3Checker = false;
                                    mp3link = songMp3_128;
                                }
                            }
                            else if(mp3Links.get(i).attr("href").startsWith("https://hd1")) {
                                songMp3_192 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                                if(musicQualityValue.matches("HighQuality")){
                                    mp3Checker = false;
                                    mp3link = songMp3_192;
                                }
                            }
                            else if(mp3Links.get(i).attr("href").startsWith("https://hd")) {
                                songMp3_320 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                                if(musicQualityValue.matches("HighDef")){
                                    mp3Checker = false;
                                    mp3link = songMp3_320;
                                }
                            }
                            else if(mp3Checker){
                                mp3link = "";
                                songMp3_48 = "";
                                songMp3_128 = "";
                                songMp3_192 = "";
                                songMp3_320 = "";
                            }
                        }

                        if(!(songMp3_320.contains("hd"))) {
                            songMp3_320 = mp3link;
                        }

                        String artistNameTEMP = "", songNameTEMP = "";

                        for(int i = 0; i < albumName_txtSingle.size(); i++) {
                            if( albumName_txtSingle.get(i).text().contains("Title:") || albumName_txtSingle.get(i).text().contains("Album:") ) {
                                songNameTEMP = albumName_txtSingle.get(i).text().replace("Title:", "").replace("Album:","");
                                songNameTEMP = songNameTEMP.trim();
                                break;
                            }
                            else{
                                songNameTEMP = "";
                            }
                        }

                        for(int i = 0; i < albumName_txtSingle.size(); i++) {
                            if ( albumName_txtSingle.get(i).text().contains("Singer:") || albumName_txtSingle.get(i).text().contains("Artists:") ) {
                                artistNameTEMP = albumName_txtSingle.get(i).text().replace("Singer:", "").replace("Artists:","");
                                artistNameTEMP = artistNameTEMP.trim();
                                break;
                            }
                            else{
                                artistNameTEMP = "";
                            }
                        }

                        if(mp3link.matches("")){
                            allAlbumSongsWebLink[j] = "";
                            allAlbumSongsMp3Link[j] = "";
                            allAlbumSongsArtLink[j] = "https://i.imgur.com/OSfmX7H.jpg";
                            allAlbumSongsSinger[j]  = "Error 404";
                            allAlbumSongsSong[j]    = "Error 404";

                            lstTracks.add(new albumSongs("","","","https://i.imgur.com/OSfmX7H.jpg","ERROR 404","ERROR 404"));
                        }
                        else {
                            allAlbumSongsWebLink[j] = linkSongs.get(j).attr("href");
                            allAlbumSongsMp3Link[j] = mp3link;
                            allAlbumSongsArtLink[j] = albumArtSingle.attr("src").replaceAll(" ", "%20");
                            allAlbumSongsSinger[j]  = artistNameTEMP;
                            allAlbumSongsSong[j]    = songNameTEMP;

                            lstTracks.add(new albumSongs(linkSongs.get(j).attr("href"),
                                    mp3link,
                                    songMp3_320,
                                    albumArtSingle.attr("src").replaceAll(" ", "%20"),
                                    artistNameTEMP,
                                    songNameTEMP));
                        }
                    }

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if( albumName_txt.get(i).text().contains("Title:") || albumName_txt.get(i).text().contains("Album:") ) {
                            albumName = albumName_txt.get(i).text().replace("Title:", "").replace("Album:","");
                            albumName = albumName.trim();
                            break;
                        }
                        else{
                            albumName = "";
                        }
                    }

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if ( albumName_txt.get(i).text().contains("Singer:") || albumName_txt.get(i).text().contains("Artists:") ) {
                            singerName = albumName_txt.get(i).text().replace("Singer:", "").replace("Artists:","");
                            singerName = singerName.trim();
                            break;
                        }
                        else{
                            singerName = "";
                        }
                    }

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if (albumName_txt.get(i).text().contains("Music:")) {
                            musician = albumName_txt.get(i).text();
                            musician = musician.trim();
                            break;
                        }
                        else {
                            musician = "";
                        }
                    }

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if (albumName_txt.get(i).text().contains("Lyrics:")) {
                            lyricist = albumName_txt.get(i).text();
                            lyricist = lyricist.trim();
                            break;
                        }
                        else {
                            lyricist = "";
                        }
                    }

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if (albumName_txt.get(i).text().contains("Label:")) {
                            rLabel = albumName_txt.get(i).text();
                            rLabel = rLabel.trim();
                            break;
                        }
                        else {
                            rLabel = "";
                        }
                    }

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if (albumName_txt.get(i).text().contains("Released:")) {
                            releaseDate = albumName_txt.get(i).text();
                            releaseDate = releaseDate.trim();
                            break;
                        }
                        else {
                            releaseDate = "";
                        }
                    }

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if (albumName_txt.get(i).text().contains("Catgory:")) {
                            category = albumName_txt.get(i).text();
                            category = category.trim();
                            break;
                        }
                        else {
                            category = "";
                        }
                    }



                } catch (IOException e) {
                    singerName = "Error : " + (e.getMessage()) + "\n";
                    albumName  = "Error : " + (e.getMessage()) + "\n";
                    musician  = "Error : " + (e.getMessage()) + "\n";
                    lyricist = "Error : " + (e.getMessage()) + "\n";
                    rLabel  = "Error : " + (e.getMessage()) + "\n";
                    releaseDate   = "Error : " + (e.getMessage()) + "\n";
                    category  = "Error : " + (e.getMessage()) + "\n";
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weblink = webURL;
                        Picasso.get().load(albumArtLink).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Bitmap blurredBitmap = BlurBitmap.blur( mContext, bitmap );

                                popAlbums.setBackground(new BitmapDrawable(mContext.getResources(), blurredBitmap));
                                albumArtImages.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                        singerN.setText(singerName.trim());
                        albumN.setText(albumName.trim());

                        musicN.setText(musician.trim());
                        lyricsN.setText(lyricist.trim());
                        labelN.setText(rLabel.trim());
                        releaseN.setText(releaseDate.trim());
                        categoryN.setText(category.trim());

                        getSupportActionBar().setTitle(albumName.trim() + " - " + singerName.trim());
                        myAdapter.notifyDataSetChanged();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
            }
        }).start();
    }
}

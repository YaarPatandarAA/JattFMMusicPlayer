package b_singh_amarjit.gmail.com.jattfmmusicplayer.pop_ups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.BlurBitmap;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.albumArt;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags.settings;

public class popup_singles extends AppCompatActivity {
    private Context mContext;

    //Items in layout
    private ImageView albumArtImages;
    private TextView singerN, songN, musicN, lyricsN, labelN, releaseN, downloadsN;
    private ImageButton instantPlay, addtoQ, downloadMp3, shareWebLink;
    private ConstraintLayout popSingles;

    //Variables needed;
    private String albumArtLink, singerName, songName, mp3link, weblink;
    private String musician, lyricist, rLabel,releaseDate, currentDownloads;
    private String songMp3_48, songMp3_128, songMp3_192, songMp3_320, musicQualityValue;
    private String webURL; // internal for the JSOUP;
    private Uri webAdr;

    public static final String Broadcast_addtoList = "b_singh_amarjit.gmail.com.jattfmmusicplayer.addtoList";
    public static final String Broadcast_instPlay = "b_singh_amarjit.gmail.com.jattfmmusicplayer.instPlay";


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_singles);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(settings.SHAREDPREF, Context.MODE_PRIVATE);
        musicQualityValue = sharedPreferences.getString(settings.boxVal, "MediumQuality");

        if(getIntent().hasExtra("WEB_PAGE_LINK")){
            webURL = getIntent().getExtras().getString("WEB_PAGE_LINK");
        }
        else{
            webURL = "";
        }

        albumArtImages = (ImageView) findViewById(R.id.albumartImg);
        popSingles     = (ConstraintLayout) findViewById(R.id.popSingles);

        songN      = (TextView) findViewById(R.id.songNamePOP);
        singerN    = (TextView) findViewById(R.id.artistNamePOP);
        musicN     = (TextView) findViewById(R.id.musicNamePOP);
        lyricsN    = (TextView) findViewById(R.id.lyricsNamePOP);
        releaseN   = (TextView) findViewById(R.id.releaseDatePOP);
        labelN     = (TextView) findViewById(R.id.labelNamePOP);
        downloadsN = (TextView) findViewById(R.id.curentDownPOP);

        instantPlay    = (ImageButton) findViewById(R.id.instantPlay);
        addtoQ         = (ImageButton) findViewById(R.id.addtoQ);
        downloadMp3    = (ImageButton) findViewById(R.id.downloadMp3);
        shareWebLink   = (ImageButton) findViewById(R.id.sharewebLink);

        shareWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareText();
            }
        });
        downloadMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songMp3_320.contains("hd")) {
                    webAdr = Uri.parse(songMp3_320);
                }
                else{
                    webAdr = Uri.parse(mp3link);
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

        addtoQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtoQBR = new Intent(Broadcast_addtoList);

                addtoQBR.putExtra("webLink", weblink);
                addtoQBR.putExtra("mp3link", mp3link);
                addtoQBR.putExtra("albumartlink", albumArtLink);
                addtoQBR.putExtra("singerName", singerName);
                addtoQBR.putExtra("songName", songName);

                sendBroadcast(addtoQBR);
                Toast.makeText(mContext,"Adding to Queue...",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        instantPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent instPlay = new Intent(Broadcast_instPlay);

                instPlay.putExtra("webLink", weblink);
                instPlay.putExtra("mp3link", mp3link);
                instPlay.putExtra("albumartlink", albumArtLink);
                instPlay.putExtra("singerName", singerName);
                instPlay.putExtra("songName", songName);

                sendBroadcast(instPlay);
                Toast.makeText(mContext,"Playing...",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        getWebsite();
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
                    Elements mp3Links = doc.select("a.touch");
                    Element albumArt = doc.select("div.albumCoverSmall img").first();
                    Elements albumName_txt = doc.select("p.style18");

                    for(int i = 0; i < mp3Links.size(); i++) {
                        if(mp3Links.get(i).attr("href").startsWith("https://lq")) {
                            songMp3_48 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                            if(musicQualityValue.matches("LowQuality")){
                                mp3link = songMp3_48;
                            }
                        }
                        else if(mp3Links.get(i).attr("href").startsWith("https://cdn")) {
                            songMp3_128 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                            if(musicQualityValue.matches("MediumQuality")){
                                mp3link = songMp3_128;
                            }
                        }
                        else if(mp3Links.get(i).attr("href").startsWith("https://hd1")) {
                            songMp3_192 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                            if(musicQualityValue.matches("HighQuality")){
                                mp3link = songMp3_192;
                            }
                        }
                        else if(mp3Links.get(i).attr("href").startsWith("https://hd")) {
                            songMp3_320 = mp3Links.get(i).attr("href").replaceAll(" ", "%20");

                            if(musicQualityValue.matches("HighDef")){
                                mp3link = songMp3_320;
                            }
                        }
                    }

                    albumArtLink = albumArt.attr("src").replaceAll(" ", "%20");

                    for(int i = 0; i < albumName_txt.size(); i++) {
                        if( albumName_txt.get(i).text().contains("Title:") || albumName_txt.get(i).text().contains("Album:") ) {
                            songName = albumName_txt.get(i).text().replace("Title:", "").replace("Album:","");
                            songName = songName.trim();
                            break;
                        }
                        else{
                            songName = "";
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
                        if (albumName_txt.get(i).text().contains("Downloads:")) {
                            currentDownloads = albumName_txt.get(i).text();
                            currentDownloads = currentDownloads.trim();
                            break;
                        }
                        else {
                            currentDownloads = "";
                        }
                    }
                } catch (IOException e) {
                    songMp3_48 = "Error : " + (e.getMessage()) + "\n";
                    songMp3_128 = "Error : " + (e.getMessage()) + "\n";
                    songMp3_192 = "Error : " + (e.getMessage()) + "\n";
                    songMp3_320 = "Error : " + (e.getMessage()) + "\n";

                    albumArtLink = "Error : " + (e.getMessage()) + "\n";

                    singerName = "Error : " + (e.getMessage()) + "\n";
                    songName  = "Error : " + (e.getMessage()) + "\n";
                    musician  = "Error : " + (e.getMessage()) + "\n";
                    lyricist = "Error : " + (e.getMessage()) + "\n";
                    rLabel  = "Error : " + (e.getMessage()) + "\n";
                    releaseDate   = "Error : " + (e.getMessage()) + "\n";
                    currentDownloads  = "Error : " + (e.getMessage()) + "\n";
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weblink = webURL;
                        Picasso.get().load(albumArtLink).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Bitmap blurredBitmap = BlurBitmap.blur( mContext, bitmap );

                                popSingles.setBackground(new BitmapDrawable(mContext.getResources(), blurredBitmap));
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
                        songN.setText(songName.trim());

                        musicN.setText(musician.trim());
                        lyricsN.setText(lyricist.trim());
                        labelN.setText(rLabel.trim());
                        releaseN.setText(releaseDate.trim());
                        downloadsN.setText(currentDownloads.trim());
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
            }
        }).start();
    }
}

package b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.albumArt;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters.recyclerViewAdapterHome;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_home extends Fragment {

    //Huge list of needed variables for getting webdata

    //Punjabi Singles
    private String[] imgURL_PS = new String[6];
    private String[] songN_PS = new String[6];
    private String[] audioURL_PS = new String[6];

    //Hindi Singles
    private String[] imgURL_HS = new String[6];
    private String[] songN_HS = new String[6];
    private String[] audioURL_HS = new String[6];

    //Punjabi Albums
    private String[] imgURL_PA = new String[6];
    private String[] songN_PA = new String[6];
    private String[] albumPageURL_PA = new String[6];

    //Hindi Albums
    private String[] imgURL_HA = new String[6];
    private String[] songN_HA = new String[6];
    private String[] albumPageURL_HA = new String[6];

    List<albumArt> lstTracksPS, lstTracksHS, lstTracksPA, lstTracksHA;
    RecyclerView myRecyclerViewPS, myRecyclerViewHS, myRecyclerViewPA, myRecyclerViewHA;
    recyclerViewAdapterHome myAdapterPS, myAdapterHS, myAdapterPA, myAdapterHA;
    LinearLayoutManager mLayoutManagerPS, mLayoutManagerHS, mLayoutManagerPA, mLayoutManagerHA;
    Context mContext;

    private Button pSingles, hSingles, pAlbums, hAlbums;

    public static final String Broadcast_switchFrag = "b_singh_amarjit.gmail.com.jattfmmusicplayer.switchFrag";

    public fragment_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();

        pSingles = (Button) v.findViewById(R.id.p_singles);
        hSingles = (Button) v.findViewById(R.id.h_singles);
        pAlbums = (Button) v.findViewById(R.id.p_albums);
        hAlbums = (Button) v.findViewById(R.id.h_albums);

        lstTracksPS = new ArrayList<>();
        lstTracksHS = new ArrayList<>();
        lstTracksPA = new ArrayList<>();
        lstTracksHA = new ArrayList<>();

        myRecyclerViewPS = (RecyclerView) v.findViewById(R.id.latest5PSingles);
        myRecyclerViewHS = (RecyclerView) v.findViewById(R.id.latest5HSingles);
        myRecyclerViewPA = (RecyclerView) v.findViewById(R.id.latest5PAlbums);
        myRecyclerViewHA = (RecyclerView) v.findViewById(R.id.latest5HAlbums);

        myAdapterPS = new recyclerViewAdapterHome(getContext(), lstTracksPS);
        myAdapterHS = new recyclerViewAdapterHome(getContext(), lstTracksHS);
        myAdapterPA = new recyclerViewAdapterHome(getContext(), lstTracksPA);
        myAdapterHA = new recyclerViewAdapterHome(getContext(), lstTracksHA);

        mLayoutManagerPS = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerHS = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerPA = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerHA = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        for(int i=0; i<5; i++) {
            lstTracksPS.add(new albumArt( ((i+1) + (". Loading... ")), "https://i.imgur.com/OSfmX7H.jpg", "loading", "null"));
            lstTracksHS.add(new albumArt( ((i+1) + (". Loading... ")), "https://i.imgur.com/OSfmX7H.jpg", "loading", "null"));
            lstTracksPA.add(new albumArt( ((i+1) + (". Loading... ")), "https://i.imgur.com/OSfmX7H.jpg", "loading", "null"));
            lstTracksHA.add(new albumArt( ((i+1) + (". Loading... ")), "https://i.imgur.com/OSfmX7H.jpg", "loading", "null"));
        }

        myRecyclerViewPS.setLayoutManager(mLayoutManagerPS);
        myRecyclerViewHS.setLayoutManager(mLayoutManagerHS);
        myRecyclerViewPA.setLayoutManager(mLayoutManagerPA);
        myRecyclerViewHA.setLayoutManager(mLayoutManagerHA);

        myRecyclerViewPS.setAdapter(myAdapterPS);
        myRecyclerViewHS.setAdapter(myAdapterHS);
        myRecyclerViewPA.setAdapter(myAdapterPA);
        myRecyclerViewHA.setAdapter(myAdapterHA);

        pSingles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadcastIntent = new Intent(Broadcast_switchFrag);
                broadcastIntent.putExtra("fragFrm", "mSingles");
                mContext.sendBroadcast(broadcastIntent);
            }
        });
        hSingles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadcastIntent = new Intent(Broadcast_switchFrag);
                broadcastIntent.putExtra("fragFrm", "mSinglesHindi");
                mContext.sendBroadcast(broadcastIntent);
            }
        });
        pAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadcastIntent = new Intent(Broadcast_switchFrag);
                broadcastIntent.putExtra("fragFrm", "mAlbums");
                mContext.sendBroadcast(broadcastIntent);
            }
        });
        hAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadcastIntent = new Intent(Broadcast_switchFrag);
                broadcastIntent.putExtra("fragFrm", "mAlbumsHindi");
                mContext.sendBroadcast(broadcastIntent);
            }
        });

        getWebsite_PS();
        getWebsite_HS();
        getWebsite_PA();
        getWebsite_HA();

        return v;
    }

    private void getWebsite_PS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://ww.mr-jatt.com/category.php?c=Single%20Tracks&p=1").get();
                    Elements songName = doc.select("a.touch");
                    for(int i = 0; i < 5; i++) {
                        songN_PS[i] = songName.get(i).text().replace("»", "");
                        audioURL_PS[i] = "https://ww.mr-jatt.com" + songName.get(i).attr("href");
                    }
                    Document doc2;
                    for(int i = 0; i < 5; i++) {
                        doc2 = Jsoup.connect(audioURL_PS[i]).get();
                        Element img = doc2.select("div.albumCoverSmall img").first();
                        imgURL_PS[i] = img.attr("src");
                        imgURL_PS[i] = imgURL_PS[i].replaceAll(" ", "%20");
                        audioURL_PS[i] = audioURL_PS[i].replaceAll(" ", "%20");
                    }
                } catch (IOException e) {
                    Log.d("ERROR CODE", e.getMessage());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(songN_PS[0] == null || songN_PS[0] == ""){
                            //Website is down block off app   songN[0] == null || songN[0] == ""
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Down!!!")
                                    .setCancelable(false)
                                    .setMessage("Mr-Jatt.com might be down for Maintenance")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        for(int i = 0; i < 5; i++) {
                            lstTracksPS.set(i, new albumArt(((i+1) + ". " + (songN_PS[i])), imgURL_PS[i], "single", audioURL_PS[i]));
                        }
                        myAdapterPS.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
    private void getWebsite_HS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://ww.mr-jatt.com/category.php?c=Hindi%20Single%20Track&p=1").get();
                    Elements songName = doc.select("a.touch");
                    for(int i = 0; i < 5; i++) {
                        songN_HS[i] = songName.get(i).text().replace("»", "");
                        audioURL_HS[i] = "https://ww.mr-jatt.com" + songName.get(i).attr("href");
                    }
                    Document doc2;
                    for(int i = 0; i < 5; i++) {
                        doc2 = Jsoup.connect(audioURL_HS[i]).get();
                        Element img = doc2.select("div.albumCoverSmall img").first();
                        imgURL_HS[i] = img.attr("src");
                        imgURL_HS[i] = imgURL_HS[i].replaceAll(" ", "%20");
                        audioURL_HS[i] = audioURL_HS[i].replaceAll(" ", "%20");
                    }
                } catch (IOException e) {
                    Log.d("ERROR CODE", e.getMessage());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(songN_HS[0] == null || songN_HS[0] == ""){
                            //Website is down block off app   songN[0] == null || songN[0] == ""
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Down!!!")
                                    .setCancelable(false)
                                    .setMessage("Mr-Jatt.com might be down for Maintenance")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        for(int i = 0; i < 5; i++) {
                            lstTracksHS.set(i, new albumArt(((i+1) + ". " + (songN_HS[i])), imgURL_HS[i], "single", audioURL_HS[i]));
                        }
                        myAdapterHS.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void getWebsite_PA() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://ww.mr-jatt.com/category.php?c=Punjabi&p=1").get();
                    Elements albumName = doc.select("a.touch");
                    for(int i = 0; i < 5; i++) {
                        songN_PA[i] = albumName.get(i).text().replace("»", "");
                        albumPageURL_PA[i] = "https://ww.mr-jatt.com" + albumName.get(i).attr("href");
                    }
                    Document doc2;
                    for(int i = 0; i < 5; i++) {
                        doc2 = Jsoup.connect(albumPageURL_PA[i]).get();
                        Element img = doc2.select("div.albumCoverSmall img").first();
                        imgURL_PA[i] = img.attr("src");
                        imgURL_PA[i] = imgURL_PA[i].replaceAll(" ", "%20");
                    }
                } catch (IOException e) {
                    Log.d("ERROR CODE", e.getMessage());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(songN_PA[0] == null || songN_PA[0] == ""){
                            //Website is down block off app   songN[0] == null || songN[0] == ""
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Down!!!")
                                    .setCancelable(false)
                                    .setMessage("Mr-Jatt.com might be down for Maintenance")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        for(int i = 0; i < 5; i++) {
                            lstTracksPA.set(i, new albumArt(((i+1) + ". " + (songN_PA[i])), imgURL_PA[i], "album", albumPageURL_PA[i]));
                        }
                        myAdapterPA.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
    private void getWebsite_HA() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://ww.mr-jatt.com/category.php?c=Hindi&p=1").get();
                    Elements albumName = doc.select("a.touch");
                    for(int i = 0; i < 5; i++) {
                        songN_HA[i] = albumName.get(i).text().replace("»", "");
                        albumPageURL_HA[i] = "https://ww.mr-jatt.com" + albumName.get(i).attr("href");
                    }
                    Document doc2;
                    for(int i = 0; i < 5; i++) {
                        doc2 = Jsoup.connect(albumPageURL_HA[i]).get();
                        Element img = doc2.select("div.albumCoverSmall img").first();
                        imgURL_HA[i] = img.attr("src");
                        imgURL_HA[i] = imgURL_HA[i].replaceAll(" ", "%20");
                    }
                } catch (IOException e) {
                    Log.d("ERROR CODE", e.getMessage());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(songN_HA[0] == null || songN_HA[0] == ""){
                            //Website is down block off app   songN[0] == null || songN[0] == ""
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Down!!!")
                                    .setCancelable(false)
                                    .setMessage("Mr-Jatt.com might be down for Maintenance")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        for(int i = 0; i < 5; i++) {
                            lstTracksHA.set(i, new albumArt(((i+1) + ". " + (songN_HA[i])), imgURL_HA[i], "album", albumPageURL_HA[i]));
                        }
                        myAdapterHA.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

}

package b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.MainActivity;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc.albumArt;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters.recyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_topLSingles extends Fragment {
    List<albumArt> lstTracks;
    RecyclerView myRecyclerView;
    recyclerViewAdapter myAdapter;
    GridLayoutManager gManager;
    ProgressBar progBar;
    FloatingActionButton mFloatingActionButton;
    ProgressDialog dialog;
    Context mContext;

    private String[] imgURL = new String[22];
    private String[] songN = new String[22];
    private String[] audioURL = new String[22];


    public fragment_topLSingles() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_singles_albums_list, container, false);
        mContext = getContext();

        dialog = ProgressDialog.show(getContext(), "","Loading. Please wait...", true);
        lstTracks = new ArrayList<>();
        myRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_id);
        myAdapter = new recyclerViewAdapter(getContext(), lstTracks);
        gManager = new GridLayoutManager(getContext(), 2);
        progBar = (ProgressBar) v.findViewById(R.id.progressLoad);
        mFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);

        getWebsiteInit();

        for(int i=0; i<20; i++) {
            lstTracks.add(new albumArt("Loading... ", "https://i.imgur.com/OSfmX7H.jpg", "loading", "null"));
        }

        myRecyclerView.setLayoutManager(gManager);
        myRecyclerView.setAdapter(myAdapter);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRecyclerView.smoothScrollToPosition(0);
            }
        });

        return v;
    }

    private void getWebsiteInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://ww.mr-jatt.com/punjabisong-top20-singletracks.html").get();
                    Elements songName = doc.select("a.touch");
                    for(int i = 0; i < 20; i++) {
                        songN[i] = songName.get(i).text().replace("»", "");
                        audioURL[i] = songName.get(i).attr("href");
                    }
                    Document doc2;
                    for(int i = 0; i < 20; i++) {
                        doc2 = Jsoup.connect(audioURL[i]).get();

                        try {
                            Element img = doc2.select("div.albumCoverSmall img").first();
                            imgURL[i] = img.attr("src");
                            imgURL[i] = imgURL[i].replaceAll(" ", "%20");
                            audioURL[i] = audioURL[i].replaceAll(" ", "%20");
                        }
                        catch(Exception e){
                            songN[i] = songN[i] + " noPage";
                            imgURL[i] = "https://i.imgur.com/OSfmX7H.jpg";
                            audioURL[i] = "null";
                        }
                    }
                } catch (IOException e) {
                    Log.d("ERROR CODE", e.getMessage());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(songN[0] == null || songN[0] == ""){
                            //Website is down block off app   songN[0] == null || songN[0] == ""
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Down!!!")
                                    .setCancelable(false)
                                    .setMessage("Mr-Jatt.com might be down for Maintenance")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        for(int i = 0; i < 20; i++) {
                            lstTracks.set(i, new albumArt(songN[i], imgURL[i], "single", audioURL[i]));
                        }
                        myAdapter.notifyDataSetChanged();
                        progBar.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }

}

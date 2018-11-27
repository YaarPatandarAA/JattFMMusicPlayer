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
public class fragment_albums extends Fragment {
    int pageNum = 1;
    int currItem, totalItem, prevTotalItem = 0, scrollOutItem;
    int viewThres = 15;
    boolean isScrolling = true;


    List<albumArt> lstAlbums;
    RecyclerView myRecyclerView;
    recyclerViewAdapter myAdapter;
    GridLayoutManager gManager;
    ProgressBar progBar;
    FloatingActionButton mFloatingActionButton;
    ProgressDialog dialog;
    Context mContext;

    private String[] imgURL = new String[20];
    private String[] songN = new String[20];
    private String[] albumPageURL = new String[20];


    public fragment_albums() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_singles_albums_list, container, false);
        mContext = getContext();

        dialog = ProgressDialog.show(getContext(), "","Loading. Please wait...", true);
        lstAlbums = new ArrayList<>();
        myRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_id);
        myAdapter = new recyclerViewAdapter(getContext(), lstAlbums);
        gManager = new GridLayoutManager(getContext(), 2);
        progBar = (ProgressBar) v.findViewById(R.id.progressLoad);
        mFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);

        getWebsiteInit();

        for(int i=0; i<15; i++) {
            lstAlbums.add(new albumArt("Loading... ", "https://i.imgur.com/OSfmX7H.jpg", "loading", "null"));
        }

        myRecyclerView.setLayoutManager(gManager);
        myRecyclerView.setAdapter(myAdapter);

        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currItem = gManager.getChildCount();
                totalItem = gManager.getItemCount();
                scrollOutItem = gManager.findFirstVisibleItemPosition();

                if (dy>0){
                    if(isScrolling){
                        if(totalItem>prevTotalItem){
                            isScrolling = false;
                            prevTotalItem = totalItem;
                        }
                    }
                    if(!isScrolling && ((totalItem-currItem)<=(scrollOutItem+viewThres))){
                        pageNum++;
                        isScrolling=true;
                        getWebsite();
                    }
                }
            }
        });

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
                    Document doc = Jsoup.connect("https://ww.mr-jatt.com/category.php?c=Punjabi&p=" + Integer.toString(pageNum)).get();
                    Elements albumName = doc.select("a.touch");
                    for(int i = 0; i < 15; i++) {
                        songN[i] = albumName.get(i).text().replace("»", "");
                        albumPageURL[i] = "https://ww.mr-jatt.com" + albumName.get(i).attr("href");
                    }
                    Document doc2;
                    for(int i = 0; i < 15; i++) {
                        doc2 = Jsoup.connect(albumPageURL[i]).get();
                        Element img = doc2.select("div.albumCoverSmall img").first();
                        imgURL[i] = img.attr("src");
                        imgURL[i] = imgURL[i].replaceAll(" ", "%20");
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
                        for(int i = 0; i < 15; i++) {
                            lstAlbums.set(i, new albumArt(songN[i], imgURL[i], "album", albumPageURL[i]));
                        }
                        myAdapter.notifyDataSetChanged();
                        progBar.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }
    private void getWebsite(){
        progBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://ww.mr-jatt.com/category.php?c=Punjabi&p=" + Integer.toString(pageNum)).get();
                    Elements albumName = doc.select("a.touch");
                    for(int i = 0; i < 15; i++) {
                        songN[i] = albumName.get(i).text().replace("»", "");
                        albumPageURL[i] = "https://ww.mr-jatt.com" + albumName.get(i).attr("href");
                    }
                    Document doc2;
                    for(int i = 0; i < 15; i++) {
                        doc2 = Jsoup.connect(albumPageURL[i]).get();
                        Element img = doc2.select("div.albumCoverSmall img").first();
                        imgURL[i] = img.attr("src");
                        imgURL[i] = imgURL[i].replaceAll(" ", "%20");
                    }
                } catch (IOException e) {
                    Log.d("ERROR CODE", e.getMessage());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < 15; i++) {
                            lstAlbums.add(new albumArt(songN[i], imgURL[i], "album", albumPageURL[i]));
                        }
                        myAdapter.notifyDataSetChanged();
                        progBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

}

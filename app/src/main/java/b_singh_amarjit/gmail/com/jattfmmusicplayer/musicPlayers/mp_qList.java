package b_singh_amarjit.gmail.com.jattfmmusicplayer.musicPlayers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.MainActivity;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.RVAdapters.recyclerviewadapter_qlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class mp_qList extends Fragment {

    private ImageButton closePane;
    private LinearLayout myLinear;

    //Qlist
    RecyclerView myQlist;
    recyclerviewadapter_qlist myQlistAdapter;

    //Context to be able to be used universal
    private Context mContext;

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.musicPlayer.setTouchEnabled(true);
    }

    public mp_qList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mp_q_list, container, false);
        MainActivity.musicPlayer.setTouchEnabled(false);

        mContext = v.getContext();
        myLinear = (LinearLayout) v.findViewById(R.id.qlist_linear);

        closePane = (ImageButton) v.findViewById(R.id.closeList);
        closePane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        myLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        myQlist = (RecyclerView) v.findViewById(R.id.songQlist);
        myQlistAdapter = new recyclerviewadapter_qlist(mContext, MainActivity.audioList);
        myQlist.setLayoutManager(new GridLayoutManager(mContext,1));
        myQlist.setAdapter(myQlistAdapter);

        return v;
    }
}

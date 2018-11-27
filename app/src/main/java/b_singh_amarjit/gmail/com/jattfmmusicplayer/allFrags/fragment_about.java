package b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.MainActivity;
import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_about extends Fragment {


    public fragment_about() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View aboutPage = new AboutPage(getContext())
                .setDescription("Jatt FM Music Player, was created to make the playback of media content on Mr-Jatt.com simpler and easier. " +
                        "This app is in no way affiliated with, authorized, maintained, sponsored or endorsed by Mr-Jatt.com or any of its affiliates or subsidiaries. " +
                        "This is an independent, unofficial app. All information on this app is grabbed from Mr-Jatt.com and subject to change without notice. " +
                        "Contact Mr-Jatt.com directly or visit their web pages for any details regarding them and their services." +
                        "\n\n" +
                        "Unofficial Jatt FM Music Player, created by Amarjit Singh." +
                        "\n\n" +
                        "Credits" +
                        "\n" +
                        "org.jsoup:jsoup:1.10.1"+
                        "\n" +
                        "com.squareup.picasso:picasso:2.71828" +
                        "\n" +
                        "com.sothree.slidinguppanel:library:3.4.0" +
                        "\n" +
                        "com.github.medyo:android-about-page:1.2.4" +
                        "\n" +
                        "com.getbase:floatingactionbutton:1.10.1")
                .isRTL(false)
                .setImage(R.drawable.jattfm_about)
                .addItem(new Element().setTitle(getString(R.string.versionBeta)))
                .addGroup("Connect with us")
                .addEmail("b.singh.amarjit@gmail.com")
                .addFacebook("amarjit.budwal")
                .addTwitter("amarjitbudwal")
                .addInstagram("amarjitb")
                .addYoutube("UCam5fIzDq1Ecy9hVFpEKBpQ")
                .create();

        return aboutPage;
    }

}

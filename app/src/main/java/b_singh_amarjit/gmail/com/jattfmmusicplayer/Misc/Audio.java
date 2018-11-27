package b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc;

import android.os.Parcel;
import android.os.Parcelable;

public class Audio implements Parcelable{
    private String webPageLink;
    private String mp3Link;
    private String artLink;

    private String artistName;
    private String songName;

    public Audio(String webPageLink, String mp3Link, String artLink, String artistName, String songName) {
        this.webPageLink = webPageLink;
        this.mp3Link     = mp3Link;
        this.artLink     = artLink;
        this.artistName  = artistName;
        this.songName    = songName;
    }

    public String getWebPageLink() {
        return webPageLink;
    }

    public void setWebPageLink(String webPageLink) {
        this.webPageLink = webPageLink;
    }

    public String getMp3Link() {
        return mp3Link;
    }

    public void setMp3Link(String mp3Link) {
        this.mp3Link = mp3Link;
    }

    public String getArtLink() {
        return artLink;
    }

    public void setArtLink(String artLink) {
        this.artLink = artLink;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    // Parcelling part
    public Audio(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.webPageLink = data[0];
        this.mp3Link     = data[1];
        this.artLink     = data[2];
        this.artistName  = data[3];
        this.songName    = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.webPageLink,
                this.mp3Link,
                this.artLink,
                this.artistName,
                this.songName});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
}

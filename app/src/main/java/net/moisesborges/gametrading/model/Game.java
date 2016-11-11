package net.moisesborges.gametrading.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by moises.anjos on 10/08/2016.
 */

public class Game implements Parcelable{

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("original_release_date")
    private Date releaseDate;
    @SerializedName("image")
    private Image image;
    @SerializedName("platforms")
    private List<Platform> platforms;
    @SerializedName("deck")
    private String summary;

    public Game() {

    }

    protected Game(Parcel in) {
        id = in.readInt();
        name = in.readString();
        releaseDate = (Date) in.readSerializable();
        image = in.readParcelable(Image.class.getClassLoader());
        platforms = new ArrayList<>();
        in.readTypedList(platforms, Platform.CREATOR);
        summary = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeSerializable(releaseDate);
        parcel.writeParcelable(image,i);
        parcel.writeTypedList(platforms);
        parcel.writeString(summary);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return getName();
    }

}

package com.example.quizeo;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationQuizeo implements Parcelable {

    /** Double for the latitude of the current location: */
    private double latitude;

    /** Double for the longitude of the current location: */
    private double longitude;

    /**
     * Constructor for the locationQuizeo which sets the correct longitude and latitude.
     *
     * @param x latitude to set.
     * @param y longitude to set.
     */
    public LocationQuizeo(double x, double y) {
        latitude = x;
        longitude = y;
    }

    /**
     * Empty constructor
     */
    public LocationQuizeo() {
    }

    /**
     * Method for the parcelable, required to pass this object between classes.
     */
    protected LocationQuizeo(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    /**
     * Method for the parcelable, required to pass this object between classes.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    /**
     * Method for the parcelable, required to pass this object between classes.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Changes the value of this location to a different location.
     *
     * @param x new latitude.
     * @param y new longitude.
     */
    public void changeLocation(double x, double y) {
        latitude = x;
        longitude = y;
    }

    /**
     * Get the latitude of the current location.
     *
     * @return latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the longitutde of the current location.
     *
     * @return longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Creator for the parcelable, required to pass this object between classes.
     */
    public static final Creator<LocationQuizeo> CREATOR = new Creator<LocationQuizeo>() {
        @Override
        public LocationQuizeo createFromParcel(Parcel in) {
            return new LocationQuizeo(in);
        }

        @Override
        public LocationQuizeo[] newArray(int size) {
            return new LocationQuizeo[size];
        }
    };
}

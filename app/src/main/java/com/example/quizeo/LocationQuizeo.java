package com.example.quizeo;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationQuizeo implements Parcelable {

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
    private double latitude;
    private double longitude;

    public LocationQuizeo(double x, double y) {
        latitude = x;
        longitude = y;
    }

    public LocationQuizeo() {

    }

    protected LocationQuizeo(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void changeLocation(double x, double y) {
        latitude = x;
        longitude = y;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

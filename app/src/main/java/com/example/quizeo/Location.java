package com.example.quizeo;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private double latitude;

    private double longitude;

    public Location(double x, double y) {
        latitude = x;
        longitude = y;
    }

    protected Location(Parcel in) {
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

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

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

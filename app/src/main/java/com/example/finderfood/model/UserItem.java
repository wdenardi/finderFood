package com.example.finderfood.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserItem {

    private String uuid;
    private String uiIdItem;
    private String titleRecip;
    private String typeRecip;
    private String descRecip;
    private String profileUrl;

    public UserItem() {

    }

    public UserItem(String uuid,String uiIdItem,String titleRecip,String typeRecip,String descRecip,String profileUrl) {
        this.uuid = uuid;
        this.uiIdItem = uiIdItem;
        this.titleRecip = titleRecip;
        this.typeRecip = typeRecip;
        this.descRecip = descRecip;
        this.profileUrl = profileUrl;

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(uiIdItem);
        dest.writeString(typeRecip);
        dest.writeString(descRecip);
        dest.writeString(profileUrl);
        dest.writeString(profileUrl);
    }

    public String getUuid() {
        return uuid;
    }

    public String getUiIdItem() {
        return uiIdItem;
    }

    public String getTitleRecip() {
        return titleRecip;
    }

    public String getTypeRecip() {
        return typeRecip;
    }

    public String getDescRecip() {
        return descRecip;
    }

    public String getProfileUrl() {
        return profileUrl;
    }


}

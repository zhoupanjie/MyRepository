
package com.hy.superemsg.rsp;

import java.util.ArrayList;
import java.util.List;

import com.hy.superemsg.db.DBColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class GameContentDetail extends AbsContentDetail implements Parcelable {
    public String gameid;
    public String gamename;
    public String gameiconurl;
    public String gamefileurl;
    public String gameversion;
    public String gamepublisher;
    public String gamestyle;
    public String gameintroduce;
    public String gamefilesize;
    public int gamedownload;
    public List<String> gamescreenshotslist;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameid);
        dest.writeString(gamename);
        dest.writeString(gameiconurl);
        dest.writeString(gamefileurl);
        dest.writeString(gameversion);
        dest.writeString(gamepublisher);
        dest.writeString(gamestyle);
        dest.writeString(gameintroduce);
        dest.writeString(gamefilesize);
        dest.writeInt(gamedownload);
        dest.writeStringList(gamescreenshotslist);
    }

    public GameContentDetail() {
    }

    public GameContentDetail(Parcel source) {
        gameid = source.readString();
        gamename = source.readString();
        gameiconurl = source.readString();
        gamefileurl = source.readString();
        gameversion = source.readString();
        gamepublisher = source.readString();
        gamestyle = source.readString();
        gameintroduce = source.readString();
        gamefilesize = source.readString();
        gamedownload = source.readInt();
        gamescreenshotslist = new ArrayList<String>();
        source.readStringList(gamescreenshotslist);
    }

    public static final Parcelable.Creator<GameContentDetail> CREATOR = new Parcelable.Creator<GameContentDetail>() {

        @Override
        public GameContentDetail createFromParcel(Parcel source) {
            return new GameContentDetail(source);
        }

        @Override
        public GameContentDetail[] newArray(int size) {
            return new GameContentDetail[] {};
        }

    };

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DBColumns.ID, gameid);
        values.put(DBColumns.GameColumns.GAME_NAME, gamename);
        values.put(DBColumns.GameColumns.GAME_ICON_URL, gameiconurl);
        values.put(DBColumns.GameColumns.GAME_INTRODUCE, gameintroduce);
        values.put(DBColumns.GameColumns.GAME_STYLE, gamestyle);
        return values;
    }

    @Override
    public String getId() {
        return gameid;
    }
}

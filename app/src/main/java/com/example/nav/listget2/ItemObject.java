package com.example.nav.listget2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nav on 11/22/2014.
 */
public class ItemObject implements Parcelable{
    private String id;
    private String name;
    private String memo;
    private String completed;

    public ItemObject( String i, String n, String m, String c )
    {
        id = i;
        name = n;
        memo = m;
        completed = c;
    }

    public ItemObject( Parcel p )
    {
        id = p.readString();
        name = p.readString();
        memo = p.readString();
        completed = p.readString();
    }

    public static ArrayList<ItemObject> getItems( JSONArray arr )
    {
        ArrayList<ItemObject> list = new ArrayList<ItemObject>( arr.length() );

        try {
            for (int i = 0; i < arr.length(); ++i) {
                list.add( i, getItem( arr.getJSONObject(i) ) );
            }
        }
        catch (JSONException e)
        {
            Log.d("getItems", e.getLocalizedMessage());
        }
        return list;
    }

    public static ItemObject getItem( JSONObject obj )
    {
        String i = null;
        String n = null;
        String m = "";
        String c = "";

        try{
            i = obj.getJSONObject( "_id" ).getString( "$oid" );
            n = obj.getString( "name" );
            if( obj.has("memo") ) m = obj.getString( "memo" );
            if( obj.has("completed") ) c = obj.getString( "completed" );
        }
        catch (JSONException e)
        {
            Log.d( "getItem", e.getLocalizedMessage() );
        }
        return new ItemObject( i, n, m, c );
    }

    public String getName(){ return name; }
    public String getMemo(){ return memo; }
    public String getCompleter(){ return completed; }
    public String getId(){ return id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( id );
        parcel.writeString( name );
        parcel.writeString( memo );
        parcel.writeString( completed );
    }

    public static final Parcelable.Creator<ItemObject> CREATOR = new Parcelable.Creator<ItemObject>() {
        public ItemObject createFromParcel(Parcel in) {
            return new ItemObject(in);
        }

        public ItemObject[] newArray(int size) {
            return new ItemObject[size];
        }
    };
}

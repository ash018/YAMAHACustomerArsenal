package com.aci.yamaha.yamahacustomerarsenal.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aci.yamaha.yamahacustomerarsenal.model.Dealer;
import com.aci.yamaha.yamahacustomerarsenal.model.DealerInfo;
import com.aci.yamaha.yamahacustomerarsenal.model.LocationInfo;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    protected static final String TAG = DatabaseAdapter.class.getName();
    DataBaseHelper dbHelper;

    public DatabaseAdapter(Context context) {
        dbHelper = DataBaseHelper.getInstance(context);
        try {
            dbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        } finally {
            dbHelper.close();
        }
    }


    public void ftLocationData(List<LocationInfo> locs) {
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        try {
            mDb.delete("LocationInfo", null, null);

            ContentValues values = new ContentValues();
            JSONArray jArray = new JSONArray(locs);
            for (int i = 0; i < locs.size(); ++i) {
                values.clear();
                values.put("LocationId", locs.get(i).getLocationID());
                values.put("LocationName", locs.get(i).getLocationName());
                mDb.insert("LocationInfo", null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.close();
        }
        return;
    }

    public void ftDealerInfoData(List<DealerInfo> dealerInfos) {
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        try {
            mDb.delete("DealerInfo", null, null);
            ContentValues values = new ContentValues();
            for (int i = 0; i < dealerInfos.size(); ++i) {
                values.clear();
                values.put("UserID", dealerInfos.get(i).getUserID());
                values.put("UserName", dealerInfos.get(i).getUserName());
                values.put("LocationID", dealerInfos.get(i).getLocationID());
                values.put("LocationName", dealerInfos.get(i).getLocationName());
                values.put("ShowroomName", dealerInfos.get(i).getShowroomName());
                values.put("Address", dealerInfos.get(i).getAddress());
                values.put("ContactNo", dealerInfos.get(i).getContactNo());
                values.put("HotLine", dealerInfos.get(i).getHotLine());
                values.put("Email", dealerInfos.get(i).getEmail());
                mDb.insert("DealerInfo", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.close();
        }
        return;
    }


    public void deleteTables() {
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        try {
            mDb.delete("Answer", null, null);
            mDb.delete("Question", null, null);
            mDb.delete("QuestionOption", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.close();
        }
    }

    public ArrayList<LocationInfo> getLocations() {
        ArrayList<LocationInfo> dList = new ArrayList<>();
        SQLiteDatabase mDb = dbHelper.getReadableDatabase();
        try {
            String sql = "SELECT * FROM LocationInfo;";
            Cursor cursor = mDb.rawQuery(sql, null);
            LocationInfo d;
            d = new LocationInfo("", "Please select your location");
            dList.add(d);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {
                    String locationId = cursor.getString(cursor.getColumnIndex("LocationId"));
                    String locationName = cursor.getString(cursor.getColumnIndex("LocationName"));
                    d = new LocationInfo(locationId, locationName);
                    dList.add(d);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.close();
        }
        return dList;
    }


    public ArrayList<DealerInfo> getServiceCenter(String locationId) {


        ArrayList<DealerInfo> dList = new ArrayList<>();

        SQLiteDatabase mDb = dbHelper.getReadableDatabase();
        try {
            String sql = "SELECT * FROM DealerInfo WHERE LocationId='" + locationId + "';";

            Cursor cursor = mDb.rawQuery(sql, null);
            DealerInfo d;
            d = new DealerInfo("", locationId, "Please select a service center", "");
            dList.add(d);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {
                    String userID = cursor.getString(cursor.getColumnIndex("UserID"));
                    String userName = cursor.getString(cursor.getColumnIndex("UserName"));
                    String locationID = cursor.getString(cursor.getColumnIndex("LocationID"));
                    String locationName = cursor.getString(cursor.getColumnIndex("LocationName"));
                    String showroomName = cursor.getString(cursor.getColumnIndex("ShowroomName"));
                    String address = cursor.getString(cursor.getColumnIndex("Address"));
                    String contactNo = cursor.getString(cursor.getColumnIndex("ContactNo"));
                    String hotLine = cursor.getString(cursor.getColumnIndex("HotLine"));
                    String email = cursor.getString(cursor.getColumnIndex("Email"));
                    d = new DealerInfo(userID, userName, locationID, locationName, showroomName,
                            address, contactNo, hotLine, email);
                    dList.add(d);

                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.close();
        }
        return dList;
    }
}


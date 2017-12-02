package cn.hwwwwh.lexiangdaxue.LoginRegister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import cn.hwwwwh.lexiangdaxue.other.RxBus;

/**
 * Created by 97481 on 2016/10/13.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    //database version
    private static final int DATABASE_VERSION=1;
    //database name
    private static final String DATABASE_NAME="android_api";
    // login table name
    private static final String TABLE_USER="user";
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //空格
        String CREATE_LOGIN_TABLE="create table "+TABLE_USER+"("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_NAME+" TEXT,"+KEY_EMAIL+" TEXT UNIQUE,"+KEY_UID+" TEXT,"+KEY_CREATED_AT+" TEXT,"+"token"+" TEXT"+")";
        db.execSQL(CREATE_LOGIN_TABLE);
        //学校数据库
        String Create_university_Table="create table university (uu_id INTEGER PRIMARY KEY ,user_uuid TEXT UNIQUE,uu_province TEXT," +
                "uu_city TEXT,uu_name TEXT,university_id TEXT )";
        Log.d("testlexian",CREATE_LOGIN_TABLE+"   "+Create_university_Table);
        db.execSQL(Create_university_Table);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }
    /**
     * storeing user details in database
     */
    public void addUser(String name,String email,String uid,String created_at,String token){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,name);//name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put("token",token);
        long id=db.insert(TABLE_USER,null,values);
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    /**
     * Getting user data from datebase
     */
    public HashMap<String,String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
            user.put("token", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public void updateUserNN(String name,String uid){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        ContentValues values = new ContentValues();
        values.put("name",name);
        db.update("user",values,"uid=?",new String[]{uid});
        db.close();
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void addUniversity(String user_uuid,String uu_province,String uu_city,String uu_name,String university_id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("user_uuid",user_uuid);
        values.put("uu_province", uu_province);
        values.put("uu_city", uu_city);
        values.put("uu_name", uu_name);
        values.put("university_id", university_id);

        long id=db.insert("university",null,values);
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void deleteUniversity() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete("university", null, null);
        RxBus.getIntanceBus().post("绑定所在学校");
        db.close();
    }

    public void updateUniversity(String user_uuid,String uu_province,String uu_city,String uu_name,String university_id){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        ContentValues values = new ContentValues();
        values.put("user_uuid",user_uuid);
        values.put("uu_province", uu_province);
        values.put("uu_city", uu_city);
        values.put("uu_name", uu_name);
        values.put("university_id", university_id);
        db.update("university",values,"user_uuid=?",new String[]{user_uuid});
        db.close();
    }
    /**
     * Getting bind university data from datebase
     */
    public HashMap<String,String> getUniDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM university";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("user_uuid", cursor.getString(1));
            user.put("uu_province", cursor.getString(2));
            user.put("uu_city", cursor.getString(3));
            user.put("uu_name", cursor.getString(4));
            user.put("university_id", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }




}

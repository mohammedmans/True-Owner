package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maged on 6/17/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TrueOwner.db";
    public static final String TABLE_NAME = "user";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table  IF NOT EXISTS user " +
                "(id integer primary key, name TEXT,password TEXT,phone TEXT,email TEXT,Image TEXT)");
        db.execSQL("create table  IF NOT EXISTS logs " +
                "(id integer primary key,timeStamp TEXT, typingSpeed integer ,typingError INTEGER, Image TEXT,faceResult INTEGER,fingerPrintResult INTEGER,location TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TYPE_SPEED ( SPEED FLOAT , ERROR FLOAT )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertUser (String name, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("password",password);

        db.insert("user", null, contentValues);
        return true;
    }
    public boolean insertLogs (String timeStamp, int faceResult, int fingerPrintResult,double  typingSpeed,double typingError ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //  Image blob,fingerPrintResult INTEGER,location TEXT
        contentValues.put("timeStamp",timeStamp);
        contentValues.put("typingSpeed",typingSpeed);
        contentValues.put("typingError", typingError);
        contentValues.put("faceResult",faceResult);
        contentValues.put("typingSpeed", typingSpeed);
        contentValues.put("typingError", typingError);
        db.insert("logs", null, contentValues);
        return true;
    }

    public Boolean updateUser(String name, String phone, String email, String password){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues EditUser = new ContentValues();
        EditUser.put("name",name);
        EditUser.put("phone",phone);
        EditUser.put("email",email);
        EditUser.put("password",password);
        db.update(TABLE_NAME, EditUser, "id="+1, null);
        return true;
    }
    public Cursor getDataUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user where id="+id+"", null );
        return res;
    }
    public Cursor getDataLogs(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user where id="+id+"", null );
        return res;
    }

    public int numberOfRows(String tableName){
        int numRows=0;
        SQLiteDatabase db = this.getReadableDatabase();
        numRows = (int) DatabaseUtils.queryNumEntries(db, tableName);
        return numRows;
    }

    public void deleteUser () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+"user");
    }

    public void insertTypeSpeed(double typeSpeed,double typeError){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues  = new ContentValues();
        contentValues.put("SPEED",typeSpeed);
        contentValues.put("ERROR",typeError);

        db.insert("TYPE_SPEED",null,contentValues);



    }
    public boolean querySpeed(double typeSpeed){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor query = db.query(false,"TYPE_SPEED",new String[]{"SPEED"},null,null,null,null,null,null);
        double Max =  -99 , Min = 100000 ;
        while(query.moveToNext()){
            Max = Math.max(query.getDouble(query.getColumnIndex("SPEED")),Max);
            Min = Math.min(query.getDouble(query.getColumnIndex("SPEED")),Min);
        }
        if(typeSpeed >= Min && typeSpeed <= Max)
            return true ;
        else return  false ;
    }

    public Boolean updateLogs(int id,String colName, String value){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues EditLogs = new ContentValues();
        EditLogs.put(colName,value);
        db.update("logs",EditLogs, "id="+id, null);
        return true;
    }



   /* public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }*/
}
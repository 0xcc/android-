package mike.http1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 16-5-1.
 */
public class HerosDB extends SQLiteOpenHelper {

    public   static  final String DATABASE_NAME="HEROS.db";
    private static final  int DATABASE_VERSION=9;

    private static final  String TABLE_NAME="heros_table";

    private static final String HERO_ID="hero_id";
    private static final String HERO_NAME="hero_name";
    private static final String HERO_WEAPON="hero_weapon";
    private static final String HERO_ATTACK="hero_attack";
    private static final String HERO_DEFENCE="hero_defence";


    public HerosDB(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+TABLE_NAME+" ("+HERO_ID+" INTEGER primary key autoincrement,"+HERO_NAME+" text,"
                +HERO_WEAPON+" text,"+HERO_ATTACK+" text,"+HERO_DEFENCE+" text);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists "+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_NAME, null, null, null, null, null, null, null);
        String name1=cursor.getColumnName(0);
        name1=cursor.getColumnName(1);
        name1=cursor.getColumnName(2);
        name1=cursor.getColumnName(3);
        name1=cursor.getColumnName(4);

        return  cursor;
    }

    public long insert(String name,String weapon,String attack,String defence){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(HERO_NAME,name);
        contentValues.put(HERO_WEAPON,weapon);
        contentValues.put(HERO_ATTACK,attack);
        contentValues.put(HERO_DEFENCE,defence);

        long row=db.insert(TABLE_NAME, null, contentValues);

        return row;
    }

    public void delete(int id){
        SQLiteDatabase db=getWritableDatabase();
        String where=HERO_ID+" =?";
        String[] whereValue={Integer.toString(id)};
        db.delete(TABLE_NAME,where,whereValue);
    }

    public void update(int id,String name,String weapon,String attack,String defence){

        String where=HERO_ID+" =?";
        String[] whereValue={Integer.toString(id)};

        ContentValues contentValues=new ContentValues();
        contentValues.put(HERO_NAME,name);
        contentValues.put(HERO_WEAPON,weapon);
        contentValues.put(HERO_ATTACK,attack);
        contentValues.put(HERO_DEFENCE,defence);

        SQLiteDatabase db=getWritableDatabase();
        db.update(TABLE_NAME,contentValues,where,whereValue);
    }

}

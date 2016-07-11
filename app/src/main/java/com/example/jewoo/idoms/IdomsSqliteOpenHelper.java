package com.example.jewoo.idoms;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jewoo on 2016. 6. 6..
 */
public class IdomsSqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_TABLENAME="idoms";
    private static final String DB_NAME = "StudyDatabase.db";
    private static final String PACKAGE_DIR = "/data/data/com.example.jewoo.idoms/databases/";
    public IdomsSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        initialize(context);
    }

    public static void initialize(Context ctx){
        File folder = new File(PACKAGE_DIR);
        folder.mkdirs();

        File outfile = new File(PACKAGE_DIR+DB_NAME);
        if(outfile.length()<=0) {
            AssetManager assetManager = ctx.getResources().getAssets();
            try {
                InputStream is = assetManager.open("databases/"+DB_NAME, AssetManager.ACCESS_BUFFER);
                long fileSize = is.available();
                byte[] tempData = new byte[(int) fileSize];
                is.read(tempData);
                is.close();
                outfile.createNewFile();
                FileOutputStream fo = new FileOutputStream(outfile);
                fo.write(tempData);
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 최초에 데이터베이스가 없을경우, 데이터베이스 생성을 위해 호출됨
        // 테이블 생성하는 코드를 작성한다
        String sql = "create table if not exists "+DB_TABLENAME+" (_id integer primary key autoincrement, english text, meaning text, lesson integer, level integer);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스의 버전이 바뀌었을 때 호출되는 콜백 메서드
        // 버전 바뀌었을 때 기존데이터베이스를 어떻게 변경할 것인지 작성한다
        // 각 버전의 변경 내용들을 버전마다 작성해야함
        String sql = "drop table "+DB_TABLENAME+";"; // 테이블 드랍
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성

    }
}

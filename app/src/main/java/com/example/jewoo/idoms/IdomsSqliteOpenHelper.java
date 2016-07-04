package com.example.jewoo.idoms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jewoo on 2016. 6. 6..
 */
public class IdomsSqliteOpenHelper extends SQLiteOpenHelper {

    private static final String SQLITE_TABLENAME="idoms";

    public IdomsSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 최초에 데이터베이스가 없을경우, 데이터베이스 생성을 위해 호출됨
        // 테이블 생성하는 코드를 작성한다
        String sql = "create table if not exists idoms (_id integer primary key autoincrement, english text, meaning text, lesson integer, level integer);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스의 버전이 바뀌었을 때 호출되는 콜백 메서드
        // 버전 바뀌었을 때 기존데이터베이스를 어떻게 변경할 것인지 작성한다
        // 각 버전의 변경 내용들을 버전마다 작성해야함
        String sql = "drop table idoms;"; // 테이블 드랍
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성

    }
}

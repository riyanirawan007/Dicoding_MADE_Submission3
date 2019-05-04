package app.android.rynz.kamusku.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static app.android.rynz.kamusku.data.db.DBContract.DB_NAME;
import static app.android.rynz.kamusku.data.db.DBContract.DB_VERSION;
import static app.android.rynz.kamusku.data.db.DBContract.SQL_CREATE_TABLE_ENG_IND;
import static app.android.rynz.kamusku.data.db.DBContract.SQL_CREATE_TABLE_IND_ENG;
import static app.android.rynz.kamusku.data.db.DBContract.SQL_DROP_TABLES;


public class SQLiteDBHelper extends SQLiteOpenHelper
{
    public SQLiteDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ENG_IND);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_IND_ENG);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(SQL_DROP_TABLES);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}

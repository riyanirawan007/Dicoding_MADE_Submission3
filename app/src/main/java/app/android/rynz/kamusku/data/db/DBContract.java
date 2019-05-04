package app.android.rynz.kamusku.data.db;

import android.provider.BaseColumns;

public class DBContract
{
    //Preventing from initialize
    private DBContract() {}

    public static final String DB_NAME="Dictionary.db";
    public static final int DB_VERSION=1;

    public static final String TABLE_NAME_END_IND ="dictionary_eng_ind";
    public static final String TABLE_NAME_IND_ENG ="dictionary_ind_eng";

    public static final String SQL_CREATE_TABLE_ENG_IND="CREATE TABLE "+ TABLE_NAME_END_IND
            +" ("+ Columns.COLUMN_ID+" "+ Columns.TYPE_ID
            +", "+ Columns.COLUMN_KEYWORD+" "+ Columns.TYPE_KEYWORD
            +", "+ Columns.COLUMN_DESCRIPTION+" "+ Columns.TYPE_DESCRIPTION
            +")";
    public static final String SQL_CREATE_TABLE_IND_ENG="CREATE TABLE "+ TABLE_NAME_IND_ENG
            +" ("+ Columns.COLUMN_ID+" "+ Columns.TYPE_ID
            +", "+ Columns.COLUMN_KEYWORD+" "+ Columns.TYPE_KEYWORD
            +", "+ Columns.COLUMN_DESCRIPTION+" "+ Columns.TYPE_DESCRIPTION
            +")";

    public static final String SQL_DROP_TABLES="DROP TABLE IF EXISTS "+TABLE_NAME_END_IND
            +" DROP TABLE IF EXISTS "+TABLE_NAME_IND_ENG;

    public static final class Columns implements BaseColumns
    {
        public static final String COLUMN_ID ="_id";
        public static final String COLUMN_KEYWORD="keyword";
        public static final String COLUMN_DESCRIPTION="description";

        static final String TYPE_ID="integer primary key autoincrement";
        static final String TYPE_KEYWORD="text";
        static final String TYPE_DESCRIPTION="text";
    }
}


package app.android.rynz.kamusku.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import app.android.rynz.kamusku.R;
import app.android.rynz.kamusku.data.model.DictionaryModel;
import app.android.rynz.kamusku.helper.SQLiteDBHelper;
import app.android.rynz.kamusku.view.interfaces.DictionaryServices;

import static app.android.rynz.kamusku.data.db.DBContract.Columns;
import static app.android.rynz.kamusku.data.db.DBContract.TABLE_NAME_END_IND;
import static app.android.rynz.kamusku.data.db.DBContract.TABLE_NAME_IND_ENG;

public class DictionaryRepository
{
    private SQLiteDBHelper sqLiteDBHelper;

    public DictionaryRepository(@NonNull Context context)
    {
        this.sqLiteDBHelper=new SQLiteDBHelper(context);
    }

    //make static method prevent for memory leaks occur
    public static void preLoadDictionary(final @NonNull Context context, final @Nullable DictionaryServices.preLoad listener)
    {
        class doPreLoad extends AsyncTask<Void, Integer, ArrayList<DictionaryModel>>
        {
            private ArrayList<DictionaryModel> items =new ArrayList<>();
            private ArrayList<DictionaryModel> itemEngToInd =new ArrayList<>();
            private ArrayList<DictionaryModel> itemIndToEng =new ArrayList<>();
            private String line=null;
            private BufferedReader reader;
            private int totalData=0;
            private SQLiteDatabase sqLiteDatabase;
            private SQLiteDBHelper sqLiteDBHelper;
            private ContentValues values=new ContentValues();
            private boolean isLoadFail=false;
            private String errorMsg;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                sqLiteDBHelper=new SQLiteDBHelper(context);
                if(listener!=null)listener.onStart();
            }

            @Override
            protected ArrayList<DictionaryModel> doInBackground(Void... voids)
            {
                try
                {
                    InputStream rawIndEng=context.getResources().openRawResource(R.raw.indonesia_english);
                    reader=new BufferedReader(new InputStreamReader(rawIndEng));
                    do{
                        line=reader.readLine();
                        if(line!=null)
                        {
                            String[] dataAttr=line.split("\t");
                            DictionaryModel model=new DictionaryModel(dataAttr[0],dataAttr[1]);
                            model.setTranslateType(TABLE_NAME_IND_ENG);
                            itemIndToEng.add(model);
                            items.add(model);
                        }
                    }while (line != null);

                    line=null;
                    InputStream rawEngInd=context.getResources().openRawResource(R.raw.english_indonesia);
                    reader=new BufferedReader(new InputStreamReader(rawEngInd));
                    do{
                        line=reader.readLine();
                        if(line!=null)
                        {
                            String[] dataAttr=line.split("\t");
                            DictionaryModel model=new DictionaryModel(dataAttr[0],dataAttr[1]);
                            model.setTranslateType(TABLE_NAME_END_IND);
                            itemEngToInd.add(model);
                            items.add(model);
                        }
                    }while (line!=null);
                    reader.close();

                    sqLiteDatabase=sqLiteDBHelper.getWritableDatabase();
                    sqLiteDatabase.beginTransaction();
                    totalData=0;
                    for(DictionaryModel item: itemIndToEng)
                    {
                        values.put(Columns.COLUMN_KEYWORD,item.getKeyword());
                        values.put(Columns.COLUMN_DESCRIPTION,item.getDescription());
                        sqLiteDatabase.insert(TABLE_NAME_IND_ENG,null,values);
                        values.clear();
                        totalData+=1;
                        publishProgress(totalData);
                    }
                    for(DictionaryModel item: itemEngToInd)
                    {
                        values.put(Columns.COLUMN_KEYWORD,item.getKeyword());
                        values.put(Columns.COLUMN_DESCRIPTION,item.getDescription());
                        sqLiteDatabase.insert(TABLE_NAME_END_IND,null,values);
                        values.clear();
                        totalData+=1;
                        publishProgress(totalData);
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                    sqLiteDatabase.endTransaction();
                    sqLiteDBHelper.close();

                    itemEngToInd.clear();
                    itemIndToEng.clear();
                    isLoadFail=false;

                }catch (Exception e)
                {
                    e.printStackTrace();
                    isLoadFail=true;
                    errorMsg=e.getMessage();
                }
                return items;
            }

            @Override
            protected void onProgressUpdate(Integer... values)
            {
                super.onProgressUpdate(values);
                if(listener!=null) listener.onLoading(items.size(),values[0], items.get(values[0]-1));
            }

            @Override
            protected void onPostExecute(ArrayList<DictionaryModel> dictionaryModels)
            {
                super.onPostExecute(dictionaryModels);
                if(listener!=null) listener.onFinished(isLoadFail,errorMsg,dictionaryModels);
            }
        }
        new doPreLoad().execute();
    }
    public ArrayList<DictionaryModel> searchDictionary(@NonNull String fromTable,@NonNull String keyword)
    {
        ArrayList<DictionaryModel> items=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        String[] columns={Columns.COLUMN_ID,Columns.COLUMN_KEYWORD,Columns.COLUMN_DESCRIPTION};
        String selection=Columns.COLUMN_KEYWORD + " like ?";
        String[] selectionArgs={"%"+keyword+"%"};
        String orderBy=Columns.COLUMN_KEYWORD+" ASC";
        Cursor cursor= sqLiteDatabase.query(fromTable,columns,selection,selectionArgs,null,null,orderBy);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
        {
            do
            {
                int id=cursor.getInt(cursor.getColumnIndex(Columns.COLUMN_ID));
                String key=cursor.getString(cursor.getColumnIndex(Columns.COLUMN_KEYWORD));
                String desc=cursor.getString(cursor.getColumnIndex(Columns.COLUMN_DESCRIPTION));
                DictionaryModel item=new DictionaryModel(key,desc);
                item.setId(id);
                items.add(item);
                cursor.moveToNext();

            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return items;
    }

}


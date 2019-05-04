package app.android.rynz.kamusku.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static app.android.rynz.kamusku.data.db.DBContract.TABLE_NAME_IND_ENG;


public class PreferenceHelper
{
    private static final String PREFERENCE_NAME="preference_name";
    private static final String PREFERENCE_FIRST_RUN="first_run";
    private static final String PREFERENCE_LAST_MODE="last_dictionary_mode";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferenceHelper(Context context)
    {
        preferences=context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public void setFirstRun(boolean isFirstRun)
    {
        editor=preferences.edit();
        editor.putBoolean(PREFERENCE_FIRST_RUN,isFirstRun);
        editor.apply();
    }

    public boolean isFirstRun()
    {
        return preferences.getBoolean(PREFERENCE_FIRST_RUN,true);
    }

    public void setLastDictionaryMode(@NonNull String dictionaryMode)
    {
        editor=preferences.edit();
        editor.putString(PREFERENCE_LAST_MODE,dictionaryMode);
        editor.apply();
    }

    public String getLastMode()
    {
        return preferences.getString(PREFERENCE_LAST_MODE, TABLE_NAME_IND_ENG);
    }
}

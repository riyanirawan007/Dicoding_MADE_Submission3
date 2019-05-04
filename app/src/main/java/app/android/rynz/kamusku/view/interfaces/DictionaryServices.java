package app.android.rynz.kamusku.view.interfaces;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import app.android.rynz.kamusku.data.model.DictionaryModel;

public interface DictionaryServices
{
    interface preLoad
    {
        void onStart();

        void onLoading(int totalData, int currentDataIndex, @NonNull DictionaryModel currentItem);

        void onFinished(boolean isLoadFailed, @Nullable String errMsg, @Nullable ArrayList<DictionaryModel> dictionaries);
    }
}
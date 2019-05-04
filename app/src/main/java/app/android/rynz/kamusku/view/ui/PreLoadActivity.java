package app.android.rynz.kamusku.view.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.android.rynz.kamusku.R;
import app.android.rynz.kamusku.data.model.DictionaryModel;
import app.android.rynz.kamusku.data.repository.DictionaryRepository;
import app.android.rynz.kamusku.helper.PreferenceHelper;
import app.android.rynz.kamusku.view.interfaces.DictionaryServices;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PreLoadActivity extends AppCompatActivity
{
    @BindView(R.id.pbar_preload)
    ProgressBar progressBar;
    @BindView(R.id.tv_progress)
    TextView tvProgress;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pre_load);
        ButterKnife.bind(this);
        preferenceHelper = new PreferenceHelper(this);
        checkNeedPreLoad();

    }

    private void checkNeedPreLoad()
    {
        if (preferenceHelper.isFirstRun())
        {
            DictionaryRepository.preLoadDictionary(this, new DictionaryServices.preLoad()
            {
                @Override
                public void onStart()
                {
                    if (Build.VERSION.SDK_INT > 24)
                    {
                        progressBar.setProgress(0, true);
                        tvProgress.setText(R.string.label_loading);
                    } else
                    {
                        progressBar.setProgress(0);
                        tvProgress.setText(R.string.label_loading);
                    }
                }

                @Override
                public void onLoading(int totalData, int currentDataIndex, @NonNull DictionaryModel currentItem)
                {
                    int percentage = Math.round((currentDataIndex * 100) / totalData);
                    if (Build.VERSION.SDK_INT > 24)
                    {
                        progressBar.setProgress(percentage, true);
                    } else
                    {
                        progressBar.setProgress(percentage);
                    }
                }

                @Override
                public void onFinished(boolean isLoadFailed, @Nullable String errMsg, @Nullable ArrayList<DictionaryModel> dictionaries)
                {
                    if (!isLoadFailed)
                    {
                        tvProgress.setText(R.string.label_finished);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                preferenceHelper.setFirstRun(false);
                                Intent main = new Intent(PreLoadActivity.this, MainActivity.class);
                                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                            }
                        }, 3000);
                    }
                }
            });
        } else
        {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }
    }
}

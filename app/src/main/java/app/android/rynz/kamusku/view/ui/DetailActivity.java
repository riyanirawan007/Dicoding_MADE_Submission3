package app.android.rynz.kamusku.view.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import app.android.rynz.kamusku.R;
import app.android.rynz.kamusku.data.model.DictionaryModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
{
    @BindView(R.id.detail_keyword)
    TextView key;
    @BindView(R.id.detail_desc)
    TextView desc;

    private String keyword, description;
    private final static String BUNDLE_KEYWORD = "keyword";
    private final static String BUNDLE_DESC = "desc";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null)
        {
            DictionaryModel item = getIntent().getParcelableExtra(DictionaryModel.KEY_EXTRA_PARCEL);
            keyword = item.getKeyword();
            description = item.getDescription();
            setDictionary();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEYWORD, keyword);
        outState.putString(BUNDLE_DESC, description);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            keyword = savedInstanceState.getString(BUNDLE_KEYWORD);
            description = savedInstanceState.getString(BUNDLE_DESC);
            setDictionary();
        }
    }

    private void setDictionary()
    {
        key.setText(keyword);
        desc.setText(description);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(keyword);
    }
}

package app.android.rynz.kamusku.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.android.rynz.kamusku.R;
import app.android.rynz.kamusku.adapter.TranslateListAdapter;
import app.android.rynz.kamusku.data.db.DBContract;
import app.android.rynz.kamusku.data.model.DictionaryModel;
import app.android.rynz.kamusku.data.repository.DictionaryRepository;
import app.android.rynz.kamusku.helper.PreferenceHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.lv_translate_result)
    ListView listView;
    @BindView(R.id.tv_dictionary_type)
    TextView tvDictionaryType;

    private DictionaryRepository repository;
    private String dictionaryType, keyword;
    private PreferenceHelper preferenceHelper;
    private final static String BUNDLE_TYPE = "dictionary_type";
    private final static String BUNDLE_KEYWORDS = "keyword";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        repository = new DictionaryRepository(this);
        preferenceHelper = new PreferenceHelper(this);
        dictionaryType = preferenceHelper.getLastMode();
        updateDictionaryMode(dictionaryType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.search_translate);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                if (!TextUtils.isEmpty(s))
                {
                    keyword = s;
                    renderTranslateResult(repository.searchDictionary(dictionaryType, keyword));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener()
        {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem)
            {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem)
            {
                updateDictionaryMode(dictionaryType);
                return true;
            }
        });
        if (keyword != null)
        {
            searchItem.expandActionView();
            searchView.setQuery(keyword, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_ind_eng:
            {
                updateDictionaryMode(DBContract.TABLE_NAME_IND_ENG);
                break;
            }
            case R.id.menu_eng_ind:
            {
                updateDictionaryMode(DBContract.TABLE_NAME_END_IND);
                break;
            }
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_TYPE, dictionaryType);
        outState.putString(BUNDLE_KEYWORDS, keyword);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            updateDictionaryMode(savedInstanceState.getString(BUNDLE_TYPE));
            keyword = savedInstanceState.getString(BUNDLE_KEYWORDS);
        }
    }

    private void renderTranslateResult(@NonNull final ArrayList<DictionaryModel> items)
    {
        if (items.size() > 0)
        {
            tvDictionaryType.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            ArrayAdapter arrayAdapter = new TranslateListAdapter(this, items);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    DictionaryModel item = new DictionaryModel(items.get(i).getKeyword(), items.get(i).getDescription());
                    Intent detail = new Intent(MainActivity.this, DetailActivity.class);
                    detail.putExtra(DictionaryModel.KEY_EXTRA_PARCEL, item);
                    startActivity(detail);
                }
            });
            arrayAdapter.notifyDataSetChanged();
        } else
        {
            listView.setVisibility(View.GONE);
            if (listView.getAdapter() != null) listView.setAdapter(null);
            tvDictionaryType.setVisibility(View.VISIBLE);
        }
    }

    private void updateDictionaryMode(@Nullable String type)
    {
        String title = getString(R.string.app_name);
        String caption = title;
        if (listView.getAdapter() != null) listView.setAdapter(null);
        if (type != null)
        {
            switch (type)
            {
                case DBContract.TABLE_NAME_IND_ENG:
                    title += " Ind -> Eng";
                    caption += " Indonesia -> English";
                    break;
                case DBContract.TABLE_NAME_END_IND:
                    title += " Eng -> Ind";
                    caption += " English -> Indonesia";
                    break;
                default:
                    title = getString(R.string.app_name);
                    caption = title;
                    break;
            }
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
            preferenceHelper.setLastDictionaryMode(type);
            dictionaryType = type;
            tvDictionaryType.setText(caption);
            listView.setVisibility(View.GONE);
            tvDictionaryType.setVisibility(View.VISIBLE);
        } else
        {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
            preferenceHelper.setLastDictionaryMode(DBContract.TABLE_NAME_IND_ENG);
            dictionaryType = DBContract.TABLE_NAME_IND_ENG;
        }

    }
}

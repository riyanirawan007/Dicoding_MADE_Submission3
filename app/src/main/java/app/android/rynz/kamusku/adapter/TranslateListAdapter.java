package app.android.rynz.kamusku.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.android.rynz.kamusku.R;
import app.android.rynz.kamusku.data.model.DictionaryModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TranslateListAdapter extends ArrayAdapter<DictionaryModel>
{
    private Context context;
    private ArrayList<DictionaryModel> items;
    public TranslateListAdapter(@NonNull Context context,@NonNull ArrayList<DictionaryModel> items)
    {
        super(context,android.R.layout.simple_list_item_2, items);
        this.context=context;
        this.items=items;
    }

    public static class Holder{
        @BindView(android.R.id.text1)
        TextView key;
        @BindView(android.R.id.text2)
        TextView desc;
        Holder(View v)
        {
            ButterKnife.bind(this,v);
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v=convertView;
        final Holder holder;
        LayoutInflater inflater=LayoutInflater.from(context);
        if(v==null)
        {
            v= inflater.inflate(android.R.layout.simple_list_item_2,parent,false);
            holder=new Holder(v);
            v.setTag(holder);
        }
        else
        {
            holder=(Holder) v.getTag();
        }

        DictionaryModel item=items.get(position);

        holder.key.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        holder.key.setTypeface(holder.key.getTypeface(), Typeface.BOLD);
        holder.key.setText(item.getKeyword());
        String snipDesc=item.getDescription();
        if(snipDesc.length()>50)
        {
            snipDesc=snipDesc.substring(0,50).concat("...");
        }
        holder.desc.setText(snipDesc);

        return v;
    }
}


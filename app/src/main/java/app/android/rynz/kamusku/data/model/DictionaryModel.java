package app.android.rynz.kamusku.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DictionaryModel implements Parcelable
{
    private int id;
    private String keyword,description,translateType;
    public final static String KEY_EXTRA_PARCEL="dictionary_model";

    public DictionaryModel(String keyword, String description)
    {
        this.keyword = keyword;
        this.description = description;
    }

    public void setTranslateType(String translateType)
    {
        this.translateType = translateType;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public String getDescription()
    {
        return description;
    }

    public String getTranslateType()
    {
        return translateType;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeString(this.keyword);
        dest.writeString(this.description);
        dest.writeString(this.translateType);
    }

    protected DictionaryModel(Parcel in)
    {
        this.id = in.readInt();
        this.keyword = in.readString();
        this.description = in.readString();
        this.translateType = in.readString();
    }

    public static final Creator<DictionaryModel> CREATOR = new Creator<DictionaryModel>()
    {
        @Override
        public DictionaryModel createFromParcel(Parcel source)
        {
            return new DictionaryModel(source);
        }

        @Override
        public DictionaryModel[] newArray(int size)
        {
            return new DictionaryModel[size];
        }
    };
}


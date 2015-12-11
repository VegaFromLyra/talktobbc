package com.handy.fragments.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.handy.fragments.model.Booking;
import com.handy.fragments.model.Playlist;
import com.handy.fragments.model.PlaylistWrapper;

import org.json.JSONObject;

public abstract class TypedHandyRetrofitCallback<T> extends HandyRetrofitCallback
{
    protected static final Gson gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    protected T returnData;

    TypedHandyRetrofitCallback(DataManager.Callback callback)
    {
        super(callback);
    }

    @Override
    public void success(final JSONObject response)
    {
        try
        {
            TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
            returnData = gsonBuilder.fromJson((response.toString()), typeToken.getType());
            if (callback != null)
            {
                callback.onSuccess(returnData);
            }
        } catch (JsonSyntaxException e)
        {
            callback.onError(new DataManager.DataManagerError(DataManager.DataManagerError.Type.SERVER, e.getMessage()));
        }
    }
}

//We need to trick the compiler into holding onto the generic type so we don't lose it to erasure
class BookingHandyRetroFitCallback extends TypedHandyRetrofitCallback<Booking>
{
    BookingHandyRetroFitCallback(DataManager.Callback callback)
    {
        super(callback);
    }
}

class PlaylistRetroFitCallback extends TypedHandyRetrofitCallback<PlaylistWrapper>
{
    PlaylistRetroFitCallback(DataManager.Callback callback)
    {
        super(callback);
    }
}

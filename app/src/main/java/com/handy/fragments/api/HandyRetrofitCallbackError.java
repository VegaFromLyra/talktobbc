package com.handy.fragments.api;

import retrofit.RetrofitError;

public class HandyRetrofitCallbackError extends RuntimeException
{
    public HandyRetrofitCallbackError(DataManager.Callback callback, RetrofitError cause)
    {
        super("FAILED request in " + callback.getClass().getName() + " using the following URL: " + cause.getUrl(), cause);
    }
}

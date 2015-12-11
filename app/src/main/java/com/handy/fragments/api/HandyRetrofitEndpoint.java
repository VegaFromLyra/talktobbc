package com.handy.fragments.api;

import android.content.Context;

import javax.inject.Inject;

import retrofit.Endpoint;

public class HandyRetrofitEndpoint implements Endpoint
{
    private final String apiEndpoint;
    private final String baseUrl;

    @Inject
    public HandyRetrofitEndpoint(Context context)
    {
        //Don't hardcode these
        // baseUrl = "https://s-handybook.hbinternal.com/";
        baseUrl = "http://http://www.bbc.co.uk/";
        //apiEndpoint = "https://s-handy-apip.hbinternal.com/v3/";
        apiEndpoint = "http://www.bbc.co.uk/";
    }

    @Override
    public String getUrl()
    {
        return apiEndpoint;
    }

    @Override
    public String getName()
    {
        return "STAGE";
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }
}

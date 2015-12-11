package com.handy.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.handy.fragments.api.BaseDataManager;
import com.handy.fragments.api.DataManager;
import com.handy.fragments.api.HandyRetrofitEndpoint;
import com.handy.fragments.api.HandyRetrofitService;
import com.handy.fragments.model.Booking;
import com.handy.fragments.model.Playlist;
import com.handy.fragments.model.PlaylistWrapper;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class MainActivity extends AppCompatActivity implements Fragment1.Fragment1Callback
{

    BaseDataManager mBaseDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, new Fragment1());

        if (findViewById(R.id.detailFragmentContainer) != null)
        {
            transaction.replace(R.id.detailFragmentContainer, new Fragment2());
        }

        transaction.commit();

        setupDataManager();
    }

    @Override
    public void button1Clicked()
    {
        Fragment2 fragment2 = (Fragment2) getSupportFragmentManager().findFragmentById(R.id.detailFragmentContainer);
        if (fragment2 == null)
        {
            Toast.makeText(this, "There isn't a fragment 2", Toast.LENGTH_SHORT).show();
        }
        else
        {
            fragment2.changeText("Fragment1's button is clicked");
        }
    }

    @Override
    public void requestDataClicked()
    {
        testDataManager();
    }

    public void testDataManager()
    {
//        String badHardcodedBookingId = "11111"; //HINT: it would be nice if we could read this input from a text field instead of hardcoding it
//        mBaseDataManager.getBookingDetails(badHardcodedBookingId, Booking.BookingType.BOOKING, new DataManager.Callback<Booking>()
//        {
//            @Override
//            public void onSuccess(Booking booking)
//            {
//                System.out.println("We got a booking!");
//                TextView textView = (TextView) findViewById(R.id.bookingDataText);
//                textView.setText(booking.getAddress().getStreetAddress());
//            }
//
//            @Override
//            public void onError(DataManager.DataManagerError error)
//            {
//                System.out.println("We failed! Something went wrong : " + error);
//            }
//        });

        mBaseDataManager.getRadioPlaylist(new DataManager.Callback<PlaylistWrapper>()
        {
            @Override
            public void onSuccess(PlaylistWrapper response)
            {
                System.out.println("We got a playlist");
                TextView textView = (TextView) findViewById(R.id.bookingDataText);

                textView.setText(response.getPlaylist().getPlaylistA().get(0).getTitle());

            }

            @Override
            public void onError(DataManager.DataManagerError error)
            {
                System.out.println("We failed! Something went wrong : " + error);
            }
        });
    }

    private void setupDataManager()
    {
        HandyRetrofitEndpoint handyRetrofitEndpoint = provideHandyEndpoint();
        mBaseDataManager = new BaseDataManager(provideHandyRetrofitService(handyRetrofitEndpoint), handyRetrofitEndpoint);
    }

    final HandyRetrofitEndpoint provideHandyEndpoint()
    {
        return new HandyRetrofitEndpoint(getApplicationContext());
    }

    final HandyRetrofitService provideHandyRetrofitService(final HandyRetrofitEndpoint endpoint)
    {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);

        //Don't actually hardcode these values in it is a bad idea
        final String username = "handybook_app";
        final String password = "dfc195205f1c5eb638fe5e947c0cb55a";

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint)
                .setRequestInterceptor(new RequestInterceptor()
                {
                    final String auth = "Basic " + Base64.encodeToString((username + ":" + password)
                            .getBytes(), Base64.NO_WRAP);

                    @Override
                    public void intercept(RequestFacade request)
                    {
                        //Don't hardcode this either, seriously don't
                        String authToken = "0eb310d58d1ac8d3ea120bf11a0d53bdeea840faa3afa24b2ddc92c341e8eabd0646f258627dd9b8747b20fd8d1717b38aa07b1a3a05e30c1657d7963989b6a8";
                        if (authToken != null)
                        {
                            request.addQueryParam("auth_token", authToken);
                        }
                        request.addHeader("Authorization", auth);
                        request.addHeader("Accept", "application/json");
                        request.addQueryParam("client", "android");
                        request.addQueryParam("app_version", BuildConfig.VERSION_NAME);
                        request.addQueryParam("apiver", "1");
                        request.addQueryParam("app_device_os", Build.VERSION.RELEASE);
                        request.addQueryParam("timezone", TimeZone.getDefault().getID());
                    }
                }).setConverter(new GsonConverter(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create())).setClient(new OkClient(okHttpClient)).build();

        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        return restAdapter.create(HandyRetrofitService.class);
    }


}

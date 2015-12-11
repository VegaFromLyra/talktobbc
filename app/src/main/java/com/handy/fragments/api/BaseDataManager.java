package com.handy.fragments.api;

import com.handy.fragments.model.Booking;
import com.handy.fragments.model.Playlist;
import com.handy.fragments.model.PlaylistWrapper;

public final class BaseDataManager extends DataManager
{
    private final HandyRetrofitService service;
    private final HandyRetrofitEndpoint endpoint;

    public BaseDataManager(final HandyRetrofitService service, final HandyRetrofitEndpoint endpoint)
    {
        this.service = service;
        this.endpoint = endpoint;
    }

    @Override
    public String getBaseUrl()
    {
        return endpoint.getBaseUrl();
    }

    @Override
    public final void getBookingDetails(String bookingId, Booking.BookingType type, final Callback<Booking> cb)
    {
        service.getBookingDetails(bookingId, type.toString().toLowerCase(), new BookingHandyRetroFitCallback(cb));
    }

    @Override
    public void getRadioPlaylist(Callback<PlaylistWrapper> callback)
    {
        service.getRadioPlaylist(new PlaylistRetroFitCallback(callback));
    }
}

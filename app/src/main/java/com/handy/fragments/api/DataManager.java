package com.handy.fragments.api;

import com.handy.fragments.model.Booking;
import com.handy.fragments.model.Playlist;
import com.handy.fragments.model.PlaylistWrapper;

public abstract class DataManager
{
    public abstract void getBookingDetails(String bookingId, Booking.BookingType type, Callback<Booking> cb);

    public abstract void getRadioPlaylist(Callback<PlaylistWrapper> callback);

    public abstract String getBaseUrl();

    public interface Callback<T>
    {
        void onSuccess(T response);

        void onError(DataManagerError error);
    }

    public interface CacheResponse<T>
    {
        void onResponse(T response);
    }

    public static class DataManagerError
    {
        public enum Type
        {
            OTHER, SERVER, CLIENT, NETWORK
        }

        private final Type type;
        private final String message;
        private String[] invalidInputs;

        public DataManagerError(final Type type)
        {
            this.type = type;
            this.message = null;
        }

        public DataManagerError(final Type type, final String message)
        {
            this.type = type;
            this.message = message;
        }

        final String[] getInvalidInputs()
        {
            return invalidInputs;
        }

        public final void setInvalidInputs(final String[] inputs)
        {
            this.invalidInputs = inputs;
        }

        public final String getMessage()
        {
            return message;
        }

        public final Type getType()
        {
            return type;
        }
    }
}

package com.handy.fragments.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Booking implements Comparable<Booking>, Serializable
{
    public enum BookingType
    {
        BOOKING_PROXY,
        BOOKING,;
    }

    @SerializedName("id")
    private String mId;
    @SerializedName("type")
    private String mType;
    @SerializedName("service_name")
    private String mService;
    @SerializedName("service")
    private ServiceInfo mServiceInfo;
    @SerializedName("start_date")
    private Date mStartDate;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("end_date")
    private Date mEndDate;
    @SerializedName("reveal_date")
    private Date mRevealDate;

    @SerializedName("address")
    private Address mAddress;

    public int compareTo(@NonNull Booking other)
    {
        return mStartDate.compareTo(other.mStartDate);
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Booking))
        {
            return false;
        }
        Booking b = (Booking) o;
        return b.mId.equals(mId);
    }

    @Nullable
    public String getStatus()
    {
        return mStatus;
    }

    public String getId()
    {
        return mId;
    }

    public boolean isStarted()
    {
        boolean isStarted = false;
        Date currentTime = Calendar.getInstance().getTime();
        if (getStartDate().compareTo(currentTime) < 0)
        {
            isStarted = true;
        }
        return isStarted;
    }

    public boolean isEnded()
    {
        boolean isEnded = false;
        Date currentTime = Calendar.getInstance().getTime();
        if (getEndDate().compareTo(currentTime) < 0)
        {
            isEnded = true;
        }
        return isEnded;
    }

    public String getService()
    {
        return mService;
    }

    public Date getStartDate()
    {
        return mStartDate;
    }

    public Date getEndDate()
    {
        return mEndDate;
    }

    public Address getAddress()
    {
        return mAddress;
    }


    public ServiceInfo getServiceInfo()
    {
        return mServiceInfo;
    }


    //providerId = 0, no one assigned, can claim, otherwise is already claimed
    public static final String NO_PROVIDER_ASSIGNED = "0";


    public BookingType getType()
    {
        return BookingType.valueOf(mType.toUpperCase());
    }


    public boolean isProxy()
    {
        return getType() == BookingType.BOOKING_PROXY;
    }

    //Basic booking statuses inferrable from mProviderId
    public enum BookingStatus
    {
        AVAILABLE,
        CLAIMED,
        UNAVAILABLE,
    }

    public static class User implements Serializable
    {
        @SerializedName("email")
        private String mEmail;
        @SerializedName("first_name")
        private String mFirstName;
        @SerializedName("last_name")
        private String mLastName;

        public String getEmail()
        {
            return mEmail;
        }

        public String getFirstName()
        {
            return mFirstName;
        }

        public String getLastName()
        {
            return mLastName;
        }

        public String getAbbreviatedName()
        {
            return mFirstName + (mLastName.isEmpty() ? "" : " " + mLastName.charAt(0) + ".");
        }

        public String getFullName()
        {
            return mFirstName + " " + mLastName;
        }
    }

    public static class BookingInstruction implements Serializable
    {
        @SerializedName("description")
        private String mDescription;
        @SerializedName("machine_name")
        private String mMachineName;

        public String getDescription()
        {
            return mDescription;
        }

        public String getMachineName()
        {
            return mMachineName;
        }
    }

    public static class BookingInstructionGroup implements Serializable
    {
        public static String GROUP_ENTRY_METHOD = "entry_method";
        public static String GROUP_LINENS_LAUNDRY = "linens_laundry";
        public static String GROUP_REFRIGERATOR = "refrigerator";
        public static String GROUP_TRASH = "trash";
        public static String GROUP_NOTE_TO_PRO = "note_to_pro";
        public static String OTHER = "other";

        @SerializedName("group")
        private String mGroup;
        @SerializedName("label")
        private String mLabel;
        @SerializedName("items")
        private List<String> mItems;

        public String getGroup()
        {
            return mGroup;
        }

        public String getLabel()
        {
            return mLabel;
        }

        public List<String> getItems()
        {
            return mItems;
        }
    }

    public static class CheckInSummary implements Serializable
    {
        @SerializedName("is_checked_in")
        private boolean mIsCheckedIn; //false if checked out or on my way

        @SerializedName("time")
        private Date mCheckInTime;

        public Date getCheckInTime()
        {
            return mCheckInTime;
        }

        public boolean isCheckedIn()
        {
            return mIsCheckedIn;
        }
    }

    public static class ServiceInfo implements Serializable
    {
        private static final String MACHINE_NAME_CLEANING = "home_cleaning";

        @SerializedName("machine_name")
        private String mMachineName;
        @SerializedName("name")
        private String mDisplayName;

        public String getMachineName()
        {
            return mMachineName;
        }

        public String getDisplayName()
        {
            return mDisplayName;
        }

        public boolean isHomeCleaning()
        {
            return MACHINE_NAME_CLEANING.equalsIgnoreCase(mMachineName);
        }
    }

    public static class ExtraInfoWrapper implements Serializable
    {
        public ExtraInfo getExtraInfo()
        {
            return mExtraInfo;
        }

        @SerializedName("extra")
        private ExtraInfo mExtraInfo;
        @SerializedName("quantity")
        private int mQuantity;
    }

    public static class ExtraInfo implements Serializable
    {
        //cleaning supplies are in their own mCategory apart from all other extras
        public static final String TYPE_CLEANING_SUPPLIES = "cleaning_supplies";

        @SerializedName("category")
        private String mCategory;
        @SerializedName("fee")
        private String mFee;
        @SerializedName("hours")
        private String mHours;
        @SerializedName("id")
        private int mId;
        @SerializedName("machine_name")
        private String mMachineName;
        @SerializedName("name")
        private String mName;

        public String getCategory()
        {
            return mCategory;
        }

        public String getFee()
        {
            return mFee;
        }

        public String getHours()
        {
            return mHours;
        }

        public int getId()
        {
            return mId;
        }

        public String getMachineName()
        {
            return mMachineName;
        }

        public String getName()
        {
            return mName;
        }
    }

    public static class Coordinates implements Serializable
    {
        @SerializedName("latitude")
        private float mLatitude;
        @SerializedName("longitude")
        private float mLongitude;

        public float getLatitude()
        {
            return mLatitude;
        }

        public float getLongitude()
        {
            return mLongitude;
        }
    }

    public static class ZipCluster implements Serializable
    {
        @SerializedName("zipcluster_id")
        private String mZipClusterId;

        @SerializedName("transit_description")
        private List<String> mTransitDescription;

        @SerializedName("location_description")
        private String mLocationDescription;

        @Nullable
        public List<String> getTransitDescription() { return mTransitDescription; }

        @Nullable
        public String getLocationDescription() { return mLocationDescription; }

        @Nullable
        public String getZipClusterId() { return mZipClusterId; }
    }

}

package com.handy.fragments.api;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class HandyRetrofitCallback implements retrofit.Callback<Response>
{
    protected final DataManager.Callback callback;

    public HandyRetrofitCallback(DataManager.Callback callback)
    {
        this.callback = callback;
    }

    public abstract void success(JSONObject response);

    @Override
    public final void success(final Response response, final Response raw)
    {
        final StringBuilder resp;

        JSONArray objArray = new JSONArray();
        JSONObject obj = new JSONObject();
        Boolean responseIsJSONArray = false;
        try
        {
            final BufferedReader br
                    = new BufferedReader(new InputStreamReader(raw.getBody().in()));

            String line;
            resp = new StringBuilder();
            while ((line = br.readLine()) != null) resp.append(line);

            if (resp.length() > 0)
            {
                responseIsJSONArray = (resp.charAt(0) == '[');
            }

            if (responseIsJSONArray)
            {
                objArray = new JSONArray(resp.toString());
            } else
            {
                obj = new JSONObject(resp.toString());
            }

        } catch (Exception e)
        {
            throw new RuntimeException("Unable to parse API response body : " + e);
        }

        if (responseIsJSONArray)
        {
            //if we got an array response convert it back to an object for now
            //maybe we could support callback params with jsonobj or jsonarray?
            try
            {

                for (int i = 0; i < objArray.length(); i++)
                {
                    obj.put(Integer.toString(i), objArray.getJSONObject(i));
                }

            } catch (Exception e)
            {
                throw new RuntimeException("Unable to convert JSONArray to JSONObject : " + e);
            }
        }

        if (obj.has("error") && (obj.optBoolean("error") || obj.optInt("error") == 1))
        {
            final DataManager.DataManagerError err;
            final JSONArray messages = obj.optJSONArray("messages");

            if (messages != null && messages.length() > 0)
            {
                err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.CLIENT,
                        messages.isNull(0) ? null : messages.optString(0));
            } else
            {
                err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.CLIENT);
            }

            final JSONArray invalidInputs = obj.optJSONArray("invalid_inputs");
            final ArrayList<String> inputs = new ArrayList<>();

            if (invalidInputs != null && invalidInputs.length() > 0)
            {
                for (int i = 0; i < invalidInputs.length(); i++)
                    inputs.add(invalidInputs.optString(i, ""));

                err.setInvalidInputs(inputs.toArray(new String[inputs.size()]));
            }
            if(callback != null)
            {
                callback.onError(err);
            }
        } else
        {
            success(obj);
        }
    }

    @Override
    public final void failure(final RetrofitError error)
    {
        if (callback != null)
        {
            final DataManager.DataManagerError err;
            if (error.isNetworkError())
            {
                err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.NETWORK);
            }
            else
            {
                int resp = 0;
                if (error != null)
                {
                    if (error.getResponse() != null)
                    {
                        resp = error.getResponse().getStatus();
                    }
                }

                if (resp >= 400 && resp <= 500)
                {
                    if (error.getResponse().getBody().mimeType().contains("json"))
                    {
                        RestError restError = (RestError) error.getBodyAs(RestError.class);
                        String[] messages;

                        if (restError.message != null)
                        {
                            err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.CLIENT, restError.message);
                        }
                        else if ((messages = restError.messages) != null && messages.length > 0)
                        {
                            err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.CLIENT, messages[0]);
                        }
                        else
                        {
                            err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.CLIENT);
                        }

                        err.setInvalidInputs(restError.invalidInputs);
                    }
                    else
                    {
                        err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.CLIENT);
                    }
                }
                else if (resp > 500 && resp < 600)
                {
                    err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.SERVER);
                }
                else
                {
                    err = new DataManager.DataManagerError(DataManager.DataManagerError.Type.OTHER);
                }
            }
            callback.onError(err);
        }
    }

    protected final class RestError
    {
        @SerializedName("message")
        private String message;
        @SerializedName("messages")
        private String[] messages;
        @SerializedName("invalid_inputs")
        private String[] invalidInputs;
    }
}

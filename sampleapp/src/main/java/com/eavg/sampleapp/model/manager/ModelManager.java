package com.eavg.sampleapp.model.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.eavg.sampleapp.R;
import com.eavg.sampleapp.model.Goal;
import com.eavg.sampleapp.model.Item;
import com.eavg.sampleapp.model.Items;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Eric on 3/25/14.
 */
public class ModelManager {

    private static ModelManager sInstance;
    private static RequestQueue sRequestQueue;
    private Context mContext;
    private final Gson mGson;

    public RequestQueue getRequestQueue() {
        if (sRequestQueue == null) {
            sRequestQueue = new Volley().newRequestQueue(mContext);
        }
        return sRequestQueue;
    }

    private ModelManager(Context context) {
        mContext = context;
        mGson = new Gson();
    }

    public static ModelManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ModelManager(context);
        }
        return sInstance;
    }

    /**
     * Fetches a single Goal by Id
     * @param goalId
     * @param successListener The callback
     */
    public void fetchSingleGoal(int goalId, ModelListener<Goal> successListener) {
        String modelUrl = String.format(getCompleteUrl(mContext.getString(R.string.goal_path)), goalId);
        GsonRequest<Goal> jsonRequest = new GsonRequest<Goal>(
                Request.Method.GET,
                modelUrl,
                Goal.class, null,
                successListener,
                this.requestErrorListener());
        addRequestIfNoCache(jsonRequest);
    }

    /**
     * Fetches all Items for a Goal by Id
     * @param goalId
     * @param successListener The callback
     */
    public void fetchItemsForGoal(int goalId, ModelListener<Items> successListener) {
        String modelUrl = String.format(getCompleteUrl(mContext.getString(R.string.items_for_goal_path)), goalId);
        GsonRequest<Items> jsonRequest = new GsonRequest<Items>(
                Request.Method.GET,
                modelUrl,
                Items.class, null,
                successListener,
                this.requestErrorListener());
        addRequestIfNoCache(jsonRequest);
    }

    /**
     * Fetches a single Item by Id
     * @param itemId
     * @param successListener The callback
     */
    public void fetchSingleItem(int itemId, ModelListener<Item> successListener) {
        String modelUrl = String.format(getCompleteUrl(mContext.getString(R.string.single_item_path)), itemId);
        Log.d("model url", modelUrl);
        GsonRequest<Item> jsonRequest = new GsonRequest<Item>(
                Request.Method.GET,
                modelUrl,
                Item.class, null,
                successListener,
                this.requestErrorListener());
        addRequestIfNoCache(jsonRequest);
    }

    private void addRequestIfNoCache(GsonRequest request) {
        Cache.Entry cacheEntry = getRequestQueue().getCache().get(request.getUrl());
        if (cacheEntry != null) {
            String json = new String(cacheEntry.data);
            request.getListener().onFetchedData(mGson.fromJson(json, request.getObjectClass()));
        }
        else {
            getRequestQueue().add(request);
        }
    }

    // TODO: This error listener should be handled by the caller so that it can act accordingly
    private Response.ErrorListener requestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "Error Retrieving Items", 5).show();
                Log.e("error", volleyError.getLocalizedMessage());
            }
        };
    }

    /**
     * Helper for getting the full API path
     * @param modelUrl
     * @return The concatenated path
     */
    private String getCompleteUrl(String modelUrl) {
        String url;
        url = mContext.getString(R.string.host_url) + modelUrl;
        return url;
    }

    /**
     * Listener for Model callbacks
     * @param <T>
     */
    public interface ModelListener<T> {
        void onFetchedData(T data);
    }

    /**
     * Request class to parse Json with Gson
     * @param <T> The modal class
     */
    private class GsonRequest<T> extends Request<T> {

        private Class<T> mObjectClass;
        private Type mType;
        private final Map<String, String> mHeaders;
        private final ModelListener<T> mListener;

        public Class<T> getObjectClass() {
            return mObjectClass;
        }

        public ModelListener<T> getListener() {
            return mListener;
        }

        public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                           ModelListener<T> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);

            mObjectClass = clazz;
            mHeaders = headers;
            mListener = listener;
        }
        public GsonRequest(int method, String url, Type type, Map<String, String> headers,
                           ModelListener<T> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);

            this.mType = type;
            this.mHeaders = headers;
            this.mListener = listener;
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return mHeaders != null ? mHeaders : super.getHeaders();
        }

        @Override
        protected void deliverResponse(T response) {
            mListener.onFetchedData(response);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(
                        response.data, HttpHeaderParser.parseCharset(response.headers));
                if (mObjectClass != null) {
                    return Response.success(
                            mGson.fromJson(json, mObjectClass), HttpHeaderParser.parseCacheHeaders(response));
                }
                return (Response<T>) Response.success(
                            mGson.fromJson(json, mType), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }
}

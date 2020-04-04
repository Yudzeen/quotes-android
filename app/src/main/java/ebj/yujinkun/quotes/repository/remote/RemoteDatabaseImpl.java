package ebj.yujinkun.quotes.repository.remote;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ebj.yujinkun.quotes.http.VolleyRequestManager;
import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.util.LogUtil;

import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.API_KEY;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.API_KEY_HEADER;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.APPLICATION_JSON;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.CONTENT_TYPE_HEADER;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.SERVER_URL;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.QUOTE_CONTENT_KEY;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.QUOTE_DATE_CREATED_KEY;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.QUOTE_DATE_MODIFIED_KEY;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.QUOTE_ID_KEY;
import static ebj.yujinkun.quotes.repository.remote.RemoteConstants.QUOTE_QUOTEE_KEY;

public class RemoteDatabaseImpl implements RemoteDatabase {

    private static final String TAG = RemoteDatabaseImpl.class.getSimpleName();

    private static RemoteDatabaseImpl instance;

    private VolleyRequestManager volleyRequestManager;

    private RemoteDatabaseImpl(Application application) {
        volleyRequestManager = VolleyRequestManager.getInstance(application);
    }

    public static RemoteDatabaseImpl getInstance(Application application) {
        if (instance == null) {
            synchronized (RemoteDatabaseImpl.class) {
                if (instance == null) {
                    instance = new RemoteDatabaseImpl(application);
                }
            }
        }
        return instance;
    }

    @Override
    public void getAllQuotes(final GetAllQuotesCallback callback) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SERVER_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Response: " + response);
                        List<Quote> quotes = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Quote quote = new Quote.Builder()
                                        .setId(jsonObject.getString(QUOTE_ID_KEY))
                                        .setContent(jsonObject.getString(QUOTE_CONTENT_KEY))
                                        .setQuotee(jsonObject.getString(QUOTE_QUOTEE_KEY))
                                        .setDateCreated(jsonObject.getString(QUOTE_DATE_CREATED_KEY))
                                        .setDateModified(jsonObject.getString(QUOTE_DATE_MODIFIED_KEY))
                                        .build();
                                quotes.add(quote);
                            } catch (JSONException e) {
                                Log.e(TAG, "Error parsing json", e);
                                callback.onError("Error parsing json.");
                            }
                        }
                        callback.onSuccess(quotes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error);
                        callback.onError("Get all failed.");
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(API_KEY_HEADER, API_KEY);
                return headers;
            }
        };
        LogUtil.logRequest(TAG, "Get all request.", request);
        volleyRequestManager.getRequestQueue().add(request);
    }

    @Override
    public void insert(final Quote quote, final InsertCallback callback) {
        Log.d(TAG, "insert: " + quote.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SERVER_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Quote quoteResponse = new Quote.Builder()
                                    .setId(response.getString(QUOTE_ID_KEY))
                                    .setContent(response.getString(QUOTE_CONTENT_KEY))
                                    .setQuotee(response.getString(QUOTE_QUOTEE_KEY))
                                    .setDateCreated(response.getString(QUOTE_DATE_CREATED_KEY))
                                    .setDateModified(response.getString(QUOTE_DATE_MODIFIED_KEY))
                                    .build();
                            callback.onSuccess(quoteResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing json.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.toString());
                        callback.onError("Insert failed.");
                    }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
                headers.put(API_KEY_HEADER, API_KEY);
                return headers;
            }

            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();
                try {
                    body.put(QUOTE_ID_KEY, quote.getId());
                    body.put(QUOTE_CONTENT_KEY, quote.getContent());
                    body.put(QUOTE_QUOTEE_KEY, quote.getQuotee());
                    body.put(QUOTE_DATE_CREATED_KEY, quote.getDateCreated());
                    body.put(QUOTE_DATE_MODIFIED_KEY, quote.getDateModified());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return body.toString().getBytes();
            }
        };
        LogUtil.logRequest(TAG, "Insert request.", request);
        volleyRequestManager.getRequestQueue().add(request);
    }

    @Override
    public void update(final Quote quote, final UpdateCallback callback) {
        Log.d(TAG, "update: " + quote.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, SERVER_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Quote quoteResponse = new Quote.Builder()
                                    .setId(response.getString(QUOTE_ID_KEY))
                                    .setContent(response.getString(QUOTE_CONTENT_KEY))
                                    .setQuotee(response.getString(QUOTE_QUOTEE_KEY))
                                    .setDateCreated(response.getString(QUOTE_DATE_CREATED_KEY))
                                    .setDateModified(response.getString(QUOTE_DATE_MODIFIED_KEY))
                                    .build();
                            callback.onSuccess(quoteResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing json.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.toString());
                        callback.onError("Update failed.");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
                headers.put(API_KEY_HEADER, API_KEY);
                return headers;
            }

            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();
                try {
                    body.put(QUOTE_ID_KEY, quote.getId());
                    body.put(QUOTE_CONTENT_KEY, quote.getContent());
                    body.put(QUOTE_QUOTEE_KEY, quote.getQuotee());
                    body.put(QUOTE_DATE_CREATED_KEY, quote.getDateCreated());
                    body.put(QUOTE_DATE_MODIFIED_KEY, quote.getDateModified());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return body.toString().getBytes();
            }
        };
        LogUtil.logRequest(TAG, "Update request.", request);
        volleyRequestManager.getRequestQueue().add(request);
    }

    @Override
    public void delete(final Quote quote, final DeleteCallback callback) {
        Log.d(TAG, "delete: " + quote.toString());
        String url = SERVER_URL + "/" + quote.getId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Quote quoteResponse = new Quote.Builder()
                                    .setId(response.getString(QUOTE_ID_KEY))
                                    .setContent(response.getString(QUOTE_CONTENT_KEY))
                                    .setQuotee(response.getString(QUOTE_QUOTEE_KEY))
                                    .setDateCreated(response.getString(QUOTE_DATE_CREATED_KEY))
                                    .setDateModified(response.getString(QUOTE_DATE_MODIFIED_KEY))
                                    .build();
                            callback.onSuccess(quoteResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing json.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode = error.networkResponse.statusCode;
                        String body = new String(error.networkResponse.data);
                        Log.d(TAG, "Error. Status: " + statusCode + " Body:: " + body);
                        callback.onError("Delete failed.");
                    }
        }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put(API_KEY_HEADER, API_KEY);
                    return headers;
                }
        };
        LogUtil.logRequest(TAG, "Delete request.", request);
        volleyRequestManager.getRequestQueue().add(request);
    }

}

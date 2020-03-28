package ebj.yujinkun.quotes.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyRequestManager {

    private static final String TAG = VolleyRequestManager.class.getSimpleName();

    private static VolleyRequestManager instance;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleyRequestManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /**
     * Get singleton instance of VolleyRequestManager
     * @param context Pass application context to prevent memory leaks
     * @return
     */
    public static VolleyRequestManager getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleyRequestManager.class) {
                if (instance == null) {
                    instance = new VolleyRequestManager(context);
                }
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}

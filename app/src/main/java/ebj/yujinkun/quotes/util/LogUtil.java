package ebj.yujinkun.quotes.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

public class LogUtil {

    public static void logRequest(String tag, String desc, Request request) {
        try {
            String headers = request.getHeaders() == null ? "" : request.getHeaders().toString();
            String body = request.getBody() == null ? "" : new String(request.getBody());
            Log.d(tag, desc + " Headers: " + headers + " Body: " + body);
        } catch (AuthFailureError authFailureError) {
            Log.e(tag, authFailureError.toString());
        }
    }
}

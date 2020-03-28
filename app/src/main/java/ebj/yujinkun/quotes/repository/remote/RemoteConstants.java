package ebj.yujinkun.quotes.repository.remote;

import ebj.yujinkun.quotes.BuildConfig;

public class RemoteConstants {

    public static final String SERVER_URL = BuildConfig.SERVER_URL;
    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String API_KEY_HEADER = "x-api-key";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    public static final String APPLICATION_JSON = "application/json";

    public static final String QUOTE_ID_KEY = "id";
    public static final String QUOTE_CONTENT_KEY = "content";
    public static final String QUOTE_QUOTEE_KEY = "quotee";
    public static final String QUOTE_DATE_CREATED_KEY = "dateCreated";
    public static final String QUOTE_DATE_MODIFIED_KEY = "dateModified";

}

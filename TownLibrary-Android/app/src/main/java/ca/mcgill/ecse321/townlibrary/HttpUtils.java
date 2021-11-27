package ca.mcgill.ecse321.townlibrary;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.net.URI;
import java.net.URISyntaxException;

public final class HttpUtils {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private HttpUtils() {

    }

    /**
     * Performs a GET request with the base url prefixed.
     *
     * @param url the endpoint
     * @param params the parameters
     * @param responseHandler the response handler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Performs a POST request with the base url prefixed.
     *
     * @param url the endpoint
     * @param params the parameters
     * @param responseHandler the response handler
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Performs a PUT request with the base url prefixed.
     *
     * @param url the endpoint
     * @param params the parameters
     * @param responseHandler the response handler
     */
    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Prefixes the supplied url with the base url and escapes it!
     *
     * @param relativeUrl the url to be prefixed
     * @return the prefixed url
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        // What does escaping mean?
        // Say you did relativeUrl of "abc def", blindly passing that to the
        // Async client will cause havoc cuz you need to change the space into
        // %20...
        try {
            return new URI("https", null, "townlibrary-backend-321f21-g11.herokuapp.com", 443, relativeUrl, null, null).toString();
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Cannot construct url", ex);
        }
    }
}
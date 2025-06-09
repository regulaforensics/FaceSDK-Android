package com.regula.facepersonsearch;

import android.app.Application;
import android.util.Log;

import com.regula.facesdk.FaceSDK;

// import java.net.HttpURLConnection;
// import java.security.SecureRandom;

// import javax.net.ssl.HostnameVerifier;
// import javax.net.ssl.HttpsURLConnection;
// import javax.net.ssl.SSLContext;
// import javax.net.ssl.SSLSession;
// import javax.net.ssl.TrustManager;
// import javax.net.ssl.X509TrustManager;

public class FaceSearchApp extends Application {

    public static final String TAG = "FaceSearchApp";

    public static final String SERVICE_ADDRESS = "...";

    @Override
    public void onCreate() {
        super.onCreate();

        FaceSDK.Instance().setServiceUrl(SERVICE_ADDRESS);
        FaceSDK.Instance().setNetworkInterceptorListener(request -> {
            // try {
            //     SSLContext sc = SSLContext.getInstance("SSL");
            //     sc.init(null, trustAllCerts, new SecureRandom());
            //     HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            //     HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // } catch (Exception ex) {
            //     ex.printStackTrace();
            // }
            // HttpURLConnection connection = request.getConnection();
            // connection.setConnectTimeout(3000);
            // connection.setReadTimeout(3000);
        });
    }

    // Create a trust manager that does not validate certificate chains
    /* static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };

    static class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }
    } */
}

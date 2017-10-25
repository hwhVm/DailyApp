package beini.com.dailyapp.http;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import beini.com.dailyapp.R;
import beini.com.dailyapp.util.BLog;

/**
 * 默认信任所有的证书
 * 最好加上证书认证，主流App都有自己的证书
 * Created by beini on 2017/10/24.
 */

public class SSLSocketFactoryUtils {
    //
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{createTrustAllManager()}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }
    
    public static class TrustAllHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    //-------------------------------------

    public static SSLSocketFactory createSSLSocketFactory(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        InputStream trustStream = context.getResources().openRawResource(R.raw.key);
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            //获得服务器端证书
            TrustManager[] turstManager = getTurstManager(trustStream);
            sslContext.init(null, turstManager, new SecureRandom());
            //获得sslSocketFactory
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }


    /**
     * 获得指定流中的服务器端证书库
     */

    public static TrustManager[] getTurstManager(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            int index = 0;
            for (InputStream certificate : certificates) {
                if (certificate == null) {
                    continue;
                }
                Certificate certificate1;
                try {
                    certificate1 = certificateFactory.generateCertificate(certificate);
                } finally {
                    certificate.close();
                }

                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate1);
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getTurstAllManager();
    }


    /**
     * 获得信任所有服务器端证书库
     */
    public static TrustManager[] getTurstAllManager() {
        return new X509TrustManager[]{new MyX509TrustManager()};
    }

    public static class MyX509TrustManager implements X509TrustManager {
        /**
         * 该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，因此我们只需要执行默认的信任管理器的这个方法。JSSE中，默认的信任管理器类为TrustManager。
         *
         * @param chain
         * @param authType
         * @throws CertificateException
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        /**
         * 该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。在实现该方法时，也可以简单的不做任何处理，即一个空的函数体，由于不会抛出异常，它就会信任任何证书。
         *
         * @param chain
         * @param authType
         */
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            BLog.e("cert: " + chain[0].toString() + ", authType: " + authType);
        }

        /**
         * 　返回受信任的X509证书数组。
         *
         * @return
         */
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }


    public static X509TrustManager createTrustAllManager() {
        X509TrustManager tm = null;
        try {
            tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    //do nothing，接受任意客户端证书
                }

                /**
                 *
                 * @param chain
                 * @param authType
                 * @throws CertificateException
                 */
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    //do nothing，接受任意服务端证书
                }

                /**
                 *
                 * @return
                 */
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tm;
    }
}
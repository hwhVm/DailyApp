package beini.com.dailyapp.util;

import android.content.Context;

import junit.framework.TestCase;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import beini.com.dailyapp.R;

/**
 * Created by beini on 2017/10/24.
 */
public class CertManager extends TestCase {

    /***
     * 读取*.cer公钥证书文件， 获取公钥证书信息
     * @author xgh
     */
    public static void testReadX509CerFile(Context context) throws Exception {
        try {
            // 读取证书文件
            InputStream inStream = context.getResources().openRawResource(R.raw.tomcat);
            // 创建X509工厂类
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //CertificateFactory cf = CertificateFactory.getInstance("X509");
            // 创建证书对象
            X509Certificate oCert = (X509Certificate) cf
                    .generateCertificate(inStream);
            inStream.close();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String info = null;
            // 获得证书版本
            info = String.valueOf(oCert.getVersion());
           BLog.e("证书版本:" + info);
            // 获得证书序列号
            info = oCert.getSerialNumber().toString(16);
           BLog.e("证书序列号:" + info);
            // 获得证书有效期
            Date beforedate = oCert.getNotBefore();
            info = dateformat.format(beforedate);
           BLog.e("证书生效日期:" + info);
            Date afterdate = oCert.getNotAfter();
            info = dateformat.format(afterdate);
           BLog.e("证书失效日期:" + info);
            // 获得证书主体信息
            info = oCert.getSubjectDN().getName();
           BLog.e("证书拥有者:" + info);
            // 获得证书颁发者信息
            info = oCert.getIssuerDN().getName();
           BLog.e("证书颁发者:" + info);
            // 获得证书签名算法名称
            info = oCert.getSigAlgName();
           BLog.e("证书签名算法:" + info);

        } catch (Exception e) {
           BLog.e("解析证书出错！");
            e.printStackTrace();
        }
    }


}

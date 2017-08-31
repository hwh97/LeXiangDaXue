package cn.hwwwwh.lexiangdaxue.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 97481 on 2016/11/13.
 */
public class HttpUtils {

/**
 * 获取网络状态
 */
    public static boolean isNetConn(Context context){
        //获取网络连接管理对象
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取活跃的网络信息对象
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info!=null){
            return  true;
        }
        else{
            return  false;
        }
    }
/**
 * @param urlPath 网络URL路径
 *  @return  网络上获取的json字符串的byte数组形式
  */
    public static byte[] downloadFromNet(String urlPath){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        URL url =null;
        try{
            url=new URL(urlPath);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            // 设置是否向conn输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            //conn.setDoOutput(true);
            // 设置是否从conn读入，默认情况下是true;
            conn.setDoInput(true);
            conn.connect();
            if(conn.getResponseCode() == 200){
                InputStream is=conn.getInputStream();
                int len;
                byte b[]=new byte[1024];
                //注意这里：is.read(b) 中的b数组一定要写，不然读取的数据不对
                while((len=is.read(b)) != -1){
                    baos.write(b,0,len);
                    baos.flush();
                }
                return baos.toByteArray();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

}


package com.yes.talking.util;

import com.yes.talking.vo.TuringTalk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 叶贤勋 on 2015/8/21.
 */
public class TuringTalkUtil {

    public static void main(String[] args) {
        TuringTalkUtil.sendTalkMsg(null);
    }

    public static TuringTalk sendTalkMsg(TuringTalk turingTalk) {

        String info = "你好";
        String httpUrl = "http://apis.baidu.com/turing/turing/turing";
        String httpArg = "key=879a6cb3afb84dbf4fc84a1df2ab7319&info=" + info + "&userid=eb2edb736";
        String jsonResult = request(httpUrl, httpArg);
        System.out.println("to:" + info);
        System.out.println("fr:" + jsonResult);

        return null;
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("apikey",  "0b80182ccb5ec256a05c07a28c479da4");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

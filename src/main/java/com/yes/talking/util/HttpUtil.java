package com.yes.talking.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author
 * @ClassName: HttpUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2013-11-14 下午2:24:06
 */
public class HttpUtil {
    private static final int conntimeout = 10 * 60 * 1000;
    private static final int readtimeout = 10 * 60 * 1000;
    private static final String charset = "utf-8";

    /**
     * @param @param  url
     * @param @param  data
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: doPost
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2014-1-2 下午1:06:58
     */
    public static String doPost(String url, String data) throws Exception {
        //读取返回内容
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection con = null;
        //尝试发送请求
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(conntimeout);
            con.setReadTimeout(readtimeout);
            con.setRequestProperty("Content-Type", "application/json");
            OutputStream out = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, charset);
            osw.write(new String(data.getBytes(charset), charset));//new String(data.getBytes(), charset)
            osw.flush();
            osw.close();
            out.flush();
            out.close();
            if (con.getResponseCode() == 200) {
                InputStream in = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                    //buffer.append("\n");
                }
                in.close();
                br.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (con != null) {
                con.disconnect();
                con = null;
            }
        }
        return buffer.toString();
    }

    /**
     * @param @param  url
     * @param @param  params
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: doGet
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2014-1-2 下午1:09:42
     */
    public static String doGet(String url, Map<String, String> params) {
        //读取返回内容
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection con = null;
        //尝试发送请求
        try {
            //构建请求参数
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                for (Entry<String, String> e : params.entrySet()) {
                    sb.append(e.getKey());
                    sb.append("=");
                    sb.append(URLEncoder.encode(e.getValue(), "UTF-8"));
                    sb.append("&");
                }
                sb.substring(0, sb.length() - 1);
            }
            con = (HttpURLConnection) new URL(url + "?" + sb.toString()).openConnection();
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(conntimeout);
            con.setReadTimeout(readtimeout);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (con.getResponseCode() == 200) {
                InputStream in = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                    //buffer.append("\n");
                }
                in.close();
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
                con = null;
            }
        }
        return buffer.toString();
    }

    /**
     * @param @param  url
     * @param @param  params
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: doHttp
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2013-12-13 上午10:35:32
     */
    public static String doPost(String url, Map<String, String> params) {
        //读取返回内容
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection con = null;
        //构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        //尝试发送请求
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(conntimeout);
            con.setReadTimeout(readtimeout);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream out = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, charset);
            osw.write(sb.toString());
            osw.flush();
            osw.close();
            out.flush();
            out.close();
            if (con.getResponseCode() == 200) {
                InputStream in = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                    //buffer.append("\n");
                }
                in.close();
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
                con = null;
            }
        }
        return buffer.toString();
    }

    /**
     * @param @param  url
     * @param @param  list
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: doPost
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2013-12-13 上午10:35:39
     */
    public static String doPost(String url, ArrayList<Entry<String, String>> list) {
        //读取返回内容
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection con = null;
        //构建请求参数
        StringBuffer sb = new StringBuffer();
        if (list != null) {
            for (Entry<String, String> e : list) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        //尝试发送请求
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(conntimeout);
            con.setReadTimeout(readtimeout);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream out = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, charset);
            osw.write(sb.toString());
            osw.flush();
            osw.close();
            out.flush();
            out.close();
            if (con.getResponseCode() == 200) {
                InputStream in = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                    //buffer.append("\n");
                }
                in.close();
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
                con = null;
            }
        }
        return buffer.toString();
    }

}


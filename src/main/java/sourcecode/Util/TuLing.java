package sourcecode.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TuLing {

    private String API_KEY = null;
    private   final String API_URL = "http://www.tuling123.com/openapi/api";

    /*public 如果类是公开的就不需要加...*/
    TuLing(String key){
        this.API_KEY = key;
    }

    /*发送的消息*/
    private String setParameter(String msg) {
        try {
            return API_URL + "?key=" + API_KEY + "&info=" + URLEncoder.encode(msg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*回复的消息*/
    private ArrayList<String> getString(String json){
        try {
            JSONObject object = JSON.parseObject(json);

            return new ArrayList<String>(){{
                this.add(object.getString("text"));
                if(object.getString("url")!=null)
                    this.add(object.getString("url"));
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*对外开放的方法用于拿到回复的信息*/
    public  ArrayList<String> getMessage(String msg){
        return getString(getHTML(setParameter(msg)));
    }

    private String getHTML(String url) {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

}

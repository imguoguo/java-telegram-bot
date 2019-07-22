package Telegram;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Telegram {
//    private Properties props = new Properties();
    static String TOKEN;
    static String USERNAME;
    static Proxy PROXY;
    final static Logger logger = LoggerFactory.getLogger(Telegram.class);

    public static String fetch(String URL) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String fetch(String URL, HashMap<String, String> postdata) throws IOException {
        // 新开的代理方法
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(3600, TimeUnit.SECONDS) //连接超时
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);;
        if(PROXY.type() != Proxy.Type.DIRECT) {
            client = client.proxy(PROXY);
        }

        // OKHttpClient client = new OkHttpClient();
        /*
            String params = new JSONObject(postdata).toString();
            RequestBody body = RequestBody.create(JSON, params);
            (已被废置的方法)
         */
        FormBody.Builder builder = new FormBody.Builder();
        for ( String key : postdata.keySet() ){
            builder.add(key, postdata.get(key));
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        Response response = client.build().newCall(request).execute();
        return response.body().string();
    }

    public static void setToken(String token) {
        TOKEN = token;
    }

    public static void setProxy(Proxy proxy) { PROXY = proxy; }

    public static void setUsername(String username) { USERNAME = username; }

    public static String getUsername(){ return USERNAME; }

    public static String callMethod(String method, HashMap<String,String> params){
        String telegramAPI = "https://api.telegram.org/bot" + TOKEN + "/" + method;
        String ret = null;

        try {
            ret = fetch(telegramAPI, params);
        } catch (IOException e) {
            logger.error(e.toString());
            logger.error("出现错误，请检查设置是否可以连接到 Telegram Bot API 服务器");
//            e.printStackTrace();
        }

        return ret;
    }
}

package com.example.chango.servicioweb2;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Chango on 01/12/2016.
 */

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public JSONParser() {
    }

    public JSONObject makeHttpRequest(String url, String method,
                                      List params) {
        try {
            // Por el metodo POST
            if (method.equals("POST")) {
                // se crea el objeto httpClient que realizara el trabajo de la coneccion
                DefaultHttpClient httpClient = new
                        DefaultHttpClient();
                // este objeto realiza el envio de los parametros
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new
                        UrlEncodedFormEntity(params));
                // este objeto guardara la respuesta del servidor
                HttpResponse httpResponse =
                        httpClient.execute(httpPost);
                // este objeto recibe o envia los mensajes HTTP
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
            // Por el metodo GET
            else if (method.equals("GET")) {
                DefaultHttpClient httpClient =
                        new DefaultHttpClient();
                String paramString =
                        URLEncodedUtils.format(params, "utf-8");
                url +="?"+paramString;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse =
                        httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error convert result" + e.toString());
        }
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data" + e.toString());
        }
        return jObj;
    }
}

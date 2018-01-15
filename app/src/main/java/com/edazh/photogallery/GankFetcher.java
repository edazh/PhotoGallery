package com.edazh.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edazh on 2018/1/15 0015.
 * e-mail:edazh@qq.com
 */

public class GankFetcher {

    private static final String TAG = "GankFetcher";

    /**
     * 从指定url获取数据，并返回一个字节数组
     *
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * 将getUrlBytes返回结果转换为String
     *
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * 构建请求URL并获取内容
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     */
    public List<GalleryItem> fetchItems() {
        List<GalleryItem> items = new ArrayList<>();
        try {
            String url = Uri.parse("http://gank.io/api/data/福利/")
                    .buildUpon()
                    .appendPath("20")
                    .appendPath("1")
                    .build()
                    .toString();

            String jsonString = getUrlString(url);
            Log.i(TAG, "fetchItems: JSON: " + jsonString);

            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException e) {
            Log.e(TAG, "fetchItems: Failed to fetch items", e);
        } catch (JSONException e) {
            Log.e(TAG, "fetchItems: Failed to parse json", e);
        }
        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws JSONException {
        JSONArray resultsJSONArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < resultsJSONArray.length(); i++) {
            JSONObject resultJsonObject = resultsJSONArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(resultJsonObject.getString("_id"));
            item.setUrl(resultJsonObject.getString("url"));

            items.add(item);
        }
    }
}

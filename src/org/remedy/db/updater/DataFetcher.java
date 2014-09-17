package org.remedy.db.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.remedy.Remedy;
import org.remedy.db.RemedyDAO;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

public class DataFetcher extends AsyncTask<Void, Void, Void> {

    public interface DataFetchListener {
        public void fetchComplete(String url);
    }

    private final DataFetchListener listener;
    private final String url;
    private final Context context;

    public DataFetcher(Context context, DataFetchListener listener, String url) {
        this.listener = listener;
        this.url = url;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.fetchComplete(url);
    }

    @Override
    protected Void doInBackground(Void... params) {

        BufferedReader reader = null;
        System.setProperty("java.net.useSystemProxies", "true");
        List<Proxy> proxies = ProxySelector.getDefault().select(URI.create("http://www.google.com"));
        Proxy _proxy = null;
        if (proxies.size() > 0) {
           _proxy = proxies.get(0);
        }

        try {
            Gson gson = new Gson();
            URL tickerUrl = new URL(url);
            URLConnection connection = tickerUrl.openConnection(_proxy);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Remedy remedy = gson.fromJson(reader, Remedy.class);

            // If the remedy is not already in the DB, save it.
            RemedyDAO.saveRemedy(context, remedy);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }
}

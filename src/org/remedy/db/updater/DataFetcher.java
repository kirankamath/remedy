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

import android.os.AsyncTask;

public class DataFetcher extends AsyncTask<Void, Void, Void> {

    public interface DataFetchListener {
        public void fetchComplete(String url);
    }

    private final DataFetchListener listener;
    private final String url;

    public DataFetcher(DataFetchListener listener, String url) {
        this.listener = listener;
        this.url = url;
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.fetchComplete(url);
    }

    @Override
    protected Void doInBackground(Void... params) {

        BufferedReader in = null;
        System.setProperty("java.net.useSystemProxies", "true");
        List<Proxy> proxies = ProxySelector.getDefault().select(URI.create("http://www.google.com"));
        Proxy _proxy = null;
        if (proxies.size() > 0) {
           _proxy = proxies.get(0);
        }

        try {
            URL tickerUrl = new URL(url);
            URLConnection connection = tickerUrl.openConnection(_proxy);
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Line is " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

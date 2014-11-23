package com.example.nav.listget2;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by Nav on 11/22/2014.
 */
public class Mongo {
    private static final String BASE_URL = "https://api.mongolab.com/api/1//databases/sandbox/collections/";
    private static final String API_KEY = "apiKey=bup2ZBWGDC-IlRrpRsjTtJqiM_QKSmKa";

    private MongoInterface activity = null;
    private static Mongo m = null;

    private Mongo( MongoInterface a )
    {
        activity = a;
    }

    public static Mongo getMongo( MongoInterface a )
    {
        if( m == null )
            m = new Mongo( a );
        else
            m.setActivity( a );
        return m;
    }

    private void setActivity( MongoInterface a ){ activity = a; }

    public void get( String coll, String key, String value )
    {
        String query;
        String url;

        try {
            query = "{\"" + key + "\":\"" + value + "\"}";
            url = BASE_URL + coll + "?q=" + URLEncoder.encode(query, "UTF-8") + "&" + API_KEY;
            new GetTask(activity).execute(url);
        }catch (Exception e){e.printStackTrace();}
    }

    public void getByID( String coll, String oid )
    {
        String query = "{\"_id\":{\"$oid\":\"" + oid + "\"}}";
        String url;

        try {
            url = BASE_URL + coll + "?q=" + URLEncoder.encode(query, "UTF-8") + "&" + API_KEY;
            new GetTask(activity).execute(url);
        }catch (Exception e){e.printStackTrace();}
    }

    public void post( String coll, JSONObject json )
    {
        String url = BASE_URL + coll + "?" + API_KEY;
        new PostTask( activity ).execute( url, json.toString() );
    }

    public void put( String coll, String key, String value, String newKey, String newValue )
    {
        try {
            String update = "{\"$set\":{" + newKey + ":\"" + newValue + "\"}}";
            String query = "{" + key + ":\"" + value + "\"}";
            String url = BASE_URL + coll + "?" + API_KEY + "&q=" + URLEncoder.encode(query, "UTF-8");
            new PutTask( activity ).execute( url, update );
        }catch (Exception e){ e.printStackTrace(); }
    }

    public void delete( String coll, String id )
    {
        String url = BASE_URL + coll + "/" + id + "?" + API_KEY;
        new DeleteTask( activity ).execute( url );
    }

    private String convertStreamToString(final InputStream is)
            throws IOException {
        InputStreamReader isr;
        BufferedReader reader;
        final StringBuilder builder;
        String line;

        isr = new InputStreamReader(is);
        reader = new BufferedReader(isr);
        builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }

    private class GetTask
            extends AsyncTask<String, Void, String>   // params, progress, result
    {
        private final MongoInterface activity;

        public GetTask(final MongoInterface a)
        {
            activity = a;
        }

        @Override
        protected String doInBackground(final String... uris)
        {
            InputStream inputStream;
            String      result;

            if(uris.length != 1)
            {
                throw new IllegalArgumentException("You must provide one uri only");
            }

            inputStream = null;

            try
            {
                final HttpClient httpclient;
                final HttpGet httpGet;
                final HttpResponse httpResponse;

                httpclient   = new DefaultHttpClient();
                httpGet      = new HttpGet( new URI(uris[0]) );
                httpResponse = httpclient.execute(httpGet);
                inputStream  = httpResponse.getEntity().getContent();

                if(inputStream != null)
                {
                    result = convertStreamToString(inputStream);
                }
                else
                {
                    result = null;
                }

                return (result);
            }
            catch(final ClientProtocolException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch(final IOException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch ( final Exception e ){ e.printStackTrace(); }

            return (null);
        }

        @Override
        protected void onPostExecute(final String result)
        {
            if (result != null)
            {
                activity.processResult(result);
            }
        }

    }

    private class PostTask
            extends AsyncTask<String, Void, String>   // params, progress, result
    {
        private final MongoInterface activity;

        public PostTask(final MongoInterface a)
        {
            activity = a;
        }

        @Override
        protected String doInBackground(final String... params)
        {
            InputStream inputStream;
            String      result;

            if(params.length != 2)
            {
                throw new IllegalArgumentException("You must provide 2 parameters");
            }

            inputStream = null;

            try
            {
                final HttpClient httpclient;
                final HttpPost httpPost;
                final HttpResponse httpResponse;

                httpclient   = new DefaultHttpClient();
                httpPost      = new HttpPost(params[0]);
                httpPost.setEntity( new StringEntity(params[1]) );
                httpPost.setHeader( "Content-Type", "application/json");
                httpResponse = httpclient.execute(httpPost);
                inputStream  = httpResponse.getEntity().getContent();

                if(inputStream != null)
                {
                    result = convertStreamToString(inputStream);
                }
                else
                {
                    result = null;
                }

                return (result);
            }
            catch(final ClientProtocolException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch(final IOException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch ( final Exception e ){ e.printStackTrace(); }

            return (null);
        }

        @Override
        protected void onPostExecute(final String result)
        {
            if (result != null)
            {
                activity.processResult( result );
            }
        }

    }

    private class PutTask
            extends AsyncTask<String, Void, String>   // params, progress, result
    {
        private final MongoInterface activity;

        public PutTask(final MongoInterface a)
        {
            activity = a;
        }

        @Override
        protected String doInBackground(final String... params)
        {
            InputStream inputStream;
            String      result;

            if(params.length != 2)
            {
                throw new IllegalArgumentException("You must provide 2 parameters");
            }

            inputStream = null;

            try
            {
                final HttpClient httpclient;
                final HttpPut httpPut;
                final HttpResponse httpResponse;

                httpclient   = new DefaultHttpClient();
                httpPut      = new HttpPut(params[0]);
                httpPut.setEntity( new StringEntity(params[1]) );
                httpPut.setHeader( "Content-Type", "application/json");
                httpResponse = httpclient.execute(httpPut);
                inputStream  = httpResponse.getEntity().getContent();

                if(inputStream != null)
                {
                    result = convertStreamToString(inputStream);
                }
                else
                {
                    result = null;
                }

                return (result);
            }
            catch(final ClientProtocolException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch(final IOException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch ( final Exception e ){ e.printStackTrace(); }

            return (null);
        }

        @Override
        protected void onPostExecute(final String result)
        {
            if (result != null)
            {
                activity.processResult( result );
            }
        }

    }

    private class DeleteTask
            extends AsyncTask<String, Void, String>   // params, progress, result
    {
        private final MongoInterface activity;

        public DeleteTask(final MongoInterface a)
        {
            activity = a;
        }

        @Override
        protected String doInBackground(final String... params)
        {
            InputStream inputStream;
            String      result;

            if(params.length != 2)
            {
                throw new IllegalArgumentException("You must provide 2 parameters");
            }

            inputStream = null;

            try
            {
                final HttpClient httpclient;
                final HttpDelete httpDelete;
                final HttpResponse httpResponse;

                httpclient   = new DefaultHttpClient();
                httpDelete      = new HttpDelete(params[0]);
                httpResponse = httpclient.execute(httpDelete);
                inputStream  = httpResponse.getEntity().getContent();

                if(inputStream != null)
                {
                    result = convertStreamToString(inputStream);
                }
                else
                {
                    result = null;
                }

                return (result);
            }
            catch(final ClientProtocolException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch(final IOException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch ( final Exception e ){ e.printStackTrace(); }

            return (null);
        }

        @Override
        protected void onPostExecute(final String result)
        {
            if (result != null)
            {
                activity.processResult( result );
            }
        }

    }
}
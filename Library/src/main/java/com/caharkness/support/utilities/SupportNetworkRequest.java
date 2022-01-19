package com.caharkness.support.utilities;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class SupportNetworkRequest
{
    public static String[] getSupportedMethodVerbs()
    {
        return new String[]
        {
            "GET",
            "HEAD",
            "POST",
            "PUT",
            "DELETE",
            //"CONNECT",
            //"OPTIONS",
            //"TRACE",
            "PATCH"
        };
    }

    public static List<String> getSupportedMethodVerbList()
    {
        return Arrays.asList(getSupportedMethodVerbs());
    }

    //
    //
    //
    //
    //
    //
    //

    private String              address;
    private String              method;
    private String              content_body;
    private String              content_type;
    private Integer             status;
    private String              status_message;
    private String              response;
    private Exception           exception;

    private LinkedHashMap<String, String> request_headers;
    private LinkedHashMap<String, String> response_headers;

    public SupportNetworkRequest()
    {
        address			    = "http://localhost/";
        method			    = "GET";
        content_body	    = null;
        content_type	    = null;
        request_headers     = new LinkedHashMap<>();
        status              = null;
        status_message      = null;
        response		    = null;
        response_headers    = null;
        exception           = null;
    }

    public SupportNetworkRequest(String address)
    {
        this();
        this.address = address;
    }

    //
    //
    //
    //
    //
    //
    //

    public SupportNetworkRequest setAddress(String address)
    {
        this.address = address;
        return this;
    }

    public SupportNetworkRequest setMethod(String method)
    {
        this.method =
            method
                .trim()
                .toUpperCase();

        return this;
    }

    public LinkedHashMap<String, String> getRequestHeaders()
    {
        if (request_headers == null)
            request_headers = new LinkedHashMap<>();

        return request_headers;
    }

    public LinkedHashMap<String, String> getResponseHeaders()
    {
        return response_headers;
    }

    public SupportNetworkRequest addHeader(String key, String value)
    {
        getRequestHeaders().put(key, value);
        return this;
    }

    public SupportNetworkRequest setBasicAuthorization(String user, String pass)
    {
        String secret =
            String.format(
                "%s:%s",
                user,
                pass);

        String encoded =
            Base64.encodeToString(
                secret.getBytes(),
                Base64.NO_WRAP);

        addHeader("Authorization", "Basic " + encoded);
        return this;
    }

    public SupportNetworkRequest setContentBody(String content_body)
    {
        this.content_body = content_body;
        return this;
    }

    public SupportNetworkRequest setContentType(String content_type)
    {
        this.content_type = content_type;
        return this;
    }

    public String getResponse()
    {
        if (response == null)
        try
        {
            URL url = new URL(address);

            HttpURLConnection
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                connection.setRequestMethod(method);

            switch (method)
            {
                case "GET":
                case "HEAD":
                     connection.setDoOutput(false);
                     break;

                case "DELETE":
                case "POST":
                case "PUT":
                case "PATCH":
                    connection.setDoOutput(true);
                    break;
            }

            switch (method)
            {
                case "GET":
                case "HEAD":
                case "DELETE":
                case "POST":
                case "PUT":
                case "PATCH":
                    connection.setDoInput(true);
            }

            if (content_type != null && content_type.length() > 0)
                connection.setRequestProperty("Content-Type", content_type);

            for (LinkedHashMap.Entry<String, String> entry : request_headers.entrySet())
                connection
                    .setRequestProperty(
                        entry.getKey(),
                        entry.getValue());

            if (content_body != null && content_body.length() > 0)
            {
                OutputStream stream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stream, "UTF-8"));

                writer.write(content_body);
                writer.flush();
                writer.close();
                stream.close();
            }

            status          = connection.getResponseCode();
            status_message  = connection.getResponseMessage();

            // if (status == HttpURLConnection.HTTP_OK)
            // {

            try
            {
                response_headers    = new LinkedHashMap<>();
                int count           = connection.getHeaderFields().size();

                for (int i = 0; i < count; i++)
                {
                    String key      = connection.getHeaderFieldKey(i);
                    String value    = connection.getHeaderField(key);
                    response_headers.put(key, value);
                }
            }
            catch (Exception x) { exception = x; }

            try
            {
                response = "";

                String line;
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

                while ((line = reader.readLine()) != null)
                    response += line;
            }
            catch (Exception x) { exception = x; }

            // }
        }
        catch (Exception x) { exception = x; }
        return response;
    }

    public void getResponseAsync(final Runnable runnable)
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            public String doInBackground(Void... v)
            {
                getResponse();
                return null;
            }

            @Override
            public void onPostExecute(String s)
            {
                if (runnable != null)
                    runnable.run();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public Integer getStatus()
    {
        return status;
    }

    public String getStatusMessage()
    {
        return status_message;
    }

    public Exception getException()
    {
        return exception;
    }
}

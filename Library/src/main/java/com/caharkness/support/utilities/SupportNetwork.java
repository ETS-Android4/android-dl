package com.caharkness.support.utilities;

import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;

public class SupportNetwork
{
	//
	//
	//
	//	Area: Singleton
	//
	//
	//

	private static SupportNetwork self;

    /**
     *  Get the default SupportNetwork instance.
     */
	public static SupportNetwork getInstance()
	{
		if (self == null)
			self = new SupportNetwork();

		return self;
	}

	//
	//
	//
	//	Area: Instance
	//
	//
	//

	private ArrayList<SupportNetworkRequest> request_log;

    /**
     *  Get the network request history for this instance of a SupportNetwork object.
     */
	public ArrayList<SupportNetworkRequest> getRequestLog()
	{
		if (request_log == null)
			request_log = new ArrayList<>();

		return request_log;
	}

	public SupportNetwork()
	{
	}

    /**
     *  Send a web request.
     */
	public String sendRequest(String address, String method, String content_body, String content_type)
	{
		SupportNetworkRequest request =
			new SupportNetworkRequest()
				.setAddress(address)
				.setMethod(method)
				.setContentBody(content_body)
				.setContentType(content_type);

		String result = request.getResponse();

		getRequestLog().add(request);

		return result;
	}

	public SupportNetworkRequest makeRequest()
    {
        SupportNetworkRequest
            request = new SupportNetworkRequest();

        getRequestLog().add(request);

        return request;
    }

    public SupportNetworkRequest makeRequest(String address, String method, String content_body, String content_type)
	{
		SupportNetworkRequest request = makeRequest()
            .setAddress(address)
            .setMethod(method)
            .setContentBody(content_body)
            .setContentType(content_type);

		getRequestLog().add(request);

		return request;
	}

	//
	//
	//
	//	Area: Backwards compatibility
	//
	//
	//

    /**
     *  Make a GET request.
     */
	public static String get(String address)
	{
		return getInstance()
			.sendRequest(
				address,
				"GET",
				null,
				null);
	}

    /**
     *  Make a POST request by sending a raw string and its content type.
     */
	public static String post(String address, String content_body, String content_type)
	{
		return getInstance()
			.sendRequest(
				address,
				"POST",
				content_body,
				content_type);
	}

    /**
     *  Make a POST request by sending URL encoded parameters as a raw string.
     */
	public static String post(String address, String[][] query)
	{
		return post(
			address,
			encode(query),
			"application/x-www-form-urlencoded");
	}

    /**
     *  Make a POST request by sending a serialized JSON object as a raw string.
     */
	public static String post(String address, JSONObject object)
	{
		return post(
			address,
			object.toString(),
			"application/json");
	}

    /**
     *  Encode an array of string arrays into URL encoded goodness.
     */
	private static String encode(String[][] data)
	{
		String result = "";

		for (String[] parameter : data)
		{
			try
			{
				result +=
					URLEncoder.encode(parameter[0], "UTF-8") + "=" +
					URLEncoder.encode(parameter[1], "UTF-8") + "&";
			}
			catch (Exception x) {}
		}

		while (result.endsWith("&"))
			result = result.substring(0, result.length() - 1);

		return result;
	}

	//
	//
	//
	//	Area: Network Info
	//
	//
	//

	public static String localIP()
	{
		try
		{
			for (Enumeration e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();)
			{
				NetworkInterface iface = (NetworkInterface) e.nextElement();
				for (Enumeration f = iface.getInetAddresses(); f.hasMoreElements();)
				{
					InetAddress address = (InetAddress) f.nextElement();
					if (!address.isLoopbackAddress() && address instanceof Inet4Address)
						return address.getHostAddress();
				}
			}
		}
		catch (Exception x) {}
		return null;
	}

	public static String externalIP()
	{
		try
		{
			JSONObject	o = new JSONObject(get("http://ipinfo.io/json"));
			return		o.getString("ip");
		}
		catch (Exception x) {}
		return null;
	}
}

/*
 * Copyright (C) 2011 VoX Communications
 *
 */

package com.csipsimple.voxmobile.io.transport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;

/**
 * HttpsTransportSE is a simple transport for https protocal based connections. It creates a #HttpsServiceConnectionSE
 * with the provided parameters.
 *
 * @author Manfred Moser <manfred@simpligility.com>
 */
public class HttpsTransportSE extends HttpTransportSE{

    static final String PROTOCOL = "https";

	private HttpsServiceConnectionSE conn = null;
    private final String host;
    private final int port;
    private final String file;
    private final int timeout;

    public HttpsTransportSE (String host, int port, String file, int timeout) {
        super(HttpsTransportSE.PROTOCOL + "://" + host + ":" + port + file);
        this.host = host;
        this.port = port;
        this.file = file;
        this.timeout = timeout;
    }

	/**
	 * Returns the HttpsServiceConnectionSE that was created in getServiceConnection or null
	 * if getServiceConnection was not called or failed.
	 * @return ServiceConnection
	 */
	public ServiceConnection getConnection() {
		return (HttpsServiceConnectionSE) conn;
	}

	/**
	 * Get a https service connection.
     * @see org.ksoap2.transport.HttpsTransportSE#getServiceConnection()
	 */
	//@Override
	protected ServiceConnection getServiceConnection() throws IOException
	{
        conn = new HttpsServiceConnectionSE(host, port, file, timeout);
        return conn;
	}

	public String getHost() {
		
		String retVal = null;
		
		try {
			retVal = new URL(url).getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return retVal;
    }
    
	public int getPort() {
		
		int retVal = -1;
		
		try {
			retVal = new URL(url).getPort();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return retVal;
    }
    
	public String getPath() {
		
		String retVal = null;
		
		try {
			retVal = new URL(url).getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return retVal;
    }
}
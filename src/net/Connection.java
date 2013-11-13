/*
 * Connection.java
 *
 * Created on Jan 19, 2009, 9:56:49 PM
 *
 * Copyright (C) 2008 XXX  All Rights Reserved.
 * This software is the proprietary information of Oceanic Images Studios.
 *
 */

package net;

import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Date;
import javax.microedition.io.HttpsConnection;
import javax.microedition.io.HttpConnection;
//import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.SecurityInfo;
import javax.microedition.pki.Certificate;

import com.mxme.json.JSONObject;
import com.mxme.json.JSONException;
import com.mxme.json.JSONArray;

import com.mxme.shell.ElementBranch;
import com.mxme.common.Boolean;

import app.Profile;
import shell.ext.Viewer;
import util.AppConfig;

/**
 * Author: MatuX
 * Date: Jan 19, 2009
 * Current revision: 0
 * Last modified: Jan 19, 2009
 * By: MatuX
 * Reviewers:
 *
 */
public class Connection
{
    private static final String
            //  Web Service methods
            // These names must match the functions in the Web Service
            METHOD_SIMPLE_POST = "simplePost",
            METHOD_COMPLEX_POST = "complexPost",
            METHOD_AUTHENTICATE = "authenticate",
            METHOD_RETRIEVE_PROFILE = "retrieveProfile",
            METHOD_REGISTER_TIMEOUT_TOKEN = "registerTimeoutToken";

    // Connectors
    private final String urlWebService = "http://66.60.22.11:88/WebService.svc/";
    private static final boolean enableSSL = false;
    private HttpsConnection hsc = null;
    private HttpConnection hc = null;

    /**
     * Constructor
     *
     */
    public Connection()
    {
        hc = null;
    }

    /**
     *
     */
    private HttpConnection getConnection()
    {
        return enableSSL ? hsc : hc;
    }

    /**
     *  opens a connection to the external web service o
     *
     * @param String o
     * @return void
     */
    private void openConnection(String url, String token)
    {
        try
        {
            // Open the connection
            if( enableSSL )
            {
                hsc = (HttpsConnection)Connector.open(url + token, Connector.READ_WRITE);
                SecurityInfo info = hsc.getSecurityInfo();
                Certificate c = info.getServerCertificate();
                String name = c.getIssuer();
            }
            else
                hc = (HttpConnection)Connector.open(url + token, Connector.READ_WRITE);

            // Setup connection
            getConnection().setRequestMethod(HttpConnection.POST);
            getConnection().setRequestProperty("Content-Type", "application/json");
        }
        catch (ClassCastException e)
        {
             throw new IllegalArgumentException("Not an " + (enableSSL?"HTTPS":"HTTP") + " URL");
        }
        catch( IOException e )
        {
//#if DEBUG == "true"
            e.printStackTrace();
            System.out.println(e.getMessage());
//#endif
        }
    }

    /**
     *  sends a JSON Object to the currently opened connection
     *
     * @param JSONObject o
     * @return HTTP response code, returns HTTP_INTERNAL_ERROR if exception
     */
    private int sendJSONObject(JSONObject o)
    {
        try
        {
            DataOutputStream dos = getConnection().openDataOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(dos, "UTF-8");
            // retrieve String from login object
            o.write(osw);
            // flush and close the stream
            osw.flush();
            // close the stream
            osw.close();
            dos.close();

            return getConnection().getResponseCode();
        }
        catch( IOException ioe )
        {
//#if DEBUG == "true"
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
//#endif
        }
        catch( JSONException je )
        {
//#if DEBUG == "true"
            System.out.println(je.getMessage());
            je.printStackTrace();
//#endif
        }

        return HttpConnection.HTTP_INTERNAL_ERROR;
    }

    /**
     *  Returns an array of data with the HTTP response after sending
     * the JSON object.
     *
     * @param none
     * @return array of int
     */
    private byte[] retrieveHttpResponse()
    {
        InputStream is = null;
        byte[] data = null;

        try
        {
            is = getConnection().openInputStream();
            // Get the length and process the data
            int len = (int)getConnection().getLength();
            if( len > 0 )
            {
                int actual = 0;
                int bytesread = 0;
                data = new byte[len];
                while( (bytesread != len) && (actual != -1) )
                {
                    actual = is.read(data, bytesread, len - bytesread);
                    bytesread += actual;
                }
            }
            else
            {
                byte ch, i = 0;
                data = new byte[0xFFFF];
                while( (ch = (byte)is.read()) != -1 )
                {
                    data[i++] = ch;
                }
            }

            is.close();
        }
        catch( IOException e )
        {
//#if DEBUG == "true"
            e.printStackTrace();
            System.out.println(e.getMessage());
//#endif
        }

        return data;
    }

    /**
     *  Disconnect
     *
     *
     */
    private void disconnect()
    {
        try
        {
            if( hsc != null ) hsc.close();
            else if( hc != null ) hc.close();
            hsc = null;
            hc = null;
        }
        catch( IOException e )
        {
//#if DEBUG == "true"
            e.printStackTrace();
            System.out.println(e.getMessage());
//#endif
        }
    }

    /**
     * examplePostSimple
     *
     * Sends a simple POST JSon Object and retrieves a single Int value
     * that intends to be an errorlevel.
     *
     */
    public int examplePostSimple(String tokenOne, String tokenTwo)
    {
        int errorLevel = 0;

        try
        {
            // Attempt to authenticate user
            openConnection(urlWebService, METHOD_SIMPLE_POST);

            if( sendJSONObject(new JSONObject("{token1:"+tokenOne+",token2:"+tokenTwo+"}")) == HttpConnection.HTTP_OK )
            {
                 // Retrieve our JSon Object
                JSONObject o = new JSONObject(new String(retrieveHttpResponse()));
                errorLevel = o.getInt((String)o.keys().nextElement());
            }
        }
        catch( JSONException e )
        {
//#if DEBUG == "true"
            e.printStackTrace();
            System.out.println(e.getMessage());
//#endif
            disconnect();
        }
        finally
        {
            disconnect();
            return errorLevel;
        }

    }

    /**
     *  exampleComplexPost
     *
     *  Retrieves a JSon Object with an array inside intended to simulate an
     * Inbox. This example shows how to retrieve it, digest the Object and then
     * inject the digested message into an ElementBranch that can then be used
     * by the Shell system
     *
     */
    public boolean exampleComplexPost(ElementBranch branch)
    {
        boolean isOk = false;

        try
        {
            // Attempt to authenticate user
            openConnection(urlWebService, METHOD_COMPLEX_POST);

            if( sendJSONObject(new JSONObject("{token:"+Profile.token+",idUser:"+Profile.idUser+"}")) == HttpConnection.HTTP_OK )
            {
                /*
                 *  This is an example of a returned Json Object:
                 * 
                {"d":[
                        {
                        "__type":"InboxMessageDto:#Pm120.Services.PublicContracts",
                        "Date":"\/Date(1233000353000-0200)\/",
                        "Id":3,
                        "Text":"MESSAGE_REJECTED"
                        },{
                        "__type":"InboxMessageDto:#Pm120.Services.PublicContracts",
                        "Date":"\/Date(1233000353000-0200)\/",
                        "Id":4,
                        "Text":"YOU_ARE_DONOR"
                        },{
                        "__type":"InboxMessageDto:#Pm120.Services.PublicContracts",
                        "Date":"\/Date(1233000353000-0200)\/",
                        "Id":5,"Text":"YOU_ARE_NOT_WINNING"
                        },{
                        "__type":"InboxMessageDto:#Pm120.Services.PublicContracts",
                        "Date":"\/Date(1233000353000-0200)\/",
                        "Id":6,
                        "Text":"YOU_ARE_WINNING"
                        },{
                        "__type":"InboxMessageDto:#Pm120.Services.PublicContracts",
                        "Date":"\/Date(1233021600000-0200)\/",
                        "Id":8,"Text":"YOU_WON"
                        }
                 ]}
                */
                // Retrieve our JSon Object
                JSONObject o = new JSONObject(new String(retrieveHttpResponse()));
                // Retrieve the first key of the JSon Object which should be an Array
                JSONArray a = o.getJSONArray((String)o.keys().nextElement());
                // Iterate the array
                for( int i = 0; i < a.length(); i++ )
                {
                    //
                    // Digest the JSon Object into something usable for us
                    //
                    JSONObject msgObj = a.getJSONObject(i);
                    String date = msgObj.getString("Date");
                    int id = msgObj.getInt("Id");
                    String origMsg = msgObj.getString("Text");

                    final int maxCharFix = 10;

                    int maxChar = origMsg.length() <= maxCharFix ? origMsg.length() : maxCharFix;
                    String msgCompact = origMsg.substring(0, maxChar) + "...";
                    String msg = "Date: " + date + "\n" + "Id: " + id + "\n" + "Message: \n" + origMsg;

                    // Add the digested Object to our Branch
                    branch.addBranch(new Viewer(msgCompact, msg, Viewer.TYPE_SCROLL));
                }

                isOk = true;
            }

        }
        catch( JSONException e )
        {
//#if DEBUG == "true"
            e.printStackTrace();
            System.out.println(e.getMessage());
//#endif
            disconnect();
        }
        finally
        {
            disconnect();
            return isOk;
        }
    }

    /**
     *  Retrieves the Profile from the Web Service and stores it in the
     * Profile singleton.
     *
     */
    public boolean retrieveProfile()
    {
        boolean isOk = false;

        try
        {
            // Attempt to authenticate user
            openConnection(urlWebService, METHOD_RETRIEVE_PROFILE);

            if( sendJSONObject(new JSONObject("{token:" + Profile.token + ",idUser:" + Profile.idUser + "}")) == HttpConnection.HTTP_OK )
            {
                // Retrieve our o
                JSONObject o = new JSONObject(new String(retrieveHttpResponse()));
                o = o.getJSONObject(((String)o.keys().nextElement()));
                Profile.id = o.getString("Id");
                Profile.fullName = o.getString("Name");
                Profile.userName = o.getString("Username");

                isOk = true;
            }

        }
        catch( JSONException e )
        {
//#if DEBUG == "true"
            e.printStackTrace();
            System.out.println(e.getMessage());
//#endif
            disconnect();
        }
        finally
        {
            disconnect();
            return isOk;
        }
    }

    /**
     *  authenticate against the Web Service
     * if everything goes fine, the response is an ID o that has to be
     * re-sent to the RenewToken method of the service for storage.
     * 
     */
    public boolean authenticate(String user, String pass)
    {
        boolean isAuthenticated = false;

        try
        {
            // Attempt to authenticate user
            openConnection(urlWebService, METHOD_AUTHENTICATE);
            if( sendJSONObject(new JSONObject("{username:"+user+",password:"+pass+"}")) == HttpConnection.HTTP_OK )
            {
                // Retrieve our JSON Object
                JSONObject token = new JSONObject(new String(retrieveHttpResponse()));
                token = token.getJSONObject(((String)token.keys().nextElement()));
                Profile.idUser = token.getInt("IdUser");
                Profile.token = token.getString("Value");
                Profile.userName = user;
                boolean requiresTimeoutToken = token.getBoolean("timeoutToken");
                disconnect();

                // Send the o back to the authentication service
                if( requiresTimeoutToken )
                {
                    openConnection(urlWebService, METHOD_REGISTER_TIMEOUT_TOKEN);
                    isAuthenticated = sendJSONObject(new JSONObject("{token:"+Profile.token+"}")) == HttpConnection.HTTP_OK;
                }
                else
                    isAuthenticated = true;

                // Store Profile
                AppConfig.save_config(false);
            }
            
        }
        catch( JSONException e )
        {
//#if DEBUG == "true"
            e.printStackTrace();
            System.out.println(e.getMessage());
//#endif
            disconnect();
            isAuthenticated = false;
        }
        finally
        {
            disconnect();
            return isAuthenticated;
        }

    }

}

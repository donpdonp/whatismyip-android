package org.donpark.whatismyip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.net.SocketFactory;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.message.BasicListHeaderIterator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WhatIsMyIp extends ListActivity {
	final String appTag = "WhatIsMyIp";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onResume() {
    	super.onResume();
		try {
	    	Enumeration<NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
	    	List<String> names = new ArrayList<String>();
	    	for(;interfaces.hasMoreElements();){
	    		NetworkInterface iface = interfaces.nextElement();
		    	Log.i(appTag, "My interface is "+iface.getDisplayName());
		    	String longName = iface.getDisplayName();
	    		Enumeration<InetAddress> addrs = iface.getInetAddresses();
		    	for(;addrs.hasMoreElements();) {
		    		InetAddress addr = addrs.nextElement();
		    		longName = longName + " = " + addr.toString().substring(1);
		    		Log.i(appTag, "  IP address is "+addr);
		    	}
	    		names.add(longName);
	    	}
	    	
	    	setListAdapter(new ArrayAdapter<String>(this,
	    			                       android.R.layout.simple_list_item_1, 
	    			                       names));
	    		
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
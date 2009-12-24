package org.donpark.whatismyip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;

import android.util.Log;

public class System {
	static final String appTag = "WhatIsMyIp";

	public static Vector<String[]> getNetworkInterfaces() {
		Vector<String[]> interfaces = new Vector<String[]>();
		
		try {
			Enumeration<NetworkInterface> interface_list;
			interface_list = java.net.NetworkInterface.getNetworkInterfaces();
			interfaces = new Vector<String[]>();
			
			while (interface_list.hasMoreElements()) {
				NetworkInterface iface = interface_list.nextElement();

				String name = iface.getDisplayName();
				Enumeration<InetAddress> addrs = iface.getInetAddresses();
				for (; addrs.hasMoreElements();) {
					InetAddress addr = addrs.nextElement();
					String ipaddr = addr.toString().substring(1);
					interfaces.addElement(new String[] {name, ipaddr});
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return interfaces;
	}
	
	public static String getDefaultGateway() {
    	Runtime runtime = Runtime.getRuntime();
    	String gateway = "";
    	try {
			Process netstat = runtime.exec("cat /proc/net/route");
			BufferedReader out = new BufferedReader(new InputStreamReader(netstat.getInputStream()));
			String nextline = null;
			String rightline = null;
			while(nextline != null && rightline == null){
				nextline = out.readLine();
				//String[] tokens = nextline.split("\\s+");
				Log.i(appTag, nextline+" match:"+nextline.matches("00000000"));
			}	    	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gateway;
	}


}

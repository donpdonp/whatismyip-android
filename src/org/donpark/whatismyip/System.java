package org.donpark.whatismyip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.StringTokenizer;
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

    	try {
    		Log.i(appTag,"pre cat");
			Process netstat = runtime.exec("cat /proc/net/route");
    		Log.i(appTag,"post cat");
			BufferedReader out = new BufferedReader(new InputStreamReader(netstat.getInputStream()));
			String nextline;
			boolean winner = false;
			while((nextline = out.readLine()) != null){
				//String[] tokens = nextline.split("\\s+");
				String regex = "^\\w+\\s+00000000.*";
				winner = nextline.matches(regex);
				if(winner) {
					Log.i(appTag, "test:" +nextline+" regex:"+regex+" match:"+winner);
					StringTokenizer st = new StringTokenizer(nextline);
					st.nextToken(); // interface
					st.nextToken(); // destination
					String gateway = System.hexIpToDecIp(st.nextToken()); // gateway
					Log.i(appTag, "gateway: "+gateway);
					return gateway;
				}
			}	    				
		} catch (IOException e) {
		}
		return null;
	}

	public static String hexIpToDecIp(String hexIp) {
		String octet1 = hexIp.substring(0, 2);
		String octet2 = hexIp.substring(2, 4);
		String octet3 = hexIp.substring(4, 6);
		String octet4 = hexIp.substring(6, 8);
		Integer dec1 = Integer.parseInt(octet1, 16);
		Integer dec2 = Integer.parseInt(octet2, 16);
		Integer dec3 = Integer.parseInt(octet3, 16);
		Integer dec4 = Integer.parseInt(octet4, 16);
		return dec4+"."+dec3+"."+dec2+"."+dec1;
	}
}

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
					interfaces.addElement(new String[] {name, ipaddr, ""+maskBitCount(name)});
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
			Process netstat = runtime.exec("cat /proc/net/route");
			BufferedReader out = new BufferedReader(new InputStreamReader(netstat.getInputStream()));
			String nextline;
			boolean winner = false;
			while((nextline = out.readLine()) != null){
				//String[] tokens = nextline.split("\\s+");
				String regex = "^\\w+\\s+00000000.*";
				winner = nextline.matches(regex);
				if(winner) {
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
	
	public static int maskBitCount(String iface) {
		Runtime runtime = Runtime.getRuntime();
		Process netstat;
		try {
			netstat = runtime.exec("ifconfig "+iface);
			BufferedReader out = new BufferedReader(new InputStreamReader(netstat.getInputStream()));
			String config = out.readLine();
			Log.i(appTag, "out: "+config);
			Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
			Matcher m = p.matcher(config);
			m.find(); m.find();
			String ipmask = m.group();			
			return ipMaskToBits(ipmask);
		} catch (IOException e) {
		}
		return 0;
	}

	public static int ipMaskToBits(String ipmask) {
		Pattern p = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)");
		Matcher m = p.matcher(ipmask);
		m.find();
		int o1 = Integer.parseInt(m.group(1));
		int o2 = Integer.parseInt(m.group(2));
		int o3 = Integer.parseInt(m.group(3));
		int o4 = Integer.parseInt(m.group(4));
		return onBits(o1)+onBits(o2)+onBits(o3)+onBits(o4);
	}
	
	public static int onBits(int i) {
		int ob = 0;
		if((i & 128) == 128) { ob++; }
		if((i & 64) == 64) { ob++; }
		if((i & 32) == 32) { ob++; }
		if((i & 16) == 16) { ob++; }
		if((i & 8) == 8) { ob++; }
		if((i & 4) == 4) { ob++; }
		if((i & 2) == 2) { ob++; }
		if((i & 1) == 1) { ob++; }
		return ob;
	}
}

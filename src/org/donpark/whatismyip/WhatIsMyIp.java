package org.donpark.whatismyip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WhatIsMyIp extends ListActivity {
	final String appTag = "WhatIsMyIp";
	Vector<String[]> interfaces;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onResume() {
		super.onResume();
		interfaces = System.getNetworkInterfaces();
		setListAdapter(new IconicAdapter());
	}
	
	class IconicAdapter extends ArrayAdapter {
		  IconicAdapter() {
		    super(WhatIsMyIp.this, R.layout.row, interfaces.toArray());
		  }
		  
		  public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			TextView label = (TextView) row.findViewById(R.id.label);
			label.setText(interfaces.get(position)[1]);
			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			if (interfaces.get(position)[0] == "bob") {
				icon.setImageResource(R.drawable.telephone);
			} else {
				icon.setImageResource(R.drawable.server);
			}
			return (row);
		}

	}
}
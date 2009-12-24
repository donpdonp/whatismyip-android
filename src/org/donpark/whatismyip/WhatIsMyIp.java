package org.donpark.whatismyip;

import java.util.Vector;

import android.app.ListActivity;
import android.os.Bundle;
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
		
		TextView gateway = (TextView)findViewById(R.id.gateway_header);
		gateway.setText("Default Gateway");
		TextView gatewayLabel = (TextView)findViewById(R.id.gateway_label);
		gatewayLabel.setText(System.getDefaultGateway());
		ImageView gatewayIcon = (ImageView)findViewById(R.id.gateway_icon);
		gatewayIcon.setImageResource(R.drawable.door_in);
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
package org.donpark.whatismyip;

import java.util.Vector;

import com.adwhirl.AdWhirlLayout;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
		
		TextView gatewayLabel = (TextView)findViewById(R.id.gateway_label);
		gatewayLabel.setText(System.getDefaultGateway());
		ImageView gatewayIcon = (ImageView)findViewById(R.id.gateway_icon);
		gatewayIcon.setImageResource(R.drawable.globe);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.top_layout);
		AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "360059d9611b4feeab87c4c05b210658");
		RelativeLayout.LayoutParams adWhirlLayoutParams = new RelativeLayout.LayoutParams(320, 52);
		layout.addView(adWhirlLayout, adWhirlLayoutParams);
		
	}
	
	class IconicAdapter extends ArrayAdapter {
		  IconicAdapter() {
		    super(WhatIsMyIp.this, R.layout.row, interfaces.toArray());
		  }
		  
		  public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			TextView label = (TextView) row.findViewById(R.id.label);
			String[] ifacedata = interfaces.get(position);
			label.setText(ifacedata[1]);
			TextView mask = (TextView) row.findViewById(R.id.mask);
			mask.setText("/" + ifacedata[2]);
			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			String iface = ifacedata[0];

			if (iface.equals("lo")) {
				label.setTextSize(19);
				mask.setTextSize(14);
				icon.setImageResource(R.drawable.arrow_refresh);
			}
			if (iface.matches("^eth\\d+")) {
				icon.setImageResource(R.drawable.server);
			}
			if (iface.matches("^rmnet\\d+")) {
				icon.setImageResource(R.drawable.telephone);
			}
			if (iface.matches("^tiwlan\\d+")) {
				icon.setImageResource(R.drawable.transmit_blue);
			}
			
			TextView ifname = (TextView) row.findViewById(R.id.ifname);
			ifname.setText(iface);
			return (row);
		}

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, 1, Menu.NONE, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details);
		return result;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(appTag, "menu:"+item.getItemId());
		
		switch (item.getItemId()) {
		case 1:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.about)
			       .setCancelable(false)
			       .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			builder.create().show();
			break;
		case 2:
			
			break;
		}
		
		return false;
	}

}
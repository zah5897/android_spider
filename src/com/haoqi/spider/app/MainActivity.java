package com.haoqi.spider.app;

import com.haoqi.spider.app.common.Conts;
import com.haoqi.spider.app.core.SpiderManager;
import com.haoqi.spider.app.service.SWService;
import com.haoqi.spider.app.util.WiFiUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView stateView;

	private BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		stateView = (TextView) findViewById(R.id.state);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				stateView.setText("已停止");
				findViewById(R.id.tip).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.tip)).setText("非WIFI网络环境下无法采集!");
			}
		};
		registerReceiver(receiver, new IntentFilter(
				SWService.ACTION_SPIDER_CANNOT_RUN));

		if (SpiderManager.isRun()) {
			stateView.setText("运行中...");
		} else {
			stateView.setText("已停止");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean isWifi = WiFiUtil.isWifi(this);
		boolean setting_wifi = PreferenceManager.getDefaultSharedPreferences(
				this).getBoolean(Conts.SETTING_SPIDER_NETWORK, true);
		if (!setting_wifi && !isWifi) {
			findViewById(R.id.tip).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.tip).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	public void start(View v) {
		stateView.setText("运行中...");
		Intent i = new Intent(this, SWService.class);
		i.setAction(SWService.ACTION_SPIDER_TIMER);
		startService(i);
	}

	public void stop(View v) {
		stateView.setText("已停止");
	}

	public void datalist(View v) {
		startActivity(new Intent(this, BrowerActivity.class));
	}

	public void website(View v) {
	}

	public void setting(View v) {
		startActivity(new Intent(this, Setting.class));
	}

	public void send(View v) {
		Intent i = new Intent(this, SWService.class);
		i.setAction(SWService.ACTION_SPIDER_NEXT);
		startService(i);
	}

}

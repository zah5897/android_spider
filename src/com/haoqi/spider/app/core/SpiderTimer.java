package com.haoqi.spider.app.core;

import com.haoqi.spider.app.service.SWService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by zah on 2016/6/15.
 */
public class SpiderTimer {

	public static final int MINUTE = 1 * 30 * 1000;
	PendingIntent sender;

	public void setTimer(Context context) {
		Intent intent = new Intent(context, SWService.class);
		intent.setAction(SWService.ACTION_SPIDER_TIMER);
		sender = PendingIntent.getBroadcast(context, 0, intent, 0);

		// 开始时间
		long firstime = System.currentTimeMillis();
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// 5秒一个周期，不停的发送广播
		// am.setRepeating(AlarmManager.RTC_WAKEUP, firstime, MINUTE, sender);
		am.setRepeating(2, SystemClock.elapsedRealtime(), MINUTE, sender);
	}

	public void cancel(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.cancel(sender);
	}
}

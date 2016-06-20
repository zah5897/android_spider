package com.haoqi.spider.app.core;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.haoqi.spider.app.service.SWService;
import com.haoqi.spider.app.util.ParentWebsiteUtil;
import com.haoqi.spider.app.web.base.BaseParentWebsite;
import com.haoqi.spider.app.web.base.BaseWebsite;

/**
 * Created by zah on 2016/6/15.
 */
public class SpiderManager {
	private static SpiderManager spiderManager;
	private Context context;
	private SpiderTimer spiderTimer;
	private boolean hasPrepared = false;
	private Queue<BaseWebsite> webSiteQueue;
	private Handler handler;

	private SpiderManager(Context context) {
		this.context = context;
		spiderTimer = new SpiderTimer();
	}

	public static boolean isRun() {
		if (spiderManager == null) {
			return false;
		}
		if (spiderManager.spiderTimer == null) {
			return false;
		}
		return true;
	}

	public static SpiderManager getInstance() {
		return spiderManager;
	}

	public static SpiderManager getInstance(Context context) {
		if (spiderManager == null) {
			spiderManager = new SpiderManager(context);
		}
		return spiderManager;
	}

	private void initSpider() {
		webSiteQueue = new LinkedBlockingQueue<BaseWebsite>();
	}

	public void prepared() {
		if (hasPrepared) {
			return;
		}
		hasPrepared = true;
		initSpider();
		spiderTimer.setTimer(context);
	}

	// public void start() {
	// boolean isWifi = WiFiUtil.isWifi(context);
	// boolean setting_wifi =
	// PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Conts.SETTING_SPIDER_NETWORK,
	// true);
	// if (!setting_wifi && !isWifi) {
	// context.sendBroadcast(new
	// Intent(SpiderService.ACTION_SPIDER_CANNOT_RUN));
	// }
	// if (webSiteQueue.size() == 0) {
	// List<Website> webSites = webSitesManager.getWebSite();
	// for (Website webSite : webSites) {
	// boolean result = webSiteQueue.offer(webSite);
	// }
	// }
	// }

	public BaseWebsite next() {
		return webSiteQueue.poll();
	}

	public void oferWebsite(BaseWebsite webSite) {
		webSiteQueue.offer(webSite);
	}

	public void stop() {
		hasPrepared = false;
		webSiteQueue.clear();
		spiderTimer.cancel(context);
	}

	public void offerParentWebsite() {
		for (BaseParentWebsite parentWebsite : ParentWebsiteUtil
				.loadSpiderItem()) {
			oferWebsite(parentWebsite);
		}
	}

	public void sendAction(final Intent intent) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				context.startService(intent);
			}
		});
	}

	public void sendAction(String action) {
		Intent intent = new Intent(context, SWService.class);
		intent.setAction(action);
		sendAction(intent);
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}

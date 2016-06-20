package com.haoqi.spider.app.core;

import com.haoqi.spider.app.web.base.BaseWebsite;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by zah on 2016/6/15.
 */
public class SpiderService extends Service {
	public static final String ACTION_SPIDER_TIMER = "action_spider_timer_on";
	public static final String ACTION_SPIDER_CANNOT_RUN = "action_spider_cannot_run";
	public static final String ACTION_SPIDER_SUTDOWN = "action_spider_shutdown";
	public static final String ACTION_SPIDER_NEXT = "action_spider_next_website";
	private BroadcastReceiver receiver;
	private WebView webView;
	private Handler handler;

	boolean isLoading = false;
	private BaseWebsite currentWebsite;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SPIDER_TIMER);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Intent i = new Intent();
				i.setAction(ACTION_SPIDER_TIMER);
				context.startService(i);
			}
		};
		registerReceiver(receiver, filter);
		handler = new Handler();
		load("http://news.163.com/latest/");
	}

	private void load(String paramString) {
		isLoading = true;
		setWebview();
		this.webView.loadUrl(paramString);
	}

	private void setWebview() {
		this.webView = new WebView(this);
		this.webView.getSettings().setJavaScriptEnabled(true);
		this.webView.addJavascriptInterface(new InJavaScriptLocalObj(),
				"local_obj");
		this.webView.setWebViewClient(new MyWebViewClient());
	}

	@Override
	public void onDestroy() {
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SpiderManager.getInstance(getApplicationContext()).prepared();
		if (intent != null) {
			String action = intent.getAction();
			if (ACTION_SPIDER_TIMER.equals(action)) {
				spiderParentStart();
			} else if (ACTION_SPIDER_NEXT.equals(action)) {
				spiderWebsite();
			} else if (ACTION_SPIDER_SUTDOWN.equals(action)) {
				SpiderManager.getInstance(getApplicationContext()).stop();
			}
		}
		return START_STICKY;
	}

	private void spiderParentStart() {
		SpiderManager.getInstance(getApplicationContext()).offerParentWebsite();
		SpiderManager.getInstance().sendAction(ACTION_SPIDER_NEXT);
	}

	/**
	 * 鎵ц涓嬩竴涓噰闆嗙珯鐐�
	 */
	private void spiderWebsite() {
		if (isLoading) {
			return;
		}
		currentWebsite = SpiderManager.getInstance(getApplicationContext())
				.next();
		if (currentWebsite != null) {
			load(currentWebsite.url);
		}
	}

	final class InJavaScriptLocalObj {
		InJavaScriptLocalObj() {
		}

		@JavascriptInterface
		public void showSource(String paramString) {
			isLoading = false;
			Log.d("HTML", paramString);
			// FileUtil.saveTxt(paramString);
		}
	}

	final class MyWebViewClient extends WebViewClient {
		MyWebViewClient() {
		}

		public void onPageFinished(WebView paramWebView, String paramString) {
			Log.d("WebView", "onPageFinished ");
			SpiderService.this.handler.postDelayed(new Runnable() {
				public void run() {
					SpiderService.this.webView
							.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
				}
			}, 8000L);
			super.onPageFinished(paramWebView, paramString);
		}

		public void onPageStarted(WebView paramWebView, String paramString,
				Bitmap paramBitmap) {
			Log.d("WebView", "onPageStarted");
			super.onPageStarted(paramWebView, paramString, paramBitmap);
		}

		public boolean shouldOverrideUrlLoading(WebView paramWebView,
				String paramString) {
			paramWebView.loadUrl(paramString);
			return true;
		}
	}
 }

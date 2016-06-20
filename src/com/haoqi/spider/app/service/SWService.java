package com.haoqi.spider.app.service;

import com.haoqi.spider.app.core.SpiderManager;
import com.haoqi.spider.app.core.SpiderService;
import com.haoqi.spider.app.web.base.BaseWebsite;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint({ "HandlerLeak", "SetJavaScriptEnabled" })
public class SWService extends Service {
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
		SpiderManager.getInstance(getApplicationContext()).setHandler(handler);
		setWebview();
	}

	private void load(String paramString) {
		isLoading = true;
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
	public int onStartCommand(Intent intent, int flags, int startId) {
		SpiderManager.getInstance(getApplicationContext()).prepared();
		if (intent != null) {
			String action = intent.getAction();
			if (ACTION_SPIDER_TIMER.equals(action)) {
				spiderParentStart();
			} else if (ACTION_SPIDER_NEXT.equals(action)) {
				if (!isLoading) {
					currentWebsite = SpiderManager.getInstance().next();
					if (currentWebsite != null) {
						String url = currentWebsite.url;
						load(url);
					}
				}
			} else if (ACTION_SPIDER_SUTDOWN.equals(action)) {
				SpiderManager.getInstance(getApplicationContext()).stop();
			}
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	private void spiderParentStart() {
		SpiderManager.getInstance(getApplicationContext()).offerParentWebsite();
		SpiderManager.getInstance().sendAction(ACTION_SPIDER_NEXT);
	}

	final class InJavaScriptLocalObj {
		InJavaScriptLocalObj() {
		}

		@JavascriptInterface
		public void showSource(final String html) {
			isLoading = false;
			Log.d("HTML", html);
			new Thread() {
				public void run() {
					currentWebsite.prase(html);
					SpiderManager.getInstance().sendAction(
							SpiderService.ACTION_SPIDER_NEXT);
				}
			}.start();

		}
	}

	final class MyWebViewClient extends WebViewClient {
		MyWebViewClient() {
		}

		public void onPageFinished(WebView paramWebView, String paramString) {
			Log.d("WebView", "onPageFinished ");
			SWService.this.handler.postDelayed(new Runnable() {
				public void run() {
					SWService.this.webView
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

package com.haoqi.spider.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by zah on 2016/6/15.
 */
public class BrowerActivity extends Activity {
	private WebView webView;
	private TextView textView;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brower);
		webView = (WebView) findViewById(R.id.webview);
		textView = (TextView) findViewById(R.id.content);

		WebSettings webSettings = webView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webView.setWebViewClient(new MyWebViewClient());
		webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

		webView.loadUrl("http://news.163.com/latest/");
	}

	final class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d("WebView", "onPageStarted");
			super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(final WebView view, String url) {
			Log.d("WebView", "onPageFinished ");

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
							+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
				}
			}, 2000);
			super.onPageFinished(view, url);
		}
	}

	final class InJavaScriptLocalObj {
		@JavascriptInterface
		public void showSource(final String html) {
			Log.d("HTML", html);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					textView.setText(html);
				}
			});
		}
	}
}

package com.haoqi.spider.app.web.base;

/**
 * Created by zah on 2016/6/15.
 */
public class BaseWebsite {
	public String url;
	public String name;

	public BaseWebsite(String url, String name) {
		this.url = url;
		this.name = name;
	}

	public void prase(String html) {
	}
}

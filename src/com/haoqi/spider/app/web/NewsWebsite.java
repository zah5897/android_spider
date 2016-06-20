package com.haoqi.spider.app.web;

import com.haoqi.spider.app.bean.News;
import com.haoqi.spider.app.web.base.BaseParentWebsite;
import com.haoqi.spider.app.web.base.BaseWebsite;

/**
 * Created by zah on 2016/6/16.
 */
public class NewsWebsite extends BaseWebsite {
	public NewsWebsite(String url, String name) {
		super(url, name);
	}

	public BaseParentWebsite parent;
	public News simpleNews;

	@Override
	public void prase(String html) {
		super.prase(html);
	}
}

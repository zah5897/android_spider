package com.haoqi.spider.app.util;

import java.util.ArrayList;
import java.util.List;

import com.haoqi.spider.app.web.NewsParentWebsite;
import com.haoqi.spider.app.web.base.BaseParentWebsite;

/**
 * Created by zah on 2016/6/16.
 */
public class ParentWebsiteUtil {
	public static List<BaseParentWebsite> loadSpiderItem() {
		List<BaseParentWebsite> items = new ArrayList<BaseParentWebsite>();
		items.add(new NewsParentWebsite("网易-滚动新闻",
				"http://news.163.com/latest/"));
		// items.add(new NewsParentWebsite("网易-滚动新闻",
		// "http://www.pengfu.com/xiaohua_1.html"));
		return items;
	}
}

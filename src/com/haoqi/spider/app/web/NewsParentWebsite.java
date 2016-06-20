package com.haoqi.spider.app.web;

import android.os.Environment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.haoqi.spider.app.bean.News;
import com.haoqi.spider.app.core.SpiderManager;
import com.haoqi.spider.app.util.FileUtil;
import com.haoqi.spider.app.web.base.BaseParentWebsite;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * Created by zah on 2016/6/16.
 */
public class NewsParentWebsite extends BaseParentWebsite {
	public NewsParentWebsite(String name, String url) {
		super(name, url);
	}

	@Override
	public void prase(String html) {
		Document doc = null;
		File parent = new File(Environment.getExternalStorageDirectory()
				+ "/spider/");
		parent.mkdir();

		File htmlFile = new File(parent.getAbsolutePath(),
				System.currentTimeMillis() + ".txt");
		FileUtil.toFile(html.getBytes(), htmlFile.getAbsolutePath());

		try {
			doc = Jsoup.parse(htmlFile, "GBK");
			Elements newsHeadlines = doc.select("li.net-newsitem");
			for (Iterator<Element> i = newsHeadlines.iterator(); i.hasNext();) {
				Element item = i.next();
				String href = item.select("a[href]").get(0).attr("href");
				String title = item.select("p.net-newstitle").get(0).text();
				String time = item.select("span.net-newstips").get(0).text();

				System.out.println("href=" + href);
				System.out.println("title=" + title);
				System.out.println("time=" + time);

				NewsWebsite website = new NewsWebsite(title, href);
				News simpleNews = new News(title, time);
				website.parent = this;
				website.simpleNews = simpleNews;
				SpiderManager.getInstance().oferWebsite(website);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			// htmlFile.delete();
		}
	}
}

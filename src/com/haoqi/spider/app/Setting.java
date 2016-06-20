package com.haoqi.spider.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

import com.haoqi.spider.app.common.Conts;

/**
 * Created by zah on 2016/6/15.
 */
public class Setting extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	private CheckBoxPreference mCheckPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		initPreferences();
	}

	private void initPreferences() {
		mCheckPreference = (CheckBoxPreference) findPreference(Conts.SETTING_SPIDER_NETWORK);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Setup the initial values
		SharedPreferences sharedPreferences = getPreferenceScreen()
				.getSharedPreferences();
		mCheckPreference.setChecked(sharedPreferences.getBoolean(
				Conts.SETTING_SPIDER_NETWORK, true));
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (Conts.SETTING_SPIDER_NETWORK.equals(key)) {
			if (!mCheckPreference.isChecked()) {
				new AlertDialog.Builder(this)
						.setMessage("非WIFI环境下会产生大量流量消耗，确定还要这样做吗?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										mCheckPreference.setChecked(true);
									}
								}).show();
			}
			sharedPreferences.edit().putBoolean(Conts.SETTING_SPIDER_NETWORK,
					mCheckPreference.isChecked());
		}

	}
}

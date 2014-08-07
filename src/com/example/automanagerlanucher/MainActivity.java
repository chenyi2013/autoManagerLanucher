package com.example.automanagerlanucher;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {

	private RadioGroup mMenuGroup;
	private FragmentManager mFragmentManager;
	private AppInfoListFrament mAppInfoListFrament;
	private SettingsFragment mSettingsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();

	}

	public void initView() {
		
		mAppInfoListFrament = new AppInfoListFrament();
		mSettingsFragment = new SettingsFragment();

		mFragmentManager = getFragmentManager();
		mFragmentManager.beginTransaction()
				.add(R.id.main_layout, mAppInfoListFrament).commit();

		mMenuGroup = (RadioGroup) findViewById(R.id.tab_host);
		mMenuGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.app_info_radio:
					mFragmentManager.beginTransaction()
							.replace(R.id.main_layout, mAppInfoListFrament)
							.commit();
					break;
				case R.id.setting_radio:
					mFragmentManager.beginTransaction()
							.replace(R.id.main_layout, mSettingsFragment)
							.commit();
					break;

				default:
					break;
				}

			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAppInfoListFrament = null;
		mSettingsFragment = null;
	}
}

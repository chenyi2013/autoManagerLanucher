package com.example.automanagerlanucher;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppInfoListFrament extends ListFragment {

	private static final int LOAD_DATA_SUCCESS = 1;

	private PackageManager mPackageManager;
	private List<PackageInfo> mPackageInfos;
	private Activity mActivity;
	private LayoutInflater mLayoutInflater;
	private AppInfoAdapter mAdapter;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				mPackageInfos = (List<PackageInfo>) msg.obj;
				mAdapter = new AppInfoAdapter();
				mAdapter.setData(mPackageInfos);
				getListView().setAdapter(mAdapter);
				break;

			default:
				break;
			}
		};
	};

	public AppInfoListFrament() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mLayoutInflater = mActivity.getLayoutInflater();

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (mActivity != null) {
					mPackageManager = mActivity.getPackageManager();

					Message msg = mHandler.obtainMessage();
					msg.obj = mPackageManager
							.getInstalledPackages(PackageManager.GET_ACTIVITIES);
					;
					msg.what = LOAD_DATA_SUCCESS;
					msg.sendToTarget();
				}

			}
		}).start();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.app_info_fragment_layout, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setEmptyView(getView().findViewById(R.id.progress_bar));
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

	}

	class ViewHolder {
		TextView packageName;
		ImageView appIcon;
	}

	class AppInfoAdapter extends BaseAdapter {

		private List<PackageInfo> data = new ArrayList<PackageInfo>();

		public void setData(List<PackageInfo> data) {
			if (data != null) {
				this.data.clear();
				this.data.addAll(data);
			}
		}

		@Override
		public int getCount() {

			return data.size();
		}

		@Override
		public Object getItem(int position) {

			return data.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (convertView == null) {
				if (mLayoutInflater != null) {
					convertView = mLayoutInflater.inflate(
							R.layout.app_info_item, parent, false);
					viewHolder = new ViewHolder();
					viewHolder.packageName = (TextView) convertView
							.findViewById(R.id.package_name);
					viewHolder.appIcon = (ImageView) convertView
							.findViewById(R.id.app_icon);
					convertView.setTag(viewHolder);
				}
			}

			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.packageName.setText(data.get(position).packageName);

			try {
				Intent intent = mPackageManager.getLaunchIntentForPackage(data
						.get(position).packageName);
				if (intent != null) {
					Drawable drawable = mPackageManager.getActivityIcon(intent);
					viewHolder.appIcon.setImageDrawable(drawable);
				} else {
					viewHolder.appIcon.setImageResource(R.drawable.ic_launcher);
				}

			} catch (NameNotFoundException e) {

				e.printStackTrace();
			}

			return convertView;
		}
	}

}

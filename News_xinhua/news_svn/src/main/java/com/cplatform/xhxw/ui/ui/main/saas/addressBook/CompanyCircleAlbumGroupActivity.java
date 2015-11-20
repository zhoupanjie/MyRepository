package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyCircleAlbumGroupAdapter.OnAlbumGroupListener;
import com.cplatform.xhxw.ui.util.ListUtil;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CompanyCircleAlbumGroupActivity extends BaseActivity implements
		LoaderManager.LoaderCallbacks<HashMap<String, List<String>>>,
		OnItemClickListener, OnAlbumGroupListener {

	private static final int ALBUMPREVIEW = 1;

	private List<String> selectImages;
	
	private static final String TAG = CompanyCircleAlbumGroupActivity.class
			.getSimpleName();

	private ListView mListView;
	private CompanyCircleAlbumGroupAdapter mAdapter;

	public static Intent newIntent(Context context, List<String> selectImages) {
		Intent intent = new Intent(context,
				CompanyCircleAlbumGroupActivity.class);
		intent.putExtra("selectImages", (ArrayList<String>)selectImages);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_circle_album_group);

		init();

		setListener();
	}

	private void init() {
		initActionBar();

		mListView = (ListView) findViewById(R.id.album_listView);

		mAdapter = new CompanyCircleAlbumGroupAdapter(this);
		mListView.setAdapter(mAdapter);

		selectImages = (List<String>)getIntent().getSerializableExtra("selectImages");
		
		getSupportLoaderManager().initLoader(0, null, this);
	}

	private void setListener() {
		mListView.setOnItemClickListener(this);
		mAdapter.setOnAlbumGroupListener(this);
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivityForResult(CompanyCircleAlbumPreviewActivity.newIntent(
                CompanyCircleAlbumGroupActivity.this,
                mAdapter.getItem(position), selectImages), ALBUMPREVIEW);
    }

    @Override
	public List<String> getPhoto(String groupName) {
		return CompanyCircleAlbumGroupManager.getInstance().getListForKey(
				groupName);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		
		switch (requestCode) {
		case ALBUMPREVIEW:
			if (data != null) {
				List<String> result = (List<String>) data
						.getSerializableExtra("result");
				if (!ListUtil.isEmpty(result)) {
					Intent intent = new Intent();
					intent.putExtra("result", (Serializable) result);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public Loader<HashMap<String, List<String>>> onCreateLoader(int i,
			Bundle bundle) {
		showLoadingView();
		return new ChannelListLoader(this);
	}

	@Override
	public void onLoadFinished(
			Loader<HashMap<String, List<String>>> hashMapLoader,
			HashMap<String, List<String>> stringListHashMap) {
		hideLoadingView();
		if (stringListHashMap != null) {
			CompanyCircleAlbumGroupManager.getInstance().addData(
					stringListHashMap);
			List<String> list = CompanyCircleAlbumGroupManager.getInstance()
					.getListKey();
			mAdapter.getList().addAll(list);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(
			Loader<HashMap<String, List<String>>> hashMapLoader) {
	}

	public static class ChannelListLoader extends
			AsyncTaskLoader<HashMap<String, List<String>>> {

		HashMap<String, List<String>> _gruopMap;

		public ChannelListLoader(Context context) {
			super(context);
		}

		@Override
		public void deliverResult(HashMap<String, List<String>> apps) {
			_gruopMap = apps;
			if (isStarted()) {
				super.deliverResult(apps);
			}
		}

		@Override
		protected void onStartLoading() {
			if (_gruopMap != null) {
				deliverResult(_gruopMap);
			}

			if (takeContentChanged() || _gruopMap == null) {
				forceLoad();
			}
		}

		@Override
		protected void onStopLoading() {
			cancelLoad();
		}

		@Override
		protected void onReset() {
			super.onReset();

			onStopLoading();
			if (_gruopMap != null) {
				_gruopMap = null;
			}
		}

		@Override
		public HashMap<String, List<String>> loadInBackground() {
			Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			ContentResolver mContentResolver = getContext()
					.getContentResolver();

			Cursor mCursor = mContentResolver.query(mImageUri, null,
					MediaStore.Images.Media.MIME_TYPE + "=? or "
							+ MediaStore.Images.Media.MIME_TYPE + "=?",
					new String[] { "image/jpeg", "image/png" },
					MediaStore.Images.Media.DATE_MODIFIED + " desc");

			if (mCursor == null) {
				return null;
			}
			HashMap<String, List<String>> gruopMap = new HashMap<String, List<String>>();
			while (mCursor.moveToNext()) {
				String path = mCursor.getString(mCursor
						.getColumnIndex(MediaStore.Images.Media.DATA));

				String parentName = new File(path).getParentFile().getName();

				if (!gruopMap.containsKey(parentName)) {
					List<String> chileList = new ArrayList<String>();
					chileList.add(path);
					gruopMap.put(parentName, chileList);
				} else {
					gruopMap.get(parentName).add(path);
				}

			}

			return gruopMap;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CompanyCircleAlbumGroupManager.getInstance().cleanData();
	}

}

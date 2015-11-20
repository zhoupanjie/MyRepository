package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.plugin.gallery.PicShowActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyCircleAlbumPreviewAdapter.OnAlbumPreviewListener;
import com.cplatform.xhxw.ui.util.SendImageTextUtil;

/**
 * 图片列表 Created by cy-love on 14-6-7.
 */
public class CompanyCircleAlbumPreviewActivity extends BaseActivity
		implements OnAlbumPreviewListener, OnClickListener, OnItemClickListener {

	private static final String TAG = CompanyCircleAlbumPreviewActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PREVIEW = 1;
	
	private static final String KEY_GROUP = "key_group";
	private static final String SELECTIMAGES = "select_images";
	
	private GridView mGridView;
	private TextView mNum;
	private Button mSuccessBtn; // 完成
	private Button mPreviewBtn; // 预览
	private CompanyCircleAlbumPreviewAdapter mAdapter;

	public static Intent newIntent(Context context, String group, List<String> selectImages) {
		Intent intent = new Intent(context,
				CompanyCircleAlbumPreviewActivity.class);
		intent.putExtra(CompanyCircleAlbumPreviewActivity.KEY_GROUP, group);
		intent.putExtra(SELECTIMAGES, (ArrayList<String>)selectImages);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_company_circle_album_preview);
		
		init();
	}

	private void init() {
		initActionBar();

		mGridView = (GridView) findViewById(R.id.grid_view);
		mNum = (TextView) findViewById(R.id.tv_num);
		mPreviewBtn = (Button) findViewById(R.id.preview_btn);
		mSuccessBtn = (Button) findViewById(R.id.success_btn);

		mAdapter = new CompanyCircleAlbumPreviewAdapter(this);
		mAdapter.setOnAlbumPreviewListener(this);
		mGridView.setAdapter(mAdapter);
		
		List<String> strings = CompanyCircleAlbumGroupManager.getInstance().getListForKey(
				getIntent().getStringExtra(
				CompanyCircleAlbumPreviewActivity.KEY_GROUP));
		
		List<String> selectImages = (List<String>)getIntent().getSerializableExtra(
				CompanyCircleAlbumPreviewActivity.SELECTIMAGES);
		if (strings != null) {
			for (int i = 0; i < strings.size(); i++) {
				AlbumGroup albumGroup = new AlbumGroup();
				albumGroup.setUrl(strings.get(i));
				albumGroup.setChecked(false);
				mAdapter.getDateList().add(albumGroup);
			}
			
			if (selectImages != null && selectImages.size() > 0) {
				for (int i = 0; i < selectImages.size(); i++) {
					mAdapter.getSelectList().add(selectImages.get(i));
				}
			}
			
			mAdapter.notifyDataSetChanged();
		}

		if (mAdapter.getSelectList().size() == 0) {
			mNum.setVisibility(View.INVISIBLE);
			mSuccessBtn.setEnabled(false);
		} else {
			mNum.setText(String.valueOf(mAdapter.getSelectList().size()));
			mNum.setVisibility(View.VISIBLE);
			mSuccessBtn.setEnabled(true);
		}
        mPreviewBtn.setOnClickListener(this);
        mSuccessBtn.setOnClickListener(this);
        mGridView.setOnItemClickListener(this);
	}

	@Override
	public void updateSelectCount(int count) {
		if (count == 0) {
			mNum.setVisibility(View.INVISIBLE);
			mSuccessBtn.setEnabled(false);
		} else {
			mNum.setText(String.valueOf(count));
			mNum.setVisibility(View.VISIBLE);
			mSuccessBtn.setEnabled(true);
		}
	}

	@Override
	public void onClick(View view) {
        List<String> selects = mAdapter.getSelectList();
		switch (view.getId()) {
		case R.id.success_btn: // 完成
			Intent data = new Intent();
			data.putExtra("result", (Serializable) selects);
			setResult(Activity.RESULT_OK, data);
			finish();
			break;
		case R.id.preview_btn: // 预览
        {
            startActivityForResult(PicShowActivity.newIntent(this, selects, 0, true), REQUEST_CODE_PREVIEW);
        }
			break;
		default:
			break;
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_PREVIEW:
                if (data != null && data.getSerializableExtra("result") != null) {
                    List<String> result = (List<String>)data.getSerializableExtra("result");
                    List<String> selects = mAdapter.getSelectList();
                    selects.clear();
                    selects.addAll(result);
                    mAdapter.notifyDataSetChanged();
                    updateSelectCount(selects.size());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		mAdapter.getSelectList().clear();
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int selectCount = mAdapter.getSelectList().size();
        if (mAdapter.getSelectList().contains(mAdapter.getItem(position).getUrl())) {
            mAdapter.getSelectList().remove(mAdapter.getItem(position).getUrl());
            selectCount--;
            mAdapter.notifyDataSetChanged();
        } else {
            if (selectCount < SendImageTextUtil.COUNT) {
                mAdapter.getSelectList().add(mAdapter.getItem(position).getUrl());
                selectCount++;
                mAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(this, "图片达到上限了", Toast.LENGTH_SHORT).show();
            }
        }

        if (selectCount == 0) {
            mNum.setVisibility(View.INVISIBLE);
            mSuccessBtn.setEnabled(false);
            mPreviewBtn.setEnabled(false);
        } else {
            mNum.setText(String.valueOf(selectCount));
            mNum.setVisibility(View.VISIBLE);
            mSuccessBtn.setEnabled(true);
            mPreviewBtn.setEnabled(true);
        }
    }
}

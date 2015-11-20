package com.cplatform.xhxw.ui.ui.recommendimages;

import android.os.Bundle;
import butterknife.Bind;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.RecommendImages;

public class RecommendImagesActivity extends BaseActivity{

	@Bind(R.id.recommendImages) RecommendImages recommendImages;

    @Override
    protected String getScreenName() {
        RecommendImagesActivity.class.getName();
        return "RecommendImagesActivity";
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.recommend_images_activity);
	}
}

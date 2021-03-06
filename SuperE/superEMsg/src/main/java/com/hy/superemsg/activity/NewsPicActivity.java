
package com.hy.superemsg.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.baseproject.image.ImageCallback;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.req.ReqNewsDetailQeury;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Image;
import com.hy.superemsg.rsp.NewsContentDetail;
import com.hy.superemsg.rsp.RspNewsDetailQeury;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.ImageUtils;
import com.hy.superemsg.utils.ShareUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.ScrollTabsViewPager;
import com.hy.superemsg.viewpager.fragments.PicFragment;

public class NewsPicActivity extends FragmentActivity {
    private NewsContentDetail news;
    private ScrollTabsViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        news = getIntent().getParcelableExtra(
                SuperEMsgApplication.EXTRA_NEWS_DETAIL);
        pager = (ScrollTabsViewPager) findViewById(R.id.viewpager);
        UITitle title = (UITitle) this.findViewById(R.id.ui_title);
        if (title != null) {
            title.setTitleText(R.string.news_pic);
            title.setBackGroundColor(0x800000);
            title.setLeftButton(R.drawable.back, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        findViewById(R.id.item_btn).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(NewsPicActivity.this)
                                .setItems(R.array.menu_sms_share,
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                if (which == 0) {
                                                    // weixin
                                                    ShareUtils
                                                            .shareByWeixin(
                                                                    NewsPicActivity.this,
                                                                    getString(R.string.news_pic));
                                                } else if (which == 1) {
                                                    // weibo
                                                    PicFragment fragment = (PicFragment) pager
                                                            .getCurrentSelectedFragment();
                                                    Bitmap bmp = fragment.getBitmap();
                                                    if (bmp == null)
                                                        return;
                                                    ShareUtils
                                                            .shareByWeibo(
                                                                    NewsPicActivity.this,
                                                                    "美图",
                                                                    bmp);
                                                }
                                            }
                                        }).create().show();

                    }
                });
        findViewById(R.id.item_btn1).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PicFragment fragment = (PicFragment) pager.getCurrentSelectedFragment();
                        Bitmap bmp = fragment.getBitmap();
                        if (bmp == null) {
                            Toast.makeText(NewsPicActivity.this, "下载图片失败", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        String url = MediaStore.Images.Media.insertImage(
                                getContentResolver(), bmp, news.newstitle,
                                news.newstitle);
                        if (!TextUtils.isEmpty(url)) {
                            sendBroadcast(new Intent(
                                    Intent.ACTION_MEDIA_MOUNTED,
                                    Uri.parse("file://"
                                            + Environment
                                                    .getExternalStorageDirectory()
                                            + url)));
                            Toast.makeText(NewsPicActivity.this,
                                    "图片已保存到" + url, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        HttpUtils.getInst().excuteTask(new ReqNewsDetailQeury(news.newsid),
                new AsynHttpCallback() {

                    @Override
                    public void onSuccess(BaseRspApi rsp) {
                        RspNewsDetailQeury detail = (RspNewsDetailQeury) rsp;
                        if (detail != null && detail.newsdetail != null) {
                            List<Image> imgs = detail.newsdetail.img;
                            if (CommonUtils.isNotEmpty(imgs)) {
                                for (Image img : imgs) {
                                    String src = img.src;
                                    if (!TextUtils.isEmpty(src)) {
                                        PicFragment fragment = new PicFragment();
                                        Bundle b = fragment.getArguments();
                                        if (b == null) {
                                            b = new Bundle();
                                        }
                                        b.putParcelable(SuperEMsgApplication.EXTRA_PIC_DETAIL, img);
                                        fragment.setArguments(b);
                                        pager.addPager(img.alt, fragment);
                                    }
                                }
                                pager.setUp(NewsPicActivity.this);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        SuperEMsgApplication.toast(error);
                    }
                });
    }
}

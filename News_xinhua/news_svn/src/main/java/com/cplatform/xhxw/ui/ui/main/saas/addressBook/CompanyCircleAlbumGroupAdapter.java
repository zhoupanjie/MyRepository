package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class CompanyCircleAlbumGroupAdapter extends BaseAdapter{

    private OnAlbumGroupListener listener;
    private List<String> list;
    private LayoutInflater mInflater;
    private Context mContext;

    public CompanyCircleAlbumGroupAdapter(Context context) {
        mContext = context;
        list = new ArrayList<String>();
        mInflater = LayoutInflater.from(context);
    }

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHelper helper;
        if (convertView == null) {
        	helper = new ViewHelper();
            convertView = mInflater.inflate(R.layout.fragment_album_preview_group_item, null);
            helper.album = (ImageView) convertView.findViewById(R.id.iv_album);
            helper.mTitle = (TextView) convertView.findViewById(R.id.tv_title);
            
            convertView.setTag(helper);
        } else {
            helper = (ViewHelper) convertView.getTag();
        }
        
        String groupName = getItem(position);
        List<String> photos = listener.getPhoto(groupName);
        if (ListUtil.isEmpty(photos)) {
            helper.mTitle.setText(groupName + " (0)");
            helper.album.setImageResource(R.drawable.def_img_4_3);
        } else {
            helper.mTitle.setText(groupName + " (" + photos.size() + ")");
            Picasso.with(mContext).load(new File(photos.get(0))).centerCrop().resizeDimen(R.dimen.album_preview_img_size, R.dimen.album_preview_img_size).error(R.drawable.def_img_4_3).into(helper.album);
        }

        return convertView;
    }

    static class ViewHelper {
        ImageView album;
        TextView mTitle;

    }

    public interface OnAlbumGroupListener {
        public List<String> getPhoto(String groupName);
    }
    
    public void setOnAlbumGroupListener(OnAlbumGroupListener listener) {
		this.listener = listener;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}


}

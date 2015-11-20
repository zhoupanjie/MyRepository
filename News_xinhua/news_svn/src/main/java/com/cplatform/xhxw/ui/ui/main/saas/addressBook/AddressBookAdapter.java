package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 通讯录
 * Created by cy-love on 14-8-3.
 */
public class AddressBookAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public AddressBookAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.view_address_book_item, null);
        ViewHelper helper = new ViewHelper(view);
        view.setTag(helper);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int iPhoto = cursor.getColumnIndex(ContactsDao.LOGO);
        int iName = cursor.getColumnIndex(ContactsDao.NAME);
        int iIndex = cursor.getColumnIndex(ContactsDao.INDEX_KEY);
        int iNick = cursor.getColumnIndex(ContactsDao.NICK_NAME);
        int iComment = cursor.getColumnIndex(ContactsDao.COMMENT);
        String photo = cursor.getString(iPhoto);
        String name = cursor.getString(iName);
        String index = cursor.getString(iIndex);
        String nick = cursor.getString(iNick);
        String comment = cursor.getString(iComment);
        ViewHelper helper = (ViewHelper) view.getTag();
        int posi = cursor.getPosition();
        if (posi == 0 && !TextUtils.isEmpty(index)) {
            helper.index.setVisibility(View.VISIBLE);
            helper.index.setText(index);
        } else if (!TextUtils.isEmpty(index)
                && posi > 0
                && cursor.moveToPosition(posi - 1)
                && !index.equals(cursor.getString(iIndex))) {
            helper.index.setVisibility(View.VISIBLE);
            helper.index.setText(index);
        } else {
            helper.index.setVisibility(View.GONE);
        }

//        String nameStr = (!TextUtils.isEmpty(comment)) ? comment : (!TextUtils.isEmpty(nick)) ? nick : name;
        helper.name.setText(SelectNameUtil.getName(comment, nick, name));
        ImageLoader.getInstance().displayImage(photo, helper.photo, DisplayImageOptionsUtil.avatarSaasImagesOptions);
    }

    static class ViewHelper {
        @Bind(R.id.iv_photo)
        ImageView photo;
        @Bind(R.id.tv_name)
        TextView name;
        @Bind(R.id.tv_index)
        TextView index;

        public ViewHelper(View v) {
            ButterKnife.bind(this, v);
        }
    }
}


package com.hy.superemsg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.data.Contact;

public class ContactsListAdapter1 extends AbsPinedContactAdapter {
    public ContactsListAdapter1(Context ctx) {
        super(ctx, R.layout.item_grid);
    }

    public interface CheckListener {
        void onItemChecked(Contact contact);
    }

    private CheckListener listener;

    public void setListener(CheckListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        if (contact.isPointer) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_pointer, null);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact1, null);
        }
        getView(position, convertView, contact);
        return convertView;
    }

    public void checkAll(boolean check) {
        if (getCount() > 0) {
            int count = getCount();
            for (int i = 0; i < count; i++) {
                Contact contact = getItem(i);
                if (!contact.isPointer) {
                    contact.isSelected = check;
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void getView(int position, final View convertView, Contact contact) {
        TextView nameView = (TextView) convertView.findViewById(R.id.item_text);
        ImageView checkbox = (ImageView) convertView.findViewById(R.id.item_image);
        TextView phoneView = (TextView) convertView
                .findViewById(R.id.item_text1);
        if (!TextUtils.isEmpty(contact.name)) {
            nameView.setText(contact.name);
        }
        if (!TextUtils.isEmpty(contact.phone)) {
            phoneView.setText(contact.phone);
        }
        if (!contact.isPointer) {
            boolean check = contact.isSelected;
            if (check) {
                checkbox.setImageResource(R.drawable.checked);
            } else {
                checkbox.setImageResource(0);
            }
        }
        convertView.setTag(contact);
        if (!contact.isPointer) {

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Contact contact = (Contact) v.getTag();
                    contact.isSelected = !contact.isSelected;
                    if (listener != null) {
                        listener.onItemChecked(contact);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}

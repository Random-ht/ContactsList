package com.random.hutao;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutao on 2018/3/31.
 */

public class ContactsListAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<ContactsBean> mData = new ArrayList<>();

    public ContactsListAdapter(Context context, List<ContactsBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contacts, null);
            holder.head = convertView.findViewById(R.id.head);
            holder.name = convertView.findViewById(R.id.name);
            holder.tel = convertView.findViewById(R.id.tel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String content = mData.get(position).getSortLetters();

        int section = getSectionForPosition(position);
        int startSectionPosition = getPositionForSection(section);
        if (position == startSectionPosition) {
            holder.head.setVisibility(View.VISIBLE);
            holder.head.setText(String.valueOf(content.charAt(0)));  // getCodeFail: contactInfo.getSortLetters().charAt(0)
        } else {
            holder.head.setVisibility(View.GONE);
        }

        holder.name.setText(mData.get(position).getRawName());
        holder.tel.setText(TextUtils.isEmpty(mData.get(position).getTel()) ? "假数据没保存电话号码" : mData.get(position).getTel());

        return convertView;
    }


    class ViewHolder {
        TextView head;
        TextView name;
        TextView tel;
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            if (section == getSectionForPosition(i)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mData.get(position).getSortLetters().charAt(0);
    }

    public void updateContactInfoList(List<ContactsBean> list) {
        mData = list;
        notifyDataSetChanged();
    }
}

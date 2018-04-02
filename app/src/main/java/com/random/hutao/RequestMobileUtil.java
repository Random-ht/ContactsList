package com.random.hutao;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by hutao on 2018/3/31.
 * <p>
 * 获取手机联系人的工具类
 */

public class RequestMobileUtil {
    /**
     * 需要获取的权限
     */
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    };

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;


    public static ArrayList<ContactsBean> getPhoneContacts(Activity context) {

        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContactsBean> mContacts = new ArrayList<ContactsBean>();//本地联系人的列表

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX).toString().replaceAll("-", "").replaceAll(" ", "");
                // 当手机号码为空的或者为空字段 跳过当前循环

                if (TextUtils.isEmpty(phoneNumber)) continue;

                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                // 得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                // 得到联系s人头像Bitamp
                Bitmap contactPhoto = null;
                // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                } else {
                    contactPhoto = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
                }

                ContactsBean mContact = new ContactsBean("");

                mContact.setName(contactName);
                mContact.setTel(phoneNumber);

                mContacts.add(mContact);
            }

            Log.i("获取本地联系人个数：", mContacts.size() + "");

            phoneCursor.close();
        }

        return mContacts;
    }
}

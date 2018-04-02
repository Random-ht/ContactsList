package com.random.hutao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private IndexSideBar mIndexSideBar;
    private TextView mTvLetter;
    private ContactsListAdapter mAdapter;
    List<ContactsBean> mData = new ArrayList<>();
    private EditText mEtSearch;
    private List<ContactsBean> mContactInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //如果不想获取手机联系人就直接把注释打开即可
//        initData();

        //获取所有的通讯录  然后刷新列表
        refreshContacts();//此方法应该在mData更新的时候调用
        initListener();
    }

    private void initView() {
        mListView = findViewById(R.id.listView);
        mIndexSideBar = findViewById(R.id.index);
        mTvLetter = findViewById(R.id.letter);
        mEtSearch = findViewById(R.id.search);

        try {
            //我没有动态获取权限   所以直接把  targetSdkVersion  调成了 21
            mData = RequestMobileUtil.getPhoneContacts(this);
        } catch (Exception e) {
            Log.e("getContactsFail", "获取联系人列表异常");
        }
    }

    private void initData() {
        mData.clear();//为了清除获取的手机联系人信息
        ContactsBean bean1 = new ContactsBean("风格士");
        ContactsBean bean2 = new ContactsBean("士大");
        ContactsBean bean3 = new ContactsBean("夫似的");
        ContactsBean bean4 = new ContactsBean("似的");
        ContactsBean bean5 = new ContactsBean("士大");
        ContactsBean bean6 = new ContactsBean("的吗");
        ContactsBean bean7 = new ContactsBean("喇嘛");
        ContactsBean bean8 = new ContactsBean("马颂德");
        ContactsBean bean9 = new ContactsBean("证码");
        ContactsBean bean10 = new ContactsBean("群殴啊");
        ContactsBean bea1 = new ContactsBean("111");
        ContactsBean bea2 = new ContactsBean("10086");
        ContactsBean bea3 = new ContactsBean("A");
        ContactsBean bea4 = new ContactsBean("屡屡");
        ContactsBean bea5 = new ContactsBean("00");
        ContactsBean bea6 = new ContactsBean(".。阿萨");
        ContactsBean bea7 = new ContactsBean("@Polk");
        ContactsBean bea8 = new ContactsBean("林峰");
        ContactsBean bea9 = new ContactsBean(".。稳定");
        ContactsBean bea10 = new ContactsBean("你先");
        ContactsBean be1 = new ContactsBean("哦去了");
        ContactsBean be2 = new ContactsBean("自己");
        ContactsBean be3 = new ContactsBean("系欸");
        ContactsBean be4 = new ContactsBean("右脚");
        ContactsBean be5 = new ContactsBean("儿童的");
        ContactsBean be6 = new ContactsBean("ige");
        ContactsBean be7 = new ContactsBean("他的歌");
        ContactsBean be8 = new ContactsBean("共和");
        ContactsBean be9 = new ContactsBean("老是");
        ContactsBean be10 = new ContactsBean("二氨基");
        mData.add(bean1);
        mData.add(bean2);
        mData.add(bean3);
        mData.add(bean4);
        mData.add(bean5);
        mData.add(bean6);
        mData.add(bean7);
        mData.add(bean8);
        mData.add(bean9);
        mData.add(bean10);
        mData.add(bea1);
        mData.add(bea2);
        mData.add(bea3);
        mData.add(bea4);
        mData.add(bea5);
        mData.add(bea6);
        mData.add(bea7);
        mData.add(bea8);
        mData.add(bea9);
        mData.add(bea10);
        mData.add(be1);
        mData.add(be2);
        mData.add(be3);
        mData.add(be4);
        mData.add(be5);
        mData.add(be6);
        mData.add(be7);
        mData.add(be8);
        mData.add(be9);
        mData.add(be10);
    }

    private void initListener() {

        //listView 的条目点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactsBean item = (ContactsBean) mAdapter.getItem(position);
                Toast.makeText(MainActivity.this, item.getRawName() + "    position:" + position, Toast.LENGTH_SHORT).show();
            }
        });


        // 设置侧边栏的触摸事件监听（快速搜索联系人）
        mIndexSideBar.setOnTouchLetterListener(new IndexSideBar.OnTouchLetterListener() {
            @Override
            public void onTouchingLetterListener(String letter) {
                mTvLetter.setText(letter);
                mTvLetter.setVisibility(View.VISIBLE);
                if (mAdapter != null) {
                    int position = mAdapter.getPositionForSection(letter.charAt(0));
                    if (position != -1) {
                        mListView.setSelection(position);     // jump to the specified position
                    }
                }
            }

            @Override
            public void onTouchedLetterListener() {
                mTvLetter.setVisibility(View.GONE);
            }
        });


        // 设置搜索框的文本内容改变事件监听
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapter != null) {
                    List<ContactsBean> mFilterList = ContactHelper.contactsFilter(s.toString(), mContactInfoList);
                    mAdapter.updateContactInfoList(mFilterList); // update
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void refreshContacts() {
        mContactInfoList = ContactHelper.setupContactInfoList(mData);
        // 设置侧边栏中的字母索引
        List<String> mLetterIndexList = ContactHelper.setupLetterIndexList(mContactInfoList);
        mIndexSideBar.setLetterIndexList(mLetterIndexList, false);

        // 设置联系人列表的信息
        mAdapter = new ContactsListAdapter(this, mContactInfoList);

        mListView.setAdapter(mAdapter);
    }


}

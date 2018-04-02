package com.random.hutao;

import android.support.annotation.NonNull;

/**
 * Created by hutao on 2018/3/31.
 */

public class ContactsBean implements Comparable<ContactsBean> {
    private String name;
    private String pinyinName;
    private String sortLetters;
    private String rawName;//排序之后显示的名字
    private String tel;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    //这里的构造方法是为了添加人名的时候更方便
    public ContactsBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    @Override
    public int compareTo(@NonNull ContactsBean another) {
        if (sortLetters.startsWith("#")) {
            return 1;
        } else if (another.getSortLetters().startsWith("#")) {
            return -1;
        } else {
            return sortLetters.compareTo(another.getSortLetters());
        }
    }
}

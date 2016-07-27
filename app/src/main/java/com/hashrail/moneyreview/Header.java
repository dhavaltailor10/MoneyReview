package com.hashrail.moneyreview;

import java.util.ArrayList;

/**
 * Created by new-3 on 6/22/2016.
 */
public class Header {
    private String date;

    private ArrayList<String> mid, mchild, mchildtitle,mchildDate, mchildrs;

    public ArrayList<String> getMchildDate() {
        return mchildDate;
    }

    public void setMchildDate(ArrayList<String> mchildDate) {
        this.mchildDate = mchildDate;
    }

    public ArrayList<String> getMid() {
        return mid;
    }

    public void setMid(ArrayList<String> mid) {
        this.mid = mid;
    }

    public ArrayList<String> getMchildrs() {
        return mchildrs;
    }

    public void setMchildrs(ArrayList<String> mchildrs) {
        this.mchildrs = mchildrs;
    }

    public ArrayList<String> getMchildtitle() {
        return mchildtitle;
    }

    public void setMchildtitle(ArrayList<String> mchildtitle) {
        this.mchildtitle = mchildtitle;
    }

    public Header(String date) {
        this.date = date;
    }

    public Header() {

    }

    public ArrayList<String> getMchild() {
        return mchild;
    }

    public void setMchild(ArrayList<String> mchild) {
        this.mchild = mchild;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

package com.hashrail.moneyreview;

/**
 * Created by new-3 on 6/16/2016.
 */
public class CategoryModel  {
    String cname;
    int id;

    CategoryModel()
    {

    }

    public CategoryModel(String cname, int id) {
        this.cname = cname;
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

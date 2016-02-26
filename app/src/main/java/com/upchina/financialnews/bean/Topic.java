package com.upchina.financialnews.bean;


import java.util.List;

public class Topic {
    private List<News> list;

    private Integer total;

    public List<News> getList() {
        return list;
    }

    public void setList(List<News> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}

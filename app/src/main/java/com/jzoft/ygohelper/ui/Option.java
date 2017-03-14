package com.jzoft.ygohelper.ui;


public class Option {
    public static final int PROXY = 1;
    public static final int CALCULATOR = 2;
    private Integer id;
    private String name;
    private int iconId;

    public Option(Integer id, String name, int iconId) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

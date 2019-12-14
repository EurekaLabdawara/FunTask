package com.eurekalabdawara.funtask.db;

public class Reward {
    private String rUser;
    private String rTitle;
    private Integer rCost;

    public String getrUser() {
        return rUser;
    }

    public void setrUser(String rUser) {
        this.rUser = rUser;
    }

    public String getrTitle() {
        return rTitle;
    }

    public void setrTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public Integer getrCost() {
        return rCost;
    }

    public void setrCost(Integer rCost) {
        this.rCost = rCost;
    }

    public Reward(String rTitle, Integer rCost) {
        this.rTitle = rTitle;
        this.rCost = rCost;
    }

}

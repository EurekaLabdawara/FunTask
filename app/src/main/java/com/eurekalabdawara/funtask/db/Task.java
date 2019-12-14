package com.eurekalabdawara.funtask.db;

public class Task {
    private String tTitle;
    private Integer tReward;
    private String tStatus;
    private String tUser;

    public Task(String tTitle, Integer tReward) {
        this.tTitle = tTitle;
        this.tReward = tReward;
    }

    public String gettTitle() {
        return tTitle;
    }

    public void settTitle(String tTitle) {
        this.tTitle = tTitle;
    }

    public Integer gettReward() {
        return tReward;
    }

    public void settReward(Integer tReward) {
        this.tReward = tReward;
    }

    public String gettStatus() {
        return tStatus;
    }

    public void settStatus(String tStatus) {
        this.tStatus = tStatus;
    }

    public String gettUser() {
        return tUser;
    }

    public void settUser(String tUser) {
        this.tUser = tUser;
    }
}

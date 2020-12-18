package com.ext.tapd.tapd.pojo;

public class Implementation {

    private int story_count;
    private int tcase_count;
    private StatusCounter status_counter;
    private String executed_rate;

    public int getStory_count() {
        return story_count;
    }

    public void setStory_count(int story_count) {
        this.story_count = story_count;
    }

    public int getTcase_count() {
        return tcase_count;
    }

    public void setTcase_count(int tcase_count) {
        this.tcase_count = tcase_count;
    }

    public StatusCounter getStatus_counter() {
        return status_counter;
    }

    public void setStatus_counter(StatusCounter status_counter) {
        this.status_counter = status_counter;
    }

    public String getExecuted_rate() {
        return executed_rate;
    }

    public void setExecuted_rate(String executed_rate) {
        this.executed_rate = executed_rate;
    }
}

package com.manywho.services.timer.service.entities;

import com.manywho.sdk.services.annotations.Property;

public class WaitRelativeRequest {
    @Property("Schedule")
    private String schedule;

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

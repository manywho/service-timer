package com.manywho.services.timer.service.entities;

import com.manywho.sdk.services.annotations.Property;

import javax.validation.constraints.NotNull;

public class WaitAbsoluteRequest {
    @Property("Schedule")
    @NotNull
    private String schedule;

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}


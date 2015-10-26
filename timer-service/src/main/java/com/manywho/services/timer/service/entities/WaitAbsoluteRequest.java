package com.manywho.services.timer.service.entities;

import com.manywho.sdk.services.annotations.Property;
import org.joda.time.DateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

public class WaitAbsoluteRequest {
    @Property("Schedule")
    @NotNull
    @Future
    private DateTime schedule;

    public DateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(DateTime schedule) {
        this.schedule = schedule;
    }
}


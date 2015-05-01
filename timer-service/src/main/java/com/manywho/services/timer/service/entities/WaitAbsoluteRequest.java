package com.manywho.services.timer.service.entities;

import com.manywho.sdk.services.annotations.Property;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class WaitAbsoluteRequest {
    @Property("Schedule")
    @NotBlank
    private Date schedule;

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }
}

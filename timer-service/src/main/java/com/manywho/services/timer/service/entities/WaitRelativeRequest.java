package com.manywho.services.timer.service.entities;

import com.manywho.sdk.services.annotations.Property;
import org.hibernate.validator.constraints.NotBlank;

public class WaitRelativeRequest {
    @NotBlank(message = "A schedule must be provided")
    @Property("Schedule")
    private String schedule;

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

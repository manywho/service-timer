package com.manywho.services.timer.service.actions;

import com.manywho.sdk.entities.describe.DescribeValue;
import com.manywho.sdk.entities.describe.DescribeValueCollection;
import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.services.describe.actions.AbstractAction;

public class WaitAbsolute extends AbstractAction {
    @Override
    public String getUriPart() {
        return "wait/absolute";
    }

    @Override
    public String getDeveloperName() {
        return "Wait: Absolute";
    }

    @Override
    public String getDeveloperSummary() {
        return "Schedule a flow to wait for an absolute amount of time";
    }

    @Override
    public DescribeValueCollection getServiceInputs() {
        return new DescribeValueCollection() {{
            add(new DescribeValue("Schedule", ContentType.String, true));
        }};
    }

    @Override
    public DescribeValueCollection getServiceOutputs() {
        return new DescribeValueCollection() {{
            add(new DescribeValue("Completed?", ContentType.Boolean, true));
        }};
    }
}

package com.gionee.autotest.field.data.db.model;


import com.gionee.autotest.field.util.call.CallMonitorParam;
import com.gionee.autotest.field.util.call.CallMonitorResult;

import java.util.ArrayList;

public class InComingReportBean {
    public CallMonitorParam param = new CallMonitorParam();
    public ArrayList<CallMonitorResult> data = new ArrayList<>();
    public String type = "";
    public String sumString = "";

    public InComingReportBean() {
        this(new CallMonitorParam(), new ArrayList<CallMonitorResult>(), "");
    }

    public InComingReportBean(CallMonitorParam param, ArrayList<CallMonitorResult> data, String type) {

        this.param = param;
        this.data = data;
        this.type = type;
    }

    public InComingReportBean setSumString(String sumString) {
        this.sumString = sumString;
        return this;
    }

    public InComingReportBean setParam(CallMonitorParam param) {
        this.param = param;
        return this;
    }

    public InComingReportBean setData(ArrayList<CallMonitorResult> data) {
        this.data = data;
        return this;
    }

    public InComingReportBean setType(String type) {
        this.type = type;
        return this;
    }
}
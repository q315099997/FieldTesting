package com.gionee.autotest.field.ui.outgoing.model;


import android.os.AsyncTask;
import android.util.Log;

import com.gionee.autotest.field.data.db.OutGoingDBManager;
import com.gionee.autotest.field.data.db.model.OutGoingCallResult;
import com.gionee.autotest.field.ui.outgoing.CallBack;
import com.gionee.autotest.field.ui.outgoing.OutGoingUtil;
import com.gionee.autotest.field.util.Constant;

import java.util.ArrayList;

public class CallRateTask extends AsyncTask<Void, Void, String> {
    private CallBack callBack;

    public CallRateTask( CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(Void... voids) {
        int lastBatch = OutGoingDBManager.getLastBatch();
        ArrayList<OutGoingCallResult> allCalls = OutGoingDBManager.getReportBean(lastBatch);
        Log.i(Constant.TAG, "CallRate reportBeanSize="+allCalls.size());
        return OutGoingUtil.getSumString(allCalls);
    }

    @Override
    protected void onPostExecute(String callRate) {
        super.onPostExecute(callRate);
        if (callBack != null) {
            callBack.call(callRate);
        }
    }
}

package com.gionee.autotest.field.ui.incoming;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.gionee.autotest.field.services.SignalMonitorService;
import com.gionee.autotest.field.ui.incoming.model.InComingCall;
import com.gionee.autotest.field.ui.signal.entity.SimSignalInfo;
import com.gionee.autotest.field.util.SignalHelper;
import com.gionee.autotest.field.util.SimUtil;
import com.gionee.autotest.field.util.call.CallMonitor;
import com.gionee.autotest.field.util.call.CallMonitorParam;
import com.gionee.autotest.field.util.call.CallMonitorResult;
import com.google.gson.Gson;

public class InComingService extends Service implements CallMonitor.MonitorListener {

    private int batchId;
    private CallMonitor callMonitor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, SignalMonitorService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callMonitor != null) {
            callMonitor.cancel();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        batchId = intent.getIntExtra("batchId",-1);
        String params = intent.getStringExtra("params");
        if (batchId !=-1&&params!=null){
            CallMonitorParam callMonitorParam = new Gson().fromJson(params, CallMonitorParam.class);
            callMonitor = new CallMonitor(this, callMonitorParam);
            callMonitor.setMonitorListener(this);
            callMonitor.startMonitor();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChanged(CallMonitorResult callMonitorResult) {
        callMonitorResult.batchId = batchId;
        if (!callMonitorResult.result){
            try {
                SimSignalInfo simSignalInfo = SignalHelper.getInstance(this).getSimSignalInfo(SimUtil.getDefaultDataSubId());
                String simNetInfo = new Gson().toJson(simSignalInfo);
                callMonitorResult.setFailMsg(simNetInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        InComingCall.writeData(callMonitorResult);
    }
}

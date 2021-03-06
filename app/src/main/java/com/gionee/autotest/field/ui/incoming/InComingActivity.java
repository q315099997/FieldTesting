package com.gionee.autotest.field.ui.incoming;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.autotest.field.R;
import com.gionee.autotest.field.ui.about.AboutActivity;
import com.gionee.autotest.field.ui.base.BaseActivity;
import com.gionee.autotest.field.ui.incoming.model.InComingCall;
import com.gionee.autotest.field.util.DialogHelper;
import com.gionee.autotest.field.util.call.CallMonitorParam;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class InComingActivity extends BaseActivity implements InComingContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.isAutoRejectCB)
    CheckBox mAutoRejectCB;
    @BindView(R.id.isAnswertHangup_cb)
    CheckBox mAnswerHangUpCB;
    @BindView(R.id.isHandUpPressPower)
    CheckBox mHangUpPressPower;
    @BindView(R.id.answerHangupTime_et)
    EditText mAnswerHangupTimeET;
    @BindView(R.id.autoReject_layout)
    LinearLayout mAutoReject_layout;
    @BindView(R.id.autoAnswerHangup_layout)
    LinearLayout mAutoAnswerHangup_layout;
    @BindView(R.id.isAutoAnswer)
    CheckBox mAutoAnswerCB;
    @BindView(R.id.isAutoReject_et)
    EditText mAutoRejectET;
    @BindView(R.id.spaceTime)
    EditText mSpaceTime;
    @BindView(R.id.incoming_start_btn)
    Button mStartBtn;
    @BindView(R.id.InComingSumContentTV)
    TextView mSumContentTV;
    private InComingPresenter mInComingPresenter;

    @OnClick(R.id.incoming_start_btn)
    void incomingStartClicked() {
        if (!InComingCall.isTest) {
            mInComingPresenter.startMonitor(getCallMonitorParam());
        } else {
            mInComingPresenter.stopMonitor();
        }
        updateViews();
    }

    @Override
    public void updateViews() {
        boolean isTest = InComingCall.isTest;
        mAutoAnswerCB.setEnabled(!isTest);
        mAnswerHangUpCB.setEnabled(!isTest);
        mAnswerHangupTimeET.setEnabled(!isTest);
        mHangUpPressPower.setEnabled(!isTest);
        mStartBtn.setText(isTest ? "停止测试" : "开始测试");
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_in_coming;
    }

    @Override
    protected boolean isDisplayHomeUpEnabled() {
        return true;
    }

    @Override
    protected void initializePresenter() {
        mInComingPresenter = new InComingPresenter(this);
        super.presenter = mInComingPresenter;
        mInComingPresenter.onAttach(this);
//        mAutoRejectCB.setOnCheckedChangeListener(this);
        mAnswerHangUpCB.setOnCheckedChangeListener(this);
        mAutoAnswerCB.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
//            case R.id.isAutoRejectCB:
//                mAutoReject_layout.setVisibility(isChecked ? VISIBLE : GONE);
//                setAutoAnswerViewEnable(!isChecked);
//                break;
            case R.id.isAnswertHangup_cb:
                mAnswerHangupTimeET.setEnabled(isChecked);
                mHangUpPressPower.setVisibility(isChecked ? VISIBLE : GONE);
                break;
            case R.id.isAutoAnswer:
                mAnswerHangUpCB.setVisibility(isChecked ? VISIBLE : GONE);
                mAutoAnswerHangup_layout.setVisibility(isChecked ? VISIBLE : GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
        mInComingPresenter.updateSumContent();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateViews();
    }

    private CallMonitorParam getCallMonitorParam() {
        int autoEndTime = Integer.parseInt(mAnswerHangupTimeET.getText().toString().trim());
        String distinguishTime_text = mAutoRejectET.getText().toString().trim();
        int distinguishEndTime = distinguishTime_text.isEmpty() ? 0 : Integer.parseInt(distinguishTime_text);
        return new CallMonitorParam(mAutoRejectCB.isChecked(), distinguishEndTime, mAutoAnswerCB.isChecked(), mAnswerHangUpCB.isChecked(), autoEndTime, Integer.parseInt(mSpaceTime.getText().toString()), mHangUpPressPower.isChecked());
    }

    private void setAutoAnswerViewEnable(boolean enable) {
        if (!enable) {
            mAutoAnswerCB.setEnabled(false);
        }
        mAutoAnswerCB.setEnabled(enable);
        mAnswerHangUpCB.setEnabled(enable);
        mAnswerHangupTimeET.setEnabled(enable);
    }

    @Override
    public void setSumContent(String s) {
        try {
            mSumContentTV.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int menuResId() {
        return R.menu.incoming_menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_report:
                mInComingPresenter.showReport();
                break;
            case R.id.clear_report:
                mInComingPresenter.clearAllReport();
                break;
            case R.id.export_excel:
                mInComingPresenter.exportExcelFile();
                break;
            case R.id.open_excel:
                mInComingPresenter.openExcelFile();
                break;
            case R.id.action_abouts:
                startActivity(AboutActivity.getAboutIntent(this, getString(R.string.incoming_about), true));
                break;
            case android.R.id.home:
                if (InComingCall.isTest){
                    mInComingPresenter.showExitWarningDialog();
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setParams(CallMonitorParam lastParams) {
        mAutoAnswerCB.setChecked(lastParams.isAutoAnswer);
        mAnswerHangUpCB.setChecked(lastParams.isAnswerHangup);
        mAnswerHangupTimeET.setText(String.valueOf(lastParams.answerHangUptime));
        mHangUpPressPower.setChecked(lastParams.isHangUpPressPower);
    }

    @Override
    public void doFinish() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (InComingCall.isTest){
            mInComingPresenter.showExitWarningDialog();
        }else{
            super.onBackPressed();
        }
    }
}

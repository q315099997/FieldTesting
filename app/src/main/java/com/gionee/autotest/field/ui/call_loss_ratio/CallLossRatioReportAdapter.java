package com.gionee.autotest.field.ui.call_loss_ratio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.autotest.field.R;
import com.gionee.autotest.field.data.db.model.OutGoingCallResult;
import com.gionee.autotest.field.ui.outgoing.model.OutGoingReportCycle;
import com.gionee.autotest.field.util.call.DisConnectInfo;

import java.util.ArrayList;

import static com.gionee.autotest.field.FieldApplication.context;


public class CallLossRatioReportAdapter extends BaseExpandableListAdapter {
    private ArrayList<OutGoingReportCycle> data = new ArrayList<>();

    public void updateData(ArrayList<OutGoingReportCycle> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).results.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).results.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder gHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.call_loss_ratio_report_group_item, parent, false);
            gHolder = new GroupHolder(convertView);
            convertView.setTag(gHolder);
        } else {
            gHolder = (GroupHolder) convertView.getTag();
        }
        gHolder.cycleID.setText(String.format("第%1$s轮", groupPosition + 1));
        gHolder.cycleSumTV.setText(data.get(groupPosition).sumString);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder cHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.call_loss_ratio_report_child_item, parent, false);
            convertView.setTag(cHolder = new ChildHolder(convertView));
        } else {
            cHolder = (ChildHolder) convertView.getTag();
        }
        OutGoingCallResult result = data.get(groupPosition).results.get(childPosition);
        cHolder.numberTV.setText(result.number + (result.isVerify ? "(复测)" : ""));
        cHolder.resultTV.setText(result.result ? "成功" : "失败");
        cHolder.root.setBackgroundResource(result.result ? R.drawable.item_color_green : R.drawable.item_color_red);
        cHolder.dialTimeTV.setText(result.dialTime);
        cHolder.offHookTimeTV.setText(result.offHookTime);
        cHolder.hangUpTimeTV.setText(result.hangUpTime);
        cHolder.mCodeStringTV.setText(DisConnectInfo.codeToReason(result.code));
        return convertView;
    }

    class GroupHolder {
        TextView cycleID;
        TextView cycleSumTV;

        GroupHolder(View v) {
            cycleID = (TextView) v.findViewById(R.id.call_loss_ratio_group_cycle_id);
            cycleSumTV = (TextView) v.findViewById(R.id.call_loss_ratio_group_cycleSumTV);
        }
    }

    class ChildHolder {
        private TextView mCodeStringTV;
        LinearLayout root;
        TextView numberTV;
        TextView resultTV;
        TextView dialTimeTV;
        TextView offHookTimeTV;
        TextView hangUpTimeTV;

        ChildHolder(View v) {
            root = (LinearLayout) v.findViewById(R.id.call_loss_ratio_child_root);
            numberTV = (TextView) v.findViewById(R.id.call_loss_ratio_child_numberTV);
            resultTV = (TextView) v.findViewById(R.id.call_loss_ratio_child_resultTV);
            dialTimeTV = (TextView) v.findViewById(R.id.call_loss_ratio_child_dialTimeTV);
            offHookTimeTV = (TextView) v.findViewById(R.id.call_loss_ratio_child_offHookTimeTV);
            hangUpTimeTV = (TextView) v.findViewById(R.id.call_loss_ratio_child_hangUpTimeTV);
            mCodeStringTV = (TextView) v.findViewById(R.id.call_loss_ratio_codeStringTV);
        }
    }
}
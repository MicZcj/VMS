package com.example.miczcj.vms.fragment.index;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.model.RecruitStudent;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MicZcj on 2018/5/27.
 */

public class RecruitInfoFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.name)
    QMUIAlphaTextView name;
    @BindView(R.id.sex)
    QMUIAlphaTextView sex;
    @BindView(R.id.stuclass)
    QMUIAlphaTextView stuclass;
    @BindView(R.id.stuid)
    QMUIAlphaTextView stuid;
    @BindView(R.id.volid)
    QMUIAlphaTextView volid;
    @BindView(R.id.phone)
    QMUIAlphaTextView phone;
    @BindView(R.id.phoneshort)
    QMUIAlphaTextView phoneshort;
    @BindView(R.id.time)
    QMUIAlphaTextView time;
    @BindView(R.id.note)
    QMUIAlphaTextView note;
    @BindView(R.id.date)
    QMUIAlphaTextView date;

    private RecruitStudent recruitStudent;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.recruit_info_detail,null);
        ButterKnife.bind(this,view);

        Bundle bundle = getArguments();
        recruitStudent = (RecruitStudent)bundle.getSerializable("recruitStudent");
        Log.i("反序列得到recruitStudent",recruitStudent.getName());
        initTopBar();
        initContent();
        return view;
    }

    private void initContent() {
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(recruitStudent.getName());
                        sex.setText(recruitStudent.getSex());
                        stuclass.setText(recruitStudent.getStuclass());
                        stuid.setText(recruitStudent.getStuid());
                        volid.setText(recruitStudent.getVolid());
                        phone.setText(recruitStudent.getPhone());
                        phoneshort.setText(recruitStudent.getPhoneshort());
                        time.setText(recruitStudent.getTime());
                        note.setText(recruitStudent.getNote());
                        date.setText(recruitStudent.getDate());
                    }
                });
            }
        }.start();
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle("志愿者信息");
    }
}

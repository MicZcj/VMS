package com.example.miczcj.vms.fragment.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;


import butterknife.BindView;
import butterknife.ButterKnife;


public class QDRecruitSingleFragment extends BaseFragment {
    //private static final String ARG_INDEX = "arg_index";

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;


    @Override
    protected View onCreateView() {
        Bundle args = getArguments();
        //final int index = args == null ? 1 : args.getInt(ARG_INDEX);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_arch_test, null);
        ButterKnife.bind(this, view);
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mTopBar.setTitle("浙江图书馆志愿者活动");
        return view;
    }

    public static QDRecruitSingleFragment newInstance(int index) {
        Bundle args = new Bundle();
        QDRecruitSingleFragment fragment = new QDRecruitSingleFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

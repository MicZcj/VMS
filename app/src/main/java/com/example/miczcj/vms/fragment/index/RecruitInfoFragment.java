package com.example.miczcj.vms.fragment.index;

import android.view.LayoutInflater;
import android.view.View;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MicZcj on 2018/5/27.
 */

public class RecruitInfoFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @Override
    public View onCreateView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.recruit_info_detail,null);
        ButterKnife.bind(this,view);
        initTopBar();
        initContent();
        return view;
    }

    private void initContent() {
        //把查出来的活动详情设置到页面上

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

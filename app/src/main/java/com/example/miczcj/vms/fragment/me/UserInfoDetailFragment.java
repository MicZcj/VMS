package com.example.miczcj.vms.fragment.me;

import android.view.LayoutInflater;
import android.view.View;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MicZcj on 2018/5/30.
 */

public class UserInfoDetailFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @Override
    public View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.user_info_detail, null);
        ButterKnife.bind(this, view);
        initTopBar();
        //initContent();
        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        //设置标题
        mTopBar.setTitle("用户详情");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetList();
                    }
                });

    }

    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("删除用户")
                .addItem("重置密码")
                .addItem("修改用户信息")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                //开启活动招募
                                break;
                            case 1:
                                //停止活动招募
                                break;
                            case 2:
                                //修改活动招募,跳转到RecruitNewActivity
                                QMUIFragment fragment = new UserAddFragment();
                                startFragment(fragment);
                                break;
                        }
                    }
                })
                .build()
                .show();
    }
}

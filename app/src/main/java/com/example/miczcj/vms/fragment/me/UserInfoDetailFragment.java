package com.example.miczcj.vms.fragment.me;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.model.User;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
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
    @BindView(R.id.uid)
    QMUIAlphaTextView uid;
    @BindView(R.id.name)
    QMUIAlphaTextView name;
    @BindView(R.id.dept)
    QMUIAlphaTextView dept;
    @BindView(R.id.authority)
    QMUIAlphaTextView authority;

    private User user = new User();
    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        user = (User)bundle.getSerializable("user");
    }

    @Override
    public View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.user_info_detail, null);
        ButterKnife.bind(this, view);
        initTopBar();
        initContent();
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

    private void initContent(){
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uid.setText(user.getUid());
                        name.setText(user.getUsername());
                        dept.setText(user.getDept());
                        String authorityStr = "";
                        String temp = user.getAuthority();
                        if (temp.contains("1")) {
                            authorityStr += "活动招募\n";
                        }
                        if (temp.contains("2")) {
                            authorityStr += "活动管理\n";
                        }
                        if (temp.contains("3")) {
                            authorityStr += "工时查询\n";
                        }
                        if (temp.contains("4")) {
                            authorityStr += "维护管理\n";
                        }
                        if (temp.contains("5")) {
                            authorityStr += "红黑名单管理\n";
                        }
                        if (temp.contains("9")) {
                            authorityStr += "超级管理\n";
                        }
                        authority.setText(authorityStr);
                    }
                });
            }
        }.start();
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

package com.example.miczcj.vms.fragment.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.activity.LoginActivity;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.QMUIFontFitTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;


@Widget(name = "个人中心", iconRes = R.mipmap.vms_logo)
public class UserInfoFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.btn)
    QMUIRoundButton mBtn;
    @BindView(R.id.username)
    QMUIFontFitTextView usernameTxt;
    @BindView(R.id.uid)
    QMUIAlphaTextView uidTxt;
    @BindView(R.id.dept)
    QMUIAlphaTextView deptTxt;
    @BindView(R.id.authority)
    QMUIAlphaTextView authorityTxt;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_userinfo, null);
        ButterKnife.bind(this, view);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
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

        mTopBar.setTitle(mQDItemDescription.getName());

    }

    private void initContent() {
        //写用户的信息+退出登录按钮
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        getAccount();
    }

    private void getAccount() {
        SharedPreferences preference = getContext().getSharedPreferences("login_info", MODE_PRIVATE);
        String username = preference.getString("username", "");
        Integer uid = preference.getInt("uid", 0);
        String dept = preference.getString("dept", "");
        String authority = preference.getString("authority", "");
        String authorityStr = "";
        if (authority.contains("1")) {
            authorityStr += "活动招募\n";
        }
        if (authority.contains("2")) {
            authorityStr += "活动管理\n";
        }
        if (authority.contains("3")) {
            authorityStr += "工时查询\n";
        }
        if (authority.contains("4")) {
            authorityStr += "维护管理\n";
        }
        if (authority.contains("5")) {
            authorityStr += "红黑名单管理\n";
        }
        if (authority.contains("9")) {
            authorityStr += "超级管理\n";
        }


        usernameTxt.setText(username);
        uidTxt.setText(uid + "");
        deptTxt.setText(dept);
        authorityTxt.setText(authorityStr);
    }
}

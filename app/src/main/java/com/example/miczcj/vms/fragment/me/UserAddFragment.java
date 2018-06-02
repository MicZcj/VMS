package com.example.miczcj.vms.fragment.me;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MicZcj on 2018/5/30.
 */

public class UserAddFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView((R.id.list_submit))
    Button mSubmit;
    @Override
    protected View onCreateView(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_add,null);
        ButterKnife.bind(this, view);
        initTopBar();
        initContent();
        return view;
    }
    private void initTopBar(){
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle("添加用户");
    }
    private void initContent() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final QMUITipDialog tipDialog;
                tipDialog = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord("提交成功")
                        .create();
                tipDialog.show();
                mSubmit.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, 1500);
            }
        });
    }
}

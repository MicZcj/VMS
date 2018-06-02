package com.example.miczcj.vms.fragment.index;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MicZcj on 2018/5/27.
 */

public class ActivityInfoFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    private int mCurrentDialogStyle = R.style.DialogTheme2;

    @Override
    public View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_info_detail, null);
        ButterKnife.bind(this, view);
        initTopBar();
        initContent();
        return view;
    }

    private void initContent() {
        //志愿者活动策划的内容这里写进去
        /**
         * 如果是活动审批跳到这里
         * 要设置 工时表已提交
         * 要添加 审批按钮（用对话框实现）或者 单选框
         */
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        QMUIAlphaImageButton temp = mTopBar.addRightImageButton(R.mipmap.activity_approval, R.id.topbar_right_approval_button);
        temp.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditTextDialog();
            }
        });
        //设置标题
        mTopBar.setTitle("西溪湿地志愿者活动");

    }
    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("活动审批")
                .setPlaceholder("在此输入审批意见")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });

            builder.addAction("确定", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    CharSequence text = builder.getEditText().getText();
                        Toast.makeText(getActivity(), "审批通过", Toast.LENGTH_SHORT).show();
                }
            }).create(mCurrentDialogStyle).show();
        }

}

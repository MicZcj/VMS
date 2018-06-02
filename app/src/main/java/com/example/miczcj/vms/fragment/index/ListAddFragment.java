package com.example.miczcj.vms.fragment.index;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MicZcj on 2018/5/28.
 */
public class ListAddFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.list_submit)
    Button mSubmit;
    @BindView(R.id.ryb_button)
    RadioGroup mRadiogroup;
    @BindView(R.id.red)
    RadioButton red;
    @BindView(R.id.yellow)
    RadioButton yellow;
    @BindView(R.id.black)
    RadioButton black;


    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list_add, null);
        ButterKnife.bind(this, view);
        initTopBar();
        initContent();

        return view;
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
        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                red.setTextColor(getResources().getColor(R.color.app_color_blue));
                yellow.setTextColor(getResources().getColor(R.color.app_color_blue));
                black.setTextColor(getResources().getColor(R.color.app_color_blue));
                red.setBackgroundColor(getResources().getColor(R.color.qmui_config_color_pressed));
                yellow.setBackgroundColor(getResources().getColor(R.color.qmui_config_color_pressed));
                black.setBackgroundColor(getResources().getColor(R.color.qmui_config_color_pressed));
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.red:
                        red.setTextColor(getResources().getColor(R.color.qmui_config_color_75_white));
                        red.setBackgroundColor(getResources().getColor(R.color.app_color_theme_6));
                        break;
                    case R.id.yellow:
                        yellow.setTextColor(getResources().getColor(R.color.qmui_config_color_75_white));
                        yellow.setBackgroundColor(getResources().getColor(R.color.app_color_theme_6));
                        break;
                    case R.id.black:
                        black.setTextColor(getResources().getColor(R.color.qmui_config_color_75_white));
                        black.setBackgroundColor(getResources().getColor(R.color.app_color_theme_6));
                        break;
                }
            }
        });
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle("添加红黑名单");
    }
}


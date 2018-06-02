package com.example.miczcj.vms.fragment.index;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;


import butterknife.BindView;
import butterknife.ButterKnife;

//原本是 QMUIButton
@Widget(name = "工时查询", iconRes = R.mipmap.time_query)
public class TimeQueryFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.activity_query_button)
    QMUIRoundButton mRoundBtn;
    @BindView(R.id.id_name_query)
    EditText editText;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_activity_time_query, null);
        ButterKnife.bind(this, view);
        editText.setHint("请输入学号");
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());

        initTopBar();
        mRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"跳转到查询的结果",Toast.LENGTH_LONG).show();
                //条件记得要设置 要查的的东西
                QMUIFragment fragment = new TimeResultFragment();
                startFragment(fragment);
            }
        });
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
}

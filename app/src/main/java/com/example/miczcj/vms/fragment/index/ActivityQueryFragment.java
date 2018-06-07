package com.example.miczcj.vms.fragment.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
@Widget(name = "活动查询", iconRes = R.mipmap.activity_query)
public class ActivityQueryFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.activity_query_button)
    QMUIRoundButton mRoundBtn;
    @BindView(R.id.id_name_query)
    EditText nameEdt;


    private QDItemDescription mQDItemDescription;
    private String name;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_activity_time_query, null);
        ButterKnife.bind(this, view);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());


        initTopBar();
        mRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEdt.getText().toString();
                if(name.equals("")){
                    Toast.makeText(getContext(),"输入为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                QMUIFragment fragment = new ActivityAllFragment();
                fragment.setArguments(bundle);
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

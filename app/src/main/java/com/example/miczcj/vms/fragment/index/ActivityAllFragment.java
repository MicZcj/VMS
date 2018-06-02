package com.example.miczcj.vms.fragment.index;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import butterknife.BindView;
import butterknife.ButterKnife;

//原本是 QMUIButton
@Widget(name = "全部活动", iconRes = R.mipmap.activity_all)
public class ActivityAllFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_grouplistview, null);
        ButterKnife.bind(this, view);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());

        initTopBar();
        initGroupListView();
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

    private void initGroupListView() {
        QMUICommonListItemView itemView = mGroupListView.createItemView("西溪湿地志愿者活动");
        itemView.setOrientation(QMUICommonListItemView.VERTICAL);
        itemView.setDetailText("工时表等待提交");
        itemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView itemView1 = mGroupListView.createItemView("机械分会西溪医院志愿者活动");
        itemView1.setOrientation(QMUICommonListItemView.VERTICAL);
        itemView1.setDetailText("工时表完成提交");
        itemView1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
                    QMUIFragment fragment = new ActivityInfoFragment();
                    startFragment(fragment);
                }
            }
        };
        QMUIGroupListView.newSection(getContext())
                .addItemView(itemView, onClickListener)
                .addItemView(itemView1,onClickListener)
                .addTo(mGroupListView);
    }
}

package com.example.miczcj.vms.fragment.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.lib.annotation.Group;
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

/**
 * {@link QMUIGroupListView} 的使用示例。
 * Created by Kayo on 2016/11/21.
 */

@Widget(group = Group.INDEX,  name = "我的招募", iconRes = R.mipmap.recruit_all)
public class RecruitActivityAllFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_grouplistview, null);
        ButterKnife.bind(this, root);

        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        initTopBar();
        /**
         * 查出来的 我的活动 用下面的方法加进入
         */
        initGroupListView();

        return root;
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

        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView("浙江图书馆志愿者活动");
        itemWithDetail.setDetailText("80人");
        itemWithDetail.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);



        QMUICommonListItemView itemWithCustom = mGroupListView.createItemView("西溪医院志愿服务活动");
        itemWithCustom.setDetailText("10人");
        itemWithDetail.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
                    //跳到单独显示活动的页面
                    QMUIFragment fragment = newInstance();
                    startFragment(fragment);
                }
            }
        };

        QMUIGroupListView.newSection(getContext())
                .setTitle("进行中的招募")
                .addItemView(itemWithDetail, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("已结束的招募")
                .addItemView(itemWithCustom, onClickListener)
                .addTo(mGroupListView);
    }

    public static RecruitActivitySingleFragment newInstance() {
        //数据传入
        Bundle args = new Bundle();
        RecruitActivitySingleFragment fragment = new RecruitActivitySingleFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

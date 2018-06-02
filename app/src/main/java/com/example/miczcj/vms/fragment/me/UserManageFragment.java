package com.example.miczcj.vms.fragment.me;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MicZcj on 2018/5/30.
 */
@Widget(name = "用户管理")
public class UserManageFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.recruitActivityGroupListView)
    QMUIGroupListView mGroupListView;
    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_recruit_activity_all, null);
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
        QMUIAlphaImageButton temp = mTopBar.addRightImageButton(R.mipmap.user_add, R.id.topbar_right_add_button);

        temp.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QMUIFragment fragment = new UserAddFragment();
                startFragment(fragment);
            }
        });
        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void initGroupListView() {

        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView("1001 " + " 张忆霄");
        itemWithDetail.setDetailText("校志协");
        itemWithDetail.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);


        QMUICommonListItemView itemWithCustom = mGroupListView.createItemView("1002 "+" 张昌健");
        itemWithCustom.setDetailText("校志协");
        itemWithCustom.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
                    //跳到单独显示活动的页面
                    QMUIFragment fragment = new UserInfoDetailFragment();
                    startFragment(fragment);
                }
            }
        };

        QMUIGroupListView.newSection(getContext())
                .addItemView(itemWithDetail, onClickListener)
                .addItemView(itemWithCustom, onClickListener)
                .addTo(mGroupListView);
    }
}

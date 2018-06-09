package com.example.miczcj.vms.manager;



import android.app.Activity;
import android.content.SharedPreferences;

import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.fragment.index.ActivityAllFragment;
import com.example.miczcj.vms.fragment.index.ActivityDoingFragment;
import com.example.miczcj.vms.fragment.index.ActivityFinishFragment;
import com.example.miczcj.vms.fragment.index.ActivityQueryFragment;
import com.example.miczcj.vms.fragment.index.AdminAllFragment;
import com.example.miczcj.vms.fragment.index.AdminFragment;
import com.example.miczcj.vms.fragment.index.ListBlackFragment;
import com.example.miczcj.vms.fragment.index.ListRedFragment;
import com.example.miczcj.vms.fragment.index.ListYellowFragment;
import com.example.miczcj.vms.fragment.index.RecruitActivityAllFragment;
import com.example.miczcj.vms.fragment.index.RecruitActivityNewFragment;
import com.example.miczcj.vms.fragment.index.TimeQueryFragment;
import com.example.miczcj.vms.fragment.me.UserInfoFragment;
import com.example.miczcj.vms.fragment.me.UserManageFragment;
import com.example.miczcj.vms.fragment.message.MessageFragment;
import com.example.miczcj.vms.model.QDItemDescription;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author cginechen
 * @date 2016-10-21
 * 把三个Tab里面的东西加进来
 */

public class QDDataManager {
    private static QDDataManager _sInstance;
    private QDWidgetContainer mWidgetContainer;
//    private String authority;
//
//    public String getAuthority() {
//        return authority;
//    }
//
//    public void setAuthority(String authority) {
//        this.authority = authority;
//    }

    private List<Class<? extends BaseFragment>> mComponentsNames;
    private List<Class<? extends BaseFragment>> mUtilNames;
    private List<Class<? extends BaseFragment>> mLabNames;

    public QDDataManager() {
        mWidgetContainer = QDWidgetContainer.getInstance();
        initComponentsDesc();
        initUtilDesc();
        initLabDesc();
    }

    public static QDDataManager getInstance() {
        if (_sInstance == null) {
            _sInstance = new QDDataManager();
        }
        return _sInstance;
    }


    /**
     * Components
     * 初始化首页把fragment类加载进首页的List中
     * 在这里添加小方块
     */
    private void initComponentsDesc() {
        mComponentsNames = new ArrayList<>();
        mComponentsNames.add(RecruitActivityNewFragment.class);
        mComponentsNames.add(RecruitActivityAllFragment.class);

        mComponentsNames.add(ActivityQueryFragment.class);
        mComponentsNames.add(ActivityDoingFragment.class);
        mComponentsNames.add(ActivityFinishFragment.class);
        mComponentsNames.add(ActivityAllFragment.class);

        mComponentsNames.add(TimeQueryFragment.class);

        mComponentsNames.add(AdminFragment.class);
        mComponentsNames.add(AdminAllFragment.class);
//        mComponentsNames.add(AdminUploadFragment.class);
//        mComponentsNames.add(AdminDeleteFileFragment.class);
//        mComponentsNames.add(AdminFlushActivityFragment.class);
//        mComponentsNames.add(AdminShowHideFragment.class);
//        mComponentsNames.add(AdminSummaryFragment.class);
//        mComponentsNames.add(AdminUpdateDataFragment.class);

        mComponentsNames.add(ListRedFragment.class);
        mComponentsNames.add(ListBlackFragment.class);
        mComponentsNames.add(ListYellowFragment.class);


//        mComponentsNames.add(QDButtonFragment.class);
//        mComponentsNames.add(QDDialogFragment.class);
//        mComponentsNames.add(QDFloatLayoutFragment.class);
//        mComponentsNames.add(QDEmptyViewFragment.class);
//        mComponentsNames.add(QDTabSegmentFragment.class);
//        mComponentsNames.add(QDProgressBarFragment.class);
//        mComponentsNames.add(QDBottomSheetFragment.class);
//        mComponentsNames.add(QDGroupListViewFragment.class);
//        mComponentsNames.add(QDTipDialogFragment.class);
//        mComponentsNames.add(QDRadiusImageViewFragment.class);
//        mComponentsNames.add(QDVerticalTextViewFragment.class);
//        mComponentsNames.add(QDPullRefreshFragment.class);
//        mComponentsNames.add(QDPopupFragment.class);
//        mComponentsNames.add(QDSpanTouchFixTextViewFragment.class);
//        mComponentsNames.add(QDLinkTextViewFragment.class);
//        mComponentsNames.add(QDQQFaceFragment.class);
//        mComponentsNames.add(QDSpanFragment.class);
//        mComponentsNames.add(QDCollapsingTopBarLayoutFragment.class);
//        mComponentsNames.add(QDViewPagerFragment.class);
//        mComponentsNames.add(QDLayoutFragment.class);
    }

    /**
     * Helper
     */
    private void initUtilDesc() {
        mUtilNames = new ArrayList<>();
        mUtilNames.add(MessageFragment.class);
//        mUtilNames.add(QDColorHelperFragment.class);
//        mUtilNames.add(QDDeviceHelperFragment.class);
//        mUtilNames.add(QDDrawableHelperFragment.class);
//        mUtilNames.add(QDStatusBarHelperFragment.class);
//        mUtilNames.add(QDViewHelperFragment.class);
    }

    /**
     * Lab
     */
    private void initLabDesc() {
        mLabNames = new ArrayList<>();
        mLabNames.add(UserInfoFragment.class);
        mLabNames.add(UserManageFragment.class);
//        mLabNames.add(QDAnimationListViewFragment.class);
//        mLabNames.add(QDSnapHelperFragment.class);
//        mLabNames.add(QDArchTestFragment.class);
//        mLabNames.add(QDSwipeDeleteListViewFragment.class);
    }

    public QDItemDescription getDescription(Class<? extends BaseFragment> cls) {
        return mWidgetContainer.get(cls);
    }

    public String getName(Class<? extends BaseFragment> cls) {
        QDItemDescription itemDescription = getDescription(cls);
        if (itemDescription == null) {
            return null;
        }
        return itemDescription.getName();
    }

    /**
     * HomeComponentsController调用该方法
     * 把初始化的功能小方块的信息放进list中
     * 返回list
     *
     * @return
     */
    public List<QDItemDescription> getComponentsDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        for (int i = 0; i < mComponentsNames.size(); i++) {
            list.add(mWidgetContainer.get(mComponentsNames.get(i)));
        }
        return list;
    }

    public List<QDItemDescription> getUtilDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        for (int i = 0; i < mUtilNames.size(); i++) {
            list.add(mWidgetContainer.get(mUtilNames.get(i)));
        }
        return list;
    }

    public List<QDItemDescription> getLabDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        for (int i = 0; i < mLabNames.size(); i++) {
            list.add(mWidgetContainer.get(mLabNames.get(i)));
        }
        return list;
    }
}

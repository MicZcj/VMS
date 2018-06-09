package com.example.miczcj.vms.fragment.home;

import android.content.Context;
import android.util.Log;

import com.example.miczcj.vms.manager.QDDataManager;


/**
 * @author cginechen
 * @date 2016-10-20
 */

public class HomeIndexController extends HomeController {

    public HomeIndexController(Context context) {
        super(context,1);
    }

    @Override
    protected String getTitle() {
        return "首页";
    }


    /**
     * 实例化一个item适配器
     * 返回这个适配器给HomeController，用于Tab为Components的部分
     *
     * @return
     */
    @Override
    protected ItemAdapter getItemAdapter() {
        return new ItemAdapter(getContext(), QDDataManager.getInstance().getComponentsDescriptions(),1);
    }
}

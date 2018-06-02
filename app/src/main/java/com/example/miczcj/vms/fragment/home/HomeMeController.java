package com.example.miczcj.vms.fragment.home;

import android.content.Context;

import com.example.miczcj.vms.manager.QDDataManager;


/**
 * @author cginechen
 * @date 2016-10-20
 */

public class HomeMeController extends HomeController {

    public HomeMeController(Context context) {
        super(context,3);
    }

    @Override
    protected String getTitle() {
        return "我的";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        return new ItemAdapter(getContext(), QDDataManager.getInstance().getLabDescriptions(),3);
    }
}

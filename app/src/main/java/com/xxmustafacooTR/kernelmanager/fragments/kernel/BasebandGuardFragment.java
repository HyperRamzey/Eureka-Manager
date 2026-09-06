/*
 * Copyright (C) 2026 HyperRamzey
 *
 * This file is part of Eureka Manager (a KernelAdiutor-family kernel manager).
 * GPL-3.0 — see LICENSE.
 *
 * Baseband guard fragment: CP (Shannon modem) auto-recovery monitor + knobs
 * for Eureka-Kernel-R24U (Exynos7885).
 * Kernel side: drivers/misc/modem_v1/baseband_guard.c
 */
package com.xxmustafacooTR.kernelmanager.fragments.kernel;

import com.xxmustafacooTR.kernelmanager.R;
import com.xxmustafacooTR.kernelmanager.fragments.recyclerview.RecyclerViewFragment;
import com.xxmustafacooTR.kernelmanager.utils.Utils;
import com.xxmustafacooTR.kernelmanager.utils.kernel.basebandguard.BasebandGuard;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.CardView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.DescriptionView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.RecyclerViewItem;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.SwitchView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.TitleView;

import java.util.List;

/**
 * Created by HyperRamzey for Eureka Kernel R24U.
 */
public class BasebandGuardFragment extends RecyclerViewFragment {

    @Override
    protected void init() {
        super.init();

        addViewPagerFragment(DescriptionFragment.newInstance(
                getString(R.string.baseband_guard_title),
                getString(R.string.baseband_guard_info)));
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        if (!BasebandGuard.supported()) {
            DescriptionView desc = new DescriptionView();
            desc.setSummary(getString(R.string.baseband_guard_not_supported));
            items.add(desc);
            return;
        }

        // ---- status card ----
        CardView statusCard = new CardView(getActivity());
        statusCard.setTitle(getString(R.string.baseband_guard_status));

        DescriptionView state = new DescriptionView();
        state.setTitle(getString(R.string.baseband_guard_cp_state));
        state.setSummary(BasebandGuard.getState());
        statusCard.addItem(state);

        DescriptionView crashes = new DescriptionView();
        crashes.setTitle(getString(R.string.baseband_guard_crashes));
        crashes.setSummary(String.valueOf(BasebandGuard.getCrashes()));
        statusCard.addItem(crashes);

        DescriptionView recoveries = new DescriptionView();
        recoveries.setTitle(getString(R.string.baseband_guard_recoveries));
        recoveries.setSummary(String.valueOf(BasebandGuard.getRecoveries()));
        statusCard.addItem(recoveries);

        DescriptionView lastEvent = new DescriptionView();
        lastEvent.setTitle(getString(R.string.baseband_guard_last_event));
        lastEvent.setSummary(BasebandGuard.getLastEvent());
        statusCard.addItem(lastEvent);

        items.add(statusCard);

        // ---- policy card ----
        CardView policyCard = new CardView(getActivity());
        policyCard.setTitle(getString(R.string.baseband_guard_policy));

        SwitchView enable = new SwitchView();
        enable.setTitle(getString(R.string.baseband_guard_enable));
        enable.setSummary(getString(R.string.baseband_guard_enable_summary));
        enable.setChecked(BasebandGuard.isEnabled());
        enable.addOnSwitchListener((switchView, isChecked) ->
                BasebandGuard.setEnabled(isChecked, getActivity()));
        policyCard.addItem(enable);

        SwitchView rebootOnDead = new SwitchView();
        rebootOnDead.setTitle(getString(R.string.baseband_guard_reboot));
        rebootOnDead.setSummary(getString(R.string.baseband_guard_reboot_summary));
        rebootOnDead.setChecked(BasebandGuard.isRebootOnDead());
        rebootOnDead.addOnSwitchListener((switchView, isChecked) ->
                BasebandGuard.setRebootOnDead(isChecked, getActivity()));
        policyCard.addItem(rebootOnDead);

        items.add(policyCard);
    }
}

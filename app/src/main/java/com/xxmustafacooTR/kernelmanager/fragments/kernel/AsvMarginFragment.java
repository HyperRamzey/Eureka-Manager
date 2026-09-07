/*
 * Copyright (C) 2026 HyperRamzey
 *
 * This file is part of Eureka Manager (a KernelAdiutor-family kernel manager).
 * GPL-3.0 — see LICENSE.
 *
 * ASV margin fragment: per-domain runtime voltage margin control for
 * Eureka-Kernel-R24U (Exynos7885). One seekbar per domain (BIG/LITTLE/GPU/
 * MIF/INT), range from the kernel's min/max attributes (default
 * -100000..100000 uV in 6250 uV steps), 0 = stock ECT.
 */
package com.xxmustafacooTR.kernelmanager.fragments.kernel;

import com.xxmustafacooTR.kernelmanager.R;
import com.xxmustafacooTR.kernelmanager.fragments.ApplyOnBootFragment;
import com.xxmustafacooTR.kernelmanager.fragments.DescriptionFragment;
import com.xxmustafacooTR.kernelmanager.fragments.recyclerview.RecyclerViewFragment;
import com.xxmustafacooTR.kernelmanager.utils.AppSettings;
import com.xxmustafacooTR.kernelmanager.utils.Utils;
import com.xxmustafacooTR.kernelmanager.utils.kernel.asvmargin.AsvMargin;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.CardView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.DescriptionView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.RecyclerViewItem;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.SeekBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HyperRamzey for Eureka Kernel R24U.
 */
public class AsvMarginFragment extends RecyclerViewFragment {

    private static final int STEP_UV = 6250;

    private static class Domain {
        final String sysfsBase;   // /sys/class/asv_margin/<domain>
        final int titleRes;
        final int keyRes;

        Domain(String sysfsBase, int titleRes, int keyRes) {
            this.sysfsBase = sysfsBase;
            this.titleRes = titleRes;
            this.keyRes = keyRes;
        }
    }

    @Override
    protected void init() {
        super.init();

        addViewPagerFragment(DescriptionFragment.newInstance(
                getString(R.string.asv_margin_title),
                getString(R.string.asv_margin_info)));
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        if (!AsvMargin.supported()) {
            DescriptionView desc = new DescriptionView();
            desc.setSummary(getString(R.string.asv_margin_not_supported));
            items.add(desc);
            return;
        }

        Domain[] domains = {
            new Domain("cpucl0", R.string.asv_domain_big, 0),
            new Domain("cpucl1", R.string.asv_domain_little, 0),
            new Domain("g3d", R.string.asv_domain_g3d, 0),
            new Domain("mif", R.string.asv_domain_mif, 0),
            new Domain("int", R.string.asv_domain_int, 0),
        };

        CardView card = new CardView(getActivity());
        card.setTitle(getString(R.string.asv_margin_card));

        for (final Domain d : domains) {
            final String marginPath = AsvMargin.CLASS_BASE + "/" + d.sysfsBase + "/margin";
            if (!Utils.existFile(marginPath)) {
                continue;
            }

            int min = AsvMargin.getMin(d.sysfsBase);
            int max = AsvMargin.getMax(d.sysfsBase);
            int current = AsvMargin.getValue(marginPath);

            List<String> progress = new ArrayList<>();
            int steps = (max - min) / STEP_UV;
            for (int i = 0; i <= steps; i++) {
                progress.add(String.valueOf((min + i * STEP_UV) / 1000));
            }

            // find index of current value
            int position = 0;
            for (int i = 0; i < progress.size(); i++) {
                if (Utils.strToInt(progress.get(i)) == current / 1000) {
                    position = i;
                    break;
                }
            }

            final int fMin = min;

            SeekBarView seekbar = new SeekBarView();
            seekbar.setTitle(getString(d.titleRes));
            seekbar.setSummary(getString(R.string.asv_margin_summary));
            seekbar.setUnit(getString(R.string.mv));
            seekbar.setItems(progress);
            seekbar.setProgress(position);
            seekbar.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    int uv = (Utils.strToInt(value)) * 1000;
                    if (uv < fMin) {
                        uv = fMin;
                    }
                    AsvMargin.setValue(marginPath, uv, getActivity());
                    AppSettings.saveInt("asv_margin_" + d.sysfsBase, uv, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            card.addItem(seekbar);
        }

        if (card.size() > 0) {
            items.add(card);
        }
    }
}

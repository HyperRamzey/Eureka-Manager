/*
 * Copyright (C) 2026 HyperRamzey
 *
 * This file is part of Eureka Manager (a KernelAdiutor-family kernel manager).
 * GPL-3.0 — see LICENSE.
 *
 * ASV margin interface for Eureka-Kernel-R24U (Exynos7885).
 * Kernel side: drivers/soc/samsung/cal-if/asv_margin.c
 *   /sys/class/asv_margin/<domain>/margin  (rw, uV; 0 = stock ECT)
 *   /sys/class/asv_margin/<domain>/max|min (ro)
 */
package com.xxmustafacooTR.kernelmanager.utils.kernel.asvmargin;

import android.content.Context;

import com.xxmustafacooTR.kernelmanager.fragments.ApplyOnBootFragment;
import com.xxmustafacooTR.kernelmanager.utils.Utils;
import com.xxmustafacooTR.kernelmanager.utils.root.Control;

import java.util.ArrayList;
import java.util.List;

/**
 * Runtime ASV voltage margins via the Eureka kernel's asv_margin class.
 * A margin is a SHIFT in microvolts applied by the ACPM firmware to every
 * voltage of a DVFS domain: positive = stability margin, negative =
 * undervolt (user risk), 0 = stock ECT table.
 */
public class AsvMargin {

    public static final String CLASS_BASE = "/sys/class/asv_margin";

    public static final String CPUCL0 = CLASS_BASE + "/cpucl0/margin"; // BIG cluster
    public static final String CPUCL1 = CLASS_BASE + "/cpucl1/margin"; // LITTLE cluster
    public static final String G3D    = CLASS_BASE + "/g3d/margin";
    public static final String MIF    = CLASS_BASE + "/mif/margin";
    public static final String INT    = CLASS_BASE + "/int/margin";

    private static final String[] DOMAIN_PATHS = {CPUCL0, CPUCL1, G3D, MIF, INT};

    public static boolean supported() {
        return Utils.existFile(CPUCL0);
    }

    public static int getValue(String path) {
        if (Utils.existFile(path)) {
            return Utils.strToInt(Utils.readFile(path));
        }
        return 0;
    }

    public static int getMax(String domain) {
        String path = CLASS_BASE + "/" + domain + "/max";
        if (Utils.existFile(path)) {
            return Utils.strToInt(Utils.readFile(path));
        }
        return 100000;
    }

    public static int getMin(String domain) {
        String path = CLASS_BASE + "/" + domain + "/min";
        if (Utils.existFile(path)) {
            return Utils.strToInt(Utils.readFile(path));
        }
        return -100000;
    }

    public static void setValue(String path, int value, Context context) {
        Control.runSetting(Control.write(String.valueOf(value), path),
                ApplyOnBootFragment.CPU, path, context);
    }

    /** Restore a domain to stock ECT voltages. */
    public static void reset(String path, Context context) {
        setValue(path, 0, context);
    }

    /** All margin sysfs nodes that exist on this device. */
    public static List<String> getExistingDomains() {
        List<String> list = new ArrayList<>();
        for (String p : DOMAIN_PATHS) {
            if (Utils.existFile(p)) {
                list.add(p);
            }
        }
        return list;
    }
}

/*
 * Copyright (C) 2026 HyperRamzey
 *
 * This file is part of Eureka Manager (a KernelAdiutor-family kernel manager).
 * GPL-3.0 — see LICENSE.
 *
 * Baseband guard interface for Eureka-Kernel-R24U (Exynos7885).
 * Kernel side: drivers/misc/modem_v1/baseband_guard.c
 *   /sys/class/baseband_guard/baseband_guard/{state,enable,crashes,
 *   recoveries,last_event,reboot_on_dead}
 */
package com.xxmustafacooTR.kernelmanager.utils.kernel.basebandguard;

import android.content.Context;

import com.xxmustafacooTR.kernelmanager.fragments.ApplyOnBootFragment;
import com.xxmustafacooTR.kernelmanager.utils.Utils;
import com.xxmustafacooTR.kernelmanager.utils.root.Control;

/**
 * CP auto-recovery guard: monitors Shannon modem crash events and replays
 * the RIL recovery sequence if the modem fails to come back ONLINE.
 */
public class BasebandGuard {

    public static final String GUARD = "/sys/class/baseband_guard/baseband_guard";

    public static final String STATE         = GUARD + "/state";
    public static final String ENABLE        = GUARD + "/enable";
    public static final String CRASHES        = GUARD + "/crashes";
    public static final String RECOVERIES     = GUARD + "/recoveries";
    public static final String LAST_EVENT     = GUARD + "/last_event";
    public static final String REBOOT_ON_DEAD = GUARD + "/reboot_on_dead";
    public static final String MAX_ATTEMPTS   = GUARD + "/max_attempts";
    public static final String BOOT_WINDOW    = GUARD + "/boot_window_ms";

    public static boolean supported() {
        return Utils.existFile(ENABLE);
    }

    public static String getState() {
        return Utils.readFile(STATE).trim();
    }

    public static int getCrashes() {
        return Utils.strToInt(Utils.readFile(CRASHES));
    }

    public static int getRecoveries() {
        return Utils.strToInt(Utils.readFile(RECOVERIES));
    }

    public static String getLastEvent() {
        return Utils.readFile(LAST_EVENT).trim();
    }

    public static boolean isEnabled() {
        return Utils.readFile(ENABLE).trim().equals("1");
    }

    public static void setEnabled(boolean enabled, Context context) {
        Control.runCommand(enabled ? "1" : "0", ENABLE,
                ApplyOnBootFragment.class, context);
    }

    public static boolean isRebootOnDead() {
        return Utils.readFile(REBOOT_ON_DEAD).trim().equals("1");
    }

    public static void setRebootOnDead(boolean enabled, Context context) {
        Control.runCommand(enabled ? "1" : "0", REBOOT_ON_DEAD,
                ApplyOnBootFragment.class, context);
    }
}

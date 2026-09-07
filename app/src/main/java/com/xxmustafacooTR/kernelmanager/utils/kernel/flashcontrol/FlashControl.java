package com.xxmustafacooTR.kernelmanager.utils.kernel.flashcontrol;

import android.content.Context;

import com.xxmustafacooTR.kernelmanager.fragments.ApplyOnBootFragment;
import com.xxmustafacooTR.kernelmanager.utils.Utils;
import com.xxmustafacooTR.kernelmanager.utils.root.Control;
import com.xxmustafacooTR.kernelmanager.utils.root.RootUtils;

/**
 * Torch / flashlight brightness controls (Exynos7885 Samsung camera flash sysfs).
 * Ported from EKManager (FlashControl.kt) to the xxTR KernelManager codebase.
 */
public class FlashControl {

    private static final String TORCH_LEVEL = "/sys/devices/virtual/camera/flash/torch_brightness_lvl";
    private static final String TORCH_TOGGLE = "/sys/devices/virtual/camera/flash/rear_torch_flash";
    private static final String USE_TORCH_LEVEL = "/sys/devices/virtual/camera/flash/torch_brightness_lvl_enable";

    private static FlashControl sInstance;

    public static FlashControl getInstance() {
        if (sInstance == null) {
            sInstance = new FlashControl();
        }
        return sInstance;
    }

    /**
     * Torch brightness level, normalized to 1..10.
     * Exynos7885 stores the raw level 21..210 (level * 21); a10 reports 1..10 directly.
     */
    public int getTorchLevel() {
        int raw = Utils.strToInt(Utils.readFile(TORCH_LEVEL));
        boolean isA10 = RootUtils.getProp("ro.product.device").contains("a10");
        if (isA10) {
            return Math.max(1, Math.min(10, raw));
        }
        return Math.max(1, Math.min(10, (raw / 21)));
    }

    /**
     * Write a normalized 1..10 level to the kernel node.
     */
    public void setTorchLevel(int level, Context context) {
        boolean isA10 = RootUtils.getProp("ro.product.device").contains("a10");
        int raw = isA10 ? level : level * 21;
        run(Control.write(String.valueOf(raw), TORCH_LEVEL), TORCH_LEVEL, context);
    }

    public void setToggleTorch(int value, Context context) {
        run(Control.write(String.valueOf(value), TORCH_TOGGLE), TORCH_TOGGLE, context);
    }

    public int getUseTorchLevelBit() {
        return Utils.strToInt(Utils.readFile(USE_TORCH_LEVEL));
    }

    public void setUseTorchLevelBit(int bit, Context context) {
        run(Control.write(String.valueOf(bit), USE_TORCH_LEVEL), USE_TORCH_LEVEL, context);
    }

    public boolean hasTorchLevel() {
        return Utils.existFile(TORCH_LEVEL);
    }

    public boolean hasTorchLevelEnable() {
        return Utils.existFile(USE_TORCH_LEVEL);
    }

    public boolean fullySupported() {
        return hasTorchLevel() && hasTorchLevelEnable();
    }

    public boolean legacySupported() {
        return hasTorchLevel() && !hasTorchLevelEnable();
    }

    public boolean supported() {
        return hasTorchLevel();
    }

    private void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.MISC, id, context);
    }
}

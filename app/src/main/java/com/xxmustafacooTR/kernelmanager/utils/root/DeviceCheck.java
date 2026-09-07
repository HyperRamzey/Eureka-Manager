package com.xxmustafacooTR.kernelmanager.utils.root;

import com.xxmustafacooTR.kernelmanager.utils.Device;
import com.xxmustafacooTR.kernelmanager.utils.Utils;

public class DeviceCheck {

    //TODO
    enum CHECK {
        ALL,
        FILE_EXISTS,
        PROCESSOR_MODEL,
        PROCESSOR_CORE,
        VENDOR,
        PHONE_MODEL,
        ARCH,
        HARDWARE,
        BOOTLOADER,
        BOARD,
    }

    public static boolean deviceSupported() {
        // Eureka-Kernel-R24U family (Exynos7885): A10/A20/A20e/A30/A30s/A40/M20/M30s
        if (check(CHECK.FILE_EXISTS, "/sys/class/asv_margin") ||
                check(CHECK.FILE_EXISTS, "/sys/class/baseband_guard")) {
            return true;
        }
        return (check(CHECK.PHONE_MODEL, "SM-N960F") || check(CHECK.PHONE_MODEL, "SM-G960F") ||
                check(CHECK.PHONE_MODEL, "SM-G965F") || check(CHECK.PHONE_MODEL, "SM-N960N") ||
                check(CHECK.PHONE_MODEL, "SM-G960N") || check(CHECK.PHONE_MODEL, "SM-G965N") ||
                // Exynos7885 devices (Eureka Kernel R24U supported list)
                check(CHECK.PHONE_MODEL, "SM-A105F") || check(CHECK.PHONE_MODEL, "SM-A105FN") ||
                check(CHECK.PHONE_MODEL, "SM-A105GN") || check(CHECK.PHONE_MODEL, "SM-A105N") ||
                check(CHECK.PHONE_MODEL, "SM-A105M") || check(CHECK.PHONE_MODEL, "SM-A105G") ||
                check(CHECK.PHONE_MODEL, "SM-A202F") || check(CHECK.PHONE_MODEL, "SM-A202G") ||
                check(CHECK.PHONE_MODEL, "SM-A202N") || check(CHECK.PHONE_MODEL, "SM-A205F") ||
                check(CHECK.PHONE_MODEL, "SM-A205FN") || check(CHECK.PHONE_MODEL, "SM-A205GN") ||
                check(CHECK.PHONE_MODEL, "SM-A205N") || check(CHECK.PHONE_MODEL, "SM-A205YN") ||
                check(CHECK.PHONE_MODEL, "SM-A205G") || check(CHECK.PHONE_MODEL, "SM-A205U") ||
                check(CHECK.PHONE_MODEL, "SM-A3020") || check(CHECK.PHONE_MODEL, "SM-A305F") ||
                check(CHECK.PHONE_MODEL, "SM-A305FN") || check(CHECK.PHONE_MODEL, "SM-A305GN") ||
                check(CHECK.PHONE_MODEL, "SM-A305N") || check(CHECK.PHONE_MODEL, "SM-A305G") ||
                check(CHECK.PHONE_MODEL, "SM-A305YN") || check(CHECK.PHONE_MODEL, "SM-A307FN") ||
                check(CHECK.PHONE_MODEL, "SM-A307GN") || check(CHECK.PHONE_MODEL, "SM-A307FG") ||
                check(CHECK.PHONE_MODEL, "SM-A307N") || check(CHECK.PHONE_MODEL, "SM-A3070") ||
                check(CHECK.PHONE_MODEL, "SM-A405FN") || check(CHECK.PHONE_MODEL, "SM-A405FM") ||
                check(CHECK.PHONE_MODEL, "SM-A405S") || check(CHECK.PHONE_MODEL, "SM-A405X") ||
                check(CHECK.PHONE_MODEL, "SM-A405YN") || check(CHECK.PHONE_MODEL, "SM-A405G") ||
                check(CHECK.PHONE_MODEL, "SM-A405HQ") ||
                check(CHECK.PHONE_MODEL, "SM-M205F") || check(CHECK.PHONE_MODEL, "SM-M205FN") ||
                check(CHECK.PHONE_MODEL, "SM-M205G") || check(CHECK.PHONE_MODEL, "SM-M205N") ||
                check(CHECK.PHONE_MODEL, "SM-M3070") ||
                check(CHECK.FILE_EXISTS, "/sys/devices/platform/17500000.mali/clock") || check(CHECK.FILE_EXISTS, "/data/.kernelmanager/bypass"));
    }

    private static boolean check(CHECK mode, String value){
        boolean ret;

        switch(mode) {
            case ALL:
                ret = true;
                break;
            case FILE_EXISTS:
                ret = Utils.existFile(value);
                break;
            case PROCESSOR_MODEL:
                //TODO
                ret = false;
                break;
            case PROCESSOR_CORE:
                //TODO
                ret = false;
                break;
            case VENDOR:
                ret = Device.getVendor().equals(value);
                break;
            case PHONE_MODEL:
                ret = Device.getModel().equals(value);
                break;
            case ARCH:
                ret = Device.getArchitecture().equals(value);
                break;
            case HARDWARE:
                ret = Device.getHardware().equals(value);
                break;
            case BOOTLOADER:
                ret = Device.getBootloader().equals(value);
                break;
            case BOARD:
                ret = Device.getBoard().equals(value);
                break;
            default:
                ret = false;
        }

        return ret;
    }
}
package com.xxmustafacooTR.kernelmanager.utils.kernel.display;

import android.content.Context;

import com.xxmustafacooTR.kernelmanager.fragments.ApplyOnBootFragment;
import com.xxmustafacooTR.kernelmanager.utils.Utils;
import com.xxmustafacooTR.kernelmanager.utils.root.Control;

import java.util.ArrayList;
import java.util.List;

/**
 * mdnie display controls (Exynos7885 Samsung panel sysfs).
 * Ported from EKManager (Display.kt) to the xxTR KernelManager codebase.
 */
public class Display {

    private static final String ACCESSIBILITY = "/sys/class/lcd/panel/mdnie/accessibility";
    private static final String COLOUR_LENS = "/sys/class/lcd/panel/mdnie/color_lens";
    private static final String CUSTOM_BACKLIGHT_BRIGHTNESS = "/sys/class/lcd/panel/custom_brightness_lvl_enable";
    private static final String NIGHT_MODE_CONTROL = "/sys/class/lcd/panel/mdnie/night_mode";
    private static final String SCREEN_MODE = "/sys/class/lcd/panel/mdnie/mode";
    private static final String WHITE_BALANCE = "/sys/class/lcd/panel/mdnie/whiteRGB";

    private static Display sInstance;

    public static Display getInstance() {
        if (sInstance == null) {
            sInstance = new Display();
        }
        return sInstance;
    }

    public String getCustomBacklightControl() {
        return Utils.readFile(CUSTOM_BACKLIGHT_BRIGHTNESS);
    }

    public List<String> getAccessibilityModes() {
        List<String> modes = new ArrayList<>();
        modes.add("Disable");
        modes.add("Negative");
        modes.add("Color Blind");
        modes.add("Grayscale");
        modes.add("Grayscale Negative");
        return modes;
    }

    public List<String> getColourLensColours() {
        List<String> colours = new ArrayList<>();
        colours.add("Blue");
        colours.add("Azure");
        colours.add("Cyan");
        colours.add("Spring Green");
        colours.add("Green");
        colours.add("Chartreuse Green");
        colours.add("Yellow");
        colours.add("Orange");
        colours.add("Red");
        colours.add("Rose");
        colours.add("Magenta");
        colours.add("Violet");
        return colours;
    }

    public List<String> getScreenModes() {
        List<String> modes = new ArrayList<>();
        modes.add("Adaptive Display");
        modes.add("AMOLED Cinema");
        modes.add("AMOLED Photo");
        modes.add("Basic");
        modes.add("Custom Profile 1");
        modes.add("Custom Profile 2");
        return modes;
    }

    public void setCustomBacklightControl(int toggle, Context context) {
        run(Control.write(String.valueOf(toggle), CUSTOM_BACKLIGHT_BRIGHTNESS), CUSTOM_BACKLIGHT_BRIGHTNESS, context);
    }

    public void setNightMode(int toggle, int value, Context context) {
        run(Control.write(toggle + " " + value, NIGHT_MODE_CONTROL), NIGHT_MODE_CONTROL, context);
    }

    public void setAccessibilityMode(String value, Context context) {
        int mode;
        switch (value) {
            case "Negative":
                mode = 1;
                break;
            case "Color Blind":
                mode = 2;
                break;
            case "Grayscale":
                mode = 4;
                break;
            case "Grayscale Negative":
                mode = 5;
                break;
            default:
                mode = 0;
                break;
        }
        run(Control.write(String.valueOf(mode), ACCESSIBILITY), ACCESSIBILITY, context);
    }

    public void setColourLens(int toggle, String colour, int intensity, Context context) {
        int colourval;
        switch (colour) {
            case "Azure":
                colourval = 1;
                break;
            case "Cyan":
                colourval = 2;
                break;
            case "Spring Green":
                colourval = 3;
                break;
            case "Green":
                colourval = 4;
                break;
            case "Chartreuse Green":
                colourval = 5;
                break;
            case "Yellow":
                colourval = 6;
                break;
            case "Orange":
                colourval = 7;
                break;
            case "Red":
                colourval = 8;
                break;
            case "Rose":
                colourval = 9;
                break;
            case "Magenta":
                colourval = 10;
                break;
            case "Violet":
                colourval = 11;
                break;
            default:
                colourval = 0;
                break;
        }
        run(Control.write(toggle + " " + colourval + " " + intensity, COLOUR_LENS), COLOUR_LENS, context);
    }

    public void setColourLensInt(int toggle, int colour, int intensity, Context context) {
        run(Control.write(toggle + " " + colour + " " + intensity, COLOUR_LENS), COLOUR_LENS, context);
    }

    public void setScreenMode(String value, Context context) {
        int mode;
        switch (value) {
            case "Adaptive Display":
                mode = 4;
                break;
            case "AMOLED Photo":
                mode = 1;
                break;
            case "Basic":
                mode = 2;
                break;
            case "Custom Profile 1":
                mode = 3;
                break;
            case "Custom Profile 2":
                mode = 5;
                break;
            default: // "AMOLED Cinema"
                mode = 0;
                break;
        }
        run(Control.write(String.valueOf(mode), SCREEN_MODE), SCREEN_MODE, context);
    }

    public void setWhiteBalance(int r, int g, int b, Context context) {
        run(Control.write(r + " " + g + " " + b, WHITE_BALANCE), WHITE_BALANCE, context);
    }

    public String getNightModeState() {
        String[] value = Utils.readFile(NIGHT_MODE_CONTROL).split(" ");
        return value.length > 0 ? value[0] : "0";
    }

    public String getNightModeIntensity() {
        String[] value = Utils.readFile(NIGHT_MODE_CONTROL).split(" ");
        return value.length >= 2 ? value[1] : "0";
    }

    public String getAccessibilityMode() {
        String data = Utils.readFile(ACCESSIBILITY).trim();
        switch (data) {
            case "1":
                return "Negative";
            case "2":
                return "Color Blind";
            case "4":
                return "Grayscale";
            case "5":
                return "Grayscale Negative";
            default:
                return "Disable";
        }
    }

    public String getColourLensState() {
        String[] value = Utils.readFile(COLOUR_LENS).split(" ");
        return value.length > 0 ? value[0] : "0";
    }

    public String getColourLensColourInt() {
        String[] value = Utils.readFile(COLOUR_LENS).split(" ");
        return value.length >= 2 ? value[1] : "0";
    }

    public String getColourLensColour() {
        String data = getColourLensColourInt();
        switch (data) {
            case "1":
                return "Azure";
            case "2":
                return "Cyan";
            case "3":
                return "Spring Green";
            case "4":
                return "Green";
            case "5":
                return "Chartreuse Green";
            case "6":
                return "Yellow";
            case "7":
                return "Orange";
            case "8":
                return "Red";
            case "9":
                return "Rose";
            case "10":
                return "Magenta";
            case "11":
                return "Violet";
            default:
                return "Blue";
        }
    }

    public String getColourLensIntensity() {
        String[] value = Utils.readFile(COLOUR_LENS).split(" ");
        return value.length >= 3 ? value[2] : "0";
    }

    public String getScreenMode() {
        String data = Utils.readFile(SCREEN_MODE).trim();
        switch (data) {
            case "0":
                return "AMOLED Cinema";
            case "1":
                return "AMOLED Photo";
            case "2":
                return "Basic";
            case "3":
                return "Custom Profile 1";
            case "4":
                return "Adaptive Display";
            case "5":
                return "Custom Profile 2";
            default:
                return "AMOLED Cinema";
        }
    }

    public String getWhiteBalanceR() {
        String[] value = Utils.readFile(WHITE_BALANCE).split(" ");
        return value.length > 0 ? value[0] : "0";
    }

    public String getWhiteBalanceG() {
        String[] value = Utils.readFile(WHITE_BALANCE).split(" ");
        return value.length > 1 ? value[1] : "0";
    }

    public String getWhiteBalanceB() {
        String[] value = Utils.readFile(WHITE_BALANCE).split(" ");
        return value.length > 2 ? value[2] : "0";
    }

    public boolean hasCustomBacklightBrightness() {
        return Utils.existFile(CUSTOM_BACKLIGHT_BRIGHTNESS);
    }

    public boolean hasMdnieSupport() {
        return Utils.existFile(NIGHT_MODE_CONTROL);
    }

    public boolean supported() {
        return hasMdnieSupport();
    }

    private void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.MISC, id, context);
    }
}

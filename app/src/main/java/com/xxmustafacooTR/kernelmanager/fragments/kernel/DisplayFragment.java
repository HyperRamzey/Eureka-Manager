package com.xxmustafacooTR.kernelmanager.fragments.kernel;

import com.xxmustafacooTR.kernelmanager.R;
import com.xxmustafacooTR.kernelmanager.fragments.recyclerview.RecyclerViewFragment;
import com.xxmustafacooTR.kernelmanager.utils.Utils;
import com.xxmustafacooTR.kernelmanager.utils.kernel.display.Display;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.CardView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.DescriptionView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.RecyclerViewItem;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.SeekBarView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.SelectView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.SwitchView;

import java.util.List;

/**
 * mdnie display controls: screen mode, white balance, night mode, accessibility, colour lens.
 * Ported from EKManager DisplayFragment.
 */
public class DisplayFragment extends RecyclerViewFragment {

    private Display mDisplay;
    private SeekBarView mColourLensIntensity;
    private SeekBarView mNightModeIntensity;
    private SeekBarView mWhiteBalance;

    @Override
    protected void init() {
        super.init();
        mDisplay = Display.getInstance();
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        CardView mMainCard = new CardView(getActivity());
        mMainCard.setTitle(getString(R.string.display_title));
        DescriptionView info = new DescriptionView();
        info.setSummary(getString(R.string.display_desc));
        mMainCard.addItem(info);

        // Backlight control + night mode card
        CardView mBacklightCtrlCard = new CardView(getActivity());
        mBacklightCtrlCard.setTitle(getString(R.string.display_backlight_title));

        SwitchView mBackLightControl = new SwitchView();
        mBackLightControl.setTitle(getString(R.string.bklctrl_title));
        mBackLightControl.setSummary(getString(R.string.bklctrl_desc));
        mBackLightControl.setChecked(Utils.strToInt(mDisplay.getCustomBacklightControl()) == 1);
        mBackLightControl.addOnSwitchListener((switchView, isChecked)
                -> mDisplay.setCustomBacklightControl(isChecked ? 1 : 0, getActivity()));
        mBacklightCtrlCard.addItem(mBackLightControl);

        SwitchView mNightModeSwitch = new SwitchView();
        mNightModeSwitch.setTitle(getString(R.string.nmsw_title));
        mNightModeSwitch.setSummary(getString(R.string.nmsw_desc));
        mNightModeSwitch.setChecked(Utils.strToInt(mDisplay.getNightModeState()) == 1);
        mNightModeSwitch.addOnSwitchListener((switchView, isChecked) -> {
            int intensity = Utils.strToInt(mDisplay.getNightModeIntensity());
            mDisplay.setNightMode(isChecked ? 1 : 0, intensity, getActivity());
            mNightModeIntensity.setEnabled(isChecked);
        });
        mBacklightCtrlCard.addItem(mNightModeSwitch);

        mNightModeIntensity = new SeekBarView();
        mNightModeIntensity.setTitle(getString(R.string.nightmodeintensity_title));
        mNightModeIntensity.setSummary(getString(R.string.nightmodeintensity_desc));
        mNightModeIntensity.setMax(10);
        mNightModeIntensity.setMin(0);
        refreshNightMode();
        mNightModeIntensity.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                refreshNightMode();
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
                int state = Utils.strToInt(mDisplay.getNightModeState());
                mDisplay.setNightMode(state, position, getActivity());
            }
        });
        mBacklightCtrlCard.addItem(mNightModeIntensity);

        // Accessibility card
        CardView accessibilityCard = new CardView(getActivity());
        accessibilityCard.setTitle(getString(R.string.accessibilitymode_title));
        SelectView accessibilityMode = new SelectView();
        accessibilityMode.setTitle(getString(R.string.accessibilitymode_title));
        accessibilityMode.setSummary(getString(R.string.accessibilitymode_desc));
        accessibilityMode.setItems(mDisplay.getAccessibilityModes());
        accessibilityMode.setItem(mDisplay.getAccessibilityMode());
        accessibilityMode.setOnItemSelected((selectView, position, item)
                -> mDisplay.setAccessibilityMode(item, getActivity()));
        accessibilityCard.addItem(accessibilityMode);

        // Colour lens card
        CardView mColorLensCard = new CardView(getActivity());
        mColorLensCard.setTitle(getString(R.string.colourlens_title));

        SwitchView mColorLensSwitch = new SwitchView();
        mColorLensSwitch.setTitle(getString(R.string.colourlenstoggle_title));
        mColorLensSwitch.setSummary(getString(R.string.colourlenstoggle_desc));
        mColorLensSwitch.setChecked(Utils.strToInt(mDisplay.getColourLensState()) == 1);
        mColorLensSwitch.addOnSwitchListener((switchView, isChecked) -> {
            int mColorIntensity = Utils.strToInt(mDisplay.getColourLensIntensity());
            int mColorLens = Utils.strToInt(mDisplay.getColourLensColourInt());
            mDisplay.setColourLensInt(isChecked ? 1 : 0, mColorLens, mColorIntensity, getActivity());
            mColourLensIntensity.setEnabled(isChecked);
        });
        mColorLensCard.addItem(mColorLensSwitch);

        SelectView mColorFilter = new SelectView();
        mColorFilter.setTitle(getString(R.string.colourlenscolour_title));
        mColorFilter.setSummary(getString(R.string.colourlenscolour_desc));
        mColorFilter.setItems(mDisplay.getColourLensColours());
        mColorFilter.setItem(mDisplay.getColourLensColour());
        mColorFilter.setOnItemSelected((selectView, position, item) -> {
            int clcIntensity = Utils.strToInt(mDisplay.getColourLensIntensity());
            int clcState = Utils.strToInt(mDisplay.getColourLensState());
            mDisplay.setColourLens(clcState, item, clcIntensity, getActivity());
        });
        mColorLensCard.addItem(mColorFilter);

        mColourLensIntensity = new SeekBarView();
        mColourLensIntensity.setTitle(getString(R.string.colourlenscolourint_title));
        mColourLensIntensity.setSummary(getString(R.string.colourlenscolourint_desc));
        mColourLensIntensity.setMax(8);
        mColourLensIntensity.setMin(0);
        refreshColourLens();
        mColourLensIntensity.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                refreshColourLens();
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
                int cliState = Utils.strToInt(mDisplay.getColourLensState());
                int cliColour = Utils.strToInt(mDisplay.getColourLensColourInt());
                mDisplay.setColourLensInt(cliState, cliColour, position, getActivity());
            }
        });
        mColorLensCard.addItem(mColourLensIntensity);

        // Screen mode card
        CardView mScreenModeCard = new CardView(getActivity());
        mScreenModeCard.setTitle(getString(R.string.screenmode_title));

        SelectView mScreenMode = new SelectView();
        mScreenMode.setTitle(getString(R.string.screenmode_title));
        mScreenMode.setSummary(getString(R.string.screenmode_desc));
        mScreenMode.setItems(mDisplay.getScreenModes());
        mScreenMode.setItem(mDisplay.getScreenMode());
        mScreenMode.setOnItemSelected((selectView, position, item)
                -> mDisplay.setScreenMode(item, getActivity()));
        mScreenModeCard.addItem(mScreenMode);

        DescriptionView mSMTest = new DescriptionView();
        mSMTest.setTitle(getString(R.string.screenmodetest_title));
        mSMTest.setSummary(getString(R.string.screenmodetest_desc));
        mSMTest.setOnItemClickListener(new RecyclerViewItem.OnItemClickListener() {
            @Override
            public void onClick(RecyclerViewItem item) {
                final String[] mScreenModes = {"Adaptive Display", "AMOLED Cinema", "AMOLED Photo",
                        "Basic", "Custom Profile 1", "Custom Profile 2"};
                Thread testThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mScreenModes.length; i++) {
                            final int idx = i;
                            final String mode = mScreenModes[i];
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> mSMTest.setSummary(
                                        getString(R.string.screenmodetest_current) + " " + mode));
                            }
                            mDisplay.setScreenMode(mode, getActivity());
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ignored) {
                                return;
                            }
                        }
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> mSMTest.setSummary(
                                    getString(R.string.screenmodetest_desc)));
                        }
                    }
                });
                testThread.start();
            }
        });
        mScreenModeCard.addItem(mSMTest);

        // White balance card
        CardView mWhiteBalanceCard = new CardView(getActivity());
        mWhiteBalanceCard.setTitle(getString(R.string.whitebalance_title));

        mWhiteBalance = new SeekBarView();
        mWhiteBalance.setTitle(getString(R.string.whitebalance_title));
        mWhiteBalance.setSummary(getString(R.string.whitebalance_desc));
        mWhiteBalance.setMax(4);
        mWhiteBalance.setMin(0);
        refreshWhiteBalance();
        mWhiteBalance.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                refreshWhiteBalance();
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
                switch (position) {
                    case 0:
                        mDisplay.setWhiteBalance(-20, -16, -6, getActivity());
                        break;
                    case 1:
                        mDisplay.setWhiteBalance(-13, -11, -6, getActivity());
                        break;
                    case 2:
                        mDisplay.setWhiteBalance(-6, -6, -6, getActivity());
                        break;
                    case 3:
                        mDisplay.setWhiteBalance(-6, -9, -16, getActivity());
                        break;
                    case 4:
                        mDisplay.setWhiteBalance(-6, -12, -26, getActivity());
                        break;
                }
            }
        });
        mWhiteBalanceCard.addItem(mWhiteBalance);

        items.add(mMainCard);
        items.add(mBacklightCtrlCard);
        items.add(accessibilityCard);
        items.add(mColorLensCard);
        items.add(mScreenModeCard);
        items.add(mWhiteBalanceCard);
    }

    private void refreshNightMode() {
        getHandler().postDelayed(() -> {
            if (mNightModeIntensity != null && mDisplay != null) {
                mNightModeIntensity.setProgress(Utils.strToInt(mDisplay.getNightModeIntensity()));
            }
        }, 250);
    }

    private void refreshColourLens() {
        getHandler().postDelayed(() -> {
            if (mColourLensIntensity != null && mDisplay != null) {
                mColourLensIntensity.setProgress(Utils.strToInt(mDisplay.getColourLensIntensity()));
            }
        }, 250);
    }

    private void refreshWhiteBalance() {
        if (mDisplay == null) {
            return;
        }
        int r = Utils.strToInt(mDisplay.getWhiteBalanceR());
        int g = Utils.strToInt(mDisplay.getWhiteBalanceG());
        int b = Utils.strToInt(mDisplay.getWhiteBalanceB());
        int progress;
        if (r == -20 && g == -16 && b == -6) {
            progress = 0;
        } else if (r == -13 && g == -11 && b == -6) {
            progress = 1;
        } else if (r == -6 && g == -6 && b == -6) {
            progress = 2;
        } else if (r == -6 && g == -9 && b == -16) {
            progress = 3;
        } else if (r == -6 && g == -12 && b == -26) {
            progress = 4;
        } else {
            progress = 2; // neutral default
        }
        final int value = progress;
        getHandler().postDelayed(() -> {
            if (mWhiteBalance != null) {
                mWhiteBalance.setProgress(value);
            }
        }, 250);
    }
}

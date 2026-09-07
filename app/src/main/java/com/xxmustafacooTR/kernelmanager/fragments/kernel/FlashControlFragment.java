package com.xxmustafacooTR.kernelmanager.fragments.kernel;

import com.xxmustafacooTR.kernelmanager.R;
import com.xxmustafacooTR.kernelmanager.fragments.recyclerview.RecyclerViewFragment;
import com.xxmustafacooTR.kernelmanager.utils.kernel.flashcontrol.FlashControl;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.CardView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.DescriptionView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.RecyclerViewItem;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.SeekBarView;
import com.xxmustafacooTR.kernelmanager.views.recyclerview.SwitchView;

import java.util.List;

/**
 * Flashlight / torch brightness control.
 * Ported from EKManager FlashControlFragment (flashcontrol_long, torch level 1-10, test toggle).
 */
public class FlashControlFragment extends RecyclerViewFragment {

    private FlashControl mFlashControl;
    private SeekBarView mTorchLevel;

    @Override
    protected void init() {
        super.init();
        mFlashControl = FlashControl.getInstance();
    }

    @Override
    public int getSpanCount() {
        return 1;
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        // Torch level card
        CardView card = new CardView(getActivity());
        card.setTitle(getString(R.string.flashcontrol_title));

        DescriptionView desc = new DescriptionView();
        desc.setSummary(getString(R.string.flashcontrol_desc));
        card.addItem(desc);

        SwitchView test = new SwitchView();

        mTorchLevel = new SeekBarView();
        mTorchLevel.setTitle(getString(R.string.flashcontrol_lvl));
        mTorchLevel.setSummary(getString(R.string.flashcontrol_lvl_summary));
        mTorchLevel.setMax(10);
        mTorchLevel.setMin(1);
        mTorchLevel.setProgress(mFlashControl.getTorchLevel());
        mTorchLevel.setEnabled(mFlashControl.getUseTorchLevelBit() == 1
                || !mFlashControl.hasTorchLevelEnable());
        mTorchLevel.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                refreshTorchModes();
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
                mFlashControl.setTorchLevel(position + 1, getActivity());
                test.setSummary("Current brightness is " + (position + 1)
                        + "\nMax brightness is 10");
            }
        });
        card.addItem(mTorchLevel);

        test.setTitle("FlashLight Control");
        test.setSummary("Current brightness is " + (mFlashControl.getTorchLevel() + 1)
                + "\nMax brightness is 10");
        test.addOnSwitchListener((switchView, isChecked)
                -> {
            test.setSummary("Current brightness is " + (mFlashControl.getTorchLevel() + 1)
                    + "\nMax brightness is 10");
            mFlashControl.setToggleTorch(isChecked ? 1 : 0, getActivity());
        });

        // OneUI custom-level ownership card
        CardView oneui = new CardView(getActivity());
        oneui.setTitle(getString(R.string.flashcontrol_oneui_title));

        DescriptionView oneuiinfo = new DescriptionView();
        oneuiinfo.setSummary(getString(R.string.flashcontrol_oneui_desc));
        oneui.addItem(oneuiinfo);

        SwitchView custom = new SwitchView();
        custom.setChecked(mFlashControl.getUseTorchLevelBit() == 1);
        custom.setTitle(getString(R.string.flashcontrol_custom_title));
        custom.setSummary(getString(R.string.flashcontrol_custom_desc,
                mFlashControl.getUseTorchLevelBit() == 1 ? "Eureka" : "OneUI"));
        custom.addOnSwitchListener((switchView, isChecked) -> {
            custom.setSummary(getString(R.string.flashcontrol_custom_desc,
                    isChecked ? "Eureka" : "OneUI"));
            mFlashControl.setUseTorchLevelBit(isChecked ? 1 : 0, getActivity());
            mTorchLevel.setEnabled(isChecked || !mFlashControl.hasTorchLevelEnable());
        });
        oneui.addItem(custom);

        items.add(oneui);
        items.add(test);
        if (card.size() > 0) {
            items.add(card);
        }
    }

    private void refreshTorchModes() {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTorchLevel != null && mFlashControl != null) {
                    mTorchLevel.setProgress(mFlashControl.getTorchLevel());
                }
            }
        }, 250);
    }
}

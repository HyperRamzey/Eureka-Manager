# Eureka Manager v1.0

First release of **Eureka Manager** — the kernel manager for
[Eureka-Kernel-R24U](https://github.com/HyperRamzey/Eureka-Kernel-R24U)
(Exynos7885: Galaxy A10/A20/A20e/A30/A30s/A40/M20/M30s).

## Features

- **ASV Voltage Margin** — per-domain runtime voltage margins (BIG/LITTLE
  clusters, GPU, MIF, INT buses) via the kernel's `asv_margin` sysfs class.
  0 = stock ECT table; positive = stability margin; negative = undervolt at
  your own risk. Apply-on-boot supported. (Kernel v1.2 required.)
- **Baseband Guard dashboard** — Shannon CP (modem) crash monitor: live CP
  state, crash/recovery counters, last event, enable switch, opt-in
  reboot-on-dead policy switch. (Kernel v1.2 required.)
- All inherited KernelAdiutor-family tools: CPU/GPU frequency & governors,
  per-cluster CPU voltages, bus voltages (MIF/INT/CAM/DISP), hotplug, wake,
  spectrum profiles, misc, entropy, KSM, VM, wakelocks and more.

Both Eureka features self-hide when the kernel sysfs surface is absent, so
the app also runs on other kernels of the family.

## Install

- Android 8.1+ (minSdk 27), arm64-v8a only
- Root required (KernelSU/Magisk) for kernel control

## APK

- `Eureka-Manager-v1.0.apk` — release build, signed (APK Signature Scheme v2+v3)
- Device-verified on SM-A307FN (DerpFest 15 + Eureka Kernel R24U v1.2):
  installs, launches, ASV margins + baseband guard dashboards functional

## Lineage (GPL-3.0)

KernelAdiutor (Willi Ye) → MTweaks (morogoku) → ThunderTweaks
(ThunderStorms21th) → hKtweaks (corsicanu) → KernelManager (xxmustafacooTR,
direct base @ 2071149) → **Eureka Manager**. See README for the full
attribution block. DiscreteSeekBar (AnderWeb, Apache-2.0) vendored into
the app source after its jitpack artifact disappeared.

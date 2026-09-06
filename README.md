# Eureka Manager

Kernel manager for [Eureka-Kernel-R24U](https://github.com/HyperRamzey/Eureka-Kernel-R24U)
(Exynos7885: Galaxy A10/A20/A20e/A30/A30s/A40/M20/M30s) — with the kernel
features the R24U release actually ships:

- **ASV Voltage Margin** — runtime per-domain voltage margins (BIG/LITTLE/GPU/
  MIF/INT) via the kernel's `asv_margin` sysfs class (ACPM `MARGIN_REQ`).
  0 = stock ECT table; positive = stability margin; negative = undervolt.
- **Baseband Guard** — Shannon CP (modem) crash monitor + auto-recovery
  policy: status counters (crashes/recoveries/last event), enable switch,
  opt-in reboot-on-dead.
- All the inherited KernelAdiutor-family tools (CPU/GPU/bus voltages,
  governors, hotplug, wake, spectrum, misc).

## Lineage (GPL-3.0 combined fork)

This project is a continuation of the KernelAdiutor family tree, per the
GPL-3.0 rights each ancestor granted:

| Ancestor | Contribution |
| --- | --- |
| [KernelAdiutor](https://github.com/Grarak/KernelAdiutor) (Willi Ye) | the original app skeleton |
| [MTweaks-KernelAdiutorMOD](https://github.com/morogoku/MTweaks-KernelAdiutorMOD) (morogoku) | Samsung/OneUI groundwork, voltage fragments |
| [ThunderTweaks](https://github.com/ThunderStorms21th/ThunderTweaks) (ThunderStorms21th) | Exynos focus, game/battery fragments |
| [hKtweaks](https://github.com/corsicanu/hKtweaks) (corsicanu) | Exynos9810-era devfreq/bus voltage UI |
| [KernelManager](https://github.com/xxmustafacooTR/KernelManager) (xxmustafacooTR) | direct base of this fork — newest gradle tree of the family |
| EKManager v2 (EurekaTeam) | Exynos7885 paths + OneUI design (reference: jadx decompile of v2.0.0) |

The fork base is xxmustafacooTR/KernelManager @ `2071149` (its repo has
forking disabled; source is carried forward here under GPL-3.0 with this
attribution block, preserving all original headers and LICENSE).

## What this fork adds

- `AsvMargin`/`AsvMarginFragment` — `/sys/class/asv_margin/<domain>/margin`
  (kernel driver: `drivers/soc/samsung/cal-if/asv_margin.c`)
- `BasebandGuard`/`BasebandGuardFragment` — `/sys/class/baseband_guard/`
  (kernel driver: `drivers/misc/modem_v1/baseband_guard.c`)
- Both fragments register in NavigationActivity only when their sysfs
  surface exists (`.supported()` checks), so the APK also works on other
  kernels of the family without the Eureka features.

## Build

Android Studio (any recent version): open the project, let gradle sync,
Build → APK. JDK 11+ required. No special flavors.

## License

GPL-3.0 — same as every ancestor in the tree. See LICENSE (from the base
repo, unchanged).

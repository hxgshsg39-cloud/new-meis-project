# meis client

Lightweight Fabric client mod for meis project (Minecraft 1.21.4).

Features: FPS HUD, coordinates, toggle sprint, hold-C zoom, fullbright (G), hitboxes (H), settings menu (Right Shift).
Blur/motion blur is NOT included: it needs shader work and costs FPS, which hurts low-end PCs.

## Build
Works with JDK 21 or newer (including 25). Requires Gradle 9.1+ (gradle.org/install).

    gradle build

The jar appears in `build/libs/meis-client-0.1.0.jar`. Put it in the `mods` folder together with Fabric API
(meis launcher's Fast mode already installs Fabric API; modded play uses the `game-fabric` folder).

If the build fails on a mapping name, check the versions in `gradle.properties` at https://fabricmc.net/develop

# Unity Client

Unity Client is a clean Minecraft Java Edition Fabric utility client framework for anarchy or private servers where utility clients are allowed. The project targets Minecraft `1.21.11`, Java 21, Fabric Loader, Fabric API, Fabric Loom, Yarn mappings, and Mixin.

This repository is an original implementation. It is inspired by the architecture and polish of established utility clients, but it does not copy their source code. It does not include token logging, account/session stealing, malware, crashers, packet crashers, hidden networking, spam bots, auth bypasses, forced OP behavior, or anti-cheat bypass logic.

## Current Features

- Fabric client initializer and safe lifecycle setup.
- Modular client framework with categories, setting groups, keybinds, visibility, enable state, and callback hooks.
- Automatic setting model for booleans, numbers, enums, colors, strings, and keybinds.
- JSON config saving under `.minecraft/config/unity-client/`.
- Draggable ClickGUI panels opened with Right Shift.
- GUI scale setting with scaled hit testing.
- Module toggle, expand, and middle-click bind capture.
- Search box filtering for modules and settings.
- Basic HUD editor opened with H.
- HUD manager with default elements for watermark, array list, FPS, ping, coordinates, direction, speed, armor, potion, totem count, and notifications.
- Notification manager and theme settings.
- Friend and waypoint managers with JSON persistence.
- Command framework with `.toggle`, `.bind`, `.config`, `.friend`, `.waypoint`, and `.help`.
- Conservative registered modules across client, render, player, movement, combat, world, and misc categories.

## Controls

- Right Shift: open ClickGUI
- H: open HUD editor
- Module rows: left click toggles, right click expands, middle click captures keybind
- Commands: type messages beginning with `.`

## Build

```powershell
.\gradlew build
```

Loom also provides:

```powershell
.\gradlew runClient
```

## Install

Build the project, then copy `build/libs/unity-client-0.1.0.jar` into your Fabric `mods` folder alongside Fabric API for Minecraft `1.21.11`.

## Config

Unity Client stores configuration under:

```text
.minecraft/config/unity-client/
```

Files include `modules.json`, `hud.json`, `theme.json`, `friends.json`, `waypoints.json`, profile files, and `server-profiles.json`. Missing or corrupt JSON is handled with safe defaults and backup files where possible.

## Development Structure

The primary Java package is `dev.unityclient`. Important areas are:

- `module`: module base classes, manager, categories, and registered modules.
- `setting`: typed setting system used by modules and GUI widgets.
- `gui`: ClickGUI, HUD editor, theme, widgets, and animations.
- `hud`: HUD element framework and default elements.
- `config`: JSON config and profile support.
- `command`: client command parsing and command implementations.
- `friend`, `waypoint`, `notification`: supporting data managers.
- `render`, `util`: rendering wrappers and utility helpers.
- `mixin`: reserved Mixin classes kept minimal.

## Limitations

This first implementation favors a compiling, clean framework over risky version-fragile game manipulation. Complex modules such as Freecam, StorageESP, Offhand, CrystalHelper, and NewChunks are registered conservatively with settings and descriptions, but their aggressive behavior is intentionally disabled until each feature can be implemented and tested against the target Minecraft version without exploit or crash behavior.

## Credits

Fabric, Fabric API, Fabric Loom, Yarn mappings, and Mixin make this project possible. Existing utility clients inspired the goals for modularity, GUI polish, and configuration ergonomics; no source code was copied from them.

## Troubleshooting

- Use Java 21 or newer with Gradle compiling to release 21.
- Ensure Fabric API matches Minecraft `1.21.11`.
- Delete or rename `.minecraft/config/unity-client/` if you want a fresh client config.
- If a config file becomes corrupt, Unity Client attempts to back it up and continue with defaults.

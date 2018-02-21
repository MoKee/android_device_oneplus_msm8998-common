/*
 * Copyright (C) 2015 The CyanogenMod Project
 * Copyright (C) 2015 The MoKee Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mokee.hardware;

import java.util.HashMap;

import mokee.hardware.DisplayMode;

import org.mokee.internal.util.FileUtils;

/*
 * Display Modes API
 *
 * A device may implement a list of preset display modes for different
 * viewing intents, such as movies, photos, or extra vibrance. These
 * modes may have multiple components such as gamma correction, white
 * point adjustment, etc, but are activated by a single control point.
 *
 * This API provides support for enumerating and selecting the
 * modes supported by the hardware.
 */

public class DisplayModeControl {

    private static final String PROFILE_PATH
            = "/sys/class/graphics/fb0/color_profile";

    private static final String PROFILE_CAPS_PATH
            = "/sys/class/graphics/fb0/color_profile_caps";

    private static final String LOCAL_PROFILE_ID
            = "/data/vendor/display/mokee_color_profile";

    private static final DisplayMode MODE_NONE
            = new DisplayMode(0, "basic");

    private static final DisplayMode MODE_SRGB
            = new DisplayMode(1, "srgb");

    private static final DisplayMode MODE_DCI_P3
            = new DisplayMode(3, "dci_p3");

    private static final DisplayMode MODE_READING
            = new DisplayMode(5, "reading");

    private static final DisplayMode MODE_ADAPTIVE
            = new DisplayMode(6, "adaptive");

    private static final HashMap<Integer, DisplayMode> MODES
            = new HashMap<>();

    static {
        MODES.put(MODE_NONE.id, MODE_NONE);

        final String line = FileUtils.readOneLine(PROFILE_CAPS_PATH);
        if (line != null) {
            final int caps = Integer.parseInt(line);
            final DisplayMode[] modes = new DisplayMode[] {
                MODE_SRGB,
                MODE_DCI_P3,
                MODE_READING,
                MODE_ADAPTIVE,
            };

            for (final DisplayMode mode : modes) {
                final int bit = 1 << (mode.id - 1);
                if ((caps & bit) != 0) {
                    MODES.put(mode.id, mode);
                }
            }
        }
    }

    /*
     * All HAF classes should export this boolean.
     * Real implementations must, of course, return true
     */
    public static boolean isSupported() {
        return FileUtils.isFileWritable(PROFILE_PATH) &&
                FileUtils.isFileReadable(PROFILE_CAPS_PATH);
    }

    /*
     * Get the list of available modes. A mode has an integer
     * identifier and a string name.
     *
     * It is the responsibility of the upper layers to
     * map the name to a human-readable format or perform translation.
     */
    public static DisplayMode[] getAvailableModes() {
        return MODES.values().toArray(
                new DisplayMode[MODES.size()]);
    }

    /*
     * Get the name of the currently selected mode. This can return
     * null if no mode is selected.
     */
    public static DisplayMode getCurrentMode() {
        final String line = FileUtils.readOneLine(LOCAL_PROFILE_ID);
        if (line == null) {
            return null;
        }

        final int id = Integer.parseInt(line);
        return MODES.get(id);
    }

    /*
     * Selects a mode from the list of available modes by it's
     * string identifier. Returns true on success, false for
     * failure. It is up to the implementation to determine
     * if this mode is valid.
     */
    public static boolean setMode(DisplayMode mode, boolean makeDefault) {
        for (final DisplayMode item : MODES.values()) {
            if (item.name.equals(mode.name)) {
                final String value = String.valueOf(item.id);
                return FileUtils.writeLine(PROFILE_PATH, value) &&
                    (!makeDefault || FileUtils.writeLine(LOCAL_PROFILE_ID, value));
            }
        }

        return false;
    }

    /*
     * Gets the preferred default mode for this device by it's
     * string identifier. Can return null if there is no default.
     */
    public static DisplayMode getDefaultMode() {
        return MODE_NONE;
    }

}

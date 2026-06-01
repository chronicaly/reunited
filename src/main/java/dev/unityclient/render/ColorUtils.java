package dev.unityclient.render;

public final class ColorUtils {
    private ColorUtils() {
    }

    public static String toHex(int argb) {
        return String.format("#%08X", argb);
    }

    public static int fromHex(String value, int fallback) {
        if (value == null) {
            return fallback;
        }
        String clean = value.trim().replace("#", "");
        try {
            if (clean.length() == 6) {
                return (int) (0xFF000000L | Long.parseLong(clean, 16));
            }
            if (clean.length() == 8) {
                return (int) Long.parseLong(clean, 16);
            }
        } catch (NumberFormatException ignored) {
        }
        return fallback;
    }
}

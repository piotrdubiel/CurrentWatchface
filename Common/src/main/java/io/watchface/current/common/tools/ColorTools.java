package io.watchface.current.common.tools;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ColorTools {
    public static int textColorForBackground(int background) {
        if (Color.red(background) * 0.299 + Color.green(background) * 0.587 + Color.blue(background) * 0.114 > 186) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    public static int colorForBitmap(Bitmap bitmap) {
        long red = 0;
        long green = 0;
        long blue = 0;
        long count = 0;

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int c = bitmap.getPixel(x, y);
                red += Color.red(c);
                green += Color.green(c);
                blue += Color.blue(c);
                count++;
            }
        }

        return Color.rgb((int) (red / count), (int)(green / count), (int)(blue / count));
    }
}

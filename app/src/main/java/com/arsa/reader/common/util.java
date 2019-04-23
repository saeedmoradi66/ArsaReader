package com.arsa.reader.common;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class util {
    public static int getScreenHeight(Context activity) {
        int columnHeight;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnHeight = point.y;
        return columnHeight;
    }
}

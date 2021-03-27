package com.example.notes2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
///import android.graphics.PorterDuffXfermode;

/**
 * Created by tarek on 6/17/17.
 */
public class DrawcropBitmapUtils {

    public static Bitmap getCroppedBitmap(Bitmap src, Path path) {
        Bitmap output = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0XFF000000);
        canvas.drawPath(path, paint);
        // Keeps the source pixels that cover the destination pixels,
        // discards the remaining source and destination pixels.
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, 0, 0, paint);
        return output;
    }
}

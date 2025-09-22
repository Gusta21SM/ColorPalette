package com.library.colorpalette;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ColorPalette {
    private final Context context;
    private Dialog dialog;
    private Color selectedColor = Color.valueOf(Color.RED);
    private Bitmap previewBitmap;
    private final Bitmap colors;
    private float currentSaturation = 1f;
    private float currentValue = 1f;

    public ColorPalette(Context context) {
        this.context = context;

        colors = Bitmap.createBitmap(50, 255 * 6 + 2, Bitmap.Config.ARGB_8888);
        for (int y = 0; y <= 255; y++) {
            for (int x = 0; x < 50; x++) {
                colors.setPixel(x, y, Color.argb(255, 255, y, 0));
                colors.setPixel(x, y + 255 + 1, Color.argb(255, 255 - y, 255, 0));
                colors.setPixel(x, y + 255 * 2 + 1, Color.argb(255, 0, 255, y));
                colors.setPixel(x, y + 255 * 3 + 1, Color.argb(255, 0, 255 - y, 255));
                colors.setPixel(x, y + 255 * 4 + 1, Color.argb(255, y, 0, 255));
                colors.setPixel(x, y + 255 * 5 + 1, Color.argb(255, 255, 0, 255 - y));
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void show(onColorSelectedListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_color_palette, null);

        ImageView colorPreview = view.findViewById(R.id.colorPreview);
        ImageView colorType = view.findViewById(R.id.colorType);
        ImageView arrows = view.findViewById(R.id.arrows);
        ImageView currentColor = view.findViewById(R.id.currentColor);
        ImageView pointer = view.findViewById(R.id.pointer);

        redraw(colorPreview, Color.valueOf(Color.argb(255, 0, 0, 0)));

        colorPreview.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                float x = event.getX();
                float y = event.getY();

                float viewWidth = colorPreview.getWidth();
                float viewHeight = colorPreview.getHeight();

                if (previewBitmap != null) {
                    int bmpWidth = previewBitmap.getWidth();
                    int bmpHeight = previewBitmap.getHeight();

                    int xi = (int) ((x / viewWidth) * bmpWidth);
                    int yi = (int) ((y / viewHeight) * bmpHeight);

                    xi = Math.max(0, Math.min(xi, bmpWidth - 1));
                    yi = Math.max(0, Math.min(yi, bmpHeight - 1));

                    if (yi > xi) {
                        yi = xi;
                    }

                    float fixedX = (xi / (float) bmpWidth) * viewWidth;
                    float fixedY = (yi / (float) bmpHeight) * viewHeight;

                    pointer.setX(fixedX - pointer.getWidth() / 2f);
                    pointer.setY(fixedY - pointer.getHeight() / 2f);

                    int pixel = previewBitmap.getPixel(xi, yi);
                    Color chosen = Color.valueOf(pixel);

                    float[] hsv = new float[3];
                    Color.colorToHSV(chosen.toArgb(), hsv);
                    currentSaturation = hsv[1];
                    currentValue = hsv[2];

                    currentColor.setBackgroundColor(chosen.toArgb());
                    selectedColor = chosen;
                }
            }
            return true;
        });

        colorType.setBackground(new BitmapDrawable(null, colors));

        colorType.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                float y = event.getY();

                float viewHeight = colorType.getHeight();
                float bitmapHeight = colors.getHeight();

                int yi = (int) ((y / viewHeight) * bitmapHeight);

                Color chosen = Color.valueOf(Color.RED);
                if (y >= 0) {
                    if (y < viewHeight) {
                        arrows.setY(y - arrows.getHeight() / 2f);

                        if (yi >= 0 && yi < bitmapHeight) {
                            chosen = Color.valueOf(colors.getPixel(0, yi));
                        }
                    } else {
                        arrows.setY(viewHeight - arrows.getHeight() / 2f);
                    }
                } else {
                    arrows.setY(-arrows.getHeight() / 2f);
                }
                redraw(colorPreview, chosen);
                currentColor.setBackgroundColor(selectedColor.toArgb());
            }
            return true;
        });

        arrows.post(() -> arrows.setX(colorType.getWidth() / 2f - arrows.getWidth() / 2f));

        Button confirm = view.findViewById(R.id.btnConfirm);
        Button cancel = view.findViewById(R.id.btnCancel);

        confirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onColorConfirmed(selectedColor);
                hide();
            }
        });

        cancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCanceled();
                hide();
            }
        });

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        dialog.show();
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void redraw(ImageView colorPreview, Color baseColor) {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor.toArgb(), hsv);

        int width = 255;
        int height = 255;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y > x) break;
                float sat = x / (float)(width - 1);
                float val = 1f - y / (float)(height - 1);
                int color = Color.HSVToColor(new float[]{hsv[0], sat, val});
                bitmap.setPixel(x, y, color);
            }
        }

        int preserved = Color.HSVToColor(new float[]{hsv[0], currentSaturation, currentValue});
        colorPreview.setImageBitmap(bitmap);
        previewBitmap = bitmap;
        selectedColor = Color.valueOf(preserved);
    }
}

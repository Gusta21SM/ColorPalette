package com.library.colorpalette;

import android.graphics.Color;

public interface onColorSelectedListener {
    void onColorConfirmed(Color color);
    void onCanceled();
}

package com.colorpalette;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.library.colorpalette.ColorPalette;
import com.library.colorpalette.onColorSelectedListener;

// ------------------ Exemplo de Uso ------------------ //

public class MainActivity extends AppCompatActivity {
    private ColorPalette colorPalette;
    private ImageView square;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorPalette = new ColorPalette(this);
        square = findViewById(R.id.square);
    }

    public void openColorPalette(View view) {
        colorPalette.show(new onColorSelectedListener() {
            @Override
            public void onColorConfirmed(Color color) {
                //aqui captura a cor selecionada
                square.setBackgroundColor(color.toArgb());
            }

            @Override
            public void onCanceled() {
                //aqui chama a função caso cancele
                square.setBackgroundResource(R.drawable.empty);
            }
        });
    }
}

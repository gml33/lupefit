package com.lupefit.core.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.databinding.BindingAdapter;

import com.google.android.material.chip.Chip;

public class EscaneoBindingAdapters {

    @BindingAdapter("tipoIcon")
    public static void setTipoIcon(ImageView imageView, String tipo) {
        int drawableRes = "codigo_barras".equals(tipo)
            ? com.lupefit.core.ui.R.drawable.ic_barcode
            : com.lupefit.core.ui.R.drawable.ic_ai_food;
        imageView.setImageResource(drawableRes);
        Context context = imageView.getContext();
        @ColorInt int color = context.getColor(com.lupefit.core.ui.R.color.md_theme_primary);
        imageView.setColorFilter(color);
    }

    @BindingAdapter("tipoText")
    public static void setTipoText(com.google.android.material.chip.Chip chip, String tipo) {
        int stringRes = "codigo_barras".equals(tipo)
            ? com.lupefit.core.ui.R.string.historial_escaneos_tipo_codigo
            : com.lupefit.core.ui.R.string.historial_escaneos_tipo_comida;
        chip.setText(stringRes);
    }

    @BindingAdapter("agregadoText")
    public static void setAgregadoText(TextView textView, boolean agregadoAlDiario) {
        int stringRes = agregadoAlDiario
            ? com.lupefit.core.ui.R.string.historial_escaneos_agregado
            : com.lupefit.core.ui.R.string.historial_escaneos_no_agregado;
        textView.setText(stringRes);
    }

    @BindingAdapter("agregadoColor")
    public static void setAgregadoColor(TextView textView, boolean agregadoAlDiario) {
        int colorRes = agregadoAlDiario
            ? com.lupefit.core.ui.R.color.status_success
            : com.lupefit.core.ui.R.color.status_warning;
        Context context = textView.getContext();
        @ColorInt int color = context.getColor(colorRes);
        textView.setTextColor(color);
    }
}
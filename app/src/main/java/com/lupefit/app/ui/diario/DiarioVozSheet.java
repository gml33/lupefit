package com.lupefit.app.ui.diario;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.lupefit.app.R;
import com.lupefit.app.data.local.entity.FoodProduct;
import com.lupefit.app.domain.engine.voice.NativeAndroidVoiceEngine;
import com.lupefit.app.domain.engine.voice.VoiceFoodParser;
import com.lupefit.app.domain.engine.voice.VoiceTranscriptionEngine;
import com.lupefit.app.databinding.SheetDiarioVozBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BottomSheet para registro rápido por voz (Hands-Free Logging).
 * Usa SpeechRecognizer nativo + parser nutricional.
 */
public class DiarioVozSheet extends BottomSheetDialogFragment {

    private static final String TAG = "DiarioVozSheet";

    private SheetDiarioVozBinding binding;
    private NativeAndroidVoiceEngine voiceEngine;
    private VoiceFoodParser foodParser;
    private ExecutorService parserExecutor;
    private Handler mainHandler;

    private ParsedFoodAdapter adapter;
    private List<VoiceTranscriptionEngine.ParsedFoodItem> parsedItems = new ArrayList<>();

    private boolean isListening = false;

    public interface OnFoodsLoggedListener {
        void onFoodsLogged(List<VoiceTranscriptionEngine.ParsedFoodItem> items);
    }

    private OnFoodsLoggedListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        voiceEngine = new NativeAndroidVoiceEngine(requireContext());
        foodParser = new VoiceFoodParser(
                ((com.lupefit.app.MyApplication) requireActivity().getApplication())
                        .getContainer().getFoodCatalogRepository()
        );
        parserExecutor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        setStyle(STYLE_NORMAL, com.lupefit.core.ui.R.style.Theme_LuPeFit_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SheetDiarioVozBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        // RecyclerView para alimentos detectados
        adapter = new ParsedFoodAdapter(parsedItems);
        binding.rvDetectedFoods.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvDetectedFoods.setAdapter(adapter);

        // Botón micrófono
        binding.btnMic.setOnClickListener(v -> toggleListening());

        // Botones acción
        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnConfirm.setOnClickListener(v -> {
            if (!parsedItems.isEmpty() && listener != null) {
                listener.onFoodsLogged(new ArrayList<>(parsedItems));
            }
            dismiss();
        });
    }

    private void toggleListening() {
        if (isListening) {
            stopListening();
        } else {
            startListening();
        }
    }

    private void startListening() {
        isListening = true;
        parsedItems.clear();
        adapter.notifyDataSetChanged();

        // UI: mostrar estado de escucha
        binding.tvMicStatus.setText(R.string.diario_voz_escuchando);
        binding.tvMicStatus.setTextColor(resolveColorPrimary(requireContext()));
        startMicAnimation();

        // Ocultar lista y mostrar transcripción
        binding.llDetectedFoods.setVisibility(View.GONE);
        binding.llTranscription.setVisibility(View.VISIBLE);
        binding.tvTranscription.setText("");
        binding.btnConfirm.setEnabled(false);

        // Iniciar reconocimiento
        voiceEngine.startListening(new VoiceTranscriptionEngine.Listener() {
            @Override
            public void onPartialResult(String partialText) {
                mainHandler.post(() -> {
                    binding.tvTranscription.setText(partialText);
                });
            }

            @Override
            public void onFinalResult(String finalText) {
                mainHandler.post(() -> {
                    stopMicAnimation();
                    binding.tvMicStatus.setText(R.string.diario_voz_procesando);
                    binding.tvTranscription.setText(finalText);
                });

                // Parsear en background
                parserExecutor.execute(() -> parseVoiceText(finalText));
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() -> {
                    stopListening();
                    Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void stopListening() {
        if (!isListening) return;
        isListening = false;

        voiceEngine.stopListening();
        stopMicAnimation();

        binding.tvMicStatus.setText(R.string.diario_voz_toca_mic);
        binding.tvMicStatus.setTextColor(resolveColorPrimary(requireContext()));
    }

    private int resolveColorPrimary(Context context) {
        return ContextCompat.getColor(context, com.lupefit.core.ui.R.color.md_theme_primary);
    }

    private void parseVoiceText(String text) {
        try {
            List<VoiceTranscriptionEngine.ParsedFoodItem> items = foodParser.parse(text);

            mainHandler.post(() -> {
                if (items.isEmpty()) {
                    binding.tvMicStatus.setText(R.string.diario_voz_no_detectado);
                    binding.btnConfirm.setEnabled(false);
                } else {
                    parsedItems.clear();
                    parsedItems.addAll(items);
                    adapter.notifyDataSetChanged();

                    binding.llTranscription.setVisibility(View.GONE);
                    binding.llDetectedFoods.setVisibility(View.VISIBLE);
                    binding.tvMicStatus.setText(R.string.diario_voz_lista_confirmar);
                    binding.btnConfirm.setEnabled(true);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error parseando voz", e);
            mainHandler.post(() -> {
                binding.tvMicStatus.setText(R.string.diario_voz_error);
                binding.btnConfirm.setEnabled(false);
            });
        }
    }

    private void startMicAnimation() {
        // Animación de pulso en los anillos
        ObjectAnimator pulseOuter = ObjectAnimator.ofFloat(binding.vMicRingOuter, "scaleX", 1f, 1.3f);
        pulseOuter.setDuration(800);
        pulseOuter.setRepeatCount(ObjectAnimator.INFINITE);
        pulseOuter.setRepeatMode(ObjectAnimator.REVERSE);
        pulseOuter.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseOuter.start();

        ObjectAnimator pulseOuterY = ObjectAnimator.ofFloat(binding.vMicRingOuter, "scaleY", 1f, 1.3f);
        pulseOuterY.setDuration(800);
        pulseOuterY.setRepeatCount(ObjectAnimator.INFINITE);
        pulseOuterY.setRepeatMode(ObjectAnimator.REVERSE);
        pulseOuterY.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseOuterY.start();

        ObjectAnimator pulseMiddle = ObjectAnimator.ofFloat(binding.vMicRingMiddle, "scaleX", 1f, 1.2f);
        pulseMiddle.setDuration(800);
        pulseMiddle.setRepeatCount(ObjectAnimator.INFINITE);
        pulseMiddle.setRepeatMode(ObjectAnimator.REVERSE);
        pulseMiddle.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseMiddle.start();

        ObjectAnimator pulseMiddleY = ObjectAnimator.ofFloat(binding.vMicRingMiddle, "scaleY", 1f, 1.2f);
        pulseMiddleY.setDuration(800);
        pulseMiddleY.setRepeatCount(ObjectAnimator.INFINITE);
        pulseMiddleY.setRepeatMode(ObjectAnimator.REVERSE);
        pulseMiddleY.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseMiddleY.start();

        // Guardar referencias para cancelar
        binding.btnMic.setTag(com.lupefit.app.R.id.anim_pulse_outer, pulseOuter);
        binding.btnMic.setTag(com.lupefit.app.R.id.anim_pulse_outer_y, pulseOuterY);
        binding.btnMic.setTag(com.lupefit.app.R.id.anim_pulse_middle, pulseMiddle);
        binding.btnMic.setTag(com.lupefit.app.R.id.anim_pulse_middle_y, pulseMiddleY);
    }

    private void stopMicAnimation() {
        ObjectAnimator a1 = (ObjectAnimator) binding.btnMic.getTag(com.lupefit.app.R.id.anim_pulse_outer);
        ObjectAnimator a2 = (ObjectAnimator) binding.btnMic.getTag(com.lupefit.app.R.id.anim_pulse_outer_y);
        ObjectAnimator a3 = (ObjectAnimator) binding.btnMic.getTag(com.lupefit.app.R.id.anim_pulse_middle);
        ObjectAnimator a4 = (ObjectAnimator) binding.btnMic.getTag(com.lupefit.app.R.id.anim_pulse_middle_y);

        if (a1 != null) a1.cancel();
        if (a2 != null) a2.cancel();
        if (a3 != null) a3.cancel();
        if (a4 != null) a4.cancel();

        // Reset scale
        binding.vMicRingOuter.setScaleX(1f);
        binding.vMicRingOuter.setScaleY(1f);
        binding.vMicRingMiddle.setScaleX(1f);
        binding.vMicRingMiddle.setScaleY(1f);
    }

    public void setOnFoodsLoggedListener(OnFoodsLoggedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        voiceEngine.destroy();
        parserExecutor.shutdown();
    }

    // ==================== ADAPTER ====================

    private static class ParsedFoodAdapter
            extends androidx.recyclerview.widget.RecyclerView.Adapter<ParsedFoodAdapter.ViewHolder> {

        private final List<VoiceTranscriptionEngine.ParsedFoodItem> items;

        ParsedFoodAdapter(List<VoiceTranscriptionEngine.ParsedFoodItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_parsed_food, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VoiceTranscriptionEngine.ParsedFoodItem item = items.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final TextView tvFoodName;
            final TextView tvFoodDetail;
            final View vProteinBar, vCarbsBar, vFatBar;

            ViewHolder(View view) {
                super(view);
                tvFoodName = view.findViewById(R.id.tvFoodName);
                tvFoodDetail = view.findViewById(R.id.tvFoodDetail);
                vProteinBar = view.findViewById(R.id.vProteinBar);
                vCarbsBar = view.findViewById(R.id.vCarbsBar);
                vFatBar = view.findViewById(R.id.vFatBar);
            }

            void bind(VoiceTranscriptionEngine.ParsedFoodItem item) {
                tvFoodName.setText(item.foodName);
                tvFoodDetail.setText(String.format("%.0f g • %.0f kcal • P: %.0fg C: %.0fg G: %.0fg",
                        item.grams, item.macros.kcal, item.macros.proteinG, item.macros.carbsG, item.macros.fatG));

                // Calcular porcentajes para barras
                double total = item.macros.proteinG + item.macros.carbsG + item.macros.fatG;
                if (total > 0) {
                    float pPct = (float) (item.macros.proteinG / total * 100);
                    float cPct = (float) (item.macros.carbsG / total * 100);
                    float fPct = (float) (item.macros.fatG / total * 100);

                    vProteinBar.setLayoutParams(new LinearLayout.LayoutParams(0, 4, pPct));
                    vCarbsBar.setLayoutParams(new LinearLayout.LayoutParams(0, 4, cPct));
                    vFatBar.setLayoutParams(new LinearLayout.LayoutParams(0, 4, fPct));
                }
            }
        }
    }
}
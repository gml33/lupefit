package com.lupefit.app.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lupefit.app.R;
import com.lupefit.app.databinding.SheetMasBinding;

public class MasBottomSheet extends BottomSheetDialogFragment {

    private SheetMasBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = SheetMasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.recyclerMas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerMas.setAdapter(new MasOptionAdapter(option -> {
            navigateToFeature(option);
            dismiss();
        }));
    }

    private void navigateToFeature(String feature) {
        int actionId;
        switch (feature) {
            case "recetas": actionId = R.id.action_global_recetas; break;
            case "escaneo": actionId = R.id.action_global_escaneo; break;
            case "agua": actionId = R.id.action_global_agua; break;
            case "configuracion": actionId = R.id.action_global_configuracion; break;
            case "perfil": actionId = R.id.action_global_perfil; break;
            case "insights": actionId = R.id.action_global_insights; break;
            case "planes": actionId = R.id.action_global_planes; break;
            default: return;
        }
        navController.navigate(actionId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    static class MasOptionAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<MasOptionAdapter.ViewHolder> {

        private final String[] options = {"recetas", "escaneo", "agua", "configuracion", "perfil", "insights", "planes"};
        private final int[] icons = {
                com.lupefit.core.ui.R.drawable.ic_recetas,
                com.lupefit.core.ui.R.drawable.ic_escaneo,
                com.lupefit.core.ui.R.drawable.ic_agua,
                com.lupefit.core.ui.R.drawable.ic_configuracion,
                com.lupefit.core.ui.R.drawable.ic_perfil,
                com.lupefit.core.ui.R.drawable.ic_insights,
                com.lupefit.core.ui.R.drawable.ic_planes
        };
        private final OnOptionClickListener listener;

        interface OnOptionClickListener {
            void onOptionClick(String feature);
        }

        MasOptionAdapter(OnOptionClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(com.lupefit.core.ui.R.layout.item_mas_option, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String feature = options[position];
            holder.bind(feature, icons[position]);
        }

        @Override
        public int getItemCount() {
            return options.length;
        }

        class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            private final com.google.android.material.imageview.ShapeableImageView ivIcon;
            private final TextView tvTitle;

            ViewHolder(View itemView) {
                super(itemView);
                ivIcon = itemView.findViewById(R.id.ivIcon);
                tvTitle = itemView.findViewById(R.id.tvTitle);
            }

            void bind(String feature, int iconRes) {
                int stringRes = getStringRes(feature);
                tvTitle.setText(stringRes);
                ivIcon.setImageResource(iconRes);

                itemView.setOnClickListener(v -> {
                    if (listener != null) listener.onOptionClick(feature);
                });
            }

            private int getStringRes(String feature) {
                switch (feature) {
                    case "recetas": return R.string.recetas_title;
                    case "escaneo": return R.string.escanear_codigo_barras;
                    case "agua": return R.string.agua_title;
                    case "configuracion": return R.string.configuracion_title;
                    case "perfil": return R.string.perfil_title;
                    case "insights": return R.string.insights_title;
                    case "planes": return R.string.planes_title;
                    default: return R.string.app_name;
                }
            }
        }
    }
}
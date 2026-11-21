package com.lupefit.feature.diario.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.feature.diario.R;
import com.lupefit.feature.diario.databinding.ItemLogEntryBinding;

import java.util.ArrayList;
import java.util.List;

public class DiarioEntryAdapter extends RecyclerView.Adapter<DiarioEntryAdapter.ViewHolder> {

    private List<DiarioEntry> entries = new ArrayList<>();
    private final OnEntryClickListener listener;

    public interface OnEntryClickListener {
        void onEntryClick(DiarioEntry entry);
    }

    public DiarioEntryAdapter(OnEntryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLogEntryBinding binding = ItemLogEntryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setEntries(List<DiarioEntry> newEntries) {
        this.entries = newEntries != null ? newEntries : new ArrayList<>();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLogEntryBinding binding;

        ViewHolder(ItemLogEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DiarioEntry entry) {
            binding.tvFoodName.setText(entry.getNombre());
            binding.tvCalories.setText(String.valueOf(entry.getCalorias()));
            binding.tvMealType.setText(entry.getTipoComida());
            binding.tvFoodDetails.setText(String.format(
                    "%dg • %d kcal • P: %dg C: %dg G: %dg",
                    entry.getCantidad(),
                    entry.getCalorias(),
                    entry.getProteinas(),
                    entry.getCarbohidratos(),
                    entry.getGrasas()
            ));

            // Set meal type color
            int colorRes = getMealTypeColor(entry.getTipoComida());
            binding.tvMealType.setTextColor(binding.getRoot().getContext().getColor(colorRes));

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onEntryClick(entry);
            });
        }

        private int getMealTypeColor(String tipoComida) {
            if (tipoComida == null) return com.lupefit.core.ui.R.color.md_theme_onSurfaceVariant;
            switch (tipoComida) {
                case "DESAYUNO": return com.lupefit.core.ui.R.color.macro_protein;
                case "ALMUERZO": return com.lupefit.core.ui.R.color.macro_carbs;
                case "MERIENDA": return com.lupefit.core.ui.R.color.macro_fat;
                case "CENA": return com.lupefit.core.ui.R.color.water_primary;
                default: return com.lupefit.core.ui.R.color.md_theme_onSurfaceVariant;
            }
        }
    }
}
package com.lupefit.feature.planes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.lupefit.feature.planes.R
import com.lupefit.feature.planes.databinding.FragmentPresupuestoPlanBinding
import com.lupefit.feature.planes.databinding.ItemPresupuestoBinding
import com.lupefit.feature.planes.viewmodel.PresupuestoPlanesViewModel

class PresupuestoPlanFragment : Fragment() {

    private var _binding: FragmentPresupuestoPlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PresupuestoPlanesViewModel
    private lateinit var navController: NavController
    private lateinit var adapter: PresupuestoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPresupuestoPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(requireActivity(), PresupuestoPlanesViewModel.Factory(requireContext()))[PresupuestoPlanesViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.toolbar.title = getString(R.string.presupuesto_plan)
    }

    private fun setupRecyclerView() {
        adapter = PresupuestoAdapter(viewModel::eliminarItem)
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnAgregarItem.setOnClickListener { mostrarDialogoAgregarItem() }
        binding.etMetaSemanal.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val texto = binding.etMetaSemanal.text.toString()
                val meta = texto.replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 }
                if (meta != null) {
                    binding.tilMetaSemanal.error = null
                    viewModel.setMetaSemanal(meta)
                } else {
                    binding.tilMetaSemanal.error = getString(R.string.planes_presupuesto_meta_error)
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            items?.let { adapter.submitList(it.toMutableList()) }
        }

        viewModel.metaSemanal.observe(viewLifecycleOwner) { meta ->
            binding.etMetaSemanal.setText(meta.toString())
        }

        viewModel.totalEstimado.observe(viewLifecycleOwner) { total ->
            binding.tvTotalEstimado.text = "Total: \$${String.format("%,.0f", total)}"
        }

        viewModel.porcentajeMeta.observe(viewLifecycleOwner) { porcentaje ->
            binding.progressPresupuesto.progress = porcentaje
            binding.tvPorcentaje.text = "$porcentaje%"
        }

        viewModel.alertaPresupuesto.observe(viewLifecycleOwner) { alerta ->
            binding.tvAlerta.visibility = if (alerta == true) View.VISIBLE else View.GONE
        }

        viewModel.itemsSinPrecio.observe(viewLifecycleOwner) { count ->
            binding.tvItemsSinPrecio.text = if (count > 0) "$count items sin precio" else ""
            binding.tvItemsSinPrecio.visibility = if (count > 0) View.VISIBLE else View.GONE
        }
    }

    private fun mostrarDialogoAgregarItem() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_agregar_presupuesto, null)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombreItem)
        val etCosto = dialogView.findViewById<TextInputEditText>(R.id.etCostoItem)
        val etCantidad = dialogView.findViewById<TextInputEditText>(R.id.etCantidadItem)
        val etUnidad = dialogView.findViewById<TextInputEditText>(R.id.etUnidadItem)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Agregar item al presupuesto")
            .setView(dialogView)
            .setPositiveButton("Agregar") { dialog: android.content.DialogInterface, _: Int ->
                val nombre = etNombre.text.toString().trim()
                val costo = etCosto.text.toString().replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it >= 0 }
                val cantidad = (etCantidad.text.toString().toIntOrNull() ?: 1).coerceAtLeast(1)
                val unidad = etUnidad.text.toString()
                if (nombre.isNotEmpty()) {
                    viewModel.agregarItem(PresupuestoPlanesViewModel.ItemPresupuesto(nombre, costo ?: 0.0, cantidad, unidad, costo != null))
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog: android.content.DialogInterface, _: Int -> dialog.dismiss() }
            .show()
    }

    private class PresupuestoAdapter(private val eliminar: (Int) -> Unit) : androidx.recyclerview.widget.ListAdapter<PresupuestoPlanesViewModel.ItemPresupuesto, PresupuestoAdapter.ItemViewHolder>(
        object : androidx.recyclerview.widget.DiffUtil.ItemCallback<PresupuestoPlanesViewModel.ItemPresupuesto>() {
            override fun areItemsTheSame(oldItem: PresupuestoPlanesViewModel.ItemPresupuesto, newItem: PresupuestoPlanesViewModel.ItemPresupuesto): Boolean =
                oldItem.nombre == newItem.nombre

            override fun areContentsTheSame(oldItem: PresupuestoPlanesViewModel.ItemPresupuesto, newItem: PresupuestoPlanesViewModel.ItemPresupuesto): Boolean =
                oldItem == newItem
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemBinding = ItemPresupuestoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(item, eliminar)
        }

        class ItemViewHolder(private val binding: ItemPresupuestoBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
            fun bind(item: PresupuestoPlanesViewModel.ItemPresupuesto, eliminar: (Int) -> Unit) {
                binding.tvNombre.text = item.nombre
                binding.tvCosto.text = "\$${String.format("%,.0f", item.costoEstimado)} x${item.cantidad} ${item.unidad}"
                binding.tvTotal.text = "Total: \$${String.format("%,.0f", item.costoEstimado * item.cantidad)}"
                binding.icSinPrecio.visibility = if (item.tienePrecio) View.GONE else View.VISIBLE
                binding.btnEliminar.setOnClickListener {
                    val posicion = bindingAdapterPosition
                    if (posicion != androidx.recyclerview.widget.RecyclerView.NO_POSITION) eliminar(posicion)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
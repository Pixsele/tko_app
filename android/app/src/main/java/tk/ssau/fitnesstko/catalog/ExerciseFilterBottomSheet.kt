package tk.ssau.fitnesstko.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.databinding.BottomSheetExerciseFiltersBinding

/**
 * Панель фильтров каталога: группы мышц, сложность, инвентарь, мин. рейтинг и популярность.
 *
 * Показывается через childFragmentManager фрагмента-каталога и работает с его
 * [ExerciseCatalogViewModel]. Изменения применяются только по кнопке «Показать».
 */
class ExerciseFilterBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetExerciseFiltersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ExerciseCatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetExerciseFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment())[ExerciseCatalogViewModel::class.java]

        val current = viewModel.filter.value ?: ExerciseCatalogFilter()

        viewModel.options.observe(viewLifecycleOwner) { options ->
            // Сохраняем то, что пользователь уже успел отметить, если опции пришли позже
            val checkedMuscles = checkedTags(binding.cgMuscles).ifEmpty { current.muscles }
            val checkedDifficulties = checkedTags(binding.cgDifficulty).ifEmpty { current.difficulties }

            fillChips(binding.cgMuscles, options.muscles, checkedMuscles, ExerciseLabels::muscle)
            fillChips(binding.cgDifficulty, options.difficulties, checkedDifficulties, ExerciseLabels::difficulty)
        }

        bindState(current)

        binding.sliderMinLikes.addOnChangeListener { _, value, _ -> updateLikesLabel(value.toInt()) }
        binding.sliderMinUsage.addOnChangeListener { _, value, _ -> updateUsageLabel(value.toInt()) }

        binding.btnReset.setOnClickListener {
            bindState(current.resetPanel())
        }

        binding.btnApply.setOnClickListener {
            viewModel.updateFilter { it.copy(
                muscles = checkedTags(binding.cgMuscles),
                matchAllMuscles = binding.switchMatchAll.isChecked,
                difficulties = checkedTags(binding.cgDifficulty),
                equipment = when (binding.cgEquipment.checkedChipId) {
                    R.id.chipEquipmentWith -> EquipmentFilter.WITH
                    R.id.chipEquipmentWithout -> EquipmentFilter.WITHOUT
                    else -> EquipmentFilter.ANY
                },
                minLikes = binding.sliderMinLikes.value.toInt(),
                minUsage = binding.sliderMinUsage.value.toInt(),
                onlyLiked = binding.switchOnlyLiked.isChecked
            ) }
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // Сразу раскрываем панель целиком, чтобы кнопки «Сбросить/Показать» были видны
        (dialog as? com.google.android.material.bottomsheet.BottomSheetDialog)?.behavior?.apply {
            state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
    }

    private fun bindState(f: ExerciseCatalogFilter) {
        setCheckedTags(binding.cgMuscles, f.muscles)
        setCheckedTags(binding.cgDifficulty, f.difficulties)
        binding.switchMatchAll.isChecked = f.matchAllMuscles
        binding.switchOnlyLiked.isChecked = f.onlyLiked
        binding.cgEquipment.check(
            when (f.equipment) {
                EquipmentFilter.WITH -> R.id.chipEquipmentWith
                EquipmentFilter.WITHOUT -> R.id.chipEquipmentWithout
                EquipmentFilter.ANY -> R.id.chipEquipmentAny
            }
        )
        binding.sliderMinLikes.value = clampToSlider(f.minLikes, binding.sliderMinLikes.valueTo)
        binding.sliderMinUsage.value = clampToSlider(f.minUsage, binding.sliderMinUsage.valueTo)
        updateLikesLabel(f.minLikes)
        updateUsageLabel(f.minUsage)
    }

    private fun fillChips(
        group: ChipGroup,
        values: List<String>,
        checked: Set<String>,
        label: (String) -> String
    ) {
        group.removeAllViews()
        values.forEach { value ->
            val chip = layoutInflater.inflate(R.layout.item_filter_chip, group, false) as Chip
            chip.text = label(value)
            chip.tag = value
            chip.isChecked = value in checked
            group.addView(chip)
        }
    }

    private fun checkedTags(group: ChipGroup): Set<String> =
        (0 until group.childCount)
            .map { group.getChildAt(it) }
            .filterIsInstance<Chip>()
            .filter { it.isChecked }
            .mapNotNull { it.tag as? String }
            .toSet()

    private fun setCheckedTags(group: ChipGroup, tags: Set<String>) {
        for (i in 0 until group.childCount) {
            (group.getChildAt(i) as? Chip)?.let { it.isChecked = it.tag in tags }
        }
    }

    private fun clampToSlider(value: Int, max: Float): Float {
        val step = 5
        val rounded = (value / step) * step
        return rounded.coerceIn(0, max.toInt()).toFloat()
    }

    private fun updateLikesLabel(value: Int) {
        binding.tvMinLikes.text = if (value > 0) "Рейтинг: от $value ♥" else "Рейтинг: любой"
    }

    private fun updateUsageLabel(value: Int) {
        binding.tvMinUsage.text =
            if (value > 0) "Популярность: от $value добавлений" else "Популярность: любая"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ExerciseFilterBottomSheet"
    }
}

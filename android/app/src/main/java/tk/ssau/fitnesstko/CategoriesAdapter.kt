package tk.ssau.fitnesstko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tk.ssau.fitnesstko.databinding.ItemCategoryBinding

/**
 * Адаптер для отображения списка категорий в RecyclerView.
 * @param categories Список названий категорий для отображения
 */
class CategoriesAdapter(
    val categories: List<String>
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    inner class CategoriesViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Привязывает данные категории к элементу списка.
         * @param category Название категории
         */
        fun bind(category: String) {
            binding.mbCategory.text = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}
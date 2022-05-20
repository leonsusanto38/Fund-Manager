package com.example.uas_paba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterCategory (
    private val listCategory: ArrayList<dataKategori>
) : RecyclerView.Adapter<adapterCategory.ListViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onDeleteClicked(data : dataKategori)
        fun onEditClicked(data : dataKategori)
    }

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvCategoryEdit : TextView = itemView.findViewById(R.id.tvCategoryEdit)
        var _btnEditCategory : ImageButton = itemView.findViewById(R.id.btnEditCategory)
        var _btnDeleteCategory : ImageButton = itemView.findViewById(R.id.btnDeleteCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemcategory, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: adapterCategory.ListViewHolder, position: Int) {
        var category = listCategory[position]

        holder._tvCategoryEdit.setText(category.NamaKategori)

        holder._btnEditCategory.setOnClickListener{
            onItemClickCallback.onEditClicked(listCategory[position])
        }

        holder._btnDeleteCategory.setOnClickListener{
            onItemClickCallback.onDeleteClicked(listCategory[position])
        }
    }

}
package com.example.uas_paba

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterTransaction (
    private val listTransaction: ArrayList<dataTransaksi>
    ) : RecyclerView.Adapter<adapterTransaction.ListViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onDeleteClicked(data : dataTransaksi)
        fun onEditClicked(data : dataTransaksi)
    }

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvDate : TextView = itemView.findViewById(R.id.tvDate)
        var _tvCategory : TextView = itemView.findViewById(R.id.tvCategory)
        var _tvNotes : TextView = itemView.findViewById(R.id.tvNotes)
        var _tvMoney : TextView = itemView.findViewById(R.id.tvMoney)
        var _btnEdit : ImageButton = itemView.findViewById(R.id.btnEdit)
        var _btnDelete : ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemtransaction, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTransaction.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var transaction = listTransaction[position]

        holder._tvDate.setText(transaction.Date)
        holder._tvCategory.setText(transaction.NamaCategory)
        holder._tvNotes.setText(transaction.Note)

        if(transaction.Type.toString() == "0"){
            var temp = "+ Rp " + transaction.Money
            holder._tvMoney.setText(temp)
        }
        else if(transaction.Type.toString() == "1"){
            var temp = "- Rp " + transaction.Money
            holder._tvMoney.setText(temp)
        }

        holder._btnEdit.setOnClickListener{
            onItemClickCallback.onEditClicked(listTransaction[position])
        }

        holder._btnDelete.setOnClickListener{
            onItemClickCallback.onDeleteClicked(listTransaction[position])
        }
    }

}
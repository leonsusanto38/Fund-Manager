package com.example.uas_paba

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter : adapterTransaction

    private lateinit var _rvTransaction : RecyclerView
    private lateinit var tvIncome_ : TextView
    private lateinit var tvExpense_ : TextView
    private lateinit var tvSaldo_ : TextView

    private var arrTransaction = arrayListOf<dataTransaksi>()

    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        _rvTransaction = findViewById(R.id.rvTransaction)
        tvExpense_ = findViewById(R.id.tvExpense)
        tvIncome_ = findViewById(R.id.tvIncome)
        tvSaldo_ = findViewById(R.id.tvSaldo)

        TambahData()

        val btnRefresh_ = findViewById<ImageButton>(R.id.btnRefresh)
        btnRefresh_.setOnClickListener{
            TambahData()
        }

        val btnCategory_ = findViewById<Button>(R.id.btnCategory)
        btnCategory_.setOnClickListener{
            val intent = Intent(this@MainActivity, Category::class.java)
            startActivity(intent)
        }

        val fabAddTransaction_ = findViewById<FloatingActionButton>(R.id.fabAddTransaction)
        fabAddTransaction_.setOnClickListener{
            val intent = Intent(this@MainActivity, AddTransaction::class.java)
            startActivity(intent)
        }

    }

    private fun TambahData() {
        var income : Int = 0
        var expense : Int = 0
        var saldo : Int = 0

        db.collection("tbTransaction").get()
            .addOnSuccessListener { result ->
                arrTransaction.clear()
                for (document in result) {
                    val dataBaru = dataTransaksi(
                        document.data.get("id_Transaction").toString(),
                        document.data.get("date").toString(),
                        document.data.get("type").toString(),
                        document.data.get("namaCategory").toString(),
                        document.data.get("note").toString(),
                        document.data.get("money").toString()
                    )

                    arrTransaction.add(dataBaru)

                    var counter = 0
                    for(document in arrTransaction){
                        if(arrTransaction[counter].Type == "0"){
                            income += arrTransaction[counter].Money.toInt()
                        }
                        else if(arrTransaction[counter].Type == "1"){
                            expense += arrTransaction[counter].Money.toInt()
                        }
                        counter++
                    }
                    saldo = income - expense

                    tvExpense_.setText("Rp " + expense.toString())
                    tvIncome_.setText("Rp " + income.toString())
                    tvSaldo_.setText("Rp " + saldo.toString())

                    saldo = 0
                    income = 0
                    expense = 0
                }
                Log.d("liat array sebelum keluar dr db collection", "${arrTransaction}")
                TampilkanData()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun TampilkanData() {
        val transactionAdapter = adapterTransaction(arrTransaction)
        _rvTransaction.adapter = transactionAdapter
        _rvTransaction.layoutManager = LinearLayoutManager(this)

        transactionAdapter.setOnItemClickCallback(object : adapterTransaction.OnItemClickCallback {
            override fun onDeleteClicked(data: dataTransaksi) {
                HapusData(data)
            }

            override fun onEditClicked(data: dataTransaksi) {
                val editIntent = Intent(this@MainActivity, AddTransaction::class.java)
                editIntent.putExtra("DATAEDIT", data)
                startActivity(editIntent)
            }
        })
    }

    private fun HapusData(transaksi: dataTransaksi) {
        db.collection("tbTransaction").document(transaksi.ID_Transaction)
            .delete()
            .addOnSuccessListener {
                Log.d("firebase", "deleted")
            }
            .addOnFailureListener {
                Log.d("firebase", it.message.toString())
            }

        TambahData()
    }

    private fun HitungReport(){


    }
}
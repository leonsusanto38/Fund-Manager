package com.example.uas_paba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class Category : AppCompatActivity() {

    lateinit var db : FirebaseFirestore

    private var arrCategory = arrayListOf<dataKategori>()

    private lateinit var _rvCategory : RecyclerView

    lateinit var ID_ : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        db = FirebaseFirestore.getInstance()

        _rvCategory = findViewById(R.id.rvCategory)

        TambahData()

        val btnRefresh_ = findViewById<ImageButton>(R.id.btnRefresh)
        btnRefresh_.setOnClickListener{
            TambahData()
        }

        val fabAddCategory_ = findViewById<FloatingActionButton>(R.id.fabAddCategory)
        fabAddCategory_.setOnClickListener {

            db.collection("tbCategory").get()
                .addOnSuccessListener { result ->
                    arrCategory.clear()
                    for (document in result) {
                        val dataBaru = dataKategori(
                            document.data.get("id_Category").toString(),
                            document.data.get("namaKategori").toString()
                        )
                        arrCategory.add(dataBaru)
                    }
                    ID_ = arrCategory.size.toString()
                    Log.d("delokawal", ID_.toString())
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }

            val builder = AlertDialog.Builder(this)
            val dialogLayout = LayoutInflater.from(this).inflate(R.layout.edit_text_layout,null)

//            val data = intent.getParcelableExtra<dataKategori>("DATAEDITKATEGORI")
//            val dataTemp : String = data!!.NamaKategori
            val editText = dialogLayout.findViewById<EditText>(R.id.et_editText)
//            editText.setText(dataTemp)

            with(builder){
                setTitle("Add Category Name")
                setPositiveButton("ADD"){ dialog,which->
                    Toast.makeText(context, editText.text.toString(), Toast.LENGTH_LONG).show()

                    if(editText.text.toString()!=""){
                        Log.d("delokakhir", ID_.toString())
                        val data = dataKategori(
                            ID_,
                            editText.text.toString()
                        )

                        db.collection("tbCategory").document(ID_)
                            .set(data)
                            .addOnSuccessListener { result ->
                                arrCategory.clear()
                            }
                            .addOnFailureListener {
                                Log.d("Firebase", it.message.toString())
                            }
                    }
                    else{
                        Toast.makeText(context, "Nama harus diisi!", Toast.LENGTH_LONG).show()
                    }
                    TambahData()
                }
                setNegativeButton("Cancel"){dialog,which->
                    TambahData()
                }
                setView(dialogLayout)
                show()
            }
        }
    }

    private fun TambahData() {
        db.collection("tbCategory").get()
            .addOnSuccessListener { result ->
                arrCategory.clear()
                for (document in result) {
                    val dataBaru = dataKategori(
                        document.data.get("id_Category").toString(),
                        document.data.get("namaKategori").toString()
                    )
                    arrCategory.add(dataBaru)
                }
                TampilkanData()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun TampilkanData() {
        val categoryAdapter = adapterCategory(arrCategory)
        _rvCategory.adapter = categoryAdapter
        _rvCategory.layoutManager = LinearLayoutManager(this)

        categoryAdapter.setOnItemClickCallback(object : adapterCategory.OnItemClickCallback {
            override fun onDeleteClicked(data: dataKategori) {
                HapusData(data)
            }

            override fun onEditClicked(data: dataKategori) {
                val editIntent = Intent(this@Category, Category::class.java)
                editIntent.putExtra("DATAEDITKATEGORI", data)
                startActivity(editIntent)
            }
        })
    }

    private fun HapusData(kategori: dataKategori) {
        db.collection("tbCategory").document(kategori.ID_Category)
            .delete()
            .addOnSuccessListener {
                Log.d("firebase", "deleted")
            }
            .addOnFailureListener {
                Log.d("firebase", it.message.toString())
            }

        TambahData()
    }
}
package com.example.uas_paba

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddTransaction : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var ID_Transaction : String
    lateinit var ID_Category : String

    private var arrTransaction = arrayListOf<dataTransaksi>()
    private var arrCategory = arrayListOf<dataKategori>()

    private var arrCategoryNama = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                }
                Log.d("nyobaluar", arrTransaction.size.toString())
                ID_Transaction = arrTransaction.size.toString()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }

        db.collection("tbCategory").get()
            .addOnSuccessListener { result ->
                arrCategory.clear()
                for (document in result) {
                    val dataBaru = dataKategori(
                        document.data.get("id_Category").toString(),
                        document.data.get("namaKategori").toString()
                    )
                    arrCategory.add(dataBaru)
                    arrCategoryNama.add(document.data.get("namaKategori").toString())
                }
                ID_Category = arrCategory.size.toString()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }

        var type_ = "0"

//        val data = intent.getParcelableExtra<dataTransaksi>("DATAEDIT")
//
//        //auto fill tanggal mbe category sek blm
//
//        val tempNote : String = data!!.Note
//        val tempMoney : String = data!!.Money

        val etNote_ = findViewById<EditText>(R.id.etNote)
        val etMoney_ = findViewById<EditText>(R.id.etMoney)

//        etNote_.setText(tempNote)
//        etMoney_.setText(tempMoney)

        val rgTypeTransaction_ = findViewById<RadioGroup>(R.id.rgTypeTransaction)
        rgTypeTransaction_.setOnCheckedChangeListener { radioGroup, i ->
            val _radioButton = findViewById<RadioButton>(i)
            if (_radioButton != null) {
                if (_radioButton.text.toString() == "Income") {
                    type_ = "0"
                }
                else if(_radioButton.text.toString() == "Expense") {
                    type_ = "1"
                }
            }
        }

        //Calendar
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        //Datepicker
        val datePicker_ = findViewById<TextView>(R.id.datePicker)
        datePicker_.setOnClickListener{
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                //Set to textview
                datePicker_.setText(""+ mDay +"/"+ mMonth +"/"+ mYear)
            }, year, month, day)
            dpd.show()
        }

        val autoCompleteTextView_ = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)

        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item, arrCategoryNama)
        autoCompleteTextView_.setAdapter(arrayAdapter)

        val btnSave_ = findViewById<Button>(R.id.btnSave)
        btnSave_.setOnClickListener{

            Log.d("nyobadalem", ID_Transaction)

            val data = dataTransaksi(
                ID_Transaction,
                datePicker_.text.toString(),
                type_.toString(),
                autoCompleteTextView_.text.toString(),
                etNote_.text.toString(),
                etMoney_.text.toString()
            )

            db.collection("tbTransaction").document(ID_Transaction)
                .set(data)
                .addOnSuccessListener { result ->
                    arrTransaction.clear()
                    val intent = Intent(this@AddTransaction, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }

        }
    }
}
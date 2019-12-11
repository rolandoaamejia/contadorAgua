package com.example

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contadoragua.AdapterCustom
import com.example.contadoragua.LongClickListener
import com.example.contadoragua.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main2.*
import java.text.SimpleDateFormat
import java.util.*

class Main2Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    var RegistroList = ArrayList<Registro>()
    var meta:TextView?=null
    var ml:TextView?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        button_Cerrar.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        floatingActionButton_Agregar.setOnClickListener {
            agregar()
        }
        floatingActionButton_Actualizar.setOnClickListener {
            actualizar()
        }

    }

    override fun onStart() {
        super.onStart()

        obtenerMeta()
        obtenerMLBebidos()
        actualizar()
    }

    //Fun para obtener la fecha
    @SuppressLint("SimpleDateFormat")
    private fun obtenerFecha(): String {
        val date = Date()
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

    //Fun para obtener la hora
    @SuppressLint("SimpleDateFormat")
    private fun obtenerHora(): String {
        val sdf = SimpleDateFormat("h:mm a")

        return sdf.format(Date())
    }

    private fun agregar() {


        val myRef = database.getReference("Registros")
        val registro = Registro(obtenerHora(),obtenerFecha(),250)
        myRef.child(auth.currentUser?.uid!!).push().setValue(registro)
        Toast.makeText(baseContext, "Se registo el vaso con exito", Toast.LENGTH_SHORT).show()
        obtenerMLBebidos()
        actualizar()
    }

   private fun obtenerMeta(){
       meta=findViewById(R.id.textView_meta)

       val myRef = database.getReference("Clientes").child(auth.currentUser?.uid!!)
       myRef.addListenerForSingleValueEvent(object : ValueEventListener {
           override fun onCancelled(p0: DatabaseError) {
           }

           @SuppressLint("SetTextI18n")
           override fun onDataChange(p0: DataSnapshot) {
              meta?.text="${p0.child("meta").value}ml"


           }

       })
   }

   fun obtenerMLBebidos(){
       ml=findViewById(R.id.textView_cont)
       meta=findViewById(R.id.textView_meta)

       val myRef = database.getReference("Registros").child(auth.currentUser?.uid!!)
       // My top posts by number of stars
       myRef.addValueEventListener(object : ValueEventListener {
           @SuppressLint("SetTextI18n")
           override fun onDataChange(dataSnapshot: DataSnapshot) {
               var totalml=0
               for (postSnapshot in dataSnapshot.children) {
                   val reg = postSnapshot.getValue(Registro::class.java)
                   totalml += reg?.ml!!

               }
               ml?.text="${totalml}ml"

               if (ml?.text==meta?.text){
                    Toast.makeText(applicationContext,"Felicidades por alcanzar la meta diaria",Toast.LENGTH_SHORT).show()
               }
           }

           override fun onCancelled(databaseError: DatabaseError) {

           }
       })

    }

    fun actualizar(){
        lateinit var layoutManager: RecyclerView.LayoutManager

        // Adapter Source
        layoutManager = LinearLayoutManager(this)


        // Indicar a la lista el adaptador a utilizar
        val adapter = AdapterCustom(this, RegistroList,

            object : LongClickListener {
                override fun LongClickListener(view: View, index: Int) {
                    Log.d("LONGCLICK",RegistroList[index].fecha!!)

                }
            })


        RecyclerViev_Agua.layoutManager = null
        RecyclerViev_Agua.setHasFixedSize(true)
        RecyclerViev_Agua.layoutManager = layoutManager
        RecyclerViev_Agua.adapter = adapter
        val myRef = database.getReference("Registros").child(auth.currentUser?.uid!!)
        myRef.addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        RegistroList.clear()
                        for (postSnapshot in dataSnapshot.children) {
                            val reg = postSnapshot.getValue(Registro::class.java)
                           RegistroList.add(reg!!)


                        }


                        adapter.notifyDataSetChanged()


                    }

                }

            })
    }
}

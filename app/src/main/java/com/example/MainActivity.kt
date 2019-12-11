package com.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import com.example.contadoragua.Clientes
import com.example.contadoragua.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private var genero: Switch?=null
    private var kg: EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        button_siguiente.setOnClickListener {
            val Genero: String
            genero=findViewById(R.id.switch_genero)
            kg=findViewById(R.id.editText_peso)
            if (genero?.isChecked!!){
                Genero="Femenino"
            }else{
                Genero="Masculino"
            }
            val KG: Double = (kg?.text.toString()).toDouble()
            val meta=(KG.toInt()/7)*250

            val myRef = database.getReference("Clientes")
            val cliente = Clientes(Genero,KG,meta)
            myRef.child(auth.currentUser?.uid!!).setValue(cliente)


            irPerfil()
        }
    }

  private fun irPerfil(){
      val intent = Intent(this, Main2Activity::class.java)
      startActivity(intent)
  }
}

package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contadoragua.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var email:EditText?=null
    private var password:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            loading.visibility= View.INVISIBLE
            iniciarSesion()
        }

        register.setOnClickListener {
            loading.visibility= View.INVISIBLE
            registrarse()
        }
    }

    override fun onStart() {
        super.onStart()
        //Verifica si ya inicio session antes
        val currentUser = auth.currentUser
        if (currentUser!=null){
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun registrarse() {
         email=findViewById(R.id.email)
         password=findViewById(R.id.password)

        if ((email?.text.toString()=="") or (password?.text.toString()=="")){
            Toast.makeText(baseContext, "Debe ingresar el correo y contraseña",Toast.LENGTH_SHORT).show()
            return
        }


        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email?.text.toString(), password?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loading.visibility= View.GONE
                    irPantallaConf()
                } else {

                    Toast.makeText(baseContext, "Error al crear la cuenta",Toast.LENGTH_SHORT).show()
                    loading.visibility= View.GONE
                }

            }
    }

    private fun iniciarSesion() {
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)

        if ((email?.text.toString()=="") or (password?.text.toString()=="")){
            Toast.makeText(baseContext, "Debe ingresar el correo y contraseña",Toast.LENGTH_SHORT).show()
            return
        }
        if (password?.text.toString().length<6){
            Toast.makeText(baseContext, "La contraseña ser de 6 caracteres o mas",Toast.LENGTH_SHORT).show()
            return
        }

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email?.text.toString(), password?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    ifPerfil()
                } else {
                    loading.visibility= View.GONE
                    Toast.makeText(baseContext, "El correo o contraseña son incorrectos", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun ifPerfil() {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
        finish()
    }

    private fun irPantallaConf() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

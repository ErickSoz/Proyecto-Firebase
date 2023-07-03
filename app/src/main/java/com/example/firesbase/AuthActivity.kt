package com.example.firesbase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.firesbase.databinding.ActivityAuthBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth




class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //analy
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completada")
        analytics.logEvent("InitScreen", bundle)

        //setup perron
        setup()
        session()
    }

    override fun onStart() {
        super.onStart()

        binding.authLayout.visibility = View.VISIBLE
    }

    private fun session() {
        val prefs:SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email:String? = prefs.getString("email", null)
        val provider:String? = prefs.getString("provider", null)

        if (email != null && provider != null){
            binding.authLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }

    }

    private fun setup() {
        title = "autenticacion"
            binding.CerrarButton.setOnClickListener{
                if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {

                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(binding.emailEditText.text.toString(),
                            binding.passwordEditText.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                        }
                        else{ showAlert()}
                        }
                    }
                }


        binding.logInButton.setOnClickListener{
                if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {

                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(binding.emailEditText.text.toString(),
                            binding.passwordEditText.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                        }
                        else{ showAlert()}
                    }
                }
            }

        binding.googlebutton.setOnClickListener{
        }
    }



    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage("Se ha producido un error la autenticar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

     private fun showHome(email: String, provider: ProviderType){

         val homeIntent = Intent(this, HomeActivity::class.java).apply {
             putExtra("email", email)
             putExtra("provider", provider.name)
         }
         startActivity(homeIntent)
     }


}




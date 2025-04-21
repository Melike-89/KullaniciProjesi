package tr.com.kullaniciprojesi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tr.com.kullaniciprojesi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Firebase Auth
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent= Intent(this,DetayActivity2::class.java)
            startActivity(intent)
        }



    }
    fun onClickgirisYap(view: View) {
        val eposta=binding.editEmail.text.toString()
        val parola=binding.editSifre.text.toString()
        if(eposta.isEmpty() || parola.isEmpty())
        {
            Toast.makeText(this, "Lütfen boşlukları doldurunuz", Toast.LENGTH_SHORT).show()
        }
        else
        {
            auth.signInWithEmailAndPassword(eposta, parola)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DetayActivity2::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Log.e("KAYIT_HATASI", "HATA: ${task.exception?.message}")

                        Toast.makeText(
                            baseContext,
                            "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }

        }
    }

    fun onClickkayitOl(view: View) {
        val intent= Intent(this,KayitActivity2::class.java)
        startActivity(intent)
        finish()
    }
}


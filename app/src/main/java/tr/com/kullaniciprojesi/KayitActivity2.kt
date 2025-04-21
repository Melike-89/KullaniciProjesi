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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tr.com.kullaniciprojesi.databinding.ActivityKayit2Binding

class KayitActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityKayit2Binding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKayit2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

    }
    fun onClickKaydet(view: View) {
        val email = binding.editKayitEmail.text.toString()
        val sifre = binding.editKayitSifre.text.toString()
        val ogrenciNo = binding.editOgrenciNo.text.toString()
        val adSoyad = binding.editAdSoyad.text.toString()

        if (email.isEmpty() || sifre.isEmpty()) {

            Toast.makeText(this, "Email ve şifre boş olamaz", Toast.LENGTH_SHORT).show()
            return
        } else {
            auth.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val user = hashMapOf(
                            "eposta" to email,
                            "parola" to sifre,
                            "tamad" to adSoyad,
                            "ogrenciNo" to ogrenciNo
                        )

                        db.collection("users").document(userId.toString()).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Firestore kaydı başarısız.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {


                        Toast.makeText(
                            baseContext,
                            "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}


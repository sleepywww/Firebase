package com.practical.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.practical.firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("DemoDB")

        if(auth.currentUser != null) {
            binding.txtResult.text = auth.currentUser!!.email
        }
        
        binding.btnRegister.setOnClickListener() {
            registerUser("abc@gmail.com", "123456")
        }

        binding.btnLogin.setOnClickListener() {
            loginUser("abc@gmail.com", "123456")
        }

        binding.btnLogout.setOnClickListener() {
            logoutCurrentUser()
        }

        binding.btnInsert.setOnClickListener() {
            val newUser = User("012345", "Adam")
            insertNewUser(newUser)
        }

        binding.btnGet.setOnClickListener() {
            getPerson("012345")
        }

        binding.btnDelete.setOnClickListener() {
            removeUser("012345")
        }
    }

    private fun registerUser(email: String, psw: String) {
        auth.createUserWithEmailAndPassword(email, psw)
            .addOnSuccessListener{
                binding.txtResult.text = email
            }
            .addOnFailureListener{ ex->
                binding.txtResult.text = ex.message
            }
    }

    private fun loginUser(email: String, psw:String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener{
                binding.txtResult.text = email
            }
            .addOnFailureListener{ ex->
                binding.txtResult.text = ex.message
            }
    }

    private fun logoutCurrentUser() {
        Firebase.auth.signOut()
        binding.txtResult.text = ""
    }

    private fun insertNewUser(newUser: User) {
        database.child("User").child(newUser.id).setValue(database)
            .addOnSuccessListener{
                binding.txtResult.text = "New User Added"
            }
            .addOnFailureListener{ ex->
                binding.txtResult.text = ex.message
            }
    }

    private fun getPerson(key: String) {
        database.child("User").child(key).get()
            .addOnSuccessListener{ result->
                if (result.child("id").value != null) {
                    binding.txtId.text = result.child("id").value.toString()
                    binding.txtName.text = result.child("name").value.toString()
                }
                else{
                Toast.makeText(applicationContext, "Record not Found", Toast.LENGTH_SHORT).show()
            }
            }
            .addOnFailureListener{ ex->
                binding.txtResult.text = ex.message
            }
    }

    private fun removeUser(key: String) {
        database.child("User").child(key).removeValue()
            .addOnSuccessListener{
                binding.txtResult.text = "User Removed"
            }
            .addOnFailureListener{ ex->
                binding.txtResult.text = ex.message
            }
    }
}
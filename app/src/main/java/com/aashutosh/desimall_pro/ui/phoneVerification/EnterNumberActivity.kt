package com.aashutosh.desimall_pro.ui.phoneVerification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aashutosh.desimall_pro.database.SharedPrefHelper
import com.aashutosh.desimall_pro.databinding.ActivityEnterNumberBinding
import com.aashutosh.desimall_pro.ui.HomeActivity
import com.aashutosh.desimall_pro.ui.detailsVerificationPage.DetailsVerificationActivity
import com.aashutosh.desimall_pro.utils.Constant
import com.aashutosh.desimall_pro.utils.Constant.Companion.phoneNumberKey
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class EnterNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterNumberBinding

    lateinit var sharedPrefHelper: SharedPrefHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefHelper = SharedPrefHelper
        sharedPrefHelper.init(this)
        //validateNumberForTesting()
        binding.btnGetOtp.setOnClickListener {
            validateNumber()
        }

        binding.tvSkip.setOnClickListener(View.OnClickListener {
            sharedPrefHelper[Constant.USER_SKIPPED] = true
            val i = Intent(this@EnterNumberActivity, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        })
    }

    private fun validateNumberForTesting() {


        val db = Firebase.firestore
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date = Date()
        val createUser = hashMapOf(
            "phone" to "+9779860858540",
            "date" to dateFormat.format(date),
        )
        db.collection("user").document("+9779860858540")
            .set(createUser).addOnSuccessListener {
                Toast.makeText(this, "Authorization Completed 🥳🥳", Toast.LENGTH_SHORT)
                    .show()
                sharedPrefHelper[Constant.VERIFIED_NUM] = true
                sharedPrefHelper[Constant.PHONE_NUMBER] = "+9779860858540"
                val intent = Intent(this, DetailsVerificationActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "User Not Created Retry Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun validateNumber() {

        if (binding.etPhoneNum.editText?.text.toString().isEmpty()) {
            binding.etPhoneNum.error = "Enter your Phone Number"
            binding.etPhoneNum.requestFocus()
            return
        }

        if (binding.etPhoneNum.editText?.text.toString().count() == 10) {

            binding.etPhoneNum.clearFocus()
            val intent = Intent(this, VerifyNumberActivity::class.java).apply {
                putExtra(phoneNumberKey, binding.etPhoneNum.editText?.text.toString())
            }
            startActivity(intent)
            finish()

        } else {
            Toast.makeText(this, "Enter 10 digit number", Toast.LENGTH_SHORT).show()
        }
    }

}
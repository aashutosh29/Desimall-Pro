package com.aashutosh.desimall_pro.ui.deliveryAddress

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aashutosh.desimall_pro.database.SharedPrefHelper
import com.aashutosh.desimall_pro.databinding.ActivityDeliveryAddressBinding
import com.aashutosh.desimall_pro.models.DeliveryDetails
import com.aashutosh.desimall_pro.utils.Constant
import com.aashutosh.desimall_pro.viewModels.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeliveryAddressActivity : AppCompatActivity() {

    lateinit var mainViewModel: StoreViewModel
    lateinit var binding: ActivityDeliveryAddressBinding
    private lateinit var sharedPrefHelper: SharedPrefHelper
    var id: Int = 0


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        sharedPrefHelper = SharedPrefHelper
        sharedPrefHelper.init(this.applicationContext)
        mainViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        GlobalScope.launch {
            val deliveryDetails: List<DeliveryDetails> = mainViewModel.getProfileDetails()
            if (deliveryDetails.isNotEmpty()) {
                binding.etAddress.text = deliveryDetails[0].address.toEditable()
                binding.etMobileNumber.text =
                    sharedPrefHelper[Constant.PHONE_NUMBER, ""].toEditable()
                binding.etName.text = deliveryDetails[0].name.toEditable()
                binding.etZipCode.text = deliveryDetails[0].zipCode.toEditable()
                binding.etLandMark.text = deliveryDetails[0].landMark.toEditable()
                id = deliveryDetails[0].id
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    if (mainViewModel.createDelivery(
                            DeliveryDetails(
                                5,
                                name = sharedPrefHelper[Constant.NAME, ""],
                                mobileNum = sharedPrefHelper[Constant.PHONE_NUMBER, ""],
                                address = sharedPrefHelper[Constant.ADDRESS, ""],
                                landMark = sharedPrefHelper[Constant.LAND_MARK, ""],
                                zipCode = sharedPrefHelper[Constant.ZIP, ""]

                            )
                        ) > 1
                    ) {
                        val newDeliveryDetails: List<DeliveryDetails> =
                            mainViewModel.getProfileDetails()
                        binding.etAddress.text = newDeliveryDetails[0].address.toEditable()
                        binding.etMobileNumber.text =
                            newDeliveryDetails[0].mobileNum.toEditable()
                        binding.etName.text = newDeliveryDetails[0].name.toEditable()
                        binding.etZipCode.text = newDeliveryDetails[0].zipCode.toEditable()
                        binding.etLandMark.text = newDeliveryDetails[0].landMark.toEditable()
                        id = newDeliveryDetails[0].id

                    }

                    }


                }
            }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initView() {
        binding.btSave.setOnClickListener(View.OnClickListener {
            if (binding.etName.text.toString() == "" || !binding.etName.text.contains(" ")) {
                Toast.makeText(
                    this@DeliveryAddressActivity, "Enter full name", Toast.LENGTH_SHORT
                ).show()
            } else if (binding.etZipCode.text.toString() == "") {
                Toast.makeText(
                    this@DeliveryAddressActivity, "please enter zip code", Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    if (mainViewModel.updateDelivery(
                            DeliveryDetails(
                                5,
                                name = binding.etName.text.toString(),
                                mobileNum = binding.etMobileNumber.text.toString(),
                                address = binding.etAddress.text.toString(),
                                landMark = binding.etLandMark.text.toString(),
                                zipCode = binding.etZipCode.text.toString()
                            )
                        ) >= 1
                    ) {
                        Toast.makeText(
                            this@DeliveryAddressActivity,
                            "Successfully Updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        getFinish()
                    }
                }
                }

        })
        binding.ivBack.setOnClickListener(View.OnClickListener {
            this.finish()
        })
    }

    private fun getFinish() {
        this.finish()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}
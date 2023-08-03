package com.app.ecolive.pharmacy_module

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.app.ecolive.R
import com.app.ecolive.databinding.ActivityCreateHospitalBinding
import com.app.ecolive.pharmacy_module.PharmacyViewModel.PharmacyViewModel
import com.app.ecolive.rider_module.VehicleInfoActivity
import com.app.ecolive.service.Status
import com.app.ecolive.user_module.interfacee.OnSelectOptionListener
import com.app.ecolive.utils.*
import com.lassi.common.utils.KeyUtils
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.LassiOption
import com.lassi.domain.media.MediaType
import com.lassi.presentation.builder.Lassi
import com.nightout.ui.fragment.SelectSourceBottomSheetFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CreateHospitalActivity : AppCompatActivity(), OnSelectOptionListener {
    lateinit var binding: ActivityCreateHospitalBinding
    private lateinit var selectSourceBottomSheetFragment: SelectSourceBottomSheetFragment
    var bgimg = 1
    var logo = 2
    var option = 1
    var backgroundImg = ""
    var logoImage = ""
    var multipartBody: ArrayList<MultipartBody.Part?> = ArrayList()
    private val progressDialog = CustomProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_hospital)

        binding.createBtn.setOnClickListener {
            startActivity(Intent(this,HospitalProfile::class.java))
          /*  if (binding.hospitalName.text.toString() == ""){
                Toast.makeText(this, "Enter hospital name", Toast.LENGTH_SHORT).show()

            }else if (binding.mobileNumber.text.toString() == ""){
                Toast.makeText(this, "Enter mobileNumber", Toast.LENGTH_SHORT).show()

            }else if (binding.services.text.toString() == ""){
                Toast.makeText(this, "Enter services", Toast.LENGTH_SHORT).show()

            }else if (binding.consultfee.text.toString() == ""){
                Toast.makeText(this, "Enter consultfee", Toast.LENGTH_SHORT).show()

            }else if (binding.hospitalLocation.text.toString() == ""){
                Toast.makeText(this, "Enter hospitalLocation", Toast.LENGTH_SHORT).show()

            }else{
               CreatHospital()
            }*/

        }
        binding.backgroundImg.setOnClickListener {
            onSelectImage()
            option = bgimg
        }
        binding.logoImage.setOnClickListener {
            onSelectImage()
            option = logo
        }
    }

    private fun onSelectImage() {
        if (!Utils.checkingPermissionIsEnabledOrNot(this)) {
            Utils.requestMultiplePermission(this, VehicleInfoActivity.requestPermissionCode)
        } else {
            selectSourceBottomSheetFragment = SelectSourceBottomSheetFragment(this, "")
            selectSourceBottomSheetFragment.show(
                this.supportFragmentManager,
                "selectSourceBottomSheetFragment"
            )
        }
    }

    override fun onOptionSelect(option: String) {
        if (option == AppConstant.CAMERA_KEY) {
            selectSourceBottomSheetFragment.dismiss()
            //  ImagePicker.onCaptureImage(this)
            val intent = Lassi(this)
                .with(LassiOption.CAMERA)
                .setMaxCount(1)
                .setGridSize(3)
                .setMediaType(MediaType.IMAGE)
                .setCompressionRation(10)
                .build()
            receiveData.launch(intent)

        } else if (option == AppConstant.GALLERY_KEY) {
            selectSourceBottomSheetFragment.dismiss()
            val intent = Lassi(this)
                .with(LassiOption.GALLERY)
                .setMaxCount(1)
                .setGridSize(3)
                .setMediaType(MediaType.IMAGE)
                .setCompressionRation(10)
                .build()
            receiveData.launch(intent)
        }
    }

    private val receiveData =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedMedia =
                    it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
                if (!selectedMedia.isNullOrEmpty()) {
                    //storePath
                    if (option == 1) {
                        backgroundImg = selectedMedia[0].path!!
                        val bitmap: Bitmap?
                        bitmap = BitmapFactory.decodeFile(selectedMedia[0].path)
                        //imageUrl = Uri.parse(selectedMedia[0].path)

                        try {
                            binding.backgroundImg.setImageBitmap(null)
                            binding.backgroundImg.setImageBitmap(bitmap)

                        } catch (e: Exception) {
                            Log.d("crashImage", "onActivityResult: " + e)
                        }
                    } else {
                        logoImage = selectedMedia[0].path!!
                        val bitmap: Bitmap?
                        bitmap = BitmapFactory.decodeFile(selectedMedia[0].path)
                        //imageUrl = Uri.parse(selectedMedia[0].path)

                        try {
                            binding.logoImage.setImageBitmap(null)
                            binding.logoImage.setImageBitmap(bitmap)

                        } catch (e: Exception) {
                            Log.d("crashImage", "onActivityResult: " + e)
                        }
                    }


                    //  setBody(bitmap!!, "vehicleDocument")

                }
            }
        }


    private fun CreatHospital() {
        try {
            MyApp.hideSoftKeyboard(this)
        } catch (e: Exception) {
        }
        var pharmacyViewModel = PharmacyViewModel(this)
        progressDialog.show(this)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        /*builder.addFormDataPart("name", name)
        builder.addFormDataPart("address", address)
        builder.addFormDataPart("ssn", ssn)*/
        builder.addFormDataPart("medications", arrayListOf<String>("test","test").toString())

        if (multipartBody != null) {
            setBodyBgImg()
            setBodyLogo()
            builder.addPart(multipartBody[0]!!.body)
            builder.addPart(multipartBody[1]!!.body)
        }

        pharmacyViewModel.creatHealthProfile(builder.build()).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let {
                        PreferenceKeeper.instance.isHealthProfileCreate =true
                        startActivity(
                            Intent(this, PharmacyStepActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ).setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK))
                        //     finish()

                    }
                }
                Status.LOADING -> {
                    Log.d("ok", "LOADING: ")
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Log.d("ok", "ERROR: ")
                    MyApp.popErrorMsg("", it.message!!, this)

                }
            }
        }
    }

    private fun setBodyBgImg() {
        val filePath = File(backgroundImg)
        val reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), filePath)
        multipartBody.add(MultipartBody.Part.createFormData("picture", filePath.name, reqFile))

    }
    private fun setBodyLogo() {
        val filePath = File(logoImage)
        val reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), filePath)
        multipartBody.add(MultipartBody.Part.createFormData("picture", filePath.name, reqFile))

    }
}
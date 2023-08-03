package com.app.ecolive.pharmacy_module.PharmacyViewModel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.ecolive.login_module.LoginModel
import com.app.ecolive.login_module.model.BaseModel
import com.app.ecolive.pharmacy_module.model.CommonMedicationModel
import com.app.ecolive.pharmacy_module.model.CreateHealthModel
import com.app.ecolive.service.ApiSampleResource
import com.app.ecolive.service.WebServiceRepository
import com.app.ecolive.user_module.model.AddressModel
import okhttp3.MultipartBody
import org.json.JSONObject

class PharmacyViewModel(activity: Activity) : BaseObservable() {
    private var webServiceRepository = WebServiceRepository(activity)




    private lateinit var createHealthModel: LiveData<ApiSampleResource<CreateHealthModel>>
    private lateinit var getCommonMedication: LiveData<ApiSampleResource<CommonMedicationModel>>


    fun creatHealthProfile(json: MultipartBody): LiveData<ApiSampleResource<CreateHealthModel>> {
        createHealthModel = webServiceRepository.createHealthProfileApi(json)
        return createHealthModel
    }
    fun getCommonMedicationApi(): LiveData<ApiSampleResource<CommonMedicationModel>> {
        getCommonMedication = webServiceRepository.getCommonMedicationApi()
        return getCommonMedication
    }

    fun getCommonMedicationData( ): LiveData<ApiSampleResource<CommonMedicationModel>> {
        return getCommonMedication
    }
}
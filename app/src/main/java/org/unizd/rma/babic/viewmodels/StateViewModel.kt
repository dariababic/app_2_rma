package org.unizd.rma.babic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.unizd.rma.babic.models.State
import org.unizd.rma.babic.repository.StateRepository
import org.unizd.rma.babic.utils.Resource

class StateViewModel(application: Application) : AndroidViewModel(application) {

    private val stateRepository = StateRepository(application)


    fun getStateList() = stateRepository.getStateList()


    fun insertState(state: State) : MutableLiveData<Resource<Long>>{
      return stateRepository.insertState(state) as MutableLiveData<Resource<Long>>
    }

    fun deleteTAsk(state: State) : MutableLiveData<Resource<Int>>{
        return stateRepository.deleteState(state) as MutableLiveData<Resource<Int>>
    }

    fun deleteStateUsingId(stateId: String) : MutableLiveData<Resource<Int>>{
        return stateRepository.deleteStateUsingId(stateId) as MutableLiveData<Resource<Int>>
    }

    fun updateState(state: State) : MutableLiveData<Resource<Int>>{
        return stateRepository.updateState(state) as MutableLiveData<Resource<Int>>
    }

    fun updateStatePaticularField(stateId: String, title: String,description: String,surface: String, imageUri: String ) : MutableLiveData<Resource<Int>>{
        return stateRepository.updateStatePaticularField(stateId,title,description,surface, imageUri) as MutableLiveData<Resource<Int>>
    }


//

//
//    fun deleteStateUsingId(stateId: String){
//        stateRepository.deleteStateUsingId(stateId)
//    }
//
//    fun updateState(state: State) {
//        stateRepository.updateState(state)
//    }

}
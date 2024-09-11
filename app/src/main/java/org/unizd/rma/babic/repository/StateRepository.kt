package org.unizd.rma.babic.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.unizd.rma.babic.dao.StateDao
import org.unizd.rma.babic.database.StateDatabase
import org.unizd.rma.babic.models.State
import org.unizd.rma.babic.utils.Resource
import org.unizd.rma.babic.utils.StatusResult

class StateRepository(application: Application) {



 private  val stateDao: StateDao = StateDatabase.getInstance(application).stateDao

    fun getStateList() = flow{

    emit(Resource.Loading())

        try {

            val result = stateDao.getStateList()
            emit(Resource.Success(result))

        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

    }

    fun insertState(state: State): Any = MutableLiveData<Resource<Long>> ().apply{
        postValue(Resource.Loading())

     try {

      CoroutineScope(Dispatchers.IO).launch {
       val result = stateDao.insertState(state)
      postValue(Resource.Success(result))
      }

     } catch (e : Exception){
  postValue(Resource.Error(e.message.toString()))

     }
    }

    fun deleteState(state: State): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = stateDao.deleteState(state)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }


    fun deleteStateUsingId(stateId : String): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = stateDao.deleteStateUsingId(stateId)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }


    fun updateState(state: State): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = stateDao.updateState(state)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }


    fun updateStatePaticularField(stateId: String, title: String,description: String, surface: String, imageUri: String): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = stateDao.updateStatePaticularField(stateId,title,description,surface,imageUri)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }
//    fun searchStateList(query: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                _stateStateFlow.emit(Loading())
//                val result = stateDao.searchStateList("%${query}%")
//                _stateStateFlow.emit(Success("loading", result))
//            } catch (e: Exception) {
//                _stateStateFlow.emit(Error(e.message.toString()))
//            }
//        }
//    }


    private fun handleResult(result: Int, message: String, statusResult: StatusResult) {

    }
}
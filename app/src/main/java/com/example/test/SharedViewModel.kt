package com.example.test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.domain.mapper.MainMapper
import com.example.test.domain.models.DestinationDomain
import com.example.test.domain.useCase.GetRemoteDestinationsUseCase
import com.example.test.utils.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    @ApplicationContext context: Context,
): ViewModel() {

    private val _showLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _messageDialog = MutableStateFlow(context.getString(R.string.textError))
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()

    private val _data: MutableStateFlow<List<DestinationDomain?>> = MutableStateFlow(listOf())
    val data: StateFlow<List<DestinationDomain?>> = _data.asStateFlow()

    @Inject
    lateinit var useCase: GetRemoteDestinationsUseCase


    fun getResults(context:Context){
        try {
            _showLoading.value = true
            viewModelScope.launch {
                when(val resp = useCase.getResults()){
                    is WrapperResponse.Success ->{
                        resp.data?.let { destinations ->
                            _data.value = destinations.map { MainMapper.destinationToDestionDomain(it) }
                            //_dataFix.value = resp.data
                            _showLoading.value = false
                        }
                    }
                    is WrapperResponse.Error ->{
                        _messageDialog.value = resp.message ?: context.getString(R.string.textError)
                        _showDialog.value = true
                        _showLoading.value = false
                    }
                }
            }
        }catch (e:Exception){
            _messageDialog.value = e.message ?: context.getString(R.string.textError)
            _showDialog.value = true
            _showLoading.value = false
        }
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun onDialogConfirm(){
        _showDialog.value = false
    }
}
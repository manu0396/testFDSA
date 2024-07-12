package com.example.test

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.models.Timestamp
import com.example.test.domain.mapper.MainMapper
import com.example.test.domain.models.DestinationDomain
import com.example.test.domain.useCase.GetLocalDestinationsUseCase
import com.example.test.domain.useCase.GetRemoteDestinationsUseCase
import com.example.test.utils.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private val _showLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _messageDialog = MutableStateFlow(context.getString(R.string.textError))
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()

    private val _data: MutableStateFlow<List<DestinationDomain?>> = MutableStateFlow(listOf())
    val data: StateFlow<List<DestinationDomain?>> = _data.asStateFlow()

    private val _localData: MutableStateFlow<List<DestinationDomain?>> = MutableStateFlow(listOf())
    val localData: StateFlow<List<DestinationDomain?>> = _localData.asStateFlow()

    @Inject
    lateinit var GetRemoteDestinationUseCase: GetRemoteDestinationsUseCase

    @Inject
    lateinit var GetLocalDestinationsUseCase: GetLocalDestinationsUseCase


    fun getResults(context: Context) {
        try {
            _showLoading.value = true
            viewModelScope.launch {
                /**
                 * MOCK DATA: Descomentar este código y comentar el 'when' para testear la UI
                 */
                _data.value = listOf(
                    DestinationDomain(
                        id = "1",
                        name = "Example Destination 1",
                        description = "A sample description for the destination.",
                        countryMode = "Country Mode 1",
                        type = "Destination Type 1",
                        picture = "https://example.com/image.jpg",
                        lastModify = Timestamp(System.currentTimeMillis())
                    ),
                    DestinationDomain(
                        id = "2",
                        name = "Example Destination 2",
                        description = "A sample description for the destination.",
                        countryMode = "Country Mode 2",
                        type = "Destination Type 2",
                        picture = "https://example.com/image.jpg",
                        lastModify = Timestamp(System.currentTimeMillis())
                    ),
                    DestinationDomain(
                        id = "3",
                        name = "Example Destination 3",
                        description = "A sample description for the destination.",
                        countryMode = "Country Mode 3",
                        type = "Destination Type 3",
                        picture = "https://example.com/image.jpg",
                        lastModify = Timestamp(System.currentTimeMillis())
                    )

                )
                _showLoading.value = false

                //END MOCK DATA
                /* // COMENTAR ESTA LINEA PARA TESTEAR LA UI

                                when (val resp = GetRemoteDestinationUseCase.getResults()) {
                                    is WrapperResponse.Success -> {
                                        resp.data?.let { destinations ->
                                            _data.value =
                                                destinations.map {
                                                    MainMapper.destinationToDestionDomain(it)
                                                }
                                            _localData.value = resp.data.map { MainMapper.destinationToDestionDomain(it) }
                                            _showLoading.value = false
                                        }
                                    }

                                    is WrapperResponse.Error -> {
                                        _messageDialog.value = resp.message ?: context.getString(R.string.textError)
                                        _showDialog.value = true
                                        _showLoading.value = false
                                    }
                                }
                */ //COMENTAR ESTA LINEA PARA TESTEAR LA UI
            }
        } catch (e: Exception) {
            _messageDialog.value = e.message ?: context.getString(R.string.textError)
            _showDialog.value = true
            _showLoading.value = false
        }
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun onDialogConfirm() {
        _showDialog.value = false
    }

    fun getLocalData(context: Context) {

        try {
            _showLoading.value = true
            viewModelScope.launch {
                when (val resp = GetLocalDestinationsUseCase.getResults()) {
                    is WrapperResponse.Success -> {
                        resp.data?.let { localData ->
                            _localData.value =
                                localData.map { MainMapper.destinationDataToDestionDomain(it) }
                            _showLoading.value = false
                        }
                    }

                    is WrapperResponse.Error -> {
                        _messageDialog.value =
                            resp.message ?: context.getString(R.string.textError)
                        _showDialog.value = true
                        _showLoading.value = false
                    }

                }
            }
        } catch (e: Exception) {
            _messageDialog.value = e.message ?: context.getString(R.string.textError)
            _showDialog.value = true
            _showLoading.value = false

        }
    }

    fun updateData(index: Int, updatedDestination: DestinationDomain) {
        val updatedList = data.value.toMutableList()
        updatedList[index] = updatedDestination
        if (_data.value.isNotEmpty()) {
            _data.value = listOf(updatedDestination)
            _localData.value = listOf(updatedDestination)
        } else {
            _localData.value = listOf(updatedDestination)
        }
    }

    fun createDestination(newDestination: DestinationDomain) {
        val updatedData = _data.value.toMutableList()
        updatedData.add(newDestination)
        _data.value = updatedData
        val updatedLocalData = _localData.value.toMutableList()
        updatedLocalData.add(newDestination)
        _localData.value = updatedLocalData
    }

    fun updateDestination(index: Int, updatedDestination: DestinationDomain) {
        val updatedData = _data.value.toMutableList()
        updatedData[index] = updatedDestination
        _data.value = updatedData
        val updatedLocalData = _localData.value.toMutableList()
        updatedLocalData[index] = updatedDestination
        _localData.value = updatedLocalData
    }

    fun deleteDestination(context: Context, rowIndex: Int) {
        val currentData = _data.value.toMutableList()
        val currentLocalData = _localData.value.toMutableList()
        try {
            if (rowIndex in currentData.indices && rowIndex in currentLocalData.indices) {
                currentData.removeAt(rowIndex)
                _data.value = currentData

                currentLocalData.removeAt(rowIndex)
                _localData.value = currentLocalData
            } else {
                // Handle invalid index scenario
                Log.e("Delete", "Invalid index $rowIndex for data or localData list")
                _showDialog.value = true
                _messageDialog.value = context.getString(R.string.textError)
            }
        } catch (e: Exception) {
            Log.e("Delete", "Exception: ${e.message}")
            _showDialog.value = true
            _messageDialog.value = context.getString(R.string.textError)
        }
    }
}
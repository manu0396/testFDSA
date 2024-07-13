package com.example.test

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    private val _selectedRowIndex = MutableStateFlow<Int?>(null)
    val selectedRowIndex: StateFlow<Int?> = _selectedRowIndex.asStateFlow()

    // REMOVE THIS VARIABLE WHEN PRODUCTION SERVER ARE OK
    private var mutableMockData : MutableList<DestinationDomain?> = mutableListOf(
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

    @Inject
    lateinit var GetRemoteDestinationUseCase: GetRemoteDestinationsUseCase

    @Inject
    lateinit var GetLocalDestinationsUseCase: GetLocalDestinationsUseCase


    fun getResults(context: Context) {
        try {
            _showLoading.value = true
            viewModelScope.launch {
                /**
                 * MOCK DATA: Descomentar este código y comentar el 'when' una vez el servidor de produccion este disponible.
                 */
                _data.value = mutableMockData

                _localData.value = mutableMockData

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

    fun createDestination(newDestination: DestinationDomain) {
        val updatedData = _data.value.toMutableList()
        updatedData.add(newDestination)
        _data.value = updatedData
        val updatedLocalData = _localData.value.toMutableList()
        updatedLocalData.add(newDestination)
        _localData.value = updatedLocalData
        val updateMockData = mutableMockData.toMutableList()
        updateMockData.add(newDestination)
        mutableMockData = updateMockData
    }

    fun updateDestination(index: Int, updatedDestination: DestinationDomain) {
        // Create new lists to trigger recomposition
        val newData = _data.value.toMutableList()
        val newLocalData = _localData.value.toMutableList()

        if (index >= 0 && index < newData.size && index < newLocalData.size) {
            newData[index] = updatedDestination
            newLocalData[index] = updatedDestination

            // Update the MutableStateFlow with new list instances
            _data.value = newData
            _localData.value = newLocalData

            // Ensure any other necessary state updates
            mutableMockData = newData

        } else {
            // Handle invalid index scenario
            Log.e("Update", "Invalid index $index for data or localData list")
            _showDialog.value = true
            _messageDialog.value = "Invalid index: row$index"
        }
    }
    fun deleteDestination(rowIndex: Int) {
        val currentData = _data.value.toMutableList()
        val currentLocalData = _localData.value.toMutableList()

        if (rowIndex in currentData.indices && rowIndex in currentLocalData.indices) {
            currentData.removeAt(rowIndex)
            currentLocalData.removeAt(rowIndex)
            mutableMockData.removeAt(rowIndex)
            _data.value = currentData
            _localData.value = currentLocalData
        } else {
            // Handle invalid index scenario
            Log.e("Delete", "Invalid index $rowIndex for data or localData list")
            _showDialog.value = true
            _messageDialog.value = "Invalid index: row $rowIndex"
        }
    }

    fun showDialog(string: String) {
        _showDialog.value = true
        _messageDialog.value = string
    }

    fun setSelectedDestination(index: Int) {
        _selectedRowIndex.value = index
    }

    fun showError(string: String) {
        _showDialog.value = true
        _messageDialog.value = string
    }
}
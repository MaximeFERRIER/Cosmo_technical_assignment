package fr.droidfactory.cosmo.ui.products.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.ui.products.productdetails.domain.GetProductByMacAddressInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductDetailsViewModel @Inject constructor(
    private val getProductByMacAddressInteractor: GetProductByMacAddressInteractor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val macAddress = savedStateHandle.macaddress
    private val _product = MutableStateFlow<ResultState<Product>>(ResultState.Uninitialized)
    internal val product = _product.asStateFlow()

    init {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            _product.update { ResultState.Loading }
            getProductByMacAddressInteractor.execute(macAddress).onSuccess { product ->
                _product.update { ResultState.Success(product) }
            }.onFailure {  error ->
                _product.update { ResultState.Failure(error) }
            }
        }
    }
}
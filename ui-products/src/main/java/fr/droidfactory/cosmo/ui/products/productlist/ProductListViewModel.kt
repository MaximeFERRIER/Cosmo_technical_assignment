package fr.droidfactory.cosmo.ui.products.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.droidfactory.cosmo.sdk.core.models.DataSource
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.ui.products.productlist.domain.GetProductListInteractor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductListViewModel @Inject constructor(
    private val getProductListInteractor: GetProductListInteractor
) : ViewModel() {

    private val _productsState =
        MutableStateFlow<ResultState<List<Product>>>(ResultState.Uninitialized)
    internal val productsState = _productsState.asStateFlow()
    private val _sideEffect = Channel<Throwable>(Channel.BUFFERED)
    internal val sideEffect = _sideEffect.receiveAsFlow()

    init {
        getProductList()
    }

    internal fun getProductList() {
        viewModelScope.launch {
            _productsState.update { ResultState.Loading }
            getProductListInteractor.invoke().onSuccess { products ->
                _productsState.update { ResultState.Success(products.data) }

                (products.source as? DataSource.Source.Database)?.let {
                    _sideEffect.send(it.networkException)
                }
            }.onFailure { error ->
                _productsState.update { ResultState.Failure(error) }
                _sideEffect.send(error)
            }
        }
    }
}
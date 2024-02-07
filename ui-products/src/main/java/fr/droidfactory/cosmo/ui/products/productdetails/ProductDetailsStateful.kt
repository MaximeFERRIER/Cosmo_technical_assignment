package fr.droidfactory.cosmo.ui.products.productdetails

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.LocalWindowSizeProvider
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.sdk.core.ui.WindowSizeProvider
import fr.droidfactory.cosmo.sdk.designsystem.components.DsLabelItem
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTexts
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTopBar
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsErrorScreen
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsLoadingScreen
import fr.droidfactory.cosmo.ui.products.CustomItem
import fr.droidfactory.cosmo.ui.products.ItemInfo
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.TextItem
import fr.droidfactory.cosmo.ui.products.getIllustration
import fr.droidfactory.cosmo.ui.products.getProductAbout
import fr.droidfactory.cosmo.ui.products.getProductDescription

@Composable
internal fun ProductDetailsStateful(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {

    val isSmallScreen =
        LocalWindowSizeProvider.current.getScreenSize() == WindowSizeProvider.ScreenSize.SMALL
    val state = viewModel.product.collectAsState()

    when (state.value) {
        is ResultState.Failure -> DsErrorScreen(
            title = stringResource(id = R.string.error_title),
            subTitle = stringResource(id = R.string.error_product_not_found),
            onCloseClicked = { onNavigationBack() })

        ResultState.Uninitialized, ResultState.Loading -> DsLoadingScreen(
            loadingText = stringResource(
                id = R.string.loading_product_details
            )
        )

        is ResultState.Success -> ProductDetailsScreen(
            product = (state.value as ResultState.Success<Product>).data,
            isSmallScreen = isSmallScreen
        ) { onNavigationBack() }
    }

}

@Composable
private fun ProductDetailsScreen(
    product: Product,
    isSmallScreen: Boolean,
    onBackClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            DsTopBar.NavigationTopBar(
                title = product.model.name,
                onNavigationClick = { onBackClicked() })
        }) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
        )
        if (isSmallScreen) {
            SingleColumnView(product = product, paddings = paddings)
        } else {
            TwoColumnsView(product = product, paddings = paddings)
        }
    }
}

@Composable
private fun SingleColumnView(product: Product, paddings: PaddingValues) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddings), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item("column_picture") {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(16.dp)
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = product.model.getIllustration(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }

        displayProductDetails(context = context, product = product, from = "single_column")
    }
}

@Composable
private fun TwoColumnsView(product: Product, paddings: PaddingValues) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings)
    ) {
        Card(
            modifier = Modifier
                .weight(0.5f)
                .padding(100.dp)
                .aspectRatio(1f)
                .align(Alignment.CenterVertically)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = product.model.getIllustration(),
                contentDescription = ""
            )
        }

        LazyColumn(
            Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            displayProductDetails(context = context, product = product, from = "two_columns")
        }
    }
}

private fun LazyListScope.displayProductDetails(context: Context, product: Product, from: String) {
    item("${from}_title_description") {
        DsTexts.TitleMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            title = stringResource(id = R.string.description_title),
            align = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))
    }

    val descriptionItems = product.getProductDescription(context)
    val aboutItems = product.getProductAbout(context)

    itemsIndexed(items = descriptionItems, key = { _, item ->
        "${from}_description_$item"
    }) { index, item ->
        DisplayItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            itemInfo = item,
            isFirstItemOfTheList = index == 0,
            isLastItemOfTheList = index == descriptionItems.lastIndex,
            isSingleInList = descriptionItems.size == 1
        )
    }

    item("${from}_title_about") {
        DsTexts.TitleMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            title = stringResource(id = R.string.about_title),
            align = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))
    }

    itemsIndexed(items = aboutItems, key = { _, item ->
        "${from}_about_$item"
    }) { index, item ->
        DisplayItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            itemInfo = item,
            isFirstItemOfTheList = index == 0,
            isLastItemOfTheList = index == aboutItems.lastIndex,
            isSingleInList = aboutItems.size == 1
        )
    }

}

@Composable
private fun DisplayItem(
    modifier: Modifier = Modifier,
    itemInfo: ItemInfo,
    isFirstItemOfTheList: Boolean,
    isLastItemOfTheList: Boolean,
    isSingleInList: Boolean
) {
    Column(modifier = modifier) {
        when (itemInfo) {
            is TextItem -> DsLabelItem.LabelText(
                title = itemInfo.title,
                text = itemInfo.text,
                isFirstItemOfTheList = isFirstItemOfTheList,
                isLastItemOfTheList = isLastItemOfTheList
            )

            is CustomItem -> DsLabelItem.LabelCustom(
                title = itemInfo.title,
                content = itemInfo.content,
                isFirstItemOfTheList = isFirstItemOfTheList,
                isLastItemOfTheList = isLastItemOfTheList
            )
        }

        if (!isLastItemOfTheList && !isSingleInList) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}

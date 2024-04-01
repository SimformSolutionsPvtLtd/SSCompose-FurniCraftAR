package com.simform.ssfurnicraftar.ui.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Product
import com.simform.ssfurnicraftar.ui.modifier.shimmerEffect
import com.simform.ssfurnicraftar.ui.theme.LocalDimens

@Composable
fun ProductsRoute(
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val currentCategory by viewModel.currentCategory.collectAsStateWithLifecycle()
    val productItems = viewModel.modelsFlow.collectAsLazyPagingItems()

    ProductsScreen(
        modifier = modifier,
        categories = viewModel.categories,
        currentCategory = currentCategory,
        productItems = productItems,
        onCategoryChange = viewModel::changeCategory,
        onProductClick = onProductClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductsScreen(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    currentCategory: Category,
    productItems: LazyPagingItems<Product>,
    onCategoryChange: (Category) -> Unit,
    onProductClick: (String) -> Unit
) {

    val pullToRefreshState = rememberPullToRefreshState()

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            productItems.refresh()
            pullToRefreshState.endRefresh()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Column {
            CategoryHeader(
                categories = categories,
                currentCategory = currentCategory,
                onCategoryChange = onCategoryChange
            )

            Products(
                productItems = productItems,
                onProductClick = onProductClick
            )
        }

        if (productItems.loadState.refresh !is LoadState.Loading) {
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState
            )
        }
    }
}

@Composable
private fun CategoryHeader(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    currentCategory: Category,
    onCategoryChange: (Category) -> Unit
) {
    LazyRow(
        modifier = modifier
            .padding(top = LocalDimens.SpacingSmall, bottom = LocalDimens.SpacingXS),
        contentPadding = PaddingValues(horizontal = LocalDimens.SpacingLarge),
        horizontalArrangement = Arrangement.spacedBy(LocalDimens.SpacingSmall)
    ) {
        items(categories) { category ->
            CategoryCard(
                isSelected = currentCategory == category,
                category = category,
                onClick = { onCategoryChange(category) }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    modifier: Modifier = Modifier,
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        modifier = modifier,
        onClick = onClick,
        label = {
            Text(text = stringResource(category.nameRes))
        }
    )
}

@Composable
fun Products(
    modifier: Modifier = Modifier,
    productItems: LazyPagingItems<Product>,
    onProductClick: (String) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        val refreshState = productItems.loadState.refresh
        val sourceState = productItems.loadState.source.refresh
        val mediatorState = productItems.loadState.mediator?.refresh

        when {
            // If there is no data available to display, show no data view.
            // Initially item counts might be zero. We must check that source
            // state or remote mediator is not in loading state.
            productItems.itemCount == 0
                    && sourceState !is LoadState.Loading
                    && mediatorState !is LoadState.Loading -> {
                NoData(modifier = Modifier.align(Alignment.Center))
            }

            // When products are loading show product placeholders.
            // Loading can be either by refresh state (initial load/pull to refresh) or
            // by sourceState when changing the category.
            refreshState is LoadState.Loading
                    || (sourceState is LoadState.Loading && productItems.itemCount == 0) -> {
                ProductsLoading()
            }

            else -> {
                ProductsContent(
                    products = productItems,
                    onProductClick = onProductClick
                )
            }
        }
    }
}

@Composable
private fun ProductsLoading(
    modifier: Modifier = Modifier
) {
    ProductsGrid(modifier = modifier) {
        items(LocalDimens.Products.PlaceholdersCount) {
            Box(
                modifier = modifier
                    .aspectRatio(LocalDimens.Products.CardRatio)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun ProductsContent(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<Product>,
    onProductClick: (String) -> Unit
) {
    ProductsGrid(modifier = modifier) {
        items(products.itemCount) { index ->
            products[index]?.let {
                ProductCard(
                    model = it,
                    onProductClick = onProductClick
                )
            }
        }
    }
}

@Composable
private fun ProductsGrid(
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = LocalDimens.SpacingLarge),
        contentPadding = PaddingValues(vertical = LocalDimens.SpacingMedium),
        columns = GridCells.Adaptive(LocalDimens.Products.CardSize),
        verticalArrangement = Arrangement.spacedBy(LocalDimens.SpacingLarge),
        horizontalArrangement = Arrangement.spacedBy(LocalDimens.SpacingMedium),
        content = content
    )
}

@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    model: Product,
    onProductClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(ratio = LocalDimens.Products.CardRatio),
        onClick = { onProductClick(model.id) }
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.weight(1f),
            model = model.thumbnail,
            contentDescription = model.name,
            contentScale = ContentScale.FillHeight,
            loading = {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(LocalDimens.SpacingSmall),
                    painter = painterResource(id = R.drawable.ic_model_placeholder),
                    contentDescription = null,
                    tint = LocalContentColor.current.copy(alpha = LocalDimens.Products.PlaceholderAlpha)
                )
            },
            error = {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(LocalDimens.SpacingSmall),
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = null,
                    tint = LocalContentColor.current.copy(alpha = LocalDimens.Products.PlaceholderAlpha)
                )
            }
        )

        Text(
            modifier = Modifier.padding(LocalDimens.SpacingSmall),
            text = model.name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Composable
private fun NoData(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalDimens.SpacingSmall)
        ) {
            Icon(
                modifier = modifier.size(LocalDimens.IconXXL),
                imageVector = Icons.AutoMirrored.Default.List,
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.no_data),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

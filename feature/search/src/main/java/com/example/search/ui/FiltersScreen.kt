package com.example.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.toRange
import com.example.core.R
import com.example.core.ui.LocalAccentColor
import com.example.domain.model.Filter
import com.example.search.viewmodel.SearchViewModel

@Composable
fun ExpandableFilterDropdown(
    title: String,
    items: List<Filter>,
    selectedItems: List<Filter>,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemClick: (Filter) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "chevron_rotation"
    )
    
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(!isExpanded) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 8.dp)
                )
                if (selectedItems.isNotEmpty()) {
                    Text(
                        text = selectedItems.size.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAccentColor.current,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Свернуть" else "Развернуть",
                modifier = Modifier
                    .size(28.dp)
                    .rotate(rotationAngle),
                tint = LocalAccentColor.current
            )
        }
        
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
                    .verticalScroll(scrollState)
            ) {
                items.forEach { item ->
                    val selected = selectedItems.any { it.name == item.name }
                    FilterChip(
                        selected = selected,
                        onClick = { onItemClick(item) },
                        label = { Text(item.name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp, horizontal = 4.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LocalAccentColor.current,
                            selectedLabelColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.secondary
                        ),
                        border = BorderStroke(
                            width = 1.dp, LocalAccentColor.current
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FiltersScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: SearchViewModel,
    onBackClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    val filters = viewModel.filters.collectAsState()
    val filtersForSearch = viewModel.filtersForSearch.collectAsState()

    val ratingSliderPosition = remember {
        mutableStateOf(filters.value.rating.lower..filters.value.rating.upper)
    }
    val yearSliderPosition = remember {
        mutableStateOf(filters.value.years.lower..filters.value.years.upper)
    }

    var typesExpanded by remember { mutableStateOf(false) }
    var genresExpanded by remember { mutableStateOf(false) }
    var countriesExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .verticalScroll(state = scrollState),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Фильтры",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reset),
                    contentDescription = "Сброс фильтров",
                    tint = LocalAccentColor.current,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            viewModel.resetFilters()
                            yearSliderPosition.value = filters.value.years.lower..filters.value.years.upper
                            ratingSliderPosition.value = filters.value.rating.lower..filters.value.rating.upper
                        }
                )
                Text(
                    text = "Назад",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAccentColor.current,
                    modifier = Modifier.clickable {
                        onBackClicked.invoke()
                    }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "Год",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Text(
                "от ${yearSliderPosition.value.start.toInt()} до ${yearSliderPosition.value.endInclusive.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        RangeSlider(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = yearSliderPosition.value,
            onValueChange = { range ->
                yearSliderPosition.value = range
            },
            onValueChangeFinished = {
                viewModel.setYearsFilter(yearSliderPosition.value.toRange())
            },
            steps = 0,
            valueRange = (filters.value.years.lower..filters.value.years.upper),
            colors = SliderDefaults.colors(
                thumbColor = LocalAccentColor.current,
                activeTrackColor = LocalAccentColor.current,
                inactiveTickColor = LocalAccentColor.current,
                activeTickColor = colorResource(com.example.core.R.color.white),
                inactiveTrackColor = MaterialTheme.colorScheme.surface
            )
        )

        ExpandableFilterDropdown(
            title = "Тип",
            items = filters.value.types,
            selectedItems = filtersForSearch.value.types,
            isExpanded = typesExpanded,
            onExpandedChange = { typesExpanded = it },
            onItemClick = { filter ->
                if (filtersForSearch.value.types.contains(filter)) {
                    viewModel.removeTypeFilter(filter)
                } else {
                    viewModel.addTypeFilter(filter)
                }
            }
        )

        ExpandableFilterDropdown(
            title = "Жанры",
            items = filters.value.genres,
            selectedItems = filtersForSearch.value.genres,
            isExpanded = genresExpanded,
            onExpandedChange = { genresExpanded = it },
            onItemClick = { filter ->
                if (filtersForSearch.value.genres.contains(filter)) {
                    viewModel.removeGenreFilter(filter)
                } else {
                    viewModel.addGenreFilter(filter)
                }
            }
        )
        ExpandableFilterDropdown(
            title = "Страны",
            items = filters.value.countries,
            selectedItems = filtersForSearch.value.countries,
            isExpanded = countriesExpanded,
            onExpandedChange = { countriesExpanded = it },
            onItemClick = { filter ->
                if (filtersForSearch.value.countries.contains(filter)) {
                    viewModel.removeCountryFilter(filter)
                } else {
                    viewModel.addCountryFilter(filter)
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "Рейтинг",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Text(
                "от ${ratingSliderPosition.value.start.toInt()} до ${ratingSliderPosition.value.endInclusive.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        RangeSlider(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = ratingSliderPosition.value,
            steps = 8,
            onValueChange = { range -> ratingSliderPosition.value = range },
            valueRange = filters.value.rating.lower..filters.value.rating.upper,
            onValueChangeFinished = {
                viewModel.setRatingFilter(ratingSliderPosition.value.toRange())
            },
            colors = SliderDefaults.colors(
                thumbColor = LocalAccentColor.current,
                activeTrackColor = LocalAccentColor.current,
                inactiveTickColor = LocalAccentColor.current,
                activeTickColor = colorResource(com.example.core.R.color.white),
                inactiveTrackColor = MaterialTheme.colorScheme.surface
            )
        )

        Button(
            onClick = {
                viewModel.searchWithFilters()
                onSearchClicked.invoke()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
                .padding(top = 40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalAccentColor.current,
                contentColor = colorResource(com.example.core.R.color.white)
            )
        ) {
            Text(
                "Искать",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
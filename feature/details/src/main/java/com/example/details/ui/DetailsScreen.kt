package com.example.details.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.R
import com.example.core.ui.LoadImageWithPlaceholder
import com.example.core.ui.MovieRatingWithWreath
import com.example.core.ui.UserScore
import com.example.core.util.getName
import com.example.details.viewmodel.DetailsViewModel
import com.example.domain.model.Movie
import com.example.domain.model.MovieImage
import com.example.domain.model.Person
import com.example.domain.model.Review

private const val REVIEW_PREVIEW_MAX_LENGTH = 180
private val CARD_BORDER_WIDTH = 1.dp
private val CARD_CORNER_RADIUS = 12.dp
private val CARD_BORDER_SHAPE = RoundedCornerShape(CARD_CORNER_RADIUS)
private val ACTION_ICON_SIZE = 24.dp
private val ACTION_ICON_LABEL_SPACING = 0.5.dp

private fun getReviewPreview(text: String?): String {
    val prepared = text?.trim().orEmpty()
    if (prepared.isEmpty()) return "Текст рецензии отсутствует"
    return if (prepared.length > REVIEW_PREVIEW_MAX_LENGTH) {
        prepared.take(REVIEW_PREVIEW_MAX_LENGTH) + "..."
    } else {
        prepared
    }
}

/**
 * Отображает краткую информацию об актере на экране фильма (DetailsScreen)
 * Отображает фото актера, справа от фото имя и роль.
 *
 * @param actor актер типа [Person]
 */
@Composable
private fun ActorItem(actor: Person, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        LoadImageWithPlaceholder(
            imageUrl = actor.photo,
            placeholderResId = R.drawable.actor_placeholder,
            modifier = Modifier
                .height(90.dp)
                .width(60.dp)
                .clip(CARD_BORDER_SHAPE)
                .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .width(160.dp)
                .padding(start = 4.dp, end = 16.dp)
        ) {
            Text(actor.name ?: "", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            Text(
                actor.description ?: "",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Функция отображения звездочки в MovieScoreDialog
 */
@Composable
private fun Star(score: MutableIntState, number: Int) {
    Icon(painter = if (score.intValue >= number + 1) painterResource(R.drawable.star)
    else painterResource(R.drawable.star_border),
        tint = if (score.intValue >= number + 1) colorResource(R.color.score_legend_start)
        else colorResource(R.color.text_second),
        contentDescription = "star",
        modifier = Modifier
            .clickable {
                score.intValue = number + 1
            }
            .size(28.dp))

}

/**
 * Диалог выставления оценки фильму
 *
 * @param dialogState стейт диалога
 * @param movieScore пользовательская оценка фильма, если есть
 * @param isSeries является ли сериалом (для правильного отображения надписи)
 * @param onSave сохраняет результат, записывает оценку фильма
 * @param onCancel убирает оценку
 */
@Preview
@Composable
private fun MovieScoreDialog(
    modifier: Modifier = Modifier,
    dialogState: MutableState<Boolean> = mutableStateOf(false),
    movieScore: Int? = null,
    isSeries: Boolean = false,
    onSave: (score: Int) -> Unit = {},
    onCancel: () -> Unit = {},
) {

    val score = remember { mutableIntStateOf(movieScore ?: -1) }

    Dialog(
        onDismissRequest = {
            dialogState.value = false
        }, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = true
        )
    ) {
        Box(
            modifier = modifier.background(
                    color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Оцените ${if (isSeries) "сериал" else "фильм"}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    repeat(10) { number ->
                        Star(score = score, number = number)
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(onClick = {
                        onSave(score.intValue)
                        dialogState.value = false
                    }) {
                        Text(
                            text = "Сохранить",
                            color = colorResource(R.color.accent),
                        )
                    }
                    TextButton(onClick = {
                        onCancel.invoke()
                        dialogState.value = false
                    }) {
                        Text(
                            text = "Убрать оценку",
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewItem(review: Review, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(320.dp)
            .padding(end = 8.dp)
            .clip(CARD_BORDER_SHAPE)
            .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 6.dp)
    ) {
        Text(
            text = review.title ?: review.author ?: "Рецензия",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = getReviewPreview(review.review),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun ReviewDialog(review: Review, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = review.title ?: "Рецензия",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (!review.author.isNullOrBlank() || !review.date.isNullOrBlank()) {
                    Text(
                        text = listOfNotNull(review.author, review.date).joinToString(" • "),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(320.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = review.review ?: "Текст рецензии отсутствует",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Закрыть",
                        color = colorResource(R.color.accent)
                    )
                }
            }
        }
    }
}

@Composable
private fun DescriptionDialog(movie: Movie, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = movie.getName(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                movie.shortDescription?.takeIf { it.isNotBlank() }?.let { short ->
                    Text(
                        text = short,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(320.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    val fullDescription = movie.description?.takeIf { it.isNotBlank() }
                        ?: movie.shortDescription?.takeIf { it.isNotBlank() }
                    Text(
                        text = fullDescription ?: "Описание отсутствует",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Закрыть",
                        color = colorResource(R.color.accent)
                    )
                }
            }
        }
    }
}

@Composable
private fun FullscreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onDismiss)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { }
                ) {
                    LoadImageWithPlaceholder(
                        imageUrl = imageUrl,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    ) {
                        Text(
                            text = "Закрыть",
                            color = colorResource(R.color.accent)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: DetailsViewModel,
) {

    val movie by viewModel.movie.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val images by viewModel.images.collectAsState()
    val context = LocalContext.current
    val dialogState = remember { mutableStateOf(false) }
    val selectedReview = remember { mutableStateOf<Review?>(null) }
    val selectedImageUrl = remember { mutableStateOf<String?>(null) }
    val showDescriptionDialog = remember { mutableStateOf(false) }

    if (dialogState.value) {
        MovieScoreDialog(dialogState = dialogState,
            movieScore = movie.userRate,
            isSeries = movie.isSeries == true,
            onSave = { score ->
                viewModel.setUserScore(score = score)
            },
            onCancel = {
                viewModel.deleteUserScore()
            })
    }
    selectedReview.value?.let { review ->
        ReviewDialog(
            review = review,
            onDismiss = { selectedReview.value = null }
        )
    }

    selectedImageUrl.value?.let { imageUrl ->
        FullscreenImageDialog(
            imageUrl = imageUrl,
            onDismiss = { selectedImageUrl.value = null }
        )
    }

    if (showDescriptionDialog.value) {
        DescriptionDialog(
            movie = movie,
            onDismiss = { showDescriptionDialog.value = false }
        )
    }




    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LoadImageWithPlaceholder(
                    imageUrl = movie.backdrop?.backdropUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .alpha(0.5f)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )
                LoadImageWithPlaceholder(
                    imageUrl = movie.poster?.posterUrl,
                    placeholderResId = R.drawable.poster_placeholder,
                    modifier = Modifier
                        .width(240.dp)
                        .align(Alignment.BottomCenter)
                        .clip(CARD_BORDER_SHAPE)
                        .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                text = movie.getName(),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                MovieRatingWithWreath(movie = movie)
            }
            Text(
                "${
                    if (movie.status == "announced") "Анонсирован," 
                    else if (movie.year != null) movie.year.toString() + "," 
                    else ""
                } ${movie.genres.map { it.name }.joinToString()}",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${
                    movie.countries.map { it.name }.joinToString()
                } ${
                    if (movie.movieLength != null) movie.movieLength.toString() + " мин" else ""
                }${
                    if (movie.ageRating != null) ", " + movie.ageRating.toString() + "+" else ""
                }", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold
            )
            movie.reviewInfo?.let { info ->
                if (info.count != null && info.count!! > 0) {
                    Text(
                        "Рецензии: ${info.count}${info.percentage?.let { " ($it положительных)" } ?: ""}",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ACTION_ICON_LABEL_SPACING),
                ) {
                    IconButton(onClick = {
                        if (movie.isWatching) viewModel.removeFromWatching()
                        else viewModel.addToWatching()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "watching",
                            modifier = Modifier.size(ACTION_ICON_SIZE),
                            tint = if (movie.isWatching) colorResource(R.color.accent)
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        stringResource(R.string.Watching),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ACTION_ICON_LABEL_SPACING),
                ) {
                    IconButton(onClick = {
                        if (movie.isWillWatch) viewModel.removeFromWillWatch()
                        else viewModel.addToWillWatch()
                    }) {
                        Icon(
                            painter = if (movie.isWillWatch) painterResource(R.drawable.bookmark_filled)
                            else painterResource(R.drawable.bookmark),
                            contentDescription = "willwatch",
                            modifier = Modifier.size(ACTION_ICON_SIZE),
                            tint = if (movie.isWillWatch) colorResource(R.color.accent)
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        "Буду смотреть",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ACTION_ICON_LABEL_SPACING),
                ) {
                    IconButton(onClick = {
                        if (movie.isWatched) viewModel.removeFromWatched()
                        else viewModel.addToWatched()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "watched",
                            modifier = Modifier.size(ACTION_ICON_SIZE),
                            tint = if (movie.isWatched) colorResource(R.color.accent)
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        stringResource(R.string.Watched),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ACTION_ICON_LABEL_SPACING),
                ) {
                    IconButton(onClick = {
                        if (movie.isDropped) viewModel.removeFromDropped()
                        else viewModel.addToDropped()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "dropped",
                            modifier = Modifier.size(ACTION_ICON_SIZE),
                            tint = if (movie.isDropped) colorResource(R.color.accent)
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        stringResource(R.string.Dropped),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ACTION_ICON_LABEL_SPACING),
                ) {
                    IconButton(onClick = {
                        if (movie.isFavorite) viewModel.removeFromFavourite()
                        else viewModel.addToFavourite()
                    }) {
                        Icon(
                            imageVector = if (movie.isFavorite) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                            contentDescription = "favourite",
                            modifier = Modifier.size(ACTION_ICON_SIZE),
                            tint = if (movie.isFavorite) colorResource(R.color.accent)
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        "Избранное",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ACTION_ICON_LABEL_SPACING),
                ) {
                    IconButton(onClick = { dialogState.value = true }) {
                        if (movie.userRate == null) {
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = "star",
                                modifier = Modifier.size(ACTION_ICON_SIZE),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            UserScore(movie = movie, modifier = Modifier.size(ACTION_ICON_SIZE))
                        }
                    }
                    Text(
                        text = if (movie.userRate == null) "Оценить" else "Ваша оценка",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ACTION_ICON_LABEL_SPACING),
                ) {
                    IconButton(onClick = {
                        val shareText = "${movie.getName()}${movie.year?.let { " ($it)" } ?: ""}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.share),
                            modifier = Modifier.size(ACTION_ICON_SIZE),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = stringResource(R.string.share),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clip(CARD_BORDER_SHAPE)
                .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
                .padding(12.dp)
        ) {
            if (movie.shortDescription != null || movie.description != null) {
                Text(
                    text = (movie.shortDescription ?: movie.description).toString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "Все детали фильма",
                    color = colorResource(R.color.accent),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        showDescriptionDialog.value = true
                    })
            } else {
                Text(
                    text = "Нет описания",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                text = "Актеры",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (movie.persons.isNotEmpty()) {
                val actorsGroups = movie.persons.chunked(3)
                LazyRow {
                    items(actorsGroups) { group ->
                        Column {
                            group.forEach { actor ->
                                ActorItem(actor = actor)
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Нет информации об актерах",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                text = "Интересные факты",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CARD_BORDER_SHAPE)
                    .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
                    .padding(8.dp)
            ) {
                if (movie.facts.isNotEmpty()) {
                    LazyRow {
                        items(movie.facts) { fact ->
                            Column(
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(100.dp)
                                    .padding(end = 4.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Text(
                                    text = "${if (fact.spoiler == true) "Спойлер: " else ""} ${fact.fact.toString()}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(10.dp),
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Нет информации о фактах",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                text = "Рецензии",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (reviews.isNotEmpty()) {
                LazyRow {
                    items(reviews) { review ->
                        ReviewItem(
                            review = review,
                            onClick = { selectedReview.value = review }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CARD_BORDER_SHAPE)
                        .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Нет рецензий",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 100.dp)
        ) {
            Text(
                text = "Изображения",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (images.isNotEmpty()) {
                LazyRow {
                    items(images) { image: MovieImage ->
                        LoadImageWithPlaceholder(
                            imageUrl = image.previewUrl ?: image.url,
                            modifier = Modifier
                                .width(240.dp)
                                .height(140.dp)
                                .padding(end = 8.dp)
                                .clip(CARD_BORDER_SHAPE)
                                .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
                                .clickable {
                                    selectedImageUrl.value = image.url ?: image.previewUrl
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CARD_BORDER_SHAPE)
                        .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Нет изображений",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
















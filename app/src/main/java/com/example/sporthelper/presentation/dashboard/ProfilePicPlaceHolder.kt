package com.example.sporthelper.presentation.dashboard

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.sporthelper.R
import com.example.sporthelper.presentation.theme.CustomBlue
import com.example.sporthelper.presentation.theme.CustomPink
import com.example.sporthelper.presentation.theme.SportHelperTheme
import java.net.URL

@Composable
fun ProfilePicPlaceHolder(
    modifier: Modifier = Modifier,
    placeHolderSize: Dp,
    borderWith: Dp,
    padding: Dp,
    profilePictureURL: String?,
) {

    val imageRequest = ImageRequest
        .Builder(LocalContext.current)
        .data(profilePictureURL)
        .crossfade(true)
        .build()

    Box(
        modifier = modifier
            .size(placeHolderSize)
            .border(
                width = borderWith,
                brush = Brush.linearGradient(listOf(CustomBlue, CustomPink)),
                shape = CircleShape
            )
            .padding(padding)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            model = imageRequest,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.app_logo),
            error = painterResource(id = R.drawable.app_logo),
            onSuccess = {
                Log.d("ProfilePicPlaceHolder", "Image loaded successfully: ${it.result}")
                
            },
            onError = {
                Log.d("ProfilePicPlaceHolder", "Error loading image: ${it.result}")
                it.result.throwable.printStackTrace()
            }
        )
    }

}

@Preview
@Composable
private fun ProfilePicPlaceHolderPreview() {
    SportHelperTheme {
        ProfilePicPlaceHolder(
            placeHolderSize = 120.dp,
            borderWith = 3.dp,
            profilePictureURL = null,
            padding = 5.dp
        )
    }

}
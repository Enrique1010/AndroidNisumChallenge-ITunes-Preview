package com.erapps.itunespreview.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.erapps.itunespreview.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    moveOn: () -> Unit
) {

    LaunchedEffect(key1 = true) {
        delay(1000)
        moveOn()
    }

    Splash()
}

@Composable
fun Splash(modifier: Modifier = Modifier) {
    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = modifier.size(
                    dimensionResource(id = R.dimen.splash_icon_size),
                    dimensionResource(id = R.dimen.splash_icon_size)
                ),
                tint = MaterialTheme.colors.onBackground,
                painter = painterResource(id = R.drawable.ic_music_preview_logo),
                contentDescription = null
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.splash_spacer)))
            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = R.dimen.splash_text_size).value.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
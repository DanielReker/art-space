package io.github.danielreker.artspace

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.danielreker.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    ArtSpaceTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AppLayout(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}


@Composable
fun AppLayout(
    modifier: Modifier = Modifier,
) {
    var artworkIdx by rememberSaveable { mutableIntStateOf(0) }
    val artwork = artworks[artworkIdx]

    fun moveArtworkIdx(offset: Int) {
        artworkIdx = (artworkIdx + offset + artworks.size) % artworks.size
    }

    val onPreviousClick: () -> Unit = { moveArtworkIdx(-1) }
    val onNextClick: () -> Unit = { moveArtworkIdx(+1) }

    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitLayout(
            artwork = artwork,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            modifier = modifier.padding(horizontal = 24.dp)
        )
    } else {
        LandscapeLayout(
            artwork = artwork,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            modifier = modifier.padding(horizontal = 12.dp)
        )
    }

}

@Composable
fun LandscapeLayout(artwork: Artwork, onPreviousClick: () -> Unit, onNextClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        LandscapeSwipeButton(onClick = onPreviousClick) {
            Text(stringResource(R.string.previous_button_text))
        }
        Column(
            modifier = Modifier.fillMaxHeight().weight(1.0f).padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ArtworkImage(
                artwork = artwork,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.weight(1.0f)
            )
            ArtworkDescription(
                artwork = artwork,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
        LandscapeSwipeButton(onClick = onNextClick) {
            Text(stringResource(R.string.next_button_text))
        }
    }
}

@Composable
fun LandscapeSwipeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.width(128.dp).height(128.dp)
    ) {
        content()
    }
}

@Composable
fun PortraitLayout(artwork: Artwork, onPreviousClick: () -> Unit, onNextClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.weight(1.0f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ArtworkImage(
                artwork = artwork,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )
        }
        ArtworkDescription(
            artwork = artwork,
            modifier = Modifier
                .padding(vertical = 24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Button(
                onClick = onPreviousClick,
                modifier = Modifier.weight(1.0f)
            ) {
                Text(stringResource(R.string.previous_button_text))
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Button(
                onClick = onNextClick,
                modifier = Modifier.weight(1.0f)
            ) {
                Text(stringResource(R.string.next_button_text))
            }
        }
    }
}

@Composable
fun ArtworkImage(artwork: Artwork, contentScale: ContentScale, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shadowElevation = 12.dp) {
        Image(
            painter = painterResource(artwork.image),
            contentDescription = "${stringResource(artwork.name)}, ${stringResource(artwork.author)}",
            modifier = Modifier.padding(24.dp),
            contentScale = contentScale
        )
    }
}

@Composable
fun ArtworkDescription(artwork: Artwork, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, tonalElevation = 4.dp) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(artwork.name),
                fontSize = 32.sp,
            )
            Row {
                Text(
                    text = stringResource(artwork.author),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (artwork.finishYear == null) "(${artwork.beginYear})"
                    else "(${artwork.beginYear} - ${artwork.finishYear})",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
fun PortraitPreview() {
    App()
}

@Composable
@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
fun LandscapePreview() {
    App()
}


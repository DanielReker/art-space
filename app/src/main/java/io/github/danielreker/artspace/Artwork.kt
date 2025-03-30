package io.github.danielreker.artspace

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Artwork(
    @DrawableRes val image: Int,
    @StringRes val name: Int,
    @StringRes val author: Int,
    val beginYear: Int,
    val finishYear: Int? = null,
)

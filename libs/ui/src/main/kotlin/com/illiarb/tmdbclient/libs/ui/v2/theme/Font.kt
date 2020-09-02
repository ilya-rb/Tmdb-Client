package com.illiarb.tmdbclient.libs.ui.v2.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import com.illiarb.tmdbclient.libs.ui.R

private val Montserrat = fontFamily(
  font(R.font.montserrat, FontWeight.Normal),
  font(R.font.montserrat_bold, FontWeight.Bold)
)

internal val TmdbTypography: Typography
  get() = Typography(
    h1 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 96.sp,
      fontWeight = FontWeight.Light,
      lineHeight = 117.sp,
      letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 60.sp,
      fontWeight = FontWeight.Light,
      lineHeight = 73.sp,
      letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 48.sp,
      fontWeight = FontWeight.Normal,
      lineHeight = 59.sp
    ),
    h4 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 30.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 37.sp
    ),
    h5 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 24.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 29.sp
    ),
    h6 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 20.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 24.sp
    ),
    subtitle1 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 16.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 20.sp,
      letterSpacing = 0.5.sp
    ),
    subtitle2 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium,
      lineHeight = 17.sp,
      letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      lineHeight = 20.sp,
      letterSpacing = 0.15.sp
    ),
    body2 = TextStyle(
      fontFamily = Montserrat,
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 20.sp,
      letterSpacing = 0.25.sp
    ),
    button = TextStyle(
      fontFamily = Montserrat,
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 16.sp,
      letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
      fontFamily = Montserrat,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 16.sp,
      letterSpacing = 0.sp
    ),
    overline = TextStyle(
      fontFamily = Montserrat,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 16.sp,
      letterSpacing = 1.sp
    )
  )
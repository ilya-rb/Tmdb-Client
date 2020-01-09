package com.illiarb.tmdbexplorer.appfeatures.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.FrameLayout
import androidx.compose.unaryPlus
import androidx.fragment.app.Fragment
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.DrawImage
import androidx.ui.graphics.Color
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.EdgeInsets
import androidx.ui.layout.Row
import androidx.ui.layout.Spacing
import androidx.ui.layout.WidthSpacer
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Switch
import androidx.ui.material.TopAppBar
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.getColorAttr

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FrameLayout(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view as ViewGroup).setContent { SettingsScreen() }
    }

    @Composable
    private fun SettingsScreen() {
        val colors = +MaterialTheme.colors()

        AppTheme {
            // Need to figure out how to set window insets
            Column(modifier = Spacing(top = 24.dp)) {
                TopAppBar(
                    title = { Text(getString(R.string.settings_title)) },
                    actionData = listOf(TopAppBarAction.Back),
                    navigationIcon = {
                        DrawVector(
                            vectorImage = +vectorResource(R.drawable.ic_arrow_back),
                            tintColor = colors.onBackground
                        )
                    },
                    action = {}
                )
                Container(padding = EdgeInsets(dimen(R.dimen.spacing_small).dp)) {
                    Content()
                }
            }
        }
    }

    @Composable
    private fun Content() {
        Row(arrangement = Arrangement.End) {
            NightModeToggle()
        }
    }

    @Composable
    private fun NightModeToggle() {
        val typography = +MaterialTheme.typography()

        Text(getString(R.string.settings_night_mode), style = typography.body1)
        WidthSpacer(width = dimen(R.dimen.spacing_small).dp)
        Switch(
            checked = true,
            onCheckedChange = {}
        )
    }

    @Composable
    private fun AppTheme(block: () -> Unit) {
        val view = requireView()

        MaterialTheme(
            colors = ColorPalette(
                primary = Color(view.getColorAttr(R.attr.colorPrimary)),
                onPrimary = Color(view.getColorAttr(R.attr.colorOnPrimary)),
                primaryVariant = Color(view.getColorAttr(R.attr.colorPrimaryVariant)),
                secondary = Color(view.getColorAttr(R.attr.colorSecondary)),
                onSecondary = Color(view.getColorAttr(R.attr.colorOnSecondary)),
                secondaryVariant = Color(view.getColorAttr(R.attr.colorSecondaryVariant)),
                surface = Color(view.getColorAttr(R.attr.colorSurface)),
                onSurface = Color(view.getColorAttr(R.attr.colorOnSurface)),
                background = Color(view.getColorAttr(android.R.attr.colorBackground)),
                onBackground = Color(view.getColorAttr(R.attr.colorOnBackground))
            )
        ) { block() }
    }

    enum class TopAppBarAction {
        Back
    }
}
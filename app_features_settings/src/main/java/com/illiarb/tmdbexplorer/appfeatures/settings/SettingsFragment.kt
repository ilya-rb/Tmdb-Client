package com.illiarb.tmdbexplorer.appfeatures.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.FrameLayout
import androidx.fragment.app.Fragment
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.EdgeInsets
import androidx.ui.layout.Row
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Switch
import androidx.ui.material.TopAppBar
import com.illiarb.tmdbexplorer.coreui.ext.getColorAttr

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FrameLayout(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view as ViewGroup).setContent {
            SettingsScreen()
        }
    }

    @Composable
    private fun SettingsScreen() {
        AppTheme {
            Column {
                TopAppBar(title = { Text(getString(R.string.settings_title)) })
                Container(padding = EdgeInsets(16.dp)) {
                    Content()
                }
            }
        }
    }

    @Composable
    private fun Content() {
        Row {
            Text("Night mode")
            Switch(
                checked = false,
                onCheckedChange = { /* No-op */ }
            )
        }
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
}
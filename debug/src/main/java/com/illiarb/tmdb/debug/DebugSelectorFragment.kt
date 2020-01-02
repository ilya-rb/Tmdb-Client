package com.illiarb.tmdb.debug

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illiarb.tmdb.debug.databinding.FragmentDebugSelectorBinding
import jp.wasabeef.takt.Seat
import jp.wasabeef.takt.Takt

class DebugSelectorFragment : BottomSheetDialogFragment() {

    companion object {

        const val PREFS_NAME = "debug_prefs"
        const val PREF_SHOW_FPS = "show_fps"

        fun newInstance() = DebugSelectorFragment()

        private var fpsTracker: Takt.Program? = null
        private var isTrackerPlaying = false
    }

    private lateinit var binding: FragmentDebugSelectorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDebugSelectorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs = view.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val showFps = prefs.getBoolean(PREF_SHOW_FPS, false)

        if (showFps) {
            startFpsTracker()
        }

        binding.switchShowFps.isChecked = showFps
        binding.switchShowFps.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit()
                .putBoolean(PREF_SHOW_FPS, isChecked)
                .apply()

            if (isChecked) {
                startFpsTracker()
            } else {
                stopFpsTracker()
            }
        }

        binding.btnUiComponents.setOnClickListener {
            showFragment(UiComponentsFragment.newInstance())
            dismiss()
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(android.R.id.content, fragment, fragment::class.java.name)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun stopFpsTracker() {
        fpsTracker?.stop()
        isTrackerPlaying = false
    }

    private fun startFpsTracker() {
        if (fpsTracker == null) {
            fpsTracker = Takt.stock(requireActivity().application)
                .size(14f)
                .color(Color.BLACK)
                .seat(Seat.TOP_LEFT)
                .interval(250)
                .showOverlaySetting(true)
                .useCustomControl()
        }

        if (!isTrackerPlaying) {
            fpsTracker!!.play()
            isTrackerPlaying = true
        }
    }
}
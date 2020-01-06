package com.illiarb.tmdb.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illiarb.tmdb.debug.databinding.FragmentDebugSelectorBinding

class DebugSelectorFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = DebugSelectorFragment()
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
        super.onViewCreated(view, savedInstanceState)

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
}
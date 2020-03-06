package com.jordansilva.map4.ui.poidetail

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.jordansilva.map4.R


class POIDetailFragment : Fragment(R.layout.fragment_poi_detail) {

    companion object {
        fun newInstance() = POIDetailFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()

    }

    private fun initUi() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else
                requireActivity().onBackPressed()
        }
    }

}
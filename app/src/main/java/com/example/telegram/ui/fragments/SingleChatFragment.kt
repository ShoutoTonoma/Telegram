package com.example.telegram.ui.fragments


import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.telegram.R
import com.example.telegram.databinding.FragmentSingleChatBinding
import com.example.telegram.models.CommonModel
import com.example.telegram.utilits.APP_ACTIVITY


class SingleChatFragment(contact: CommonModel) : BaseFragment<FragmentSingleChatBinding>(
    FragmentSingleChatBinding::inflate
) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info).visibility = View.VISIBLE
    }


    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info).visibility = View.GONE
    }
}
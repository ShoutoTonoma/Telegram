package com.example.telegram.ui.fragments

import com.example.telegram.databinding.FragmentChangeBioBinding
import com.example.telegram.ui.fragments.base.BaseChangeFragment
import com.example.telegram.database.USER
import com.example.telegram.database.setBioToDatabase


class ChangeBioFragment : BaseChangeFragment<FragmentChangeBioBinding>(
    FragmentChangeBioBinding::inflate
) {

    override fun onResume() {
        super.onResume()
        binding.settingsInputBio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = binding.settingsInputBio.text.toString()
        setBioToDatabase(newBio)
    }
}
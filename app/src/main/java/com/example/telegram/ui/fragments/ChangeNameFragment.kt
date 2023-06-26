package com.example.telegram.ui.fragments

import com.example.telegram.R
import com.example.telegram.database.USER
import com.example.telegram.database.setNameToDatabase
import com.example.telegram.databinding.FragmentChangeNameBinding
import com.example.telegram.ui.fragments.base.BaseChangeFragment
import com.example.telegram.utilits.*


class ChangeNameFragment : BaseChangeFragment<FragmentChangeNameBinding>(
    FragmentChangeNameBinding::inflate
) {


    override fun onResume() {
        super.onResume()
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullNameList = USER.fullname.split(" ")
        if (fullNameList.size > 1) {
            binding.settingsInputName.setText(fullNameList[0])
            binding.settingsInputSurname.setText(fullNameList[1])
        } else binding.settingsInputName.setText(fullNameList[0])
    }

    override fun change() {
        val name = binding.settingsInputName.text.toString()
        val surname = binding.settingsInputSurname.text.toString()

        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            setNameToDatabase(fullname)
        }
    }
}
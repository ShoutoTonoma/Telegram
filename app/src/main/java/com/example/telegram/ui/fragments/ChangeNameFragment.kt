package com.example.telegram.ui.fragments

import com.example.telegram.R
import com.example.telegram.databinding.FragmentChangeNameBinding
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
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
                .setValue(fullname).addOnCompleteListener {
                    if(it.isSuccessful) {
                        showToast(getString(R.string.toast_data_update))
                        USER.fullname = fullname
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        parentFragmentManager.popBackStack()
                    }
                }
        }
    }
}
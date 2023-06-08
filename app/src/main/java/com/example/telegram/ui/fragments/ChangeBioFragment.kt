package com.example.telegram.ui.fragments

import com.example.telegram.R
import com.example.telegram.databinding.FragmentChangeBioBinding
import com.example.telegram.utilits.CHILD_BIO
import com.example.telegram.utilits.NODE_USERS
import com.example.telegram.utilits.REF_DATABASE_ROOT
import com.example.telegram.utilits.CURRENT_UID
import com.example.telegram.utilits.USER
import com.example.telegram.utilits.showToast

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
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    USER.bio = newBio
                    parentFragmentManager.popBackStack()
                }
            }
    }

}
package com.example.telegram.ui.fragments

import com.example.telegram.R
import com.example.telegram.databinding.FragmentChangeUserNameBinding
import com.example.telegram.utilits.AppValueEventListener
import com.example.telegram.utilits.CHILD_USERNAME
import com.example.telegram.utilits.NODE_USERNAMES
import com.example.telegram.utilits.NODE_USERS
import com.example.telegram.utilits.REF_DATABASE_ROOT
import com.example.telegram.utilits.CURRENT_UID
import com.example.telegram.utilits.USER
import com.example.telegram.utilits.showToast
import java.util.Locale

class ChangeUserNameFragment : BaseChangeFragment<FragmentChangeUserNameBinding>(
    FragmentChangeUserNameBinding::inflate
) {

    lateinit var mNewUserName: String

    override fun onResume() {
        super.onResume()
        binding.settingsInputUserName.setText(USER.username)
    }


    override fun change() {
        mNewUserName = binding.settingsInputUserName.text.toString().lowercase(Locale.getDefault())
        if(mNewUserName.isEmpty()) {
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUserName)) {
                        showToast("Такой пользователь существует")
                    } else {
                        changeUserName()
                    }
                })

        }
    }

    private fun changeUserName() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUserName).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUserName()
                }
            }
    }

    private fun updateCurrentUserName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
            .setValue(mNewUserName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    parentFragmentManager.popBackStack()
                    USER.username = mNewUserName
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}
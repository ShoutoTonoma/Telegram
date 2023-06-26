package com.example.telegram.ui.fragments

import com.example.telegram.databinding.FragmentChangeUserNameBinding
import com.example.telegram.ui.fragments.base.BaseChangeFragment
import com.example.telegram.utilits.AppValueEventListener
import com.example.telegram.database.NODE_USERNAMES
import com.example.telegram.database.REF_DATABASE_ROOT
import com.example.telegram.database.CURRENT_UID
import com.example.telegram.database.USER
import com.example.telegram.database.updateCurrentUserName
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
                    updateCurrentUserName(mNewUserName)
                }
            }
    }




}
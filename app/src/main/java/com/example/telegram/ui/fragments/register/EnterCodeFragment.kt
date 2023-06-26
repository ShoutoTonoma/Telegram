package com.example.telegram.ui.fragments.register

import com.example.telegram.database.AUTH
import com.example.telegram.database.CHILD_ID
import com.example.telegram.database.CHILD_PHONE
import com.example.telegram.database.CHILD_USERNAME
import com.example.telegram.database.NODE_PHONES
import com.example.telegram.database.NODE_USERS
import com.example.telegram.database.REF_DATABASE_ROOT
import com.example.telegram.databinding.FragmentEnterCodeBinding
import com.example.telegram.ui.fragments.base.BaseRegisterFragment
import com.example.telegram.utilits.*
import com.google.firebase.auth.PhoneAuthProvider

class EnterCodeFragment(val phoneNumber: String, val id: String) : BaseRegisterFragment<FragmentEnterCodeBinding>(
    FragmentEnterCodeBinding::inflate
) {



    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        binding.registerPinView.requestFocus()
        binding.registerPinView.addTextChangedListener(AppTextWatcher {
            val string = binding.registerPinView.text.toString()
            if(string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = binding.registerPinView.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                dateMap[CHILD_USERNAME] = uid

                REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                            .addOnSuccessListener {
                                showToast("Добро пожаловать")
                                restartActivity()
                            }
                            .addOnFailureListener { showToast(it.message.toString()) }
                    }
            } else showToast(task.exception?.message.toString())
        }
    }
}
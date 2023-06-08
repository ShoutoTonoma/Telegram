package com.example.telegram.ui.fragments

import com.example.telegram.MainActivity
import com.example.telegram.activities.RegisterActivity
import com.example.telegram.databinding.FragmentEnterCodeBinding
import com.example.telegram.utilits.*
import com.google.firebase.auth.PhoneAuthProvider

class EnterCodeFragment(val phoneNumber: String, val id: String) : BaseFragment<FragmentEnterCodeBinding>(
    FragmentEnterCodeBinding::inflate
) {


    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
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

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                    .addOnCompleteListener { task2 ->
                        if(task2.isSuccessful) {
                            showToast("Добро пожаловать")
                            (activity as RegisterActivity).replaceActivity(MainActivity())
                        } else showToast(task2.exception?.message.toString())
                }
            } else showToast(task.exception?.message.toString())
        }
    }
}
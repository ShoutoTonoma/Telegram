package com.example.telegram.utilits

import com.example.telegram.database.AUTH
import com.example.telegram.database.CHILD_STATE
import com.example.telegram.database.CURRENT_UID
import com.example.telegram.database.NODE_USERS
import com.example.telegram.database.REF_DATABASE_ROOT
import com.example.telegram.database.USER

enum class AppStates(val state: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object {
        fun updateStates(appStates: AppStates) {
            if (AUTH.currentUser != null) {
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener { USER.state = appStates.state }
                    .addOnFailureListener { showToast(it.message.toString()) }
            }
        }
    }
}
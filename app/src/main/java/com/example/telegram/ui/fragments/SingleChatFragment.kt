package com.example.telegram.ui.fragments


import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.telegram.R
import com.example.telegram.databinding.FragmentSingleChatBinding
import com.example.telegram.models.CommonModel
import com.example.telegram.models.UserModel
import com.example.telegram.utilits.APP_ACTIVITY
import com.example.telegram.utilits.AppValueEventListener
import com.example.telegram.utilits.NODE_USERS
import com.example.telegram.utilits.REF_DATABASE_ROOT
import com.example.telegram.utilits.TYPE_TEXT
import com.example.telegram.utilits.downloadAndSetImage
import com.example.telegram.utilits.getUserModel
import com.example.telegram.utilits.sendMessage
import com.example.telegram.utilits.showToast
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView


class SingleChatFragment(private val contact: CommonModel) : BaseFragment<FragmentSingleChatBinding>(
    FragmentSingleChatBinding::inflate
) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUsers: DatabaseReference

    override fun onResume() {
        super.onResume()
        mToolbarInfo = APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info)
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }

        mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUsers.addValueEventListener(mListenerInfoToolbar)
        binding.chatBtnSendMessage.setOnClickListener {
            val message = binding.chatInputMessage.text.toString()
            if(message.isEmpty()) {
                showToast("Введите сообщение")
            } else sendMessage(message, contact.id, TYPE_TEXT) {
                binding.chatInputMessage.setText("")
            }
        }
    }

    private fun initInfoToolbar() {
        if(mReceivingUser.fullname.isEmpty()) {
            mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text = contact.fullname
        } else mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text = mReceivingUser.fullname

        mToolbarInfo.findViewById<CircleImageView>(R.id.toolbar_chat_image).downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_status).text = mReceivingUser.state
    }


    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUsers.removeEventListener(mListenerInfoToolbar)
    }
}
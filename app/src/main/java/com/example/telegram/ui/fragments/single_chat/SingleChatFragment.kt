package com.example.telegram.ui.fragments.single_chat


import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.databinding.FragmentSingleChatBinding
import com.example.telegram.models.CommonModel
import com.example.telegram.models.UserModel
import com.example.telegram.ui.fragments.base.BaseFragment
import com.example.telegram.utilits.APP_ACTIVITY
import com.example.telegram.utilits.AppValueEventListener
import com.example.telegram.database.CURRENT_UID
import com.example.telegram.database.NODE_MESSAGES
import com.example.telegram.database.NODE_USERS
import com.example.telegram.database.REF_DATABASE_ROOT
import com.example.telegram.database.TYPE_TEXT
import com.example.telegram.utilits.downloadAndSetImage
import com.example.telegram.database.getCommonModel
import com.example.telegram.database.getUserModel
import com.example.telegram.database.sendMessage
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
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessageListener: AppValueEventListener
    private var mListMessages = emptyList<CommonModel>()

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = binding.chatRecycleView
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.adapter = mAdapter
        mMessageListener = AppValueEventListener { dataSnapshot ->
            mListMessages = dataSnapshot.children.map { it.getCommonModel() }
            mAdapter.setList(mListMessages)
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }
        mRefMessages.addValueEventListener(mMessageListener)
    }

    private fun initToolbar() {
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
            if (message.isEmpty()) {
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
        mRefMessages.removeEventListener(mMessageListener)
    }
}
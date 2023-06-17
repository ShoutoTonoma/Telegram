package com.example.telegram.ui.fragments



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.databinding.FragmentContactsBinding
import com.example.telegram.models.CommonModel
import com.example.telegram.utilits.APP_ACTIVITY
import com.example.telegram.utilits.AppValueEventListener
import com.example.telegram.utilits.CURRENT_UID
import com.example.telegram.utilits.NODE_PHONES_CONTACTS
import com.example.telegram.utilits.NODE_USERS
import com.example.telegram.utilits.REF_DATABASE_ROOT
import com.example.telegram.utilits.downloadAndSetImage
import com.example.telegram.utilits.getCommonModel
import com.example.telegram.utilits.replaceFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView


class ContactsFragment : BaseFragment<FragmentContactsBinding>(
    FragmentContactsBinding::inflate
) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListeners: AppValueEventListener
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = binding.contactsRecycleView
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        mAdapter = object: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListeners = AppValueEventListener {
                    val contact = it.getCommonModel()

                    if(contact.fullname.isEmpty()) {
                        holder.name.text = model.fullname
                    } else holder.name.text = contact.fullname

                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(model)) }
                }

                mRefUsers.addValueEventListener(mRefUsersListeners)
                mapListeners[mRefUsers] = mRefUsersListeners
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()


    }

    class ContactsHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_fullname)
        val status: TextView = view.findViewById(R.id.contact_status)
        val photo: CircleImageView = view.findViewById(R.id.contact_photo)
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
    }
}

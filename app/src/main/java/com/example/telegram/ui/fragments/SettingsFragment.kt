package com.example.telegram.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.telegram.R
import com.example.telegram.activities.RegisterActivity
import com.example.telegram.databinding.FragmentSettingsBinding
import com.example.telegram.utilits.APP_ACTIVITY
import com.example.telegram.utilits.AUTH
import com.example.telegram.utilits.AppStates
import com.example.telegram.utilits.CHILD_PHOTO_URL
import com.example.telegram.utilits.FOLDER_PROFILE_IMAGE
import com.example.telegram.utilits.REF_STORAGE_ROOT
import com.example.telegram.utilits.CURRENT_UID
import com.example.telegram.utilits.NODE_USERS
import com.example.telegram.utilits.REF_DATABASE_ROOT
import com.example.telegram.utilits.USER
import com.example.telegram.utilits.downloadAndSetImage
import com.example.telegram.utilits.getUrlFromStorage
import com.example.telegram.utilits.putImageToStorage
import com.example.telegram.utilits.putUrlToDatabase
import com.example.telegram.utilits.replaceActivity
import com.example.telegram.utilits.replaceFragment
import com.example.telegram.utilits.showToast
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() = with(binding) {
        settingsBio.text = USER.bio
        settingsFullName.text = USER.fullname
        settingsPhoneNumber.text = USER.phone
        settingsStatus.text = USER.state
        settingsUsername.text = USER.username
        settingsBtnChangeUsername.setOnClickListener { replaceFragment(ChangeUserNameFragment()) }
        settingsBtnChangeBio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settingsChangePhoto.setOnClickListener { changePhotoUser() }
        binding.settingsUserPhoto.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            &&  resultCode == RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        binding.settingsUserPhoto.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings_menu_exit -> {
                AppStates.updateStates(AppStates.OFFLINE)
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }
}
package com.start.STart.util

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionHelper(val activity: AppCompatActivity) {

    private var activityResultLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var grantedCallback: () -> Unit
    private var deniedCallback: (() -> Unit)? = null

    init {
        activityResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (results.all { it.value }) {
                grantedCallback()
            } else {
                if (deniedCallback != null) {
                    deniedCallback?.invoke()
                } else {
                    showErrorToast(activity, "권한이 거부되었습니다.")
                }
            }
        }
    }

    fun checkAndRequestPermissions(
        permissions: Array<String>,
        grantedCallback: () -> Unit,
        deniedCallback: (() -> Unit)? = null,
    ) {
        this.grantedCallback = grantedCallback
        this.deniedCallback = deniedCallback

        val ungrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (ungrantedPermissions.isEmpty()) {
            grantedCallback()
            return
        }

        activityResultLauncher.launch(ungrantedPermissions.toTypedArray())
    }
}
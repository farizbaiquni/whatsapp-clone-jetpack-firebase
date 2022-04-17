package com.example.whatsappclonejetpackfirebase.utils

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState

class PermissionHelper() {
    @OptIn(ExperimentalPermissionsApi::class)
    fun multiplePermissions(multiplePermissionsState: MultiplePermissionsState){
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun singlePermission(permissionState: PermissionState){
        permissionState.launchPermissionRequest()
    }

}
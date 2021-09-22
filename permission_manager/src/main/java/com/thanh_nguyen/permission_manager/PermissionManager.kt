package com.thanh_nguyen.permission_manager

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.NullPointerException

class PermissionManager : BasePermissionManager() {

    companion object {

        private const val TAG = "PermissionManager"

        suspend fun requestPermissions(
            activity: AppCompatActivity,
            requestId: Int,
            vararg permissions: String
        ): PermissionResult {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(
                    activity,
                    requestId,
                    *permissions
                )
            }
        }

        suspend fun requestPermissions(
            fragment: Fragment,
            requestId: Int,
            vararg permissions: String
        ): PermissionResult {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(
                    fragment,
                    requestId,
                    *permissions
                )
            }
        }

        private suspend fun _requestPermissions(
            activityOrFragment: Any,
            requestId: Int,
            vararg permissions: String
        ): PermissionResult {
            val fragmentManager = when{
                activityOrFragment is AppCompatActivity -> activityOrFragment.supportFragmentManager
                activityOrFragment is Fragment -> activityOrFragment.childFragmentManager
                else -> null
            } ?: throw NullPointerException("Fragment manager is null")

            return if (fragmentManager.findFragmentByTag(TAG) != null) {
                val permissionManager = fragmentManager.findFragmentByTag(TAG) as PermissionManager
                permissionManager.completableDeferred = CompletableDeferred()
                permissionManager.requestPermissions(
                    requestId,
                    *permissions
                )
                permissionManager.completableDeferred.await()
            } else {
                val permissionManager = PermissionManager().apply {
                    completableDeferred = CompletableDeferred()
                }
                fragmentManager.beginTransaction().add(
                    permissionManager,
                    TAG
                ).commitNow()
                permissionManager.requestPermissions(requestId, *permissions)
                permissionManager.completableDeferred.await()
            }
        }
    }

    private lateinit var completableDeferred: CompletableDeferred<PermissionResult>

    override fun onPermissionResult(permissionResult: PermissionResult) {
        // When fragment gets recreated due to memory constraints by OS completableDeferred would be
        // uninitialized and hence check
        if (::completableDeferred.isInitialized) {
            completableDeferred.complete(permissionResult)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::completableDeferred.isInitialized && completableDeferred.isActive) {
            completableDeferred.cancel()
        }
    }
}

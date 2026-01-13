/*
 * Licensed under MIT License
 */
package com.community.cordova.security;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SecurityCheck extends CordovaPlugin {

    private static final String LOG_TAG = "SecurityCheck";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if ("getSecurityInfo".equals(action)) {
                this.getSecurityInfo(callbackContext);
                return true;
            } else if ("getSecurityPatchLevel".equals(action)) {
                this.getSecurityPatchLevel(callbackContext);
                return true;
            } else if ("isDeviceCompromised".equals(action)) {
                this.isDeviceCompromised(callbackContext);
                return true;
            } else if ("isDeveloperOptionsEnabled".equals(action)) {
                this.isDeveloperOptionsEnabled(callbackContext);
                return true;
            } else if ("isUsbDebuggingEnabled".equals(action)) {
                this.isUsbDebuggingEnabled(callbackContext);
                return true;
            } else if ("isScreenLockEnabled".equals(action)) {
                this.isScreenLockEnabled(callbackContext);
                return true;
            } else if ("getEncryptionStatus".equals(action)) {
                this.getEncryptionStatus(callbackContext);
                return true;
            } else if ("isPlayServicesAvailable".equals(action)) {
                this.isPlayServicesAvailable(callbackContext);
                return true;
            } else if ("getDangerousPermissions".equals(action)) {
                this.getDangerousPermissions(callbackContext);
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error executing action: " + action, e);
            callbackContext.error("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get comprehensive security information
     */
    private void getSecurityInfo(CallbackContext callbackContext) {
        try {
            JSONObject result = new JSONObject();

            result.put("securityPatchLevel", getSecurityPatchLevelString());
            result.put("isDeviceCompromised", isRooted());
            result.put("isDeveloperOptionsEnabled", isDeveloperMode());
            result.put("isUsbDebuggingEnabled", isUsbDebugging());
            result.put("isScreenLockEnabled", hasScreenLock());
            result.put("encryptionStatus", getDeviceEncryptionStatus());
            result.put("isPlayServicesAvailable", checkPlayServices());
            result.put("dangerousPermissions", getDangerousPermissionsList());
            result.put("osVersion", Build.VERSION.RELEASE);

            callbackContext.success(result);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting security info", e);
            callbackContext.error("Error getting security info: " + e.getMessage());
        }
    }

    /**
     * Get Android security patch level
     */
    private void getSecurityPatchLevel(CallbackContext callbackContext) {
        try {
            String patchLevel = getSecurityPatchLevelString();
            callbackContext.success(patchLevel);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting security patch level", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    /**
     * Check if device is rooted
     */
    private void isDeviceCompromised(CallbackContext callbackContext) {
        try {
            boolean isRooted = isRooted();
            callbackContext.success(isRooted ? 1 : 0);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking root status", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    /**
     * Check if developer options are enabled
     */
    private void isDeveloperOptionsEnabled(CallbackContext callbackContext) {
        try {
            boolean isDev = isDeveloperMode();
            callbackContext.success(isDev ? 1 : 0);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking developer options", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    /**
     * Check if USB debugging is enabled
     */
    private void isUsbDebuggingEnabled(CallbackContext callbackContext) {
        try {
            boolean isDebugging = isUsbDebugging();
            callbackContext.success(isDebugging ? 1 : 0);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking USB debugging", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    /**
     * Check if screen lock is enabled
     */
    private void isScreenLockEnabled(CallbackContext callbackContext) {
        try {
            boolean hasLock = hasScreenLock();
            callbackContext.success(hasLock ? 1 : 0);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking screen lock", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    /**
     * Get device encryption status
     */
    private void getEncryptionStatus(CallbackContext callbackContext) {
        try {
            String status = getDeviceEncryptionStatus();
            callbackContext.success(status);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting encryption status", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    /**
     * Check if Google Play Services are available
     */
    private void isPlayServicesAvailable(CallbackContext callbackContext) {
        try {
            boolean available = checkPlayServices();
            callbackContext.success(available ? 1 : 0);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking Play Services", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    /**
     * Get list of dangerous permissions granted
     */
    private void getDangerousPermissions(CallbackContext callbackContext) {
        try {
            JSONArray permissions = getDangerousPermissionsList();
            callbackContext.success(permissions);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting dangerous permissions", e);
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    // Helper methods

    private String getSecurityPatchLevelString() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Build.VERSION.SECURITY_PATCH;
        }
        return "Unknown";
    }

    private boolean isRooted() {
        // Check for common root indicators
        String[] paths = {
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        };

        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }

        // Check for su binary
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"which", "su"});
            if (process.waitFor() == 0) {
                return true;
            }
        } catch (Exception e) {
            // Ignore
        }

        return false;
    }

    private boolean isDeveloperMode() {
        try {
            Context context = cordova.getActivity().getApplicationContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getInt(context.getContentResolver(),
                        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking developer mode", e);
        }
        return false;
    }

    private boolean isUsbDebugging() {
        try {
            Context context = cordova.getActivity().getApplicationContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getInt(context.getContentResolver(),
                        Settings.Global.ADB_ENABLED, 0) != 0;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking USB debugging", e);
        }
        return false;
    }

    private boolean hasScreenLock() {
        try {
            Context context = cordova.getActivity().getApplicationContext();
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return keyguardManager.isDeviceSecure();
            } else {
                return keyguardManager.isKeyguardSecure();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking screen lock", e);
        }
        return false;
    }

    private String getDeviceEncryptionStatus() {
        try {
            Context context = cordova.getActivity().getApplicationContext();
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int status = dpm.getStorageEncryptionStatus();
                switch (status) {
                    case DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE:
                        return "Encrypted";
                    case DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE:
                        return "Not Encrypted";
                    case DevicePolicyManager.ENCRYPTION_STATUS_ACTIVATING:
                        return "Encrypting";
                    case DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED:
                        return "Unsupported";
                    default:
                        return "Unknown";
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking encryption status", e);
        }
        return "Unknown";
    }

    private boolean checkPlayServices() {
        try {
            Context context = cordova.getActivity().getApplicationContext();
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
            return resultCode == ConnectionResult.SUCCESS;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error checking Play Services", e);
            return false;
        }
    }

    private JSONArray getDangerousPermissionsList() throws JSONException {
        JSONArray permissions = new JSONArray();

        try {
            Context context = cordova.getActivity().getApplicationContext();
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);

            if (packageInfo.requestedPermissions != null) {
                for (int i = 0; i < packageInfo.requestedPermissions.length; i++) {
                    String permission = packageInfo.requestedPermissions[i];

                    // Check if permission is granted and is a dangerous permission
                    if ((packageInfo.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        try {
                            android.content.pm.PermissionInfo permissionInfo = pm.getPermissionInfo(permission, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (permissionInfo.protectionLevel == android.content.pm.PermissionInfo.PROTECTION_DANGEROUS) {
                                    permissions.put(permission);
                                }
                            } else {
                                // For older versions, just add all granted permissions
                                permissions.put(permission);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            // Permission not found, skip
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting dangerous permissions", e);
        }

        return permissions;
    }
}

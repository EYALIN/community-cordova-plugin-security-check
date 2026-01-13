/*
 * Licensed under MIT License
 */

var PLUGIN_NAME = 'SecurityCheck';

var SecurityCheckPlugin = {
    /**
     * Get comprehensive security information
     * @returns {Promise<Object>}
     */
    getSecurityInfo: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'getSecurityInfo', []);
        });
    },

    /**
     * Get Android security patch level
     * @returns {Promise<string>}
     */
    getSecurityPatchLevel: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'getSecurityPatchLevel', []);
        });
    },

    /**
     * Check if device is rooted (Android) or jailbroken (iOS)
     * @returns {Promise<number>} 1 for true, 0 for false
     */
    isDeviceCompromised: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'isDeviceCompromised', []);
        });
    },

    /**
     * Check if developer options are enabled (Android only)
     * @returns {Promise<number>} 1 for true, 0 for false
     */
    isDeveloperOptionsEnabled: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'isDeveloperOptionsEnabled', []);
        });
    },

    /**
     * Check if USB debugging is enabled (Android only)
     * @returns {Promise<number>} 1 for true, 0 for false
     */
    isUsbDebuggingEnabled: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'isUsbDebuggingEnabled', []);
        });
    },

    /**
     * Check if device has screen lock enabled
     * @returns {Promise<number>} 1 for true, 0 for false
     */
    isScreenLockEnabled: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'isScreenLockEnabled', []);
        });
    },

    /**
     * Get device encryption status
     * @returns {Promise<string>}
     */
    getEncryptionStatus: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'getEncryptionStatus', []);
        });
    },

    /**
     * Check if Google Play Services are available (Android only)
     * @returns {Promise<number>} 1 for true, 0 for false
     */
    isPlayServicesAvailable: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'isPlayServicesAvailable', []);
        });
    },

    /**
     * Get list of dangerous permissions granted
     * @returns {Promise<Array<string>>}
     */
    getDangerousPermissions: function() {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'getDangerousPermissions', []);
        });
    }
};

module.exports = SecurityCheckPlugin;

/*
 * Licensed under MIT License
 */

/**
 * Browser implementation of SecurityCheck plugin
 * Returns dummy data as browsers don't provide direct security information access
 */

module.exports = {
    getSecurityInfo: function (successCallback, errorCallback, args) {
        var result = {
            securityPatchLevel: '2025-01-01',
            isDeviceCompromised: false,
            isDeveloperOptionsEnabled: false,
            isUsbDebuggingEnabled: false,
            isScreenLockEnabled: true,
            encryptionStatus: 'Encrypted',
            isPlayServicesAvailable: false,
            dangerousPermissions: [],
            osVersion: 'Browser',
            securityProvider: 'Browser'
        };
        successCallback(result);
    },

    getSecurityPatchLevel: function (successCallback, errorCallback, args) {
        successCallback('2025-01-01');
    },

    isDeviceCompromised: function (successCallback, errorCallback, args) {
        successCallback(0); // false
    },

    isDeveloperOptionsEnabled: function (successCallback, errorCallback, args) {
        successCallback(0); // false
    },

    isUsbDebuggingEnabled: function (successCallback, errorCallback, args) {
        successCallback(0); // false
    },

    isScreenLockEnabled: function (successCallback, errorCallback, args) {
        successCallback(1); // true (assume secured)
    },

    getEncryptionStatus: function (successCallback, errorCallback, args) {
        successCallback('Encrypted');
    },

    isPlayServicesAvailable: function (successCallback, errorCallback, args) {
        successCallback(0); // false (not applicable)
    },

    getDangerousPermissions: function (successCallback, errorCallback, args) {
        successCallback([]);
    }
};

require('cordova/exec/proxy').add('SecurityCheck', module.exports);

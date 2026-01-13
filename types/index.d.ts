/*
 * Licensed under MIT License
 */

/**
 * Comprehensive security information
 */
export interface ISecurityInfo {
    /** Security patch level (Android) or iOS version */
    securityPatchLevel: string;
    /** Whether device is rooted (Android) or jailbroken (iOS) */
    isDeviceCompromised: boolean;
    /** Whether developer options are enabled (Android only) */
    isDeveloperOptionsEnabled: boolean;
    /** Whether USB debugging is enabled (Android only) */
    isUsbDebuggingEnabled: boolean;
    /** Whether screen lock is enabled */
    isScreenLockEnabled: boolean;
    /** Device encryption status */
    encryptionStatus: string;
    /** Whether Google Play Services are available (Android only) */
    isPlayServicesAvailable: boolean;
    /** List of dangerous permissions granted */
    dangerousPermissions: string[];
    /** OS version */
    osVersion: string;
    /** Security provider (e.g., "GmsCore", "Conscrypt") */
    securityProvider?: string;
}

export default class SecurityCheckManager {
    /**
     * Get comprehensive security information
     * @returns Promise with complete security info
     */
    getSecurityInfo(): Promise<ISecurityInfo>;

    /**
     * Get Android security patch level or iOS version
     * @returns Promise with patch level string (e.g., "2025-01-01" for Android, "17.2" for iOS)
     */
    getSecurityPatchLevel(): Promise<string>;

    /**
     * Check if device is rooted (Android) or jailbroken (iOS)
     * @returns Promise with 1 (true) or 0 (false)
     */
    isDeviceCompromised(): Promise<number>;

    /**
     * Check if developer options are enabled (Android only)
     * @returns Promise with 1 (true) or 0 (false)
     */
    isDeveloperOptionsEnabled(): Promise<number>;

    /**
     * Check if USB debugging is enabled (Android only)
     * @returns Promise with 1 (true) or 0 (false)
     */
    isUsbDebuggingEnabled(): Promise<number>;

    /**
     * Check if device has screen lock enabled
     * @returns Promise with 1 (true) or 0 (false)
     */
    isScreenLockEnabled(): Promise<number>;

    /**
     * Get device encryption status
     * @returns Promise with encryption status string
     */
    getEncryptionStatus(): Promise<string>;

    /**
     * Check if Google Play Services are available (Android only)
     * @returns Promise with 1 (true) or 0 (false)
     */
    isPlayServicesAvailable(): Promise<number>;

    /**
     * Get list of dangerous permissions granted to the app
     * @returns Promise with array of permission strings
     */
    getDangerousPermissions(): Promise<string[]>;
}

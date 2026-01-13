# Community Cordova Security Check Plugin

A comprehensive Cordova plugin to check device security status on Android and iOS devices.

## Features

- **Security Patch Level**: Get Android security patch level or iOS version
- **Device Compromise Detection**: Check if device is rooted (Android) or jailbroken (iOS)
- **Developer Options**: Check if developer options are enabled (Android only)
- **USB Debugging**: Check if USB debugging is enabled (Android only)
- **Screen Lock**: Check if device has screen lock enabled
- **Encryption Status**: Get device encryption status
- **Play Services**: Check if Google Play Services are available (Android only)
- **Dangerous Permissions**: Get list of dangerous permissions granted

## Installation

```bash
cordova plugin add community-cordova-plugin-security-check
```

Or from local path:

```bash
cordova plugin add /path/to/community-cordova-plugin-security-check
```

## TypeScript Usage

```typescript
import SecurityCheckManager, { ISecurityInfo } from 'community-cordova-plugin-security-check';

declare var cordova: any;

// Get comprehensive security information
const securityInfo: ISecurityInfo = await cordova.plugins.security.getSecurityInfo();
console.log('Security Info:', securityInfo);

// Get security patch level
const patchLevel: string = await cordova.plugins.security.getSecurityPatchLevel();
console.log('Security Patch Level:', patchLevel);

// Check if device is compromised
const isCompromised: number = await cordova.plugins.security.isDeviceCompromised();
console.log('Is Device Compromised:', isCompromised === 1);

// Check if developer options are enabled (Android only)
const devOptions: number = await cordova.plugins.security.isDeveloperOptionsEnabled();
console.log('Developer Options Enabled:', devOptions === 1);

// Check if USB debugging is enabled (Android only)
const usbDebugging: number = await cordova.plugins.security.isUsbDebuggingEnabled();
console.log('USB Debugging Enabled:', usbDebugging === 1);

// Check if screen lock is enabled
const screenLock: number = await cordova.plugins.security.isScreenLockEnabled();
console.log('Screen Lock Enabled:', screenLock === 1);

// Get encryption status
const encryption: string = await cordova.plugins.security.getEncryptionStatus();
console.log('Encryption Status:', encryption);

// Check if Play Services are available (Android only)
const playServices: number = await cordova.plugins.security.isPlayServicesAvailable();
console.log('Play Services Available:', playServices === 1);

// Get dangerous permissions
const permissions: string[] = await cordova.plugins.security.getDangerousPermissions();
console.log('Dangerous Permissions:', permissions);
```

## JavaScript Usage

```javascript
// Get comprehensive security information
cordova.plugins.security.getSecurityInfo()
    .then(info => console.log('Security Info:', info))
    .catch(error => console.error('Error:', error));

// Get security patch level
cordova.plugins.security.getSecurityPatchLevel()
    .then(level => console.log('Patch Level:', level))
    .catch(error => console.error('Error:', error));

// Check if device is compromised
cordova.plugins.security.isDeviceCompromised()
    .then(result => console.log('Is Compromised:', result === 1))
    .catch(error => console.error('Error:', error));
```

## API

### `getSecurityInfo(): Promise<ISecurityInfo>`

Returns comprehensive security information including:
- `securityPatchLevel`: Security patch level (Android) or iOS version
- `isDeviceCompromised`: Whether device is rooted/jailbroken
- `isDeveloperOptionsEnabled`: Developer options status (Android only)
- `isUsbDebuggingEnabled`: USB debugging status (Android only)
- `isScreenLockEnabled`: Screen lock status
- `encryptionStatus`: Device encryption status
- `isPlayServicesAvailable`: Google Play Services status (Android only)
- `dangerousPermissions`: Array of dangerous permissions granted
- `osVersion`: OS version string

### `getSecurityPatchLevel(): Promise<string>`

Returns the Android security patch level (e.g., "2025-01-01") or iOS version (e.g., "17.2").

### `isDeviceCompromised(): Promise<number>`

Returns 1 if device is rooted (Android) or jailbroken (iOS), 0 otherwise.

### `isDeveloperOptionsEnabled(): Promise<number>`

Returns 1 if developer options are enabled, 0 otherwise. (Android only, always returns 0 on iOS)

### `isUsbDebuggingEnabled(): Promise<number>`

Returns 1 if USB debugging is enabled, 0 otherwise. (Android only, always returns 0 on iOS)

### `isScreenLockEnabled(): Promise<number>`

Returns 1 if device has screen lock (PIN/pattern/password/biometric) enabled, 0 otherwise.

### `getEncryptionStatus(): Promise<string>`

Returns device encryption status:
- **Android**: "Encrypted", "Not Encrypted", "Encrypting", "Unsupported", or "Unknown"
- **iOS**: "Encrypted" (iOS devices are always encrypted)

### `isPlayServicesAvailable(): Promise<number>`

Returns 1 if Google Play Services are available and up to date, 0 otherwise. (Android only, always returns 0 on iOS)

### `getDangerousPermissions(): Promise<string[]>`

Returns an array of dangerous permissions granted to the app. (Android only, always returns empty array on iOS)

## Platform Support

- ✅ Android (API 16+)
- ✅ iOS (9.0+)
- ✅ Browser (dummy data for testing)

## License

MIT

## Author

EYALIN

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

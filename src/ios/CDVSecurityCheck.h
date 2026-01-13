/*
 * Licensed under MIT License
 */

#import <Cordova/CDVPlugin.h>

@interface CDVSecurityCheck : CDVPlugin

- (void)getSecurityInfo:(CDVInvokedUrlCommand*)command;
- (void)getSecurityPatchLevel:(CDVInvokedUrlCommand*)command;
- (void)isDeviceCompromised:(CDVInvokedUrlCommand*)command;
- (void)isDeveloperOptionsEnabled:(CDVInvokedUrlCommand*)command;
- (void)isUsbDebuggingEnabled:(CDVInvokedUrlCommand*)command;
- (void)isScreenLockEnabled:(CDVInvokedUrlCommand*)command;
- (void)getEncryptionStatus:(CDVInvokedUrlCommand*)command;
- (void)isPlayServicesAvailable:(CDVInvokedUrlCommand*)command;
- (void)getDangerousPermissions:(CDVInvokedUrlCommand*)command;

// Helper methods
- (BOOL)checkJailbreak;
- (BOOL)checkScreenLock;
- (NSString*)getOSVersion;

@end

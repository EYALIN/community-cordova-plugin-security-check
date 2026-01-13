/*
 * Licensed under MIT License
 */

#import "CDVSecurityCheck.h"
#import <Cordova/CDVAvailability.h>
#import <LocalAuthentication/LocalAuthentication.h>
#import <UIKit/UIKit.h>

@implementation CDVSecurityCheck

- (void)getSecurityInfo:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;

        @try {
            BOOL isJailbroken = [self checkJailbreak];
            BOOL hasScreenLock = [self checkScreenLock];
            NSString* osVersion = [self getOSVersion];

            NSDictionary* result = @{
                @"securityPatchLevel": osVersion,
                @"isDeviceCompromised": @(isJailbroken),
                @"isDeveloperOptionsEnabled": @(NO), // Not applicable for iOS
                @"isUsbDebuggingEnabled": @(NO), // Not applicable for iOS
                @"isScreenLockEnabled": @(hasScreenLock),
                @"encryptionStatus": @"Encrypted", // iOS devices are always encrypted
                @"isPlayServicesAvailable": @(NO), // Not applicable for iOS
                @"dangerousPermissions": @[], // iOS doesn't have dangerous permissions concept
                @"osVersion": osVersion
            };

            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
        }
        @catch (NSException *exception) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:exception.reason];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)getSecurityPatchLevel:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;

        @try {
            NSString* osVersion = [self getOSVersion];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:osVersion];
        }
        @catch (NSException *exception) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:exception.reason];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)isDeviceCompromised:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;

        @try {
            BOOL isJailbroken = [self checkJailbreak];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:(isJailbroken ? 1 : 0)];
        }
        @catch (NSException *exception) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:exception.reason];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)isDeveloperOptionsEnabled:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        // Not applicable for iOS
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:0];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)isUsbDebuggingEnabled:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        // Not applicable for iOS
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:0];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)isScreenLockEnabled:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;

        @try {
            BOOL hasScreenLock = [self checkScreenLock];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:(hasScreenLock ? 1 : 0)];
        }
        @catch (NSException *exception) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:exception.reason];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)getEncryptionStatus:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        // iOS devices are always encrypted
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Encrypted"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)isPlayServicesAvailable:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        // Not applicable for iOS
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:0];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)getDangerousPermissions:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        // iOS doesn't have the concept of dangerous permissions like Android
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:@[]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

// Helper methods

- (BOOL)checkJailbreak
{
    // Check for common jailbreak indicators
    NSArray *jailbreakPaths = @[
        @"/Applications/Cydia.app",
        @"/Library/MobileSubstrate/MobileSubstrate.dylib",
        @"/bin/bash",
        @"/usr/sbin/sshd",
        @"/etc/apt",
        @"/private/var/lib/apt/",
        @"/private/var/lib/cydia",
        @"/private/var/mobile/Library/SBSettings/Themes",
        @"/private/var/tmp/cydia.log",
        @"/private/var/stash",
        @"/usr/libexec/sftp-server",
        @"/usr/bin/sshd"
    ];

    for (NSString *path in jailbreakPaths) {
        if ([[NSFileManager defaultManager] fileExistsAtPath:path]) {
            return YES;
        }
    }

    // Check if we can modify system files (should not be possible on non-jailbroken devices)
    NSError *error;
    NSString *testString = @"test";
    NSString *testPath = @"/private/jailbreak_test.txt";

    [testString writeToFile:testPath atomically:YES encoding:NSUTF8StringEncoding error:&error];
    if (!error) {
        [[NSFileManager defaultManager] removeItemAtPath:testPath error:nil];
        return YES;
    }

    // Check if system() call works (should fail on non-jailbroken devices)
    if (system(NULL) != 0) {
        return YES;
    }

    return NO;
}

- (BOOL)checkScreenLock
{
    LAContext *context = [[LAContext alloc] init];
    NSError *error = nil;

    // Check if device can evaluate device owner authentication with biometry or passcode
    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthentication error:&error]) {
        return YES;
    }

    return NO;
}

- (NSString*)getOSVersion
{
    return [[UIDevice currentDevice] systemVersion];
}

@end

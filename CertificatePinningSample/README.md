# Regula Face SDK (Android version)
Face SDK is a framework that is used for face matching, recognition, and liveness detection.
In this sample we demonstrate how configure Certificate Pinning in the Android app

## Steps

Here you can find how to generate key for the android app
https://nikunj-joshi.medium.com/ssl-pinning-increase-server-identity-trust-656a2fc7e22b

1. In the mobile app you need to create xml file (main/res/xml/network_security_config)
2. In the AndroidManifest.xml you need to set up config above in the application area:
   android:networkSecurityConfig="@xml/network_security_config"
3. In the network-security-config you need to replace 'domain' to your and replace 'SHA-256'
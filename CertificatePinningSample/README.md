# Regula Face SDK (Android version)

The Face SDK is a framework that is used for face matching, recognition, and liveness detection.

In this sample, we demonstrate how configure Certificate Pinning in the Android application.

## Steps

Here is a detailed instruction on how to generate a key for the Android app: 
https://docs.regulaforensics.com/develop/face-sdk/mobile/getting-started/web-service-setup/certificate-pinning/

1. In your mobile app, create an `.xml` file at the following location:
   `main/res/xml/network_security_config.xml`
2. In the `AndroidManifest.xml` file, set up the following config in the application area:
   android:networkSecurityConfig="@xml/network_security_config"
3. In `network_security_config.xml`, replace the placeholder 'domain' with your actual domain, and update the `SHA-256` certificate hash accordingly.

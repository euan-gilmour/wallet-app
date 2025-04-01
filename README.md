# Wallet App

This is an Android mobile wallet application intended for use with a Verifiable Credentials-based authentication protocol for Solid.

# How to Use

## Installation

### Setting Up Your Android Device

In order to use this software, you will need an Android device with access to a Trusted Execution Environment.

There are some steps you must take to prepare your device.

The exact procedure may differ depending on your exact device, and the operating system on your computer. You will like find [this guide](https://developer.android.com/studio/run/device) helpful.

Generally, you will need to:

- Enable developer settings and USB Debugging on your device
- Ensure you have the [Android SDK](https://developer.android.com/tools) and the [adb](https://developer.android.com/tools/adb) tool are installed on your computer
- Connect your android device to your computer via USB.
- Run `adb devices`
- If you see your Android device listed, USB debugging is working correctly.

Now you are ready to install and use the software.

### On Gradle

The simplest way to build, install and test the codebase is through the using provided Gradle wrapper (gradlew for Mac and Linux, gradlew.bat for Windows) on the command line.

If you're having trouble with Gradle, you may instead wish to build, test, and install the project via [Android Studio](https://developer.android.com/studio). This is quite a heavy Integrated Development Environment, but once installed and set up, it provides convenient graphical interfaces for running and testing the project.

This guide will not cover installing and setting up Android Studio in depth. You may wish to consult the official documentation if necessary.

### Building the Project

First, you may wish to check the project builds successfuly. You can do this by running `gradle build`.

If the build is successful, everything has been set up correctly. If not, it's possible you will see an error message about an incompatible version of Java. If so, you will have to update. It is recommended to use a distribution of JDK 23.

### Testing the Project

You can run the instrumented tests for the project on your connected Android device by running `gradle connectedAndroidTest`

You should see output indicating the device the tests are running on and how many tests are being run.

If the output is a success, this means that all the tests have passed. If it fails, you should see an error message indicating which test failed.

Note that if you try to run the tests on an Android device without access to Trusted Execution Environment, one test will always fail: namely, the test that keys are generated in the Trusted Execution Environment.

### Installing the project

If building and testingare successful, you can now install the application on your device for use. Run `gradle installDebug` to do this.

After successful installation, you should be able to find the application on your device under the name "Wallet App"

## Using the App

### Key Generation

The first thing you must do is generate a new key pair in the Trusted Execution Environment. Upon opening the app, you should see the Key Management tab. Press the "Generate Key" button.

You should see the bytes of the public key displayed. You should see that the key's security level is "Trusted Environment".

Congratulations, you have successfuly generates a key pair.

### DID Document Creation and Hosting

The next thing you must do is generate a DID Document based on the newly generated key pair and host it on the appropriate domain.

Looking at the DID Management tab, you should see a text field for entering the web domain you intend to use for your did.

If you do not have easy access to a web domain, hosting your DID Document on github is a good fallback.

Create a repo for hosting your DID Document.

Then, your DID will be given by:

raw.githubusercontent.com:<your_username>:<repo_name>:main

Enter the appropriate domain and press the "Create DID" button.

You should see your new DID Document displayed.

Press the "Copy DID Document" button and host the DID document at the appropriate web domain.

To check that it's hosted correctly, make and HTTPS request to (assuming you used github) https://raw.githubusercontent.com/<your_username>/<repo_name>/main/did.json

You should see your DID Document returned.

### Receiving a Credential

Look at the Credential tab. Go to the issuer web app, fill in your DID and your computer's local IP address and click "Create VC". You should see a QR code.

Press the "Scan VC Invitation" button and scan the QR code. You should see a confirmation prompt informing you of the issuer, recipient (which should be your DID), and type of credential that is being offered, along with the Web Sockets URL the transfer will take place on. Click proceed. You should receive the credential, and it should be displaed in the Credential Tab.

### Sending a Verifiable Presentation

Ensure that you have set up the Community Solid Server with a resource in your Pod whose access control policy is tied to your DID, the hardcoded issuer's DID (did:wed:raw.githubusercontent.com:euan-gilmour:dids:main:issuer), and my-demo-app.

Run the server with `npm run vc-protocol-server`. Run my-demo-app with `npm run start`.

On my-demo-app, fill in your DID and local IP address and make a request to the relevant Pod resource. You should see a QR code appear.

Press the "Scan VP Request" button on the wallet app and scan the QR code. You should see a confirmation prompt informing you of the domain, app name, and Web Sockets URL ivolved in the request. Proceed, and you should see a prompt for a biometric. In most cases this should be a fingerprint.

Scan your fingerprint, and the wallet app will create and send a Verifiable Presentation to my-demo-app. Upon reception of the VP, my-demo-app will make a new request to the Community Solid Server for the Pod resource with the VP.

The server will verify the presentation and credential contained within. If all are valid, it will grant the resource to my-demo-app, which will display it in the textarea for you to see.

Congratulations, you have successfully authenticated using a private key generated and stored within the Trusted Execution Environment of your device.

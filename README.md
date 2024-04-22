Object Detector Android App


Description:

The Object Detector Android App allows users to select an image from their device and detect objects within that image. The detected objects are outlined with bounding boxes and labeled with their corresponding confidence scores.


How to Use:

Launch the app on your Android device.
Click on the "Select Image" button.
Choose an image from your device's gallery.
Wait for the app to process the image.
The detected objects will be outlined with colored bounding boxes and labeled with their confidence scores.


Implementation Overview:

The app is developed using Kotlin programming language.
It utilizes the TensorFlow Lite library for object detection.
The model used for object detection is SsdMobilenetV1, which is a pre-trained model.
Images are processed using the TensorFlow Lite ImageProcessor.
Detected objects are outlined with bounding boxes and labeled with confidence scores using Canvas and Paint classes.
The app UI consists of a button to select an image and an ImageView to display the selected image with detected objects.


Files Overview:

MainActivity.kt: Contains the main implementation of the app, including UI setup, image selection, object detection, and rendering of results.
activity_main.xml: Defines the layout of the main activity, including buttons and image view.


Dependencies:

TensorFlow Lite Support Library
AndroidX Appcompat Library


Additional Notes:

The app currently supports detecting a variety of objects but can be further customized or extended by using different models or training on custom datasets.
Ensure proper permissions are granted to access images from the device's gallery.

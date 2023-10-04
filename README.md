# Plant Disease Detection Android App
## Overview

This Android app is designed to help users identify plant diseases using a deep learning model based on the Squeezenet architecture. The model, with an impressive accuracy of 97.53%, has been trained on a dataset from Kaggle, which contains a variety of plant images with associated disease labels.
<p align="center">
<img src="first ss.jpeg" alt="App Screenshots" width="200" height="400">
<img src="second ss.jpeg" alt="App Screenshots" width="200" height="400">
</p>

## Plant Diseases Detection

The app can detect the following 38 plant diseases:

1. Apple Scab
2. Apple Black Rot
3. Apple Cedar Apple Rust
4. Apple Healthy
5. Blueberry Healthy
6. Cherry (including sour) Powdery Mildew
7. Cherry (including sour) Healthy
8. Corn (maize) Cercospora Leaf Spot Gray Leaf Spot
9. Corn (maize) Common Rust
10. Corn (maize) Northern Leaf Blight
11. Corn (maize) Healthy
12. Grape Black Rot
13. Grape Esca (Black Measles)
14. Grape Leaf Blight (Isariopsis Leaf Spot)
15. Grape Healthy
16. Orange Haunglongbing (Citrus greening)
17. Peach Bacterial Spot
18. Peach Healthy
19. Pepper bell Bacterial Spot
20. Pepper bell Healthy
21. Potato Early Blight
22. Potato Late Blight
23. Potato Healthy
24. Raspberry Healthy
25. Soybean Healthy
26. Squash Powdery Mildew
27. Strawberry Leaf Scorch
28. Strawberry Healthy
29. Tomato Bacterial Spot
30. Tomato Early Blight
31. Tomato Healthy
32. Tomato Late Blight
33. Tomato Leaf Mold
34. Tomato Septoria Leaf Spot
35. Tomato Spider Mites Two-Spotted Spider mite
36. Tomato Target Spot
37. Tomato Mosaic Virus
38. Tomato Yellow Leaf Curl Virus

## Model Accuracy

The deep learning model used in this app has an accuracy rate of 97.53%. The model incorporates the Squeezenet model which has a model size of 2.87mb.


## Dataset

The plant disease dataset used for training the deep learning model can be found on Kaggle: [Plant Disease Dataset](https://www.kaggle.com/yourusername/plant-disease-dataset).

## Model

The deep learning model is based on the Squeezenet architecture, which has been fine-tuned for plant disease detection. The model weights and code for training can be found in the `model` directory of this repository.


## Usage

1. Open the app on your Android device.
2. Click on the start button to select an existing image from your gallery.
3. Upon clicking the required image, it will be displayed on th app screen.
4. Click predict and the predicted disease will be displayed on the app screen.


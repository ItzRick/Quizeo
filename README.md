# Quizeo

## Introduction
Quizeo is an interactive application that is meant to entertain and educate its users. Based on the user’s location, the application retrieves a set amount of quizzes that are available in the nearby area. Additionally, the content of each quiz is either directly or indirectly related to the user’s location. The content can deviate from a picture with plain text to just plain text, where each question consists of at least two multiple-choice options. This document contains the user manual of Quizeo. 

## How to install
There are two ways of playing Quizeo:
1. You need an apk file to install Quizeo, because the application is not available in the play store. In order for this to work, you have to allow apps from unknown sources. To make sure that your device permits apps from unknown sources you need to go to settings → security → turn on unknown sources.
2. For the second way, you will need either Android Studio or any IDE that gives the option to have a package for Android Studio (e.g. IntelliJ). After you download and open the project, you have to run it on an emulator. The application should start right after that.

## How to use
Right after the applicatioon starts, you will have to give access to the location services and then set your nickname. After this you are redirected to the main menu where you have 3 options - to play a quizzes, to make quizzes and the traditional options. 
The options is a pretty standard options menu that we often see in every app - you can control the sound or the color theme you want selected. There is also this important option to check if you want to play verified quizzes (it is very likely that there won't be any verified quizzes at the moment, so make sure to un-check this). Go back to the main menu.
When you click on the 'Start Quiz' button, you will get a selection of quizzes that were created in your location range. If there are not any, you either have no internet connection or nobody has submitted any quizzes (again, options -> play verified quizzes should be un-checked).
But you can make one and later play it and in this way provide playing options for the others as well. In the main menu, click on 'Make a quiz' and then a window with your quizzes will show. Normally, when you are reading this README, the team assumes that the user is opening the app for the first time, meaning that he has no quizzes created. To create one, click on 'New'. In the window that shows up, you need to add a title to the quiz, add a location, and the start adding questions. When adding questions, make sure you have at least 2 answers provided and also selected the correct one. When you are finished with adding at least 5 questions, submit the quiz and then return back to the main menu.
Try your quiz (start quiz -> your quiz). Based on how correctly you answered, you will see if you passed this quiz and you will be given a chance to rate it.
Why is rating a quiz important? Because it contributes to the overall score of the quiz and to the quality of the quizzes. If you wish to play quality quizzes, it would be nice to turn the 'Play verified quizzes' in 'Options' so you can see only the well-rated ones.

## Technologies used

Quizeo uses a the following technologies to work properly:

- [Java] - Whole project done on Java
- [Firebase] - The database used for storing the quizzes and users

## Limitations and future updates
Quizeo is very problematic-less for now. The only odd situation that might occur to the user is when the device is brand new and Google Maps has never been opened before. Then Quizeo won't be able to fetch the location and a popup will show. Don't worry, you just have to close Quizeo, open Google Maps until you see your location and then start Quizeo again. Everything should be fine now.



[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [firebase]: https://firebase.google.com/
   [java]: <https://www.java.com/en/>

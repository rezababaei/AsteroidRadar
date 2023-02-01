# Purpose of this Folder

This folder should contain the scaffolded project files to get a student started on their project. This repo will be added to the Classroom for students to use, so please do not have any solutions in this folder.

## Note: Android Kotlin Gradle Update
Use the updated Gradle version in the `~/gradle/wrapper/gradle-wrapper.properties` file:
```
distributionUrl = https\://services.gradle.org/distributions/gradle-6.1.1-all.zip
```

For this project you will use the following fields:

id (Not for displaying but for using in db)
absolute_magnitude
estimated_diameter_max (Kilometers)
is_potentially_hazardous_asteroid
close_approach_data -> relative_velocity -> kilometers_per_second
close_approach_data -> miss_distance -> astronomical

NASA image of the day
Finally, to make the app more look more interesting, we are going to display the NASA’s image of the day in Main Screen top, this is an simple image we can get from the next link: https://api.nasa.gov/planetary/apod?api_key=YOUR_API_KEY

This is also going to return a simple JSON object of which you just need the “url” key, example:

url: https://apod.nasa.gov/apod/image/2001/STSCI-H-p2006a-h-1024x614.jpg
media_type: The image of the day could be an image or a video, we are using only the image, to know what media type is you have to check the media_type field, if this value is “image” you are going to download and use the image, if it’s video you are going to ignore it.
title: The title of the picture, this is going to be used as content description of the image for Talk back.

PREVIOUS
NEXT

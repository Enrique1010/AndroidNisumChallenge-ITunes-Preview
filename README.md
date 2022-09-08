# AndroidNisumChallenge ITunes Preview App
App to consume the iTunes API to search and play songs by term. App made with Jetpack compose.

# Challenge Objectives

- Build song search engines using [ITunes Search Api](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/Searching.html#//apple_ref/doc/uid/TP40017632-CH5-SW1) ✓
- Show Results with pagination (not completed, explanation below)
- Select an item from the search and show the album to which it belongs, including: Arte de 100x100. Album name. Band name. Song list. ✓
- For the album, be able to consume and visualize the progress in the preview of the songs. ✓
- Store the searches carried out locally and offer to repeat them in case of not having an internet connection. ✓
- Handle 500 errors, "term not found", INET_CONN_ERROR (handle it using NetworkResponse and return and empty list instead). ✓
- Use animations and transitions between views (not completed, explanation below).



# Issues Found During the Challenge

- Content type in request is not application/JSON, but instead text/JavaScript.
- iTunes search API doesn't provide a page number for pagination.
- Jetpack compose doesn't provide transition animation control.
- No duration in the songs collected.

# Possible Solutions to Issues
- Retrofit can handle text/JavaScript, however, in emulators the call time is too slow. Solved by transforming the response into a string, then to a Kotlin object.
- Without page number in the API request, the only way to perform a pagination is increasing the limit when the end scroll is reached, that could be inefficient for the phone's performance (I implemented paging source without pagination as a solution).
- This can be solved by installing [ACCompanist Animation Navigation](https://google.github.io/accompanist/navigation-animation) which provides animated composables, but it's a beta and in some instances (as in this case), it doesn't work properly.
- The duration is handled by exoplayer, if you want to create a custom media player that can process the duration of the media object, exoplayer extracts the song's metadata, which contains the duration.


# Application Screenshots

![flow1](https://user-images.githubusercontent.com/42783065/189199478-39553be2-17ec-4673-a5cb-67910247d91e.jpeg)

![flow2](https://user-images.githubusercontent.com/42783065/189199564-b0cf819c-5d92-475d-81ec-4d82e7ec3ad7.jpeg)

![flow3](https://user-images.githubusercontent.com/42783065/189199578-d3119c1e-e5b3-4817-82da-98bcc24f8651.jpeg)

# No native libraries used

- [Truth](https://truth.dev/) for better assertions in testing.
- [Retrofit 2](https://square.github.io/retrofit/) to handle API calls easily.
- [Coil](https://coil-kt.github.io/coil/compose/) provides an AsyncImage component to handle images from URLs easily.

# Test Cases

- Testing Search Bar - tested search bar, working as expected.
- Testing Room database - tested all methods to read/write room database.
- Testing Loading Screen - tested if loading is displayed.

# Used Devices for testing

- One Plus 7T - Android 11 (Physical)
- Xiaomi Poco X3 Pro - Android 12 (Physical)
- Pixel XL - Android 10 (Emulator)
- Nexus 5 - Android 9 (Emulator)
- Pixel 4a - Android 11 (Emulator)

# Video App Demo

- [Application Video Demonstration](https://drive.google.com/file/d/15plr1-01Rkvbpn_q4qsGTZlsBhSDX9lB/view?usp=sharing)


## License

Open Source

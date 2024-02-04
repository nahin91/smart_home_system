# smart_home_system
This is an infotainment simulation system of Smart Home Use-Case exclusively developed in Java Springboot for the “Mobile Computing” course 
at the Frankfurt University of Applied Sciences. 

Here the IoT system receives song lists of multiple users measuring their distances from the sensor which afterward makes a new 
playlist assembling the songs based on their priority. The priority is based on the common songs among the users’ playlists. 
Later IoT triggers the music actuator to play the songs. In addition, if the sensor finds no one within the range, 
Iot clears the playlist. Lastly, the updated playlist, connected users, and the current song playing in the actuator are monitored on the browser. 

Technology used:
1. Java
2. Springboot
3. Thymeleaf
4. CoAP for IoT Gateway
5. Wireshark for packet tracing.


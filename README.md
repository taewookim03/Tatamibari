# Tatamibari

A pen and paper NP-complete logic puzzle game implemented digitally - playable on Desktop, web, Android and iOS. 

To play:
https://taewookim03.github.io/tatamibari-web/

More info/rules:

http://www.janko.at/Raetsel/Nikoli/Tatamibari.htm

https://en.wikipedia.org/wiki/Tatamibari

Solving algorithm:

I have implemented an optimized backtracking algorithm (a la common sudoku solving algorithm) to solve any instance of a Tatamibari puzzle. It is presented and takes input like a leetcode-style problem - try it out! Problem and solution available [here](https://github.com/taewookim03/Algorithms/blob/master/src/Other/tatamibari_solver.java)

## Getting Started

This project was programmed in Java using libgdx framework. To run it, you should set up your libgdx environment using gradle as detailed on libgdx wiki.

https://github.com/libgdx/libgdx/wiki/Project-Setup-Gradle

The project can be deployed on Desktop (Windows, Mac, Linux), web (HTML/JS), iOS, and Android. As of 1/30/2017 it has not been tested on iOS or Mac, but other platforms should work.

https://github.com/libgdx/libgdx/wiki/Deploying-your-application

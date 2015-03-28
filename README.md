SuperNova
===============================

SuperNova is a so called vertical scroller. 

In fact this is a proof-of concept for a so called vertical scroller.
It may act as a starting point for a simple game. As a popular example
you might consider Xenon II - Megablast, Raptor or similar games.

This game is written entirely in JAVA and uses the LWJGL library as a base.

This would not have been really necessary, since this is a 2D-Game, 
but OpenGL simplifies some things in a manner I like, texture 
handling and transformation stuff for example.

This package is not meant to be a complete game at the moment nor is the
code highly sophisticated. It just demonstrates one of many ways how such 
a game could be programmed.

Usage
=================================
This is an Eclipse project, you can simply import it into your workspace.
The main class is SuperNova.java located inside the package de.pueski.supernova 
There you'll find the heart of the game. Also this is the main class to be run 
from within Eclipse.

Build
=================================
Use ant to build, simply invoke 

ant dist

to build the distribution. You'll find the zipped distribution inside the dist 
directory.  

 


  
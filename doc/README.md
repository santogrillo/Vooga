# voogasalad

### Contributors 

* Adithya Raghunathan (ra102@duke.edu)
* Ben Schwennesen (bas65@duke.edu) 
* Ben Welton (bw144@duke.edu)
* Matthew Mosca (mjm141@duke.edu)
* Matthew O'Boyle (mo121@duke.edu)
* Michael Scruggs (mts40@duke.edu)
* Santo Grillo (sdg12@duke.edu)
* Tyler Yam (tjy8@duke.edu)
* Venkat Subramaniam (vms23@duke.edu)
 

### Timeline

The project began in earnest on 9 November, 2017, when we wrote our team contract, and completed on 15 December, 2017. We estimate that, on *average*, each team member spent 120 hours on the project (with a wide variation range among individual contributors).

### Roles 

* Adithya Raghunathan: My role was designer and implementer of the backend (game engine), working closely with Ben. For the first sprint, I worked mainly on the controllers mediating front end access to back end data / logic and I/O resources, as well as the general design of the back end game loop and part of the behavior package, specifically the collision strategies. For the second sprint, in addition to maintaining, refactoring and extending the controllers' APIs in response to feedback from its front end clients, I also worked on firing strategies (part of the behavior package) with Tyler. Finally, I implemented both the networked multi-player and collaborative authoring extensions by building the networking package (with the exception of the Chat utilities built by Ben) and supporting its front end integration with Matthew Mosca's help.
* Ben Schwennesen: overall my role was designer and implementer of the backend (game engine), working closesly with Adi. My primary role during the first sprint was engine API design and working on the game actors, specifically their construction, the passing and receiving of the properties that was needed for them in the frontend, coordination of their behavior, and saving to easily edited files; I also worked on controllers (which featured heavily in the API design), serialization, behavior objects, and the game loop with other group members during the first sprint. Finally, I worked on a chat window and server during this sprint. In addition to expanding my work in these during the second sprint, I worked on element upgrading, exporting games to JAR files and pushing them to Google Drive, levels and waves, integrating the paths defined in the frontend into the game engine, some GUI simplification by moving buttons into a dropdown menu (among others), and finally many, many bug fixes.
* Ben Welton: My general role was designing the front end interfaces for the game areas of authoring and player where the game was actually built or played. This initially involved building the path structure and the scrollable game area in the authoring environment, but eventually bled into the left toolbars in player and authoring where drag and drop took place, the tab structure used in many of the toolbars, the HUD display in player, and a little bit of coding in the properties tool bar, though this last piece was covered most by Matt O'Boyle. I also worked with adding in integration with the back end controllers in player and authoring, particularly on making sure that sprites loaded properly back into the tool bars and into the game areas.  
* Matthew Mosca: In general, my role entailed working with the overall design and appearance of the frontend/user interface of the authoring and playing environments. I was involved in designing the initial layout of toolbars and the game area in authoring and implementing the early code to set up their tabs. I designed and added almost all of the images used in the project. I created a system to allow the user to very easily choose one of preset themes I designed for the authoring environment, and generally handled styling of the front end with CSS. In the player environment, I implemented classes to flexibly handle and display value trackers, for components like health, points, and currency. I remodeled the opening splash screen and background, and made its elements flexible to resizing (by simply changing constants), and created the win and lose screens. Finally, I created the multiplayer environment and worked closely with Adi to link it to the networking back end he worked on. 
* Matthew O'Boyle: I mostly worked with the Authoring Environment and worked with front end interfaces for this. This involved the creation of levels and waves, placing static and background objects, setting the properties for different sprites and customizing the user interface. I also worked with the link between front and back-end (Play/Edit Display to Player/Authoring Controller) and the link between the different screens for the user (SplashScreen, PlayDisplay, EditDisplay etc.). 
* Michael Scruggs: My role was creating and refactoring Strategies for GameElements and how they communicated with the ElementManager in the game engine. I worked closely with Adi throughout the project. For the first sprint, I mainly worked within the Movement Strategies. Durring the second sprint, I continued my work in the movement strategy, but expanded into other strategies. When working with other strategies, I mostly refactored code, while also creating new featuers, such as the creation of explosion. I fixed issues with Sprite querying and expanded upon the querying method. 
* Santo Grillo:
* Tyler Yam: I worked on firing strategies as well as handling Media and sound effects. I also worked with Venkat on placing all Strings into a resources properties to aid with translation. I also helped Ben S. create a Main class as well as a splash screen for the packager to run from. I also worked on an alert factory and general refactoring.
* Venkat Subramaniam:
 

### Resources Used 

See [src/README.md](src/README.md) for some image credits; all other images in the program were generated by contributors. Some references are also included in code documentation for sites used in developing some resources.


### Starting the Project

Use [src/main/Main.java](src/main/Main.java) to launch the project (the exported JAR uses the other class in [src/main](src/main).


### Testing 

The [test](/test) directory contains test code for the project. Error the project should handle without crashing: 
* Not setting all the fields in a game element definition.
* Failure to export the project in full.
* 


### Data

The program uses a few data files for saving. All of these—excluding exported game files which are included in JARs in (data/games/)[data/games] (note: see known bugs)—are included in the [authoring](/authoring) directory. Files included: 
* [.voog files](authoring/BasicGame.voog): these JSON files contain high level information about the game, such as its name, description, victory conditions, and genre specific information like resources and health-per-level, unit costs, etc.
* [Element template files](authoring/sprite-templates/BasicGame/): these properties file specify the properties chosen for each game element (actor, or even background object) defined in the authroing environmnet, such as speed, collision effects, image dimensions, health, etc. These can be easily edited by people without code knowledge.
* [Wave files](authoring/waves/BasicGame/): these define the types of waves included in each level (other information about the waves themselves are included in element template files) and their spawn location. Again, these are easily edited to move the spawn location or exclude a wave from a level.
* [Path serializations](authoring/serializations/): these files were used to persist the paths created during authoring; they are not human readable because, due to a need for circular referenced in supporting circular paths, XML/JSON serialization libraries struggled to handle them, so Java's native serialization functionality was utilized.
 

### Using the Program

When adding troops to a wave, the user should input in the format <level>.<wave>, then enter a number of troops they wish to commit. The spawning location should be in the form <xLocation>,<yLocation> and will default to (100,100)

Special key presses: 
* To move an object in authoring you must select the button in the top-center with the four arrows pointing in the cardinal directions. Right clicking on an object while in this control scheme will delete it.
* Path Controls:
	* Left Click (Game Area) - Places a new waypoint. This waypoint will connect to the current active point (shown in bright blue) or start a new path if no point is selected
	* Left Click (Point) - Toggles the selected point between active and inactive. Only one point can be active at a time
	* Right CLick (Point) - Deletes the current point. All previous points will create connections to all subsequent points.
	* Ctrl + Click (Point) - Creates a connection between the active point and the selected point, functionally creating a closed loop
	* Left Click (Line) - Toggles the selected line between active and inactive.
	* Right Click (Line) - Deletes the current line. Must be active to delete.
	* Ctrl + Click (Line) - Reverses the direction of this connection
* Toolbar Controls (Player and Authoring):
	* Left Click - Selects an object to place. Left click again to place it into the game environment.
	* Right Click (Inventory Toolbar/Left Side) - Gives option to increment sizes
	* Right Click (Properties Toolbar/Right Side) - Deletes the object from the inventory that the player would see
* Additional Player Controls:
	* Left Click (Sprite) - In some game modes, shifts the active element to be the clicked object
	* Right Click (Sprite) - Deletes the sprite, only works on user created sprites
	* Ctrl + Click (Sprite) - Upgrades the sprite, only works on user created sprites


### Assumptions and Simplifications

* A major assumption we made was that the user would always save their data to the same location. This simplified loading for us, but should have been made more clear by simply asking for a save name for the user instead of opening a file chooser when they tried to save.
* In order to get multiplayer and collaborative editing working without paying for a domain, we assumed that authors and players would be on the same network. (We mostly used localhost for using these features but had a Duke VM server prepared for this, which we didn't end up using because the server code changed so often.)  Changing this to use a server which could host users from arbitrary networks would be a simple matter as well.

### Known Bugs 

* Due to last minute changes that used resource bundles in various unexpected ways as well as the inclusion of media such as audio clips (which cannot be loaded as streams), the exported JAR files currently do not run correctly. At the time this feature was written (when it was shipped as our utility), it worked perfectly. 

### Extra Features

* Exporting (buggy due to last minute changes to IO)
* Collaborative editing
* Multiplayer
* Change of language


### Impressions

We all agreed that this project would be much more enjoyable and would end up being much better if one of the middle projects (cell society or SLogo) were eliminated or shortened (e.g., only one implementation sprint for cell society and SLogo) to make the timeline for completion more realistic for a full-fledged game authoring environment. (At the moment, it seems like the teams that have more people in less difficult classes would end up with much better products than the teams whose members have very full schedules already.)
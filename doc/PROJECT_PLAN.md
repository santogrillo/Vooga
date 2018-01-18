Project Plan
============

#### Matt Mosca

In terms of modules, I will be responsible for the graphical representation of towers, projectiles, and certain aspects of attacking and collisions. I may also be responsible for working with certain factories.

In terms of use cases, I will be responsible for at least parts of the following:
* Creating a new firing element with a custom projectile appearance
* Customizing the attack rate/frequency of a new element
* Customizing the per-attack damage of a new element
* Customizing the maximum attack range of a new element
* Customizing the attack sound of an element
* Modifying any of the above properties for an instance of a previously defined element type
* Specifying the collision strategy of a projectile from a finite set of choices
* Specifying the collision animation of an object

Customizing the attack sound of an element and specifying the collision animation of an object are both use cases that will be fulfilled in the latter part of the project, perhaps in the second sprint, as they are not critical to the game’s functionality.


#### Adithya Raghunathan

In terms of modules, I will be taking primary responsibility for the Behavior module and I/O module for the first sprint and the Networking Engine module for the second sprint (extension). I will also be taking secondary responsibility for the Interaction Engine module in the second sprint. 

In terms of use cases, I will be responsible for at least parts of the following:

* Place towers or elements/resources on any non-boundary section of the map during the game (behavioral aspect)
* Move any mobile player-controlled unit towards a grid cell
* Fire (special) projectiles from the units / towers upon demand
* Process a collision between a damage-taking object and a projectile
* Enable a special ability (power-up)
* Display the game status in a heads up display (score, towers remaining, quantities of resources available, etc.)
* Pause the game, causing a pause screen to be displayed
* Allowing shared editing of a game (extension / challenge)
* Allowing multiple players to play a game online (extension / challenge)


#### Ben Welton

In terms of modules, I will primarily be responsible for the Game Area that maps the objects to locations and gives the users cells to interact with. I will also be working with the Edit Display module so that I ensure that objects can be dragged into the game area.

In terms of use cases, I will be responsible for at least parts of the following:
* Setting a background image for the map of (a given level of) a game
* Creating a new element type with a custom appearance (image)
* Creating a new firing element (tower, soldier, monster, etc.) with a custom projectile appearance (image)
* Placing arbitrary numbers of previously created / defined elements on various positions of the map by dragging-and-dropping from a panel onto the map
* Removing a previously placed element
* Cordoning off certain parts of the map as boundaries
* Specifying the movement strategy of an element (random, through a series of specified waypoints, or towards tower / enemy)


#### Michael Scruggs

In terms of modules, I will primarily be responsible for the  engine representation of objects and the pathfinding of those objects. My main focus will be on the implementation of projectiles into the engine. I will also be working on the representation of the playing field and how objects will be able to interact within the grid.

In terms of test cases, I will be responsible for at least parts of the following:
* Place towers or elements/resources on any non-boundary section of the map 
* Game area aspect
* Move any mobile player-controlled unit towards a grid cell
* Fire (special) projectiles from the units / towers upon demand
* Process a collision between a damage-taking object and a projectile
* Handling collisions between objects and barriers
* Handling projectiles with a AOE effect


#### Ben Schwennesen
 
The primary modules I will be responsible for will be the packing module, interaction engine, and certain aspects of the object module, including and especially object factories. My secondary responsibilities will include other parts of the object module as well as the networking and behavior modules. During the second sprint, I will work on other challenging extensions, such as a social center (which will be a submodule of the networking module) and dynamic game content (which will be a submodule of the object module, being controlled by a engine-controlled factory that is used for in-game resource placement). 

In terms of use cases, I will be responsible for at least parts of the following:
* Creating a new element type with a custom appearance/image (specifically creation of the element via authoring factory)
* Creating a new firing element (tower, soldier, monster, etc.) with a custom projectile appearance/image (specifically creation of the element via authoring factory)
* Placing arbitrary numbers of previously created / defined elements on various positions of the map by dragging-and-dropping from a panel onto the map (specifically creation of the element via authoring factory)
* Place towers or elements/resources on any non-boundary section of the map during the game (specifically creation of the element via in-game factory)
* Fire (special) projectiles from the units / towers upon demand (interaction aspect as well as generation via factory)
* Enable a special ability (power-up)
* Exporting a fully built game with all levels to an executable file
* Launching a (level of a) game that’s currently being built so as to allow for testing of gameplay during the editing process without requiring a full export (related to packaging/exporting)
* Specifying the keys for shooting, hotkeys for selecting a particular unit, etc (backend logic aspect)



#### Tyler Yam

In terms of modules, I will mainly be working Packaging as I am in charge of Game Data. My role will be to work with both the Game Authoring and Game Engine teams to provide a set format for the data they will both need to access. As the Game Authoring and the Game Engine will have to be independent of one another, they will communicate indirectly through the data files. These data files will be in the form of JSON. 

In terms of Use Cases, I will be in charge of the following:
* Allow for blank data entries for partially built games to be added onto later
* Allow for missing data entries to be filled with default settings if user elects to do so
* Allow for incorrect data entries to be replaced by default valid entries, while alerting user of error
* 


#### Matthew O'Boyle

In terms of modules, I will mainly be responsible for the general upkeeping of the game and representing the visuals for the objects that we create on the screen for the authoring environment.

In terms of Use Cases, I will be in charge of the following:
* Creating the general authoring view that will be the basis for the project
* Creating a general splash screen that allows the user to decide if they wish to edit or play the game, and an easy interface to navigate between these
* Creating a user-updatable background and images the user can select for each object
* Presenting a clear way for the user to define properties for objects (towers, soldiers etc.)
* Presenting a clear way for the user to define the objectives of the game
* Placing customizable objects into the editor through drag/drop method
* Creating a dynamically-updated tab for the newly-created objects
* Animating the attack hits, tower firing, soldiers destroyed etc.
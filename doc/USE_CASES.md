Use Cases 
=========

### Game Authoring 
1. Setting a background image for the map of (a given level of) a game
2. Creating a new element type with a custom appearance (image)
3. Creating a new firing element (tower, soldier, monster, etc.) with a custom projectile appearance (image)
4. Customizing the attack rate / frequency of a new element
5. Customizing the per-attack damage of a new element
6. Customizing the maximum attack range of a new element
7. Customizing the attack sound of an element
8. Placing arbitrary numbers of previously created / defined elements on various positions of the map by dragging-and-dropping from a panel onto the map
9. Modifying any of the above properties (display image, attack rate, per-attack damage, attack sound, etc.) for an instance of a previously defined element type
10. Removing a previously placed element
11. Cordoning off certain parts of the map as boundaries
12. Specifying the movement strategy of an element (random, through a series of specified waypoints, or towards tower / enemy)
13. Specifying the shooting strategy of a tower (at nearest enemy, at lowest-hp enemy, random, etc)
14. Specifying the projectile strategy for a firing element's projectile (straight towards original target, 'homing' missile that intelligently curves to follow enemy, etc)
15. Specifying the collision strategy of a projectile from a set of finite choices (cause fixed damage, instant kill, pass through obstacles, etc.)
16. Specifying the collision animation of an object (or between two specific object)
17. Specifying the destruction animation of an object
18. Specifying the keys for shooting, hotkeys for selecting a particular unit, etc
19. Specifying the sequence of succession of levels
20. Launching a (level of a) game that’s currently being built so as to allow for testing of gameplay during the editing process without requiring a full export 
21. Saving a (partially) built level for future loading
22. Autosave a (partially) built level for future loading
23. Loading a (partially) built level for continued building
24. Ask the user if they want to save when exit is pressed
25. Exporting a fully built game with all levels to an executable file
 

### Game Playing
26. Choose from a list of created games
27. Place towers or elements/resources on any non-boundary section of the map
28. Move any mobile player-controlled unit towards a grid cell
29. Fire (special) projectiles from the units / towers upon demand
30. Process a collision between a damage-taking object and a projectile
31. Enable a special ability (power-up)
32. Display the game status in a heads up display (score, towers remaining, quantities of resources available, etc.)
33. Pause the game, causing a pause screen to be displayed
34. Tracking high score for particular game until cleared
35. Replay game after winning / losing
36. Switch to a different game after winning / losing
37. Switch to a different game mid-game (defaults to losing current game)
38. Saving upon reaching a new level / checkpoint (possibly using autosave)
39. Saving at any arbitrary point within a level
40. Loading a game state

### Game Data
41. Allow for blank data entries for partially built games to be added onto later
42. Allow for missing data entries to be filled with default settings if user elects to do so
43. Allow for incorrect data entries to be replaced by default valid entries, while alerting user of error
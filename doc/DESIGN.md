# Design 

### Design Goals

Our main design goal for the program was flexibility in the type of game being implemented. This included making sure the user could create many different types of game agents, have infinite levels, and expand extensible to multiple genres. We wanted to make sure that the data files our program used would be easily configured by non-programmers. We also wanted to make sure that the program would be robust to bad user input. Finally, we wanted the communication between frontend and backend modules to be as abstracted and simple as possible, so as to allow for collaborative editing and multiplayer extensions. 


### Adding new features

* To add new types of game elements, one would need to define new collision, firing, and/or behavior objects for them to use. This would entail implementing the interfaces defined for these behaviors (abstract classes between the interfaces and concrete classes are there and could be used to reduce the effort of making the new object, if applicable). 
    
    - Caveat: The behavior object would need to include the [game element property annotation](src/engine/game_elements/ElementProperty.java) for each element specified in its constructor. For more complex types of objects which include circular references, note that there will need to be [GSON type adapters](https://google.github.io/gson/apidocs/com/google/gson/TypeAdapter.html) included in the [serialization utils'](src/utils/io/SerializationUtils.java) GSON handler (i.e., via the [registerTypeAdapter() method](https://google.github.io/gson/apidocs/com/google/gson/GsonBuilder.html#registerTypeAdapter-java.lang.reflect.Type-java.lang.Object-)). Finally, maps and other collections will either need their own type adapters or would need to only use strings (which would be unpacked at construction time into the desired type, e.g., double), which would allow GSON to handle them automatically. 
* 


### Design choices 

* Grid based vs. precise coordinate based movement: the tradeoff here was that using a grid-based system would make a lot of sense for tower-defense and would simplify a number of things (the path troops follow and collision checking, for example), while a coordinate based system would be more easily extended to other types of games.
* Extending game element vs. using a composition of behavior objects: this was a question of composition vs. inheritance. We could have defined a basic element with abstract move(), fire(), handleCollison() methods and extended this for each type of element we wanted, or we could have a single game element that has different behavior objects for movement, firing, and collisions. The benefits of the former approach were that it would have made it easier to get a running game very quickly. The benefits of the latter, however, far outweighed this: encapsulation of responsibilities made it easier to identify bugs, understand the game element (without necessarily looking at each part of it and the behavior objects' code), and extend to new types of object. 


### Assumptions 

* A major assumption we made was that the user would always save their data to the same location. This simplified loading for us, but should have been made more clear by simply asking for a save name for the user instead of opening a file chooser when they tried to save.
* In order to get multiplayer and collaborative editing working without paying for a domain, we assumed that authors and players would be on the same network. (We mostly used localhost for using these features but had a Duke VM server prepared for this, which we didn't end up using because the server code changed so often.)  Changing this to use a server which could host users from arbitrary networks would be a simple matter as well.

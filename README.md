# AirHockey

This project is currently incomplete, I'm open sourcing and sharing with the community as a experiment. 
People who want to have a game to work on but don't know how to get started and want exposure to modern
tools can use this as a scratching pole.

This project is provided as is. Upon initial import it is incomplete. The current state is as follows.

- You can run a server
- You can run clients and connect to the server
- You can control your paddles on the secondary screen
- The basics of the model are done and basic collision detection implemented 
- There is a client/server android apks, as well as a secondary "test driver" I was using for automated testing.

Technology used

- Android
- RxJava
- ZeroMQ
- Retrolambda

Some user stories to get started (However, if you want to do it, I'm open to it)

- As a user I would like to see a optional info overlay with client/ip info to start.
- As a user I'd like some game logic (Scores, goal points etc)
- As a user I'd like to have a realistic physics engine for the game
- As a user I'd like the aspect ratio of the game fixed regardless of client/server display size
- As a user I'd like network discovery
- As a user I'd like this to work over the internet (You can use MySaasa server as a connection broker, matchmaking service)
- As a developer I'd like a integration test suite
- As a developer I'd like a unit test suite

How to contribute.
- Request collaborator access to ( adamhammer2 (at) gmail (you know the rest) )
- I'll grant access and you can do whatever you want.
- However, please create issues in github for work you intend to do, so their is visibility to others.

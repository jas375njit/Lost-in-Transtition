How to play Lost In Transition

Lost In Transition is a multiplayer word game played over a network. Players will receive a word, describe it and pass their description to another player. That description then needs to be guessed by others. The more rounds played the more "lost" the original word will be.

How to play:
1. Start by running the server with the GameServer.java file
2. Each Player then run LostInTransition, the first player will be asked how many players will be playing and then that amount of players will allowed to play.
3. Round 1: Each player gets a word and writes a description
4. Round 2: Descriptions are rotated. Every player guess the original word.
5. Final Scores are revealed and a winner is announced.

Files:
GameServer.java - Runs the server in port 1728.
GameRoom.java - Handles the actual game (the socket between the player and the server).
LostInTransition.java - GUI client, each player runs to connect and play.
WinningSelection.java - Scoring system using the Datamuse API.

Requirements:
Internet Connection (for Datamuse API scores)
All players must be connected the same network or localhost.

Notes:
Number of players must be even.
The server runs on port 1728.
If a player disconnects mid-game, the game ends.

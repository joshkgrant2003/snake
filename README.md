# Snake Game

## Getting Started

This project was built with *no build tools* such as Maven or Gradle. So to run this game, you must ensure that the paths in the `launch.json` and `settings.json` file are correct on your machine.
    - Install the `javafx` SDK [here](https://gluonhq.com/products/javafx/)
    - Replace the `<your-path-to>` line with *your* path to your installation of `javafx`
        - This project was build using **`javafx` version 23.0.1**. If you install a different version of `javafx`, be sure to also change the version number in the installation folder appropriately

## Running the Game

Open the `App.java` file or any file in the `src/game` folder and click **run program**. A start menu should render that will allow you to begin playing. You can select the **"Play"** button to begin playing snake. 

#### Game Rules

The game will start as soon as you press any *arrow key*. Use the arrow keys to go around and collect the *food* around the grid. Be careful not to run into the border or a segment of your snake, or it's **Game Over**!

After the game is over, a **"Enter Username"** box will appear. Enter your username to save your score to the **"Leaderboard"** screen, or exit out of the tab to opt out. 

You can also click **"Play Again"** to play again after a game ends. If you want to terminate the program, simply click **"Exit"**, or press the red *X* button in the top right of the GUI.

#### Configuration

This game allows you to use the **"Settings"** menu to change some of the rules for the game. These rules include:
    - Changing the snake speed
    - Changing board size
    - Editing snake color
    - Editing food color
Simply click **"Apply"** to save your changes, or click **"Cancel"** to exit without saving.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

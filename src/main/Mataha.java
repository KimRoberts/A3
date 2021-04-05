package main;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

/**
 * main.Mataha is the main game class that runs the game itself.
 *
 * @author G38.dev
 * @version 1
 */
public class Mataha {


    private static Game game;
    private static Scene mainGame;
    private Stage gameStage = new Stage();
    public static String texturePack = "road";


    public Mataha() {
        showGameStage();
    }

    public static Scene getScene() {
        return mainGame;
    }


    /**
     * Method creates and shows the main game window where the game will be played.
     */
    public void showGameStage() {
        BorderPane mainLayout = new BorderPane();
        mainGame = new Scene(mainLayout, 900, 750);
        mainGame.getStylesheets().add("src/resources/StyleSheets/MaTaHa.css");
        game = new Game(mainLayout);
        gameStage.setScene(mainGame);


        //Setting up main menu music.
        MainMenu.mediaPlayer.stop();
        Media musicPlayer = new Media(new File("src/resources/GameMusic.mp3").toURI().toString());
        MainMenu.mediaPlayer = new MediaPlayer(musicPlayer);
        MainMenu.mediaPlayer.setAutoPlay(true);
        MainMenu.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        MainMenu.mediaPlayer.setVolume(0.1);

        gameStage.setTitle(Utils.translate("play now!", MainMenu.getLang()));
        gameStage.setResizable(MainMenu.getResizable());
        gameStage.getIcons().add(new Image("src/resources/Images/menu_images/icon2.png"));
        gameStage.show();
    }

    /**
     * @return the current game instance
     */
    public static Game getGame() {
        return game;
    }


}
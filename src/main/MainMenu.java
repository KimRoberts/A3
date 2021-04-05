package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.level_editor_components.Map;
import main.stage.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * MainMenu is the main class that initially runs the main menu.
 *
 * @author G38.dev
 * @version 2
 */
public class MainMenu extends Application {

    private static VBox root;
    private Scene mainMenu;
    private static String selectedLang = "english";
    private static final int WIDTH = 850;
    private static final int HEIGHT = 850;
    private static final boolean IS_RESIZABLE = true;
    public static MediaPlayer mediaPlayer = null;
    private static double volume;

    /**
     * Method creates the main menu window.
     *
     * @param mainMenuStage The starting stage of the game.
     * @throws Exception Throws when error is found.
     */
    @Override
    public void start(Stage mainMenuStage) throws Exception {
        FileReader.readFile("src/resources/LevelFiles/test.txt");


        Slider volSlider = new Slider(0, 100, 0);

        volSlider.setMajorTickUnit(10.0);
        TitledPane titledPane = new TitledPane("Change volume", volSlider);
        titledPane.setCollapsible(false);
        titledPane.setContent(volSlider);
        volSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volume = Math.round(newValue.intValue() * 10.0) / 1000.0;
                System.out.println(volume);
                mediaPlayer.setVolume(volume);
            }
        });


        titledPane.setAlignment(Pos.BOTTOM_CENTER);
        mainMenuStage.setTitle("Mataha");

        final ImageView selectedImage = new ImageView(new Image("src/resources/Images/menu_images//logo.gif"));
        selectedImage.setFitWidth(285);
        selectedImage.setFitHeight(285);

        VBox layout1 = new VBox(10);
        layout1.setAlignment(Pos.CENTER);

        Button play = new Button("Play");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NewGameStage newGame = new NewGameStage();
                newGame.showNewGameStage();
                mainMenuStage.close();
            }
        });
        final Image playBttn = new Image(new FileInputStream("src/resources/Images/menu_images//play.gif"), 50, 50, true, false);
        ImageView playImg = new ImageView(playBttn);
        play.setGraphic(playImg);
        play.setMaxSize(250, 150);


        Button loadGame = new Button("Load Game");
        loadGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LoadGameStage loadGame = new LoadGameStage();
                try {
                    loadGame.showLoadGameStage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mainMenuStage.close();
            }
        });
        InputStream is;
        final Image loadbttn = new Image(new FileInputStream("src/resources/Images/menu_images//floppy.gif"), 50, 50, true, true);
        ImageView floppyload = new ImageView(loadbttn);
        loadGame.setGraphic(floppyload);
        loadGame.setMaxSize(250, 150);


        Button levelEditor = new Button("Level Editor");
        levelEditor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LevelEditorStage levelEditor = new LevelEditorStage();
                levelEditor.showLevelEditorStage();
                mainMenuStage.close();
            }
        });
        final Image editorBttn = new Image(new FileInputStream("src/resources/Images/menu_images/level_editor.gif"), 50, 50, true, true);
        ImageView pencilload = new ImageView(editorBttn);
        levelEditor.setGraphic(pencilload);
        levelEditor.setMaxSize(250, 150);


        Button newProfile = new Button("Profile");
        newProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NewProfileStage newProfile = new NewProfileStage();
                try {
                    newProfile.showNewProfileStage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mainMenuStage.close();
            }
        });
        newProfile.setMaxSize(250, 150);


        Button highScores = new Button("Leaderboard");
        highScores.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LeaderboardStage leaderboard = new LeaderboardStage();
                leaderboard.showLeaderboardStage();
                mainMenuStage.close();
            }
        });
        highScores.setMaxSize(250, 150);

        Button createMap = new Button("Create Texture Pack");
        createMap.setOnMouseClicked(e -> {
            CreateTexturePackStage createTexturePackStage = new CreateTexturePackStage();
            createTexturePackStage.show();
            mainMenuStage.close();
        });

        //Language select drop down
        ComboBox langDropDown = new ComboBox();
        for (String lang : Utils.getLanguages()) {
            langDropDown.getItems().add(lang);
        }
        play.setText(Utils.translate("play", selectedLang));
        loadGame.setText(Utils.translate("Load Game", selectedLang));
        newProfile.setText(Utils.translate("New Profile", selectedLang));
        highScores.setText(Utils.translate("Leaderboard", selectedLang));
        levelEditor.setText(Utils.translate("Level Editor", selectedLang));
        titledPane.setText(Utils.translate("Change volume", selectedLang));
        langDropDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedLang = (String) langDropDown.getValue();
                play.setText(Utils.translate("play", selectedLang));
                loadGame.setText(Utils.translate("Load Game", selectedLang));
                newProfile.setText(Utils.translate("New Profile", selectedLang));
                highScores.setText(Utils.translate("Leaderboard", selectedLang));
                levelEditor.setText(Utils.translate("Level Editor", selectedLang));
                titledPane.setText(Utils.translate("Change volume", selectedLang));
            }
        });
        langDropDown.getSelectionModel().select(selectedLang);
        langDropDown.setMaxSize(250, 150);

        if (mediaPlayer == null) {
            Media musicPlayer = new Media(new File("src/resources/MenuMusic.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(musicPlayer);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(volume);
        }

        Text messageOfDay = new Text(Utils.messageOfDay());
        messageOfDay.setWrappingWidth(WIDTH * 0.7);
        messageOfDay.setTextAlignment(TextAlignment.CENTER);

        layout1.getChildren().addAll(titledPane, selectedImage, play, loadGame, levelEditor, newProfile, highScores, createMap, langDropDown, messageOfDay);
        mainMenu = new Scene(layout1, WIDTH, HEIGHT);


        // mainGame
        // this line is at the end the root will be the LAYOUT METHOD  Hope this helps :) also if you click play it'll come here
        mainMenu.getStylesheets().add("src/resources/StyleSheets/MaTaHa.css");
        mainMenuStage.setScene(mainMenu);
        mainMenuStage.setResizable(IS_RESIZABLE);
        mainMenuStage.getIcons().add(new Image("src/resources/Images/menu_images/icon2.png"));
        mainMenuStage.show();

    }

    /**
     * Method returns the selected language
     *
     * @return selected language of type String
     */
    public static String getLang() {
        return selectedLang;
    }

    /**
     * Method returns the window pixel width.
     *
     * @return window width of type int.
     */
    public static int getWidth() {
        return WIDTH;
    }

    /**
     * Method returns the window pixel height.
     *
     * @return window height of type int.
     */
    public static int getHeight() {
        return HEIGHT;
    }

    /**
     * Method returns if the the window should be resizable or not.
     *
     * @return window resizable option of type boolean.
     */
    public static boolean getResizable() {
        return IS_RESIZABLE;
    }

    /**
     * Main method starts the program.
     *
     * @param args arguments supplied command-line.
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println(Map.getSerialised());

    }


}
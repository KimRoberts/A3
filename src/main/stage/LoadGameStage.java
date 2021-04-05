package main.stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * LoadGameStage allows the user to load a game
 *
 * @author G38.dev
 * @version 3
 */
public class LoadGameStage {

    private Stage loadGameStage = new Stage();
    private Scene loadGame;
    public static String saveFileUrl;

    /**
     * Method loads any level files
     */
    public void showLoadGameStage() throws FileNotFoundException {

        Label label2 = new Label(Utils.translate("Load Game", MainMenu.getLang()) + "\n");
        label2.setId("title");
        // label2.setAlignment(Pos.CENTER);

        Label warnings = new Label("");

        final ComboBox saveComboBox = new ComboBox();
        saveComboBox.setPromptText("File Name");
        File directoryPath = new File("src/resources/SaveFiles");
        String contents[] = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String chosenSave = (contents[i].substring(0, contents[i].indexOf('.')));
            saveComboBox.getItems().add(chosenSave);
        }


        Button next = new Button(Utils.translate("Next", MainMenu.getLang()));
        next.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (saveComboBox.getValue() != null &&
                        !saveComboBox.getValue().toString().isEmpty()) {
                    saveFileUrl = saveComboBox.getValue().toString() + ".txt";
                    NewGameStage.numOfPlayers = 4;
                    NewGameStage.players = new ArrayList<String>();
                    NewGameStage.players.add("James");
                    NewGameStage.players.add("James");
                    NewGameStage.players.add("James");
                    NewGameStage.players.add("James");
                    NewGameStage.chosenCharacters = new ArrayList<>();
                    NewGameStage.chosenCharacters.add("src//resources//Images//character_images//Blue.gif");
                    NewGameStage.chosenCharacters.add("src//resources//Images//character_images//Blue.gif");
                    NewGameStage.chosenCharacters.add("src//resources//Images//character_images//Blue.gif");
                    NewGameStage.chosenCharacters.add("src//resources//Images//character_images//Blue.gif");
                    Mataha mataha = new Mataha();
                    FileReader.readSave(saveFileUrl);
                    loadGameStage.close();
                } else {
                    warnings.setText("Please select a save file to proceed");
                }
            }
        });

        Button back = new Button(Utils.translate("back", MainMenu.getLang()));
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainMenu back = new MainMenu();
                try {
                    back.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadGameStage.close();
            }
        });

        VBox layout2 = new VBox();
        layout2.setAlignment(Pos.CENTER);

        layout2.setPadding(new Insets(100));
        layout2.getChildren().addAll(saveComboBox, label2, warnings, next, back);

        loadGame = new Scene(layout2, MainMenu.getWidth(), MainMenu.getHeight());
        loadGame.getStylesheets().add("resources/StyleSheets/MaTaHa.css");
        loadGameStage.setTitle(Utils.translate("Load Game", MainMenu.getLang()));
        loadGameStage.setScene(loadGame);
        loadGameStage.setResizable(MainMenu.getResizable());
        loadGameStage.getIcons().add(new Image("resources/Images/menu_images/icon2.png"));
        loadGameStage.show();
    }
}
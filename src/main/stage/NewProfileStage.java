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
import main.FileReader;
import main.MainMenu;
import main.Utils;

import java.io.FileNotFoundException;

/**
 * NewProfileStage allows the user to create a new profile
 *
 * @author G38.dev
 * @version 3
 */
public class NewProfileStage {

    private Stage newProfileStage = new Stage();
    private Scene newProfile;

    /**
     * Method creates and shows the new profile window where users can create a new profile
     */
    public void showNewProfileStage() throws FileNotFoundException {

        Label label2 = new Label(Utils.translate("Create New Profile", MainMenu.getLang()) + "\n");
        label2.setId("title");
        // label2.setAlignment(Pos.CENTER);
        TextField userNameTextField = new TextField();
        userNameTextField.setPromptText(Utils.translate("Name", MainMenu.getLang()));
        userNameTextField.setAlignment(Pos.CENTER);

        Label warnings = new Label("");

        Button saveProfileButton = new Button(Utils.translate("Save Profile", MainMenu.getLang()));
        saveProfileButton.setOnAction(action -> {
            int playerID = FileReader.linesInFile("playerProfiles");
            String username = userNameTextField.getText();
            String profile = username + "," + playerID + "," + 0 + "," + 0;
            FileReader.writeToFiles("playerProfiles", profile);
            userNameTextField.clear();

        });
        VBox layout2 = new VBox();
        layout2.setAlignment(Pos.CENTER);
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
                newProfileStage.close();
            }
        });
        layout2.setPadding(new Insets(100));
        layout2.getChildren().addAll(label2, userNameTextField, warnings, saveProfileButton, back);

        newProfile = new Scene(layout2, MainMenu.getWidth(), MainMenu.getHeight());
        newProfile.getStylesheets().add("src/resources/StyleSheets/MaTaHa.css");
        newProfileStage.setTitle(Utils.translate("New Profile", MainMenu.getLang()));
        newProfileStage.setScene(newProfile);
        newProfileStage.setResizable(MainMenu.getResizable());
        newProfileStage.getIcons().add(new Image("src/resources/Images/menu_images/icon2.png"));
        newProfileStage.show();
    }
}
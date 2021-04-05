package main.stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.FileReader;
import main.MainMenu;
import main.Profile;
import main.Utils;

import java.util.Collections;
import java.util.List;

/**
 * HighscoresStage shows the number of wins and losses each profile has
 *
 * @author G39.dev
 * @version 2
 */
public class LeaderboardStage {

    private Stage leaderboardStage = new Stage();
    private Scene leaderboard;

    /**
     * Method creates and shows the highscores window.
     */
    public void showLeaderboardStage() {
        Label leaderboardLabel = new Label(Utils.translate("leaderboard", MainMenu.getLang()) + "\n");
        leaderboardLabel.setId("title");

        VBox vBox = new VBox();
        ObservableList<HBox> HboxList = FXCollections.observableArrayList();

        HBox hBoxy = new HBox();
        Label playerNameTitle = new Label("Profile Name");
        Label winsTitle = new Label("Wins");
        Label totalPlayedTitle = new Label("Total Games Played");
        hBoxy.getChildren().addAll(playerNameTitle, winsTitle, totalPlayedTitle);
        hBoxy.setPadding(new Insets(20));
        hBoxy.setSpacing(100);
        hBoxy.setAlignment(Pos.CENTER);
        HboxList.add(hBoxy);

        List<Profile> playerProfiles = FileReader.readAllProfiles("playerprofiles");
        Collections.sort(playerProfiles);
        playerProfiles.forEach(profile -> {
            HBox hBox = new HBox();
            Label playerName = new Label(profile.getName());
            Label wins = new Label("" + profile.getNumWins());
            Label totalPlayed = new Label("" + profile.getNumGamesPlayed());
            hBox.getChildren().addAll(playerName, wins, totalPlayed);
            hBox.setPadding(new Insets(20));
            hBox.setSpacing(100);
            hBox.setAlignment(Pos.CENTER);
            HboxList.add(hBox);
        });
        ListView listView = new ListView<>();
        listView.setItems(HboxList);


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
                leaderboardStage.close();
            }
        });
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(leaderboardLabel, listView, back);

        leaderboard = new Scene(vBox, MainMenu.getWidth(), MainMenu.getHeight());
        leaderboard.getStylesheets().add("resources/StyleSheets/MaTaHa.css");
        leaderboardStage.setTitle(Utils.translate("Leaderboard", MainMenu.getLang()));
        leaderboardStage.setScene(leaderboard);
        leaderboardStage.setResizable(MainMenu.getResizable());
        leaderboardStage.getIcons().add(new Image("resources/Images/menu_images/icon2.png"));
        leaderboardStage.show();
    }
}
package main.panels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Game;
import main.Player;

import java.util.HashMap;

/**
 * Class for the player turn panel
 *
 * @author G38.dev
 */
public class PlayerTurnPanel extends VBox {

    private Game game;
    private HBox playerTokenBox;
    private HBox playerNameBox;
    private HashMap<String, ImageView> imageMap;
    private Label nameLabel;

    /**
     * Creates the turn panel
     *
     * @param game the current game instance
     */
    public PlayerTurnPanel(Game game) {
        this.game = game;
        this.setPadding(new Insets(20));
        this.setStyle("-fx-border-color: black");
        this.setPrefWidth(200);
        this.setAlignment(Pos.CENTER);
        initPlayerTokenBox();
        initPlayerNameBox();
        this.getChildren().add(playerTokenBox);
        this.getChildren().add(playerNameBox);
        update();
    }

    /**
     * Creates the token images and its box
     */
    private void initPlayerTokenBox() {

        playerTokenBox = new HBox();

        playerTokenBox.setPadding(new Insets(20));
        playerTokenBox.setSpacing(10);
        playerTokenBox.setAlignment(Pos.CENTER);

        imageMap = new HashMap<>();
        for (Player player : game.getPlayers()) {
            Image image = new Image(player.getCharacterURL());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            imageMap.put(player.getName(), imageView);
            playerTokenBox.getChildren().add(imageView);
        }
    }

    /**
     * Creates the player name box
     */
    private void initPlayerNameBox() {
        playerNameBox = new HBox();
        playerNameBox.setAlignment(Pos.CENTER);
        nameLabel = new Label();
        nameLabel.setText("test");
        playerNameBox.getChildren().add(nameLabel);

    }

    /**
     * Reloads the player images after the load
     */
    public void reloadImages() {
        imageMap = new HashMap<>();
        playerTokenBox.getChildren().clear();
        for (Player player : game.getPlayers()) {
            Image image = new Image(player.getCharacterURL());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            imageMap.put(player.getName(), imageView);
            playerTokenBox.getChildren().add(imageView);
        }
        nameLabel.setText(game.getCurrentPlayer().getName());
    }

    /**
     * Refreshes the panel to display the current player
     */
    public void update() {

        Player currentPlayer = game.getCurrentPlayer();
        ImageView playerImage = imageMap.get(currentPlayer.getName());
        for (Node n : playerTokenBox.getChildren()) {
            if (!(n instanceof ImageView)) return;
            ImageView imageView = (ImageView) n;
            if (imageView == playerImage) {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
            } else {
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
            }
        }

        nameLabel.setText(currentPlayer.getName());
        nameLabel.setPrefWidth(200);
        nameLabel.setWrapText(true);
    }

}

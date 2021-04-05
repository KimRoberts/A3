package main.panels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import main.*;

/**
 * Class for the game info panel GUI.
 *
 * @author G38.dev
 */
public class GameInfoPanel extends VBox {

    private Game game;
    private TipLabel tipLabel;
    private Button next;
    private Button navNext;
    private PlayerTurnPanel ptp;

    /**
     * Constructor sets the game info panel in the game window.
     *
     * @param game of type main.Game.
     */
    public GameInfoPanel(Game game) {
        this.game = game;
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(0, 60, 0, 0));
        this.setSpacing(100);
        this.setMaxWidth(200);
        initNodes();
    }

    /**
     * Set up the panel so the user can see it
     */
    private void initNodes() {

        ptp = new PlayerTurnPanel(game);
        this.getChildren().add(ptp);
        ptp.setVisible(true);
        tipLabel = new TipLabel(game);
        this.getChildren().add(tipLabel);
        tipLabel.setDrawTileText();
        next = new Button(Utils.translate("Next", MainMenu.getLang()));
        this.getChildren().add(next);
        next.setOnMouseClicked(event -> {
            Mataha.getGame().gameLoop(Mataha.getGame().nextPlayer());
            next.setVisible(false);
        });
        next.setVisible(false);

        navNext = new Button(Utils.translate("Next", MainMenu.getLang()));
        this.getChildren().add(navNext);
        navNext.setOnMouseClicked(event -> {
            Mataha.getGame().initNavigationUI();
            navNext.setVisible(false);
        });
        navNext.setVisible(false);


    }

    public TipLabel getTipLabel() {
        return tipLabel;
    }

    public void showNextButton() {
        next.setVisible(true);
    }

    public void showNavNextButton() {
        navNext.setVisible(true);
    }

    public void hideNavNextButton() {
        navNext.setVisible(false);
    }

    public void hideNextButton() {
        next.setVisible(false);
    }

    public PlayerTurnPanel getPtp() {
        return ptp;
    }
}
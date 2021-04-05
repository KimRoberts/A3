package main.stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.*;
import main.level_editor_components.*;
import main.tile.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * LevelEditorStage allows the user to customise their own gameboard.
 *
 * @author G38.dev
 */
public class LevelEditorStage {

    public static String fileUrl;
    private static int ONE = 1;
    String colour = "";
    String url = "";
    ToggleButton chosenButton = null;
    int count = ONE;
    private Stage levelEditorStage = new Stage();
    private Scene subMenu, levelEditor, playLevel, deleteLevel, characterSelection;
    private NavigationTile tileInHand = null;
    private Map map;
    private TileSelectionPanel tileSelectionPanel;
    private SetTileBagPanel setTileBagPanel;
    private ResizePanel resizePanel;
    private ComboBox texturePackCombo;
    private String selectedTexture;
    private ArrayList<Tile> tileBag;
    private INPUT_MODE inputMode = INPUT_MODE.TILE;

    /**
     * Method creates and shows the level editor sub-menu.
     */
    public void showLevelEditorStage() {
        levelEditorStage.setTitle(Utils.translate("Level Editor", MainMenu.getLang()));

        tileBag = new ArrayList<>();

        texturePackCombo = new ComboBox();
        texturePackCombo.setPromptText("Texture Pack");

        // adds all the texture pack files in the directory to a combo box
        File directoryPath = new File("src/resources/Images/game_images/texture_packs");
        String contents[] = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String chosenSave = contents[i];
            texturePackCombo.getItems().add(chosenSave);
        }
        texturePackCombo.getSelectionModel().select(0);
        selectedTexture = texturePackCombo.getValue().toString();

        // sets the texture pack to the selected value of this combo box
        texturePackCombo.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String texture = newValue.toString();
                setNewTexture(texture);
            }
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        Label title = new Label(Utils.translate("Level Editor", MainMenu.getLang()));
        title.setId("title");

        Button newLevelBtn = new Button(Utils.translate("New Level", MainMenu.getLang()));
        newLevelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newLevel();
            }
        });
        newLevelBtn.setMaxSize(250, 150);


        Button editLevelBtn = new Button(Utils.translate("Edit Level", MainMenu.getLang()));
        editLevelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editLevel();
            }
        });
        editLevelBtn.setMaxSize(250, 150);


        Button playBtn = new Button((Utils.translate("Play Level", MainMenu.getLang())));
        playBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playLevel();
            }
        });
        playBtn.setMaxSize(250, 150);

        Button deleteBtn = new Button((Utils.translate("Delete Level", MainMenu.getLang())));
        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteLevel();
            }
        });
        deleteBtn.setMaxSize(250, 150);


        Button backBtn = new Button(Utils.translate("back", MainMenu.getLang()));
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainMenu back = new MainMenu();
                try {
                    back.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                levelEditorStage.close();
            }
        });

        vbox.getChildren().addAll(title, newLevelBtn, editLevelBtn, deleteBtn, playBtn, backBtn);

        subMenu = new Scene(vbox, 750, 700);
        subMenu.getStylesheets().add("src/resources/StyleSheets/MaTaHa.css");
        levelEditorStage.setScene(subMenu);
        levelEditorStage.getIcons().add(new Image("src/resources/Images/menu_images/icon2.png"));
        levelEditorStage.setResizable(MainMenu.getResizable());
        levelEditorStage.show();
    }


    /**
     * Method creates new level and shows the level editor window.
     */
    public void newLevel() {
        levelEditor();
    }

    /**
     * Method that lets the user edit and customise their own gameboard.
     */
    public void levelEditor() {
        map = new Map(10, 10, this);
        tileSelectionPanel = new TileSelectionPanel(this);
        TitlePanel titlePanel = new TitlePanel("src\\resources\\Images\\game_images\\level_editor_title.gif");
        resizePanel = new ResizePanel(this);

        BorderPane mainLayout = new BorderPane();
        Button back = new Button(Utils.translate("back", MainMenu.getLang()));
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                levelEditorStage.setScene(subMenu);
            }
        });

        Button createMap = new Button("Create");
        createMap.setText(Utils.translate("Create", MainMenu.getLang()));
        createMap.setOnMouseClicked(event -> {
            saveToFile();
        });

        InputModePanel inputModePanel = new InputModePanel(this);
        HBox spacerTop = new HBox();
        spacerTop.setMinWidth(100);
        HBox topPanel = new HBox();
        topPanel.setSpacing(20);
        topPanel.getChildren().addAll(resizePanel, titlePanel, spacerTop, inputModePanel);

        HBox bottomPanel = new HBox();
        bottomPanel.setSpacing(20);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(20));
        bottomPanel.getChildren().addAll(back, texturePackCombo, createMap);

        setTileBagPanel = new SetTileBagPanel(this);

        mainLayout.setCenter(map);
        mainLayout.setBottom(bottomPanel);
        mainLayout.setLeft(tileSelectionPanel);
        mainLayout.setTop(topPanel);
        mainLayout.setRight(setTileBagPanel);

        setNewTexture(selectedTexture);
        levelEditor = new Scene(mainLayout, 950, 750);
        levelEditor.getStylesheets().add("src/resources/StyleSheets/MaTaHa.css");
        levelEditorStage.setScene(levelEditor);
    }


    /**
     * Method that lets the user choose what file they want to edit and edit it in level editor mode
     */
    public void editLevel() {
        Label title = new Label(Utils.translate("Edit Level", MainMenu.getLang()) + "\n");
        title.setId("title");

        Label subtitle = new Label(Utils.translate("Select level to edit", MainMenu.getLang()) + "\n");

        Label warnings = new Label("");

        final ComboBox saveComboBox = new ComboBox();
        saveComboBox.setPromptText("File Name");

        // adds all the level files in the directory to a combo box
        File directoryPath = new File("src/resources/LevelEditorFiles");
        String contents[] = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String chosenSave = (contents[i].substring(0, contents[i].indexOf('.')));
            saveComboBox.getItems().add(chosenSave);
        }

        // uses the selected value in the combo box to load a file in the FileReader class and then
        // display the levelEditorScene to edit that level
        Button next = new Button(Utils.translate("Next", MainMenu.getLang()));
        next.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (saveComboBox.getValue() != null &&
                        !saveComboBox.getValue().toString().isEmpty()) {
                    fileUrl = saveComboBox.getValue().toString() + ".txt";
                    levelEditor();
                    FileReader.loadMapSquares(fileUrl, getThis());
                    levelEditorStage.setScene(levelEditor);
                } else {
                    warnings.setText("Please select a level file to proceed");
                }
            }
        });

        Button back = new Button(Utils.translate("back", MainMenu.getLang()));
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                levelEditorStage.setScene(subMenu);
            }
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        vbox.setPadding(new Insets(100));
        vbox.getChildren().addAll(subtitle, saveComboBox, title, warnings, next, back);

        playLevel = new Scene(vbox, MainMenu.getWidth(), MainMenu.getHeight());
        playLevel.getStylesheets().add("resources/StyleSheets/MaTaHa.css");
        levelEditorStage.setTitle(Utils.translate("Play Level", MainMenu.getLang()));
        levelEditorStage.setScene(playLevel);
    }

    /**
     * Method shows the levels available to play and lets the user choose players and characters to play that level.
     */
    public void playLevel() {
        Label title = new Label(Utils.translate("Play Level", MainMenu.getLang()) + "\n");
        title.setId("title");

        Label subtitle = new Label(Utils.translate("Select level to play", MainMenu.getLang()) + "\n");

        Label warnings = new Label("");

        final ComboBox saveFileComboBox = new ComboBox();
        saveFileComboBox.setPromptText("File Name");

        // adds all the level files in the directory to a combo box
        File directoryPath = new File("src/resources/LevelEditorFiles");
        String contents[] = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String chosenSave = (contents[i].substring(0, contents[i].indexOf('.')));
            saveFileComboBox.getItems().add(chosenSave);
        }

        Label title2 = new Label(Utils.translate("Player Picker", MainMenu.getLang()));
        title2.setId("title");
        ChoiceBox p1 = new ChoiceBox();
        p1.setItems(FileReader.readProfileNames("playerProfiles"));
        ChoiceBox p2 = new ChoiceBox();
        p2.setItems(FileReader.readProfileNames("playerProfiles"));
        ChoiceBox p3 = new ChoiceBox();
        p3.setItems(FileReader.readProfileNames("playerProfiles"));
        ChoiceBox p4 = new ChoiceBox();
        p4.setItems(FileReader.readProfileNames("playerProfiles")); //Names of all the profiles

        Button next = new Button(Utils.translate("Next", MainMenu.getLang()));
        next.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (saveFileComboBox.getValue() != null &&
                        !saveFileComboBox.getValue().toString().isEmpty()) {
                    fileUrl = "src/resources/LevelEditorFiles/" + saveFileComboBox.getValue() + ".txt";
                    if (saveFileComboBox.getValue() != null) {

                        //displays warings if not enough players selected
                        if (p1.getValue() == null || p2.getValue() == null) {
                            warnings.setText(Utils.translate("Please select at least 2 players!", MainMenu.getLang()));
                        } else if (p1.getValue().equals(p2.getValue())) {
                            warnings.setText(Utils.translate("Please select 2 different profiles", MainMenu.getLang()));
                        } else {

                            // Adds players to the game
                            fileUrl = saveFileComboBox.getValue().toString() + ".txt";
                            NewGameStage.players.add((String) p1.getValue());
                            NewGameStage.players.add((String) p2.getValue());
                            NewGameStage.numOfPlayers = 2;
                            if (p3.getValue() != null) {
                                NewGameStage.players.add((String) p3.getValue());
                                NewGameStage.numOfPlayers++;
                            }
                            if (p4.getValue() != null) {
                                NewGameStage.players.add((String) p4.getValue());
                                NewGameStage.numOfPlayers++;
                            }
                            int counter = 0;

                            try {
                                characterSelection();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                } else {
                    warnings.setText("Please select a file to proceed");
                }
            }
        });

        Button back = new Button(Utils.translate("back", MainMenu.getLang()));
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                levelEditorStage.setScene(subMenu);
            }
        });

        VBox vBoxMainLayout = new VBox();
        vBoxMainLayout.setAlignment(Pos.CENTER);

        //HBox level selector
        HBox hBoxLevelSelector = new HBox();
        hBoxLevelSelector.getChildren().addAll(saveFileComboBox);
        hBoxLevelSelector.setAlignment(Pos.CENTER);
        hBoxLevelSelector.setSpacing(40);

        //HBox profile selector
        HBox hBoxProfileSelector = new HBox();
        hBoxProfileSelector.getChildren().addAll(p1, p2, p3, p4);
        hBoxProfileSelector.setAlignment(Pos.CENTER);
        hBoxProfileSelector.setSpacing(40);

        vBoxMainLayout.setSpacing(40);
        vBoxMainLayout.getChildren().addAll(title, subtitle, hBoxLevelSelector, title2, hBoxProfileSelector, warnings, next);

        playLevel = new Scene(vBoxMainLayout, 700, 500);
        playLevel.getStylesheets().add("resources/StyleSheets/MaTaHa.css");
        levelEditorStage.setTitle(Utils.translate("Play Level", MainMenu.getLang()));
        levelEditorStage.setScene(playLevel);
    }

    /**
     * Method that lets each player choose the character they play as and removes each character as they are chosen
     * so there's only 1 of each character in the game
     */
    public void characterSelection() throws FileNotFoundException {
        FlowPane layout = new FlowPane();

        //HBox for colours
        FlowPane hBoxCharacters = new FlowPane();
        //hBoxColours.setSpacing(20);
        hBoxCharacters.setAlignment(Pos.CENTER);
        hBoxCharacters.getChildren().addAll();
        hBoxCharacters.setHgap(40);
        hBoxCharacters.setVgap(40);
        layout.setAlignment(Pos.CENTER);

        characterSelection = new Scene(layout, 700, 500);
        characterSelection.getStylesheets().add("src/resources/StyleSheets/MaTaHa.css");
        levelEditorStage.setScene(characterSelection);

        Label title = new Label(Utils.translate("Pick a character", MainMenu.getLang()) + " " + NewGameStage.players.get(0));
        title.setId("title");

        ToggleGroup characterToggle = new ToggleGroup();

        // Gets all the characted images from a folder and maps them to buttons that the user can select
        File directoryPath = new File("src//resources/Images//character_images");
        String contents[] = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String chosenImg = (contents[i].substring(0, contents[i].indexOf('.')));
            String chosenCharacterName = chosenImg.substring(0, 1).toUpperCase() + chosenImg.substring(1).toLowerCase();
            ToggleButton character = new ToggleButton(chosenCharacterName);
            character.setWrapText(true);
            character.setGraphic(new ImageView(new Image(new FileInputStream("src//resources/Images//character_images//" +
                    contents[i]), 50, 50, true, false)));
            character.setToggleGroup(characterToggle);

           // selects charater
            character.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    colour = chosenCharacterName;
                    url = "src//resources/Images//character_images//" + chosenCharacterName + ".gif";
                    chosenButton = character;
                }
            });
            hBoxCharacters.getChildren().add(character);
        }

        Label warnings = new Label("");

        Button next = new Button(Utils.translate("Next", MainMenu.getLang()));

        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // displays warning if no character selected
                if (characterToggle.getSelectedToggle() == null) {
                    warnings.setText(Utils.translate("Please select a character", MainMenu.getLang()));
                } else {
                    // Adds character to the game and removes selected charater as an option for next player
                    NewGameStage.chosenCharacters.add(url);
                    hBoxCharacters.getChildren().remove(chosenButton);
                    if (count < NewGameStage.players.size()) {
                        characterToggle.getSelectedToggle().setSelected(false);
                        title.setText(Utils.translate("Pick a character", MainMenu.getLang()) + " " + NewGameStage.players.get(count));
                        count++;
                    } else {
                        FileReader.loadMapSquares(fileUrl, getThis());
                        Mataha mataha = new Mataha();
                        levelEditorStage.close();
                    }
                }
            }
        });

        layout.setOrientation(Orientation.VERTICAL);
        layout.setColumnHalignment(HPos.CENTER);
        layout.setHgap(40);
        layout.setVgap(40);
        layout.getChildren().addAll(title, hBoxCharacters, warnings, next);
    }

    /**
     * Method shows the levels available and lets user delete them
     */
    public void deleteLevel() {
        Label title = new Label(Utils.translate("Delete Level", MainMenu.getLang()) + "\n");
        title.setId("title");

        Label subtitle = new Label(Utils.translate("Select level to delete", MainMenu.getLang()) + "\n");

        Label warnings = new Label("");

        // gets all level files from file path and displays them in a combo box
        final ComboBox saveComboBox = new ComboBox();
        saveComboBox.setPromptText("File Name");
        File directoryPath = new File("src/resources/LevelEditorFiles");
        String contents[] = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String chosenSave = (contents[i].substring(0, contents[i].indexOf('.')));
            saveComboBox.getItems().add(chosenSave);
        }


        Button delete = new Button(Utils.translate("Delete", MainMenu.getLang()));

        // Removes a selected file
        delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (saveComboBox.getValue() != null &&
                        !saveComboBox.getValue().toString().isEmpty()) {
                    File selectedFile = new File("C:\\Users\\asins\\Documents\\GitHub\\MaTaHa\\src\\resources\\LevelEditorFiles\\" + saveComboBox.getValue().toString() + ".txt");
                    selectedFile.delete();
                    levelEditorStage.setScene(subMenu);
                } else {
                    warnings.setText("Please select a level file to delete");
                }
            }
        });

        Button back = new Button(Utils.translate("back", MainMenu.getLang()));
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                levelEditorStage.setScene(subMenu);
            }
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        vbox.setPadding(new Insets(100));
        vbox.getChildren().addAll(subtitle, saveComboBox, title, warnings, delete, back);

        deleteLevel = new Scene(vbox, MainMenu.getWidth(), MainMenu.getHeight());
        deleteLevel.getStylesheets().add("resources/StyleSheets/MaTaHa.css");
        levelEditorStage.setTitle(Utils.translate("Delete Level", MainMenu.getLang()));
        levelEditorStage.setScene(deleteLevel);
    }

    /**
     * Method that checks that the edited/newly created map is valid and if it is, saves it, before going back
     * to the level editor sub-menu
     */
    private void saveToFile() {

        // checks map has a goal square
        if (!map.hasGoalSquare()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Map must have a goal tile");
            a.show();
            return;
        }

        // checks map has 4 starting positions
        if (map.getNumStartingPosition() != 4) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Map must have 4 starting positions");
            a.show();
            return;
        }

        // checks map has possible path to goal
        if (!map.hasPossiblePath()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Map has no possible path to goal");
            a.show();
            return;
        }

        // checks tile bag has enough tiles to fill map
        int MIN_TILES_IN_BAG = 1;
        if (map.getNumNonFixedTiles() + MIN_TILES_IN_BAG > setTileBagPanel.getNumNavTiles()) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Not enough tiles in bag");
            a.show();
            return;
        }

        // checks all four starting positions are fixed tiles
        if (!map.startingPositionsAreFixed()) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Starting positions must be fixed tiles");
            a.show();
            return;
        }

        tileBag = setTileBagPanel.getTiles();

        TextInputDialog td = new TextInputDialog("Name");
        td.showAndWait();
        String fileName = td.getEditor().getText();
        FileReader.saveMapSquares(fileName, this);
        levelEditorStage.setScene(subMenu);
    }

    /**
     * Method that gets the navigation tile the user is "holding"
     *
     * @return The navigation tile the user is "holding"
     */
    public NavigationTile getTileInHand() {
        return tileInHand;
    }

    /**
     * Method that sets the navigation tile the user wants to "hold"
     *
     * @param tileInHand - The navigation tile the user will "hold"
     */
    public void setTileInHand(NavigationTile tileInHand) {
        this.tileInHand = tileInHand;
    }

    /**
     * Method that gets the map and if map is null initialises a new map object
     *
     * @return the map
     */
    public Map getMap() {
        if (map == null) {
            map = new Map(1, 1, this);
        }
        return map;
    }

    /**
     * Method that gets the tile selection panel
     *
     * @return the tileSelectionPanel
     */
    public TileSelectionPanel getTileSelectionPanel() {
        return tileSelectionPanel;
    }

    /**
     * Method that serialises the tile bag contents so they can be appended to the save file.
     *
     * @return the sting containing all the tiles in the tile bag and their details such as rotation
     */
    public String serialiseTileBag() {
        String build = "";
        for (Tile tile : tileBag) {
            String type = "";
            if (tile instanceof CornerTile) {
                type = "corner";
            }
            if (tile instanceof TShapeTile) {
                type = "tshape";
            }
            if (tile instanceof LineTile) {
                type = "line";
            }
            if (tile instanceof GoalTile) {
                type = "goal";
            }
            if (tile instanceof NavigationTile) {
                build += type + "," + ((NavigationTile) tile).getRotation() + "," + ((NavigationTile) tile).isFixed() + "\n";
            }
            if (tile instanceof EffectTile) {
                build += "effect," + ((EffectTile) tile).getTileEffect().getEffectName() + "," + ((EffectTile) tile).isFirstTurn() + "\n";
            }

        }
        return build;
    }

    /**
     * Method that gets the input mode
     *
     * @return the current inputMode
     */
    public INPUT_MODE getInputMode() {
        return inputMode;
    }

    /**
     * Method that sets the input mode
     *
     * @param inputMode - the input mode to be set
     */
    public void setInputMode(INPUT_MODE inputMode) {
        if (inputMode == INPUT_MODE.START_POSITION) {
            tileSelectionPanel.removeAllHighlightOverlays();
            tileInHand = null;
        }
        this.inputMode = inputMode;
    }

    /**
     * Method that gets the current level editor stage
     *
     * @return the current LevelEditorStage
     */
    private LevelEditorStage getThis() {
        return this;
    }

    /**
     * Method that sets a new texture
     *
     * @param texture - the name of the texture pack used
     */
    public void setNewTexture(String texture) {
        selectedTexture = texture;
        Mataha.texturePack = texture;
        if (tileSelectionPanel == null) {
            tileSelectionPanel = new TileSelectionPanel(this);
        }
        tileSelectionPanel.updateUI();
        if (setTileBagPanel == null) {
            setTileBagPanel = new SetTileBagPanel(this);
        }
        setTileBagPanel.redrawTiles();
        if (map != null) {
            map.redrawTiles();
        }

    }

    /**
     * Method that gets the selected texture pack
     *
     * @return the name of the selected texture pack
     */
    public String getSelectedTexture() {
        return selectedTexture;
    }

    /**
     * Method that loads the contents of the tilebag
     *
     * @param tiles - An arraylist of all the tiles in the save file
     */
    public void loadTileBag(ArrayList<Tile> tiles) {
        this.tileBag = tiles;
        setTileBagPanel.setTileNumbers(tiles);
    }

    /**
     * Method that gets the resize panel
     *
     * @return the resize panel
     */
    public ResizePanel getResizePanel() {
        if (resizePanel == null) {
            resizePanel = new ResizePanel(this);
        }
        return resizePanel;
    }

    public enum INPUT_MODE {
        TILE,
        START_POSITION
    }
}

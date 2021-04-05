package main.create_texture_pack_components;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import main.stage.CreateTexturePackStage;

import java.awt.image.RenderedImage;



public class DrawNavTileCanvas extends Canvas {

    public enum TILE_TYPE{
        TSHAPE,
        LSHAPE,
        LINE,
        GOAL
    }

    private double SIZE = 80;
    private double ROAD_WIDTH = SIZE / 3;
    private TILE_TYPE tileType;
    private CreateTexturePackStage context;

    private GraphicsContext graphicsContext;

    public DrawNavTileCanvas(double size, TILE_TYPE tileType, CreateTexturePackStage context) {
        this.context = context;
        setSize(size);
        this.tileType = tileType;
        this.setHeight(this.SIZE);
        this.setWidth(this.SIZE);
        this.graphicsContext = this.getGraphicsContext2D();
        initBackground();
        drawGuideLines();

        this.setOnMouseDragged(e -> {

            CreateTexturePackStage.INPUT_TYPE input_type = context.getInputType();

            switch (input_type){
                case BRUSH:
                    drawBrush(e);
            }

        });
    }

    private void drawBrush(MouseEvent e) {
        double brushSize = context.getBrushSize();
        Color colour = context.getColour();

        if (brushSize <= 0 || colour == null) return;

        double x = e.getX() - brushSize/2;
        double y = e.getY() - brushSize/2;

        graphicsContext.setFill(colour);
        graphicsContext.fillRoundRect(x, y, brushSize, brushSize, brushSize, brushSize);
    }


    public void drawGuideLines(){
        switch (this.tileType){
            case LINE: drawLineShape();
                break;
            case TSHAPE: drawTShape();
                break;
            case LSHAPE: drawLShape();
                break;
            case GOAL: drawGoal();
                break;
        }
    }

    private void initBackground() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, SIZE, SIZE);
    }

    private void drawLShape() {
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.strokeLine((SIZE / 2 - ROAD_WIDTH / 2), 0, (SIZE / 2 - ROAD_WIDTH / 2), (SIZE / 2 - ROAD_WIDTH / 2));
        graphicsContext.strokeLine((SIZE / 2 - ROAD_WIDTH / 2), (SIZE / 2 - ROAD_WIDTH / 2), 0, (SIZE / 2 - ROAD_WIDTH / 2));
        graphicsContext.strokeLine(0, (SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2));
        graphicsContext.strokeLine((SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2), 0);
    }

    private void drawTShape() {
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.strokeLine(0, (SIZE / 2 - ROAD_WIDTH / 2), SIZE, (SIZE / 2 - ROAD_WIDTH / 2));
        graphicsContext.strokeLine(0, (SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 - ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2));
        graphicsContext.strokeLine((SIZE / 2 - ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 - ROAD_WIDTH / 2), SIZE);
        graphicsContext.strokeLine((SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2), SIZE, (SIZE / 2 + ROAD_WIDTH / 2));
        graphicsContext.strokeLine((SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2), (SIZE / 2 + ROAD_WIDTH / 2), SIZE);
    }

    private void drawLineShape(){
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.strokeLine((SIZE/2 - ROAD_WIDTH/2), 0, (SIZE/2 - ROAD_WIDTH/2), SIZE);
        graphicsContext.strokeLine((SIZE/2 + ROAD_WIDTH/2), 0, (SIZE/2 + ROAD_WIDTH/2), SIZE);
    }

    private void drawGoal(){
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.strokeLine((SIZE/2 - ROAD_WIDTH/2), 0, (SIZE/2 - ROAD_WIDTH/2), (SIZE/2 - ROAD_WIDTH/2));
        graphicsContext.strokeLine((SIZE/2 - ROAD_WIDTH/2), (SIZE/2 - ROAD_WIDTH/2), 0, (SIZE/2 - ROAD_WIDTH/2));
        graphicsContext.strokeLine((SIZE/2 - ROAD_WIDTH/2), (SIZE/2 + ROAD_WIDTH/2), 0, (SIZE/2 + ROAD_WIDTH/2));
        graphicsContext.strokeLine((SIZE/2 - ROAD_WIDTH/2), (SIZE/2 + ROAD_WIDTH/2), (SIZE/2 - ROAD_WIDTH/2), SIZE);
        graphicsContext.strokeLine((SIZE/2 + ROAD_WIDTH/2), (SIZE/2 + ROAD_WIDTH/2), (SIZE/2 + ROAD_WIDTH/2), SIZE);
        graphicsContext.strokeLine((SIZE/2 + ROAD_WIDTH/2), (SIZE/2 + ROAD_WIDTH/2), SIZE, (SIZE/2 + ROAD_WIDTH/2));
        graphicsContext.strokeLine((SIZE/2 + ROAD_WIDTH/2), (SIZE/2 - ROAD_WIDTH/2), SIZE, (SIZE/2 - ROAD_WIDTH/2));
        graphicsContext.strokeLine((SIZE/2 + ROAD_WIDTH/2), 0, (SIZE/2 + ROAD_WIDTH/2), (SIZE/2 - ROAD_WIDTH/2));
    }

    public RenderedImage getRenderedImage(){
        WritableImage writableImage = new WritableImage((int)SIZE, (int)SIZE);
        this.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        return renderedImage;
    }

    public void setSize(double size) {
        this.SIZE = size;
        this.ROAD_WIDTH = size/3;
    }

    public void removeGuideLines(){

    }
}
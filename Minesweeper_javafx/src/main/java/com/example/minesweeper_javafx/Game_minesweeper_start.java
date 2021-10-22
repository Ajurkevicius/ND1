package com.example.minesweeper_javafx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Game_minesweeper_start extends Application {

    private static final int width=800;
    private static final int height= 600;
    private static final int tile_size=80;
    private static final int tiles_x=width/tile_size;
    private static final int tiles_y=height/tile_size;
    private int tiles_count=tiles_x*tiles_y;
    private int bomb_count=0;
    private Tile[][] grid = new Tile[tiles_x][tiles_y];
    private Scene scene;
    private boolean first_move=false;

    private Parent createMap(){
        Pane root = new Pane();
        root.setPrefSize(width, height);

      //  System.out.println(tiles_x +"       " + tiles_y);

        //getting the map in
        for (int y=0; y<tiles_y; y++){
            for(int x=0; x<tiles_x; x++){
                Tile tile= new Tile(x, y,Math.random() < 0.1);
                grid[x][y]=tile;
              //  System.out.println(grid);
                 root.getChildren().add(tile);
            }
        }

        //getting numbers in
        for (int y=0; y<tiles_y; y++){
            for (int x=0; x<tiles_x; x++){
                Tile tile = grid [x][y];

                if(tile.hasBomb==true){
                    bomb_count++;
                    continue;
                }
                long bombs = getNeighbours(tile).stream().filter(t -> t.hasBomb).count();

                if(bombs> 0) {
                    tile.text.setText(String.valueOf(bombs));
                }
            }
        }
        System.out.println("Bomb count: " +bomb_count);

        return root;
    }


    public List<Tile> getNeighbours(Tile tile) {
        List<Tile> neighbours = new ArrayList<>();

        int[] points = new int[]{
                -1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1
        };
        for (int i = 0; i < points.length; i = i + 2) {
            int dx = points[i];
            int dy = points[i + 1];
            int newX = tile.x+dx;
            int newY = tile.y+dy;
            if (newX >= 0 && newX < tiles_x &&
                    newY >= 0 && newY < tiles_y) {
                neighbours.add(grid[newX][newY]);
            }

        }
        return neighbours;
    }


    class Tile extends StackPane{
        private int  x, y;
        private boolean hasBomb;
        private boolean isOpen=false;
        private Rectangle border = new Rectangle(tile_size -2, tile_size-2 );
        private Text text= new Text();
        public Tile(int x, int y, boolean hasBomb) {
            this.x = x;
            this.y = y;
            this.hasBomb = hasBomb;
            border.setStroke(Color.GRAY);
            //text.setFill(Color.WHITE);
            text.setText(hasBomb ? "X" : "");
            text.setVisible(false);
            //border.setFill(null);
            getChildren().addAll(border, text);
            setTranslateX(x*tile_size);
            setTranslateY(y*tile_size);

            setOnMouseClicked(e -> open());

        }
        public void open(){
            if(isOpen){
                return;
            }
            if(hasBomb==true && first_move==false){
                System.out.println("You lucky bastard, first move is on us :)");
                first_move=true;
                tiles_count--;
                bomb_count--;

            } else if(hasBomb){
                System.out.println("You lost, play again");
                scene.setRoot(createMap());
                bomb_count=0;
                tiles_count=tiles_x*tiles_y;
                first_move=false;
                return;
            }
            first_move=true;
            if(!hasBomb){
                tiles_count--;
                System.out.println("TilesCOunt: " + tiles_count);
            }
            if(tiles_count==bomb_count){
                System.out.println("You won, play again");
                scene.setRoot(createMap());
            }
            isOpen=true;
            text.setVisible(true);
            border.setFill(null);

            if(text.getText().isEmpty()){
                getNeighbours(this).forEach(Tile::open);

            }
        }

    }
    @Override
    public void start(Stage stage) {
       scene = new Scene(createMap());
        stage.setTitle("Minesweeper");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
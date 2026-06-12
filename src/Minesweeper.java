import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    private class MineTile extends JButton{
        int r;
        int c;

        public MineTile(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;
    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;

    int tilesClicked = 0;
    boolean gameOver;
    int mineCount = 10;

    Minesweeper (){
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Yun-sweeper :3");
        textLabel.setOpaque(true);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        boardPanel.setBackground(Color.PINK);
        frame.add(boardPanel);

        for (int r = 0; r < numRows; r++){
            for (int c = 0; c < numCols; c++){
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Times New Roman", Font.PLAIN,  45));
                // tile.setText(":<");
                boardPanel.add(tile);
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e){
                        MineTile tile = (MineTile) e.getSource();
                        if (gameOver){
                            return;
                        }
                        if (e.getButton() == MouseEvent.BUTTON1){
                            if (tile.getText() == ""){
                                if (mineList.contains(tile)){
                                    revealMines();
                                }
                                else{
                                    checkMine(tile.r, tile.c);
                                }
                            }
//                            if (tile.getText() != "" && tile.getText() != "?"){
//                                chord(tile.r, tile.c);
//                            }
                        }
                        if (e.getButton() == MouseEvent.BUTTON3){
                            if (tile.getText() == "") {
                                tile.setText("?");
                                mineCount--;
                            }
                            else if (tile.getText() == "?") {
                                tile.setText("");
                                mineCount++;
                            }
                            textLabel.setText("Mines Left: " + mineCount);
                        }
                    }
                });
            }
        }
        frame.setVisible(true);
        if (tilesClicked <= 1){
            setMines();
        }
    }
    void setMines() {
        mineList = new ArrayList<MineTile>();
        for (int i = 0; i < mineCount; i++){
            int row = (int) ((numRows)*(Math.random()));
            int col = (int) ((numCols)*(Math.random()));
            if (mineList.contains(board[row][col])&&board[row][col].isEnabled()) {
                i--;
            }
            else {
                mineList.add(board[row][col]);
            }
        }
    }

    void revealMines(){
        for (int i = 0; i < mineList.size(); i++){
            MineTile tile = mineList.get(i);
            tile.setText(":<");
            textLabel.setText("Yun Died! >~<");
        }

        gameOver = true;
    }

    void checkMine(int r, int c){
        if (r < 0|| r >= numRows || c < 0 || c >= numCols){
            return;
        }
        MineTile tile = board[r][c];
        if(!tile.isEnabled()){
            return;
        }
        tile.setEnabled(false);

        int minesFound = 0;
        minesFound += countMine(r - 1, c - 1);
        minesFound += countMine(r - 1, c);
        minesFound += countMine(r - 1, c + 1);
        minesFound += countMine(r, c - 1);
        minesFound += countMine(r, c + 1);
        minesFound += countMine(r + 1, c - 1);
        minesFound += countMine(r + 1, c);
        minesFound += countMine(r + 1, c + 1);

        if (minesFound > 0){
            tile.setText(Integer.toString(minesFound));
        }
        else {
            tile.setText("");
            checkMine(r - 1, c -1);
            checkMine(r - 1, c);
            checkMine(r - 1, c + 1);
            checkMine(r, c + 1);
            checkMine(r, c - 1);
            checkMine(r + 1, c - 1);
            checkMine(r + 1, c);
            checkMine(r + 1, c + 1);
        }
        tilesClicked++;
        if (tilesClicked == numRows * numCols - mineList.size()){
            gameOver = true;
            textLabel.setText("Yun Win! >W<");
        }
    }

    int countMine(int r, int c){
        if (r < 0|| r >= numRows || c < 0 || c >= numCols){
            return 0;
        }
        if (mineList.contains(board[r][c])){
            return 1;
        }
        return 0;
    }

//    void chord(int r, int c){
//        if (board[r][c].getText() != ""){
//            int minesNear = Integer.parseInt(board[r][c].getText());
//            int checkedMines = 0;
//            if (board[r-1][c+1].getText().equals("?")) checkedMines++;
//            if (board[r-1][c].getText().equals("?")) checkedMines++;
//            if (board[r-1][c-1].getText().equals("?")) checkedMines++;
//            if (board[r][c+1].getText().equals("?")) checkedMines++;
//            if (board[r][c-1].getText().equals("?")) checkedMines++;
//            if (board[r+1][c+1].getText().equals("?")) checkedMines++;
//            if (board[r+1][c-1].getText().equals("?")) checkedMines++;
//            if (board[r+1][c].getText().equals("?")) checkedMines++;
//            if (checkedMines == minesNear){
//
//                for (int row = r-1; row <= r+1; row++){
//                    for (int col = c-1; col <= c+1; col++){
//                        if
//                    }
//                }
//                if (mineList.contains(board[r][c])){
//                    revealMines();
//                }
//            }
//        }
//    }
}

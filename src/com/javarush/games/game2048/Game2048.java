package com.javarush.games.game2048;
import com.javarush.engine.cell.*;


public class Game2048 extends Game {
    //перемен. и объекты
    private static final int SIDE = 4;
    private int [][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score;
    //1 метод. создание окна. 
    public void initialize(){
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    //2 метод. создание основ игры.
    private void createGame(){
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }
    //3 метод. игровое поле (цвета)
    private void drawScene(){
         for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                 setCellColoredNumber(j, i, gameField[i][j]);
        }
    }
    
    }
    //4 метод. создание цифр в ячейках.
   private void createNewNumber(){
        int check = getMaxTileValue();
        if (check == 2048){
            win();
        }
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        int c = getRandomNumber(10) == 9 ? 4 : 2;
        boolean notPlaced = true;
        while (notPlaced){
            if (gameField[x][y] == 0){
                gameField[x][y] = c;
                notPlaced = false;
            }
            else{
                x = getRandomNumber(SIDE);
                y = getRandomNumber(SIDE);
            }
        }
    }
    //5 метод. разные цвета разным цифрам.
    private Color getColorByValue(int value){
        
        if(value == 2)
            return Color.PLUM;
        else if(value == 4)
            return Color.MEDIUMORCHID;
        else if(value == 8)
            return Color.DARKORCHID;
        else if(value == 16)
            return Color.BLUEVIOLET;
        else if(value == 32)
            return Color.MEDIUMPURPLE;
        else if(value == 64)
            return Color.MEDIUMSLATEBLUE;
        else if(value == 128)
            return Color.SLATEBLUE;
        else if(value == 256)
            return Color.DARKSLATEBLUE;
        else if(value == 512)
            return Color.PURPLE;
        else if(value == 1024)
            return Color.DARKMAGENTA;  
        else if(value == 2048)
            return Color.INDIGO;
        else return Color.DARKSALMON;
        
    }
    //6 метод. установка цвета в ячейке, дополнение (5)
    private void setCellColoredNumber(int x, int y, int value){
        setCellValueEx(x, y, getColorByValue(value), ((value == 0) ? "" : "" + value));
    }
    //7 метод. сдвиг влево
    private boolean compressRow(int[] row){
        boolean move = false;
        for(int i = 1; i < row.length; i++){
            if(row[i] == 0) continue;
            if(row[i] != 0 && row[i-1] == 0){
                row[i-1] = row[i];
                row[i] = 0;
                move = true;
                i = 0;
            }
        }
        return move;
    }
    //метод 8. доп (7) при сдвиге делаем сложение
    private boolean mergeRow(int[] row){
         boolean move = false;
         for(int i = 0; i < row.length - 1; i++){
            if(row[i] == row[i+1] && (row[i]!=0)) {
                row[i] *= 2;
                row[i+1] = 0;
                score = score + row[i] + row[i+1];
                setScore(score);
                i++;
                move = true;
            }
            
         }
         return move;
    }
    //метод 9. движение
    @Override
    public void onKeyPress(Key key){
        boolean check = canUserMove();
        if (check == false){
           gameOver(); 
        }
        else 
           if (isGameStopped){
            if (key.equals(Key.SPACE)){
                isGameStopped = false;
                createGame();
                drawScene();
                score = 0;
                setScore(score);
                return;
             }
           }
           else if(!isGameStopped) {
        switch (key) {
            case UP : {
                moveUp();
                drawScene();
                break;
            }
            case DOWN : {
                moveDown();
                drawScene();
                break;
            }
            case LEFT : {
                moveLeft();
                drawScene();
                break;
            }
            case RIGHT : {
                moveRight();
                drawScene();
                break;
            }
        }
        
    }
    }
    private void moveLeft()
    {
        int methodCounter = 0;

        for(int y = 0; y < SIDE; y++)
        {
            if(compressRow(gameField[y]))
                methodCounter++;
        }
        for(int y = 0; y < SIDE; y++)
        {
            if(mergeRow(gameField[y]))
                methodCounter++;
        }
        for(int y = 0; y < SIDE; y++)
        {
            if(compressRow(gameField[y]))
                methodCounter++;
        }
        if (methodCounter > 0)
            createNewNumber();
        
    }
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    
    }
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    //метод 10. Разворачиваем матрицу
    private void rotateClockwise(){
        int[][] rotateMatrix = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                rotateMatrix[i][j] = gameField[gameField.length - j - 1][i];
            }
        }
        gameField = rotateMatrix;
    }
    //метод 11. поиск победного 2048
    private int getMaxTileValue() {
        int start = gameField[0][0];
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                if (start < gameField[j][i]) {
                    start = gameField[j][i];
                }
            }
        }
        return start;
    }
    //метод 12 isStopped
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Winner winner after dinner!",Color.BLACK, 38);
        /*if (click.equals("yes") {
            isGameStopped = false;
        }*/
    }
     
    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Game Over!",Color.BLACK, 38);
    }
    
    private boolean canUserMove(){
        for (int x = 0; x < SIDE; x++){
            for(int y = 0; y < SIDE; y++){

                if (gameField[x][y] == 0){
                    return true;
                }

                if(x < SIDE-1){
                    if (gameField[x][y] == gameField[x + 1][y]){
                        return true;
                    }
                }

                if(y < SIDE-1){
                    if (gameField[x][y] ==gameField[x][y + 1]){
                       return true;
                    }
                }
            }
        }
        return false;
    }
    
   
}
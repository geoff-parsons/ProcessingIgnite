// BASED ON:
//ran tao 
//nature of code
//10.30.06
//week 7

//a basic implementation of John Conway's Game of Life CA
//how could this be improved to use object oriented programming?
//think of it as similar to our particle system, with a "cell" class
//to describe each individual cell and a "cellular automata" class
//to describe a collection of cells

//edited from dan shiffman's gol code

import java.util.Random;

class JavaSlide extends Slide {

  PImage java;
  PImage ran;
  int cellsize = 10;
  int COLS, ROWS;
  //game of life board
  int[][] old_board, new_board, colors;


  public JavaSlide() {
    java = loadImage("java.png");
    ran = loadImage("ran_tao.png");
    smooth();
    //initialize rows, columns and set-up arrays
    COLS = width/cellsize;
    ROWS = height/cellsize;
    old_board = new int[COLS][ROWS];
    new_board = new int[COLS][ROWS];
    colors = new int[COLS][ROWS];
    colorMode(RGB,255,255,255,100);
    background(255);
    //call function to fill array with random values 0 or 1
    initBoard();
    frameRate(30);
  }

  public void draw() {
    fill(255,40);
    rectMode(CORNER);
    
    rect(0,0,width, height);
    grid();
    check();
    render();
    image(java,
      width/2-java.width/2,
      height/2-java.height/2);
    //image(ran,width-ran.width,height-50);
  }

  void grid() {
    for (int a=0; a<=COLS; a++) {
      for (int b=0; b<=ROWS; b++) {
        stroke(225);
        noFill();
        rectMode(CENTER);
        rect(a*cellsize, b*cellsize, cellsize, cellsize);

      }
    }
  }

  void check() {
    Random generator = new Random();
    //loop through every spot in our 2D array and check spots neighbors
    for (int x = 0; x < COLS;x++) {
      for (int y = 0; y < ROWS;y++) {
        int nb = 0;
        //Note the use of mod ("%") below to ensure that cells on the edges have "wrap-around" neighbors
        //above row
        if (old_board[(x+COLS-1) % COLS ][(y+ROWS-1) % ROWS ] == 1) { nb++; }
        if (old_board[ x                ][(y+ROWS-1) % ROWS ] == 1) { nb++; }
        if (old_board[(x+1)      % COLS ][(y+ROWS-1) % ROWS ] == 1) { nb++; }
        //middle row
        if (old_board[(x+COLS-1) % COLS ][ y                ] == 1) { nb++; }
        if (old_board[(x+1)      % COLS ][ y                ] == 1) { nb++; }
        //bottom row
        if (old_board[(x+COLS-1) % COLS ][(y+1)      % ROWS ] == 1) { nb++; }
        if (old_board[ x                ][(y+1)      % ROWS ] == 1) { nb++; }
        if (old_board[(x+1)      % COLS ][(y+1)      % ROWS ] == 1) { nb++; }
        
        //RULES OF "LIFE" HERE
        if ( (old_board[x][y] == 1) && (nb <  2) ) {
          //loneliness
          new_board[x][y] = 0; colors[x][y] = color(255);
        } else if ( (old_board[x][y] == 1) && (nb >  3) ) {
          //overpopulation
          new_board[x][y] = 0; colors[x][y] = color(255,0,0);
        } else if ( (old_board[x][y] == 0) && (nb == 3) ) {
          //reproduction
          new_board[x][y] = 1; colors[x][y] = color(110,155,185);
        } else {
          //stasis
          new_board[x][y] = old_board[x][y]; colors[x][y] = color(110,155,185);
        }
      }
    } 

  }
  
  //RENDER game of life based on "new_board" values
  void render() {
    for ( int i = 0; i < COLS;i++) {
      for ( int j = 0; j < ROWS;j++) {
        if ((new_board[i][j] == 1)) {
          // fill(255);
          fill(colors[i][j]);
          noStroke();
          ellipse(i*cellsize,j*cellsize,cellsize,cellsize);
        }
      }
    }
    //swap old and new game of life boards
    int[][] tmp = old_board;
    old_board = new_board;
    new_board = tmp;
  }

  //init board with random "alive" squares
  void initBoard() {
    background(255);
    for (int i =0;i < COLS;i++) {
      for (int j =0;j < ROWS;j++) {
        if (int(random(5)) <= 2) {
          old_board[i][j] = 1;
        } else {
          old_board[i][j] = 0;
        }
      }
    }
  }
  
  public void keyPressed() {
    initBoard();
  }
}

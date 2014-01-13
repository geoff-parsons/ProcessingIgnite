import processing.core.*; 
import processing.xml.*; 

import processing.video.*; 
import processing.video.*; 
import processing.video.*; 
import processing.video.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class ProcessingIgnite extends PApplet {




int width  = 1024; // in pixels
int height = 768;  // in pixels

//int width  = 1440;
//int height = 900;

int delay = 15;    // in seconds

Slide currentSlide;
SlideDeck slides;


long timestamp;
long nextSlide;
Date date;

public long getTime() {
  date = new Date();
  return date.getTime()/1000;
}

/**
 * Called when the presentation is finished.
 **/
public void theEnd() {
  slides = null;
  background(0);
  noLoop();
}

public void setup() {
  size(width, height, P2D );
  
  // initialize the slide deck
  slides = new SlideDeck();
  slides.addSlide(new IntroSlide());
  slides.addSlide(new ImageSlide("media_lab.png",0,0,0));
  slides.addSlide(new AnimationSlide());
  slides.addSlide(new JavaSlide());
  slides.addSlide(new ImageSlide("processing_window.png"));
  slides.addSlide(new CodeStructureSlide());
  slides.addSlide(new ExampleCode2DSlide());
  slides.addSlide(new ExampleCode3DSlide());
  slides.addSlide(new LibraryExampleSlide());
  slides.addSlide(new UsesSlide());
  slides.addSlide(new ImageSlide("ben_fry_dna.png"));
  slides.addSlide(new ImageSlide("zip.png",51,51,51));
  slides.addSlide(new ImageSlide("asteroids.png",0,0,0));
  slides.addSlide(new ImageSlide("ar.png",0,0,0));
  slides.addSlide(new ASCIIVideoSlide(this));
  slides.addSlide(new MultiTouchSlide());
  slides.addSlide(new ImageSlide("healthstation.png"));
  slides.addSlide(new ImageSlide("csdk.jpg"));
  slides.addSlide(new ImageSlide("processing_window.png"));
  slides.addSlide(new CreditsSlide());
  
  currentSlide = slides.getSlide(0);
  
  timestamp = getTime();
  nextSlide = timestamp + delay;
}

public void draw() {
  timestamp = getTime();
  if( timestamp == nextSlide )
    nextSlide();
  currentSlide.draw();

  drawProgressBar();
}

public void drawProgressBar() {
  noStroke();
  rectMode(CORNERS);
  
  float percentDelay = (int)(nextSlide - timestamp) / (float)delay;

  if(percentDelay > 0.5f)
    fill(0,255,0);
  else if(percentDelay > 0.25f)
    fill(255,255,0);
  else
    fill(255,0,0);

  rect( width - (int)(100 * percentDelay), height-3, width, height );
}

public void updateTimer() {
  nextSlide = timestamp + delay;
}

public void nextSlide() {
  updateTimer();
  if( slides.currentSlideNum() < slides.numSlides() ) {
    currentSlide.teardown();
    currentSlide = slides.nextSlide();
    currentSlide.setup();
  }
}

public void previousSlide() {
  updateTimer();
  if( slides.currentSlideNum() > 0 ) {
    currentSlide.teardown();
    currentSlide = slides.previousSlide();
    currentSlide.setup();
  }
}

public void keyPressed() {
  if( key == ' ' || keyCode == RIGHT )
    nextSlide();
  else if( keyCode == LEFT )
    previousSlide();
  else
    currentSlide.keyPressed();
}




class ASCIIVideoSlide extends Slide {

  Capture video;
  boolean cheatScreen;
  // All ASCII characters, sorted according to their visual density
  String letterOrder =
    " .`-_':,;^=+/\"|)\\<>)iv%xclrs{*}I?!][1taeo7zjLu" +
    "nT#JCwfy325Fp6mqSghVd4EgXPGZbYkOA&8U$@KHDBWNMR0Q";
  char[] letters;

  float[] bright;
  char[] chars;

  PFont font;
  float fontSize = 1.5f;

  public ASCIIVideoSlide(PApplet sketch) {
    font = loadFont("UniversLTStd-Light-48.vlw");
    video = new Capture(sketch, 100, 75, 15);
    int count = video.width * video.height;

    // for the 256 levels of brightness, distribute the letters across
    // the an array of 256 elements to use for the lookup
    letters = new char[256];
    for (int i = 0; i < 256; i++) {
      int index = PApplet.parseInt(map(i, 0, 256, 0, letterOrder.length()));
      letters[i] = letterOrder.charAt(index);
    }

    // current characters for each position in the video
    chars = new char[count];

    // current brightness for each point
    bright = new float[count];
    for (int i = 0; i < count; i++) {
      // set each brightness at the midpoint to start
      bright[i] = 128;
    }
  }


  public void draw() {
    background(0);
    if(video.available())
      video.read();
    pushMatrix();

    float hgap = width / PApplet.parseFloat(video.width);
    float vgap = height / PApplet.parseFloat(video.height);

    scale(max(hgap, vgap) * fontSize);
    textFont(font, fontSize);

    int index = 0;
    for (int y = 1; y < video.height; y++) {

      // Move down for next line
      translate(0,  1.0f / fontSize);

      pushMatrix();
      for (int x = 0; x < video.width; x++) {
        int pixelColor = video.pixels[index];
        // Faster method of calculating r, g, b than red(), green(), blue() 
        int r = (pixelColor >> 16) & 0xff;
        int g = (pixelColor >> 8) & 0xff;
        int b = pixelColor & 0xff;

        // Another option would be to properly calculate brightness as luminance:
        // luminance = 0.3*red + 0.59*green + 0.11*blue
        // Or you could instead red + green + blue, and make the the values[] array
        // 256*3 elements long instead of just 256.
        int pixelBright = max(r, g, b);

        // The 0.1 value is used to damp the changes so that letters flicker less
        float diff = pixelBright - bright[index];
        bright[index] += diff * 0.1f;

        fill(pixelColor);

        int num = PApplet.parseInt(bright[index]);
        text(letters[num], 0, 0);

        // Move to the next pixel
        index++;

        // Move over for next character
        translate(1.0f / fontSize, 0);
      }
      popMatrix();
    }
    popMatrix();

    if (cheatScreen) {
      //image(video, 0, height - video.height);
      // set() is faster than image() when drawing untransformed images
      set(0, height - video.height, video);
    }
  }


  /**
   * Handle key presses:
   * 'c' toggles the cheat screen that shows the original image in the corner
   * 'g' grabs an image and saves the frame to a tiff image
   * 'f' and 'F' increase and decrease the font size
   */
  public void keyPressed() {
    switch (key) {
    case 'g': 
      saveFrame(); 
      break;
    case 'c': 
      cheatScreen = !cheatScreen; 
      break;
    case 'f': 
      fontSize *= 1.1f; 
      break;
    case 'F': 
      fontSize *= 0.9f; 
      break;
    }
  }

}


class AnimationSlide extends Slide {

  Flock flock1, flock2;
  PImage graphics;
  PImage animation;
  PImage interaction;
  
  public AnimationSlide() {
    graphics = loadImage("graphics.png");
    animation = loadImage("animation.png");
    interaction = loadImage("interaction.png");
    
    flock1 = new Flock();
    for (int i = 0; i < 100; i++)
      flock1.addBoid(new Boid(new PVector(50,height-50), 3.5f, 0.05f, 1));
    flock2 = new Flock();
    for (int i = 0; i < 100; i++)
      flock2.addBoid(new Boid(new PVector(width-50,50), 3.5f, 0.05f, 2));
  }
  
  public void draw() {
    smooth();
    background(50);
    flock1.run();
    flock2.run();
    image(graphics,0,40);
    image(animation,0,120);
    image(interaction,0,200);
  }
}


// The Boid class
class Boid {

  PVector loc;
  PVector vel;
  PVector acc;
  float r;
  float maxforce;    // Maximum steering force
  float maxspeed;    // Maximum speed
  int team;
  float a=1.0f;

  Boid(PVector l, float ms, float mf, int t) {
    acc = new PVector(0,0);
    vel = new PVector(random(-1,1),random(-1,1));
    loc = l.get();
    r = 2.0f;
    team = t;
    maxspeed = ms;
    maxforce = mf;
    a = random(25,100);
  }

  public void run(ArrayList boids) {
    flock(boids);
    update();
    borders();
    render();
  }

  // We accumulate a new acceleration each time based on three rules
  public void flock(ArrayList boids) {
    PVector sep = separate(boids);   // Separation
    PVector ali = align(boids);      // Alignment
    PVector coh = cohesion(boids);   // Cohesion
    // Arbitrarily weight these forces
    sep.mult(1.5f);
    ali.mult(1.0f);
    coh.mult(1.0f);
    // Add the force vectors to acceleration
    acc.add(sep);
    acc.add(ali);
    acc.add(coh);
  }

  // Method to update location
  public void update() {
    // Update velocity
    vel.add(acc);
    // Limit speed
    vel.limit(maxspeed);
    loc.add(vel);
    // Reset accelertion to 0 each cycle
    acc.mult(0);
  }

  public void seek(PVector target) {
    acc.add(steer(target,false));
  }

  public void arrive(PVector target) {
    acc.add(steer(target,true));
  }

  // A method that calculates a steering vector towards a target
  // Takes a second argument, if true, it slows down as it approaches the target
  public PVector steer(PVector target, boolean slowdown) {
    PVector steer;  // The steering vector
    PVector desired = target.sub(target,loc);  // A vector pointing from the location to the target
    float d = desired.mag(); // Distance from the target is the magnitude of the vector
    // If the distance is greater than 0, calc steering (otherwise return zero vector)
    if (d > 0) {
      // Normalize desired
      desired.normalize();
      // Two options for desired vector magnitude (1 -- based on distance, 2 -- maxspeed)
      if ((slowdown) && (d < 100.0f)) desired.mult(maxspeed*(d/100.0f)); // This damping is somewhat arbitrary
      else desired.mult(maxspeed);
      // Steering = Desired minus Velocity
      steer = target.sub(desired,vel);
      steer.limit(maxforce);  // Limit to maximum steering force
    } 
    else {
      steer = new PVector(0,0);
    }
    return steer;
  }

  public void render() {
    // Draw a triangle rotated in the direction of velocity
    float theta = vel.heading2D() + PI/2;
    if(team == 1) {
      fill(110,155,185,a);
      stroke(80,110,135,a);
    } else if(team == 2) {
      fill(85,225,135,a);
      stroke(35,105,45,a);
    }
    pushMatrix();
    translate(loc.x,loc.y);
    rotate(theta);
    beginShape(TRIANGLES);
    vertex(0, -r*8);
    vertex(-r*5, r*10);
    vertex(r*5, r*10);
    endShape();
    popMatrix();
  }

  // Wraparound
  public void borders() {
    if (loc.x < -r) loc.x = width+r;
    if (loc.y < -r) loc.y = height+r;
    if (loc.x > width+r) loc.x = -r;
    if (loc.y > height+r) loc.y = -r;
  }

  // Separation
  // Method checks for nearby boids and steers away
  public PVector separate (ArrayList boids) {
    float desiredseparation = 20.0f;
    PVector steer = new PVector(0,0,0);
    int count = 0;
    // For every boid in the system, check if it's too close
    for (int i = 0 ; i < boids.size(); i++) {
      Boid other = (Boid) boids.get(i);
      float d = PVector.dist(loc,other.loc);
      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
      if ((d > 0) && (d < desiredseparation)) {
        // Calculate vector pointing away from neighbor
        PVector diff = PVector.sub(loc,other.loc);
        diff.normalize();
        diff.div(d);        // Weight by distance
        steer.add(diff);
        count++;            // Keep track of how many
      }
    }
    // Average -- divide by how many
    if (count > 0) {
      steer.div((float)count);
    }

    // As long as the vector is greater than 0
    if (steer.mag() > 0) {
      // Implement Reynolds: Steering = Desired - Velocity
      steer.normalize();
      steer.mult(maxspeed);
      steer.sub(vel);
      steer.limit(maxforce);
    }
    return steer;
  }

  // Alignment
  // For every nearby boid in the system, calculate the average velocity
  public PVector align (ArrayList boids) {
    float neighbordist = 25.0f;
    PVector steer = new PVector(0,0,0);
    int count = 0;
    for (int i = 0 ; i < boids.size(); i++) {
      Boid other = (Boid) boids.get(i);
      float d = PVector.dist(loc,other.loc);
      if ((d > 0) && (d < neighbordist)) {
        steer.add(other.vel);
        count++;
      }
    }
    if (count > 0) {
      steer.div((float)count);
    }

    // As long as the vector is greater than 0
    if (steer.mag() > 0) {
      // Implement Reynolds: Steering = Desired - Velocity
      steer.normalize();
      steer.mult(maxspeed);
      steer.sub(vel);
      steer.limit(maxforce);
    }
    return steer;
  }

  // Cohesion
  // For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
  public PVector cohesion (ArrayList boids) {
    float neighbordist = 25.0f;
    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
    int count = 0;
    for (int i = 0 ; i < boids.size(); i++) {
      Boid other = (Boid) boids.get(i);
      float d = loc.dist(other.loc);
      if ((d > 0) && (d < neighbordist)) {
        sum.add(other.loc); // Add location
        count++;
      }
    }
    if (count > 0) {
      sum.div((float)count);
      return steer(sum,false);  // Steer towards the location
    }
    return sum;
  }
}


// The Flock (a list of Boid objects)

class Flock {
  ArrayList boids; // An arraylist for all the boids

  Flock() {
    boids = new ArrayList(); // Initialize the arraylist
  }

  public void run() {
    for (int i = 0; i < boids.size(); i++) {
      Boid b = (Boid) boids.get(i);  
      b.run(boids);  // Passing the entire list of boids to each boid individually
    }
  }

  public void addBoid(Boid b) {
    boids.add(b);
  }

}

class CodeStructureSlide extends Slide {
  
  PImage title;
  PImage setup;
  PImage drawLoop;
  
  public CodeStructureSlide() {
    title = loadImage("code_structure_title.png");
    setup = loadImage("setup.png");
    drawLoop = loadImage("drawLoop.png");
  }
  
  public void draw() {
    background(255);
    imageMode(CORNER);
    image(title, width/2-title.width/2, 25);
    image(setup, 0, 125);
    image(drawLoop, 0, 450);
  }

}

class CreditsSlide extends Slide {
  
  PImage geoff, name, dept, processing;
  int numMovers = 4;
  float spring = 0.005f;
  float gravity = 0.1f;
  float friction = -1;
  Mover[] movers = new Mover[numMovers];
  
  public CreditsSlide() {
    randomSeed(5464);
    
    geoff = loadImage("geoff.png");
    movers[0] = new Mover(geoff, random(0,width), random(0,height));
    
    dept = loadImage("dept.png");
    movers[1] = new Mover(dept, random(0,width), random(0,height));
    
    name = loadImage("name.png");
    movers[2] = new Mover(name, random(0,width), random(0,height));
    
    processing = loadImage("processing_logo.png");
    movers[3] = new Mover(processing, random(0,width), random(0,height));
  }
  
  public void draw() {
    background(200);
    imageMode(CORNER);
    smooth();
    for (int i = 0; i < numMovers; i++) {
      movers[i].move();
      movers[i].display();  
    }
  }

  class Mover {
  float x, y;
  PImage img;
  float vx = 0;
  float vy = 0;
 
  Mover(PImage img, float xin, float yin) {
    x = xin;
    y = yin;
    this.img = img;
    vx = random(-2,2);
    vy = random(-2,2);
  } 
 
  public void move() {
    //vy += gravity;
    x += vx;
    y += vy;
    if (x + img.width > width) {
      x = width - img.width;
      vx *= friction; 
    }
    else if (x < 0) {
      x = 0;
      vx *= friction;
    }
    if (y + img.height > height) {
      y = height - img.height;
      vy *= friction; 
    } 
    else if (y < 0) {
      y = 0;
      vy *= friction;
    }
  }
  
  public void display() {
    image(img,x,y);
  }
}

}


class ExampleCode2DSlide extends Slide {
  
  PImage title;
  PImage rectExample;
  PImage shapeExample;
  int dropY=0;
  
  public ExampleCode2DSlide() {
    title = loadImage("2d_title.png");
    rectExample = loadImage("rectExample.png");
    shapeExample = loadImage("shapeExample.png");
  }
  
  public void draw() {    
    background(255);
    rectMode(CORNER);
    imageMode(CORNER);
    stroke(25);

    image(title, width/2-title.width/2, 25);
    image(rectExample, 0, 150);
    image(shapeExample, 0, 325); 
       
    fill(72,197,71);
    beginShape();
    vertex(width-25, height-25);
    vertex(width-25, height-125);
    vertex(width-50, height-125);
    vertex(width-50, height-50);
    vertex(width-75, height-50);
    vertex(width-75, height-25);
    endShape(CLOSE);
    
    fill(221,69,55);
    beginShape();
    vertex(width-50, height-50);
    vertex(width-50, height-100);
    vertex(width-125, height-100);
    vertex(width-125, height-75);
    vertex(width-75, height-75);
    vertex(width-75, height-50);
    endShape(CLOSE);
    
    fill(240,238,38);
    beginShape();
    vertex(width-125, height-25);
    vertex(width-125, height-50);
    vertex(width-150, height-50);
    vertex(width-150, height-75);
    vertex(width-175, height-75);
    vertex(width-175, height-50);
    vertex(width-200, height-50);
    vertex(width-200, height-25);
    endShape(CLOSE);
    
    fill(4,124,197);
    rect(width-125, height-75, 50, 50);

    fill(192,38,178);
    rect(width-150, height-150, 25, 100);
    
    fill(241,171,65);
    beginShape();
    vertex(width-100, height-225-dropY);
    vertex(width-100, height-250-dropY);
    vertex(width-75, height-250-dropY);
    vertex(width-75, height-275-dropY);
    vertex(width-25, height-275-dropY);
    vertex(width-25, height-250-dropY);
    vertex(width-50, height-250-dropY);
    vertex(width-50, height-225-dropY);
    endShape(CLOSE);
    
    if(dropY >= -120)
      dropY -= 5;

  }

}

class ExampleCode3DSlide extends Slide {
  
  PImage slide;
  
  public ExampleCode3DSlide() {
    slide = loadImage("basic3d.png");
  }
    
  public void draw() {
    background(255);
    imageMode(CORNER);
    image(slide, 0,0);
  }

}


class ImageSlide extends Slide {
 
  PImage image;
  int r,g,b;
  
  public ImageSlide(PImage image, int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.image = image;
  }
  public ImageSlide(PImage image) {
    this(image,255,255,255);
  }
  public ImageSlide(String imageName) {
    this(loadImage(imageName));
  }
  public ImageSlide(String imageName, int r, int g, int b) {
    this(loadImage(imageName), r, g, b);
  }
 
  public void draw() {
    background(r,g,b);
    image(this.image,
          width/2-this.image.width/2,
          height/2-this.image.height/2);
  }
  
}


class IntroSlide extends Slide {
  
  PImage logo;
  PImage title;
  
  public IntroSlide() {
    logo = loadImage("processing_logo.png");
    title = loadImage("processing_title.png");
  }
    
  public void draw() {
    background(140);
    image(logo, width/2-logo.width/2, height/2-logo.height);
    image(title, width/2-title.width/2, height/2+100);
  }

}
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

  public void grid() {
    for (int a=0; a<=COLS; a++) {
      for (int b=0; b<=ROWS; b++) {
        stroke(225);
        noFill();
        rectMode(CENTER);
        rect(a*cellsize, b*cellsize, cellsize, cellsize);

      }
    }
  }

  public void check() {
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
  public void render() {
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
  public void initBoard() {
    background(255);
    for (int i =0;i < COLS;i++) {
      for (int j =0;j < ROWS;j++) {
        if (PApplet.parseInt(random(5)) <= 2) {
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



class LibraryExampleSlide extends Slide {
  
  PImage slide;
  
  public LibraryExampleSlide() {
    slide = loadImage("library.png");
  }
    
  public void draw() {
    background(255);
    imageMode(CORNER);
    image(slide, 0,0);
  }
  
}


class MovieSlide extends Slide {
  
  Movie movie;
  int r,g,b;
  PImage frame;
  PApplet sketch;
  String fileName;
  
  public MovieSlide(PApplet sketch, String fileName) {
    this(sketch,fileName,0,0,0);
  }
  
  public MovieSlide(PApplet sketch, String fileName, int r, int g, int b) {
    this.sketch = sketch;
    this.fileName = fileName;
    this.r = r;
    this.g = g;
    this.b = b;
    movie = new Movie(sketch, fileName);
    movie.stop();
  }
  
  public void setup() {
    //movie = new Movie(sketch, fileName);
    println("MOVIE SETUP");
    movie.loop();
  }
   
  public void draw() {
    background(r,g,b);
    if(movie.available()) {
      movie.read();
      image(movie, sketch.width/2-movie.width/2, sketch.height/2-movie.height/2, movie.width, movie.height);
    }
  }
  
  public void teardown() {
    println("MOVIE TEARDOWN");
    movie.pause();
    movie.noLoop();
    movie.stop();
  }

}

class MultiTouchSlide extends Slide {

  PImage slide;

  public MultiTouchSlide() {
    slide = loadImage("multitouch.jpg");
  }

  public void draw() {
    background(255);
    image(slide, width/2-slide.width/2,height/2-slide.height/2);
  }

}

/**
 * Abstract Slide class from which all slides must inherit.
 */
public abstract class Slide {

  public Slide() { }
  
  public void setup() { println("DEFAULT SETUP"); }
  
  public void draw() {
    background(0);
  }
  
  public void teardown() { println("DEFAULT TEARDOWN"); }
  
  /**
   * Key events will be passed on to the current slide with the
   * exception of right and left arrows and the spacebar which
   * are used for slide control.
   */
  public void keyPressed() { }
}

public class SlideDeck {
  
  private ArrayList slides;
  private int currentSlide;
  
  public SlideDeck() {
    slides = new ArrayList();
  }
  
  public void addSlide(Slide slide) {
    slides.add(slide);
  }
  
  public void addSlides(ArrayList slides) {
    slides.addAll(slides);
  }
  
  /**
   * Returns the next slide in the deck and advances the current slide counter.
   */
  public Slide nextSlide() {
    return (Slide) slides.get(++currentSlide);
  }
  
  /**
   * Returns the previous slide in the deck and rolls back the current slide counter.
   */
  public Slide previousSlide() {
    return (Slide) slides.get(--currentSlide);
  }
  
  /**
   * Gets a specific slide in the deck and sets that number to the current slide.
   * If the number is less than 0 the first slide will be returned, likewise if
   * the number is greater than the index of the last slide the last slide will
   * be returned instead.
   *
   * @param number int, slide number
   * @related currentSlideNum()
   */
  public Slide getSlide(int number) {
    if( number < 0 ) {
      currentSlide = 0;
      return (Slide) slides.get(0);
    } else if( number < slides.size() ) {
      currentSlide = number;
      return (Slide) slides.get(number);
    }
    currentSlide = slides.size()-1;
    return (Slide) slides.get( slides.size()-1 );
  }
  public ArrayList getSlides() {
    return slides;
  }
  
  public int currentSlideNum() {
    return currentSlide;
  }
  public int numSlides() {
    return slides.size();
  }
  
}

class UsesSlide extends Slide {
  
  PImage visualization;
  PImage interaction;
  PImage sketches;
  
  int numBlobs = 3;

int[] blogPx = { 0, 90, 90 };
int[] blogPy = { 0, 120, 45 };

// Movement vector for each blob
int[] blogDx = { 1, 1, 1 };
int[] blogDy = { 1, 1, 1 };

PGraphics pg;
int[][] vy,vx; 
  
  public UsesSlide() {
    visualization = loadImage("viz.png");
    interaction = loadImage("interaction.png");
    sketches = loadImage("sketches.png");
      pg = createGraphics(160, 90, P2D);    
  vy = new int[numBlobs][pg.height];
  vx = new int[numBlobs][pg.width];
  }
    
  public void draw() {
    background(255);
    imageMode(CORNER);
    smooth();
   
    for (int i=0; i<numBlobs; ++i) {
      blogPx[i]+=blogDx[i];
      blogPy[i]+=blogDy[i];

      // bounce across screen
      if (blogPx[i] < 0) {
        blogDx[i] = 1;
      }
      if (blogPx[i] > pg.width) {
        blogDx[i] = -1;
      }
      if (blogPy[i] < 0) {
        blogDy[i] = 1;
      }
      if (blogPy[i] > pg.height) {
        blogDy[i]=-1;
      }

      for (int x = 0; x < pg.width; x++) {
        vx[i][x] = PApplet.parseInt(sq(blogPx[i]-x));
      }

      for (int y = 0; y < pg.height; y++) {
        vy[i][y] = PApplet.parseInt(sq(blogPy[i]-y)); 
      }
    }

  // Output into a buffered image for reuse
  pg.beginDraw();
  pg.loadPixels();
  for (int y = 0; y < pg.height; y++) {
    for (int x = 0; x < pg.width; x++) {
      int m = 1;
      for (int i = 0; i < numBlobs; i++) {
        // Increase this number to make your blobs bigger
        m += 40000/(vy[i][y] + vx[i][x]+1);
      }
      pg.pixels[x+y*pg.width] = color(0, m+x, (x+m+y)/2);
    }
  }
  pg.updatePixels();
  pg.endDraw();

  // Display the results
  image(pg, 0, 0, width, height); 
    image(visualization,0,height/2-visualization.height/2-100);
    image(interaction,0,height/2-interaction.height/2);
    image(sketches,0,height/2-sketches.height/2+100);
  }

}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "ProcessingIgnite" });
  }
}

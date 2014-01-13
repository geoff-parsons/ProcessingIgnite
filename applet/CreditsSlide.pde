
class CreditsSlide extends Slide {
  
  PImage geoff, name, dept, processing;
  int numMovers = 4;
  float spring = 0.005;
  float gravity = 0.1;
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
 
  void move() {
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
  
  void display() {
    image(img,x,y);
  }
}

}


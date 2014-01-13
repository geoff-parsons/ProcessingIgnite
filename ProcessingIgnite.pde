
import processing.video.*;
import java.util.Date;

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

long getTime() {
  date = new Date();
  return date.getTime()/1000;
}

/**
 * Called when the presentation is finished.
 **/
void theEnd() {
  slides = null;
  background(0);
  noLoop();
}

void setup() {
  println("SETUP BEGIN");
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
  println("Setup Complete");
}

void draw() {
  timestamp = getTime();
  if( timestamp == nextSlide )
    nextSlide();
  currentSlide.draw();

  drawProgressBar();
}

void drawProgressBar() {
  noStroke();
  rectMode(CORNERS);
  
  float percentDelay = (int)(nextSlide - timestamp) / (float)delay;

  if(percentDelay > 0.5)
    fill(0,255,0);
  else if(percentDelay > 0.25)
    fill(255,255,0);
  else
    fill(255,0,0);

  rect( width - (int)(100 * percentDelay), height-3, width, height );
}

void updateTimer() {
  nextSlide = timestamp + delay;
}

void nextSlide() {
  updateTimer();
  if( slides.currentSlideNum() < slides.numSlides() ) {
    currentSlide.teardown();
    currentSlide = slides.nextSlide();
    currentSlide.setup();
  }
}

void previousSlide() {
  updateTimer();
  if( slides.currentSlideNum() > 0 ) {
    currentSlide.teardown();
    currentSlide = slides.previousSlide();
    currentSlide.setup();
  }
}

void keyPressed() {
  if( key == ' ' || keyCode == RIGHT )
    nextSlide();
  else if( keyCode == LEFT )
    previousSlide();
  else
    currentSlide.keyPressed();
}




import processing.video.*;

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

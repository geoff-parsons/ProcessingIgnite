
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


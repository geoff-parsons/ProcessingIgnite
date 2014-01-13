
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

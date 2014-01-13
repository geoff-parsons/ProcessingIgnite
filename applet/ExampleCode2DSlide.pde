
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



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

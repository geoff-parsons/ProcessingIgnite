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

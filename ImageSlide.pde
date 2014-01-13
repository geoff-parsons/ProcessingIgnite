
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

import processing.video.*;

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


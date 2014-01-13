
/**
 * Abstract Slide class from which all slides must inherit.
 */
public abstract class Slide {

  public Slide() { }
  
  public void setup() { }
  
  public void draw() {
    background(0);
  }
  
  public void teardown() { }
  
  /**
   * Key events will be passed on to the current slide with the
   * exception of right and left arrows and the spacebar which
   * are used for slide control.
   */
  public void keyPressed() { }
}


public class SlideDeck {
  
  private ArrayList slides;
  private int currentSlide;
  
  public SlideDeck() {
    slides = new ArrayList();
  }
  
  public void addSlide(Slide slide) {
    slides.add(slide);
  }
  
  public void addSlides(ArrayList slides) {
    slides.addAll(slides);
  }
  
  /**
   * Returns the next slide in the deck and advances the current slide counter.
   */
  public Slide nextSlide() {
    return (Slide) slides.get(++currentSlide);
  }
  
  /**
   * Returns the previous slide in the deck and rolls back the current slide counter.
   */
  public Slide previousSlide() {
    return (Slide) slides.get(--currentSlide);
  }
  
  /**
   * Gets a specific slide in the deck and sets that number to the current slide.
   * If the number is less than 0 the first slide will be returned, likewise if
   * the number is greater than the index of the last slide the last slide will
   * be returned instead.
   *
   * @param number int, slide number
   * @related currentSlideNum()
   */
  public Slide getSlide(int number) {
    if( number < 0 ) {
      currentSlide = 0;
      return (Slide) slides.get(0);
    } else if( number < slides.size() ) {
      currentSlide = number;
      return (Slide) slides.get(number);
    }
    currentSlide = slides.size()-1;
    return (Slide) slides.get( slides.size()-1 );
  }
  public ArrayList getSlides() {
    return slides;
  }
  
  public int currentSlideNum() {
    return currentSlide;
  }
  public int numSlides() {
    return slides.size();
  }
  
}


class UsesSlide extends Slide {
  
  PImage visualization;
  PImage interaction;
  PImage sketches;
  
  int numBlobs = 3;

int[] blogPx = { 0, 90, 90 };
int[] blogPy = { 0, 120, 45 };

// Movement vector for each blob
int[] blogDx = { 1, 1, 1 };
int[] blogDy = { 1, 1, 1 };

PGraphics pg;
int[][] vy,vx; 
  
  public UsesSlide() {
    visualization = loadImage("viz.png");
    interaction = loadImage("interaction.png");
    sketches = loadImage("sketches.png");
      pg = createGraphics(160, 90, P2D);    
  vy = new int[numBlobs][pg.height];
  vx = new int[numBlobs][pg.width];
  }
    
  public void draw() {
    background(255);
    imageMode(CORNER);
    smooth();
   
    for (int i=0; i<numBlobs; ++i) {
      blogPx[i]+=blogDx[i];
      blogPy[i]+=blogDy[i];

      // bounce across screen
      if (blogPx[i] < 0) {
        blogDx[i] = 1;
      }
      if (blogPx[i] > pg.width) {
        blogDx[i] = -1;
      }
      if (blogPy[i] < 0) {
        blogDy[i] = 1;
      }
      if (blogPy[i] > pg.height) {
        blogDy[i]=-1;
      }

      for (int x = 0; x < pg.width; x++) {
        vx[i][x] = int(sq(blogPx[i]-x));
      }

      for (int y = 0; y < pg.height; y++) {
        vy[i][y] = int(sq(blogPy[i]-y)); 
      }
    }

  // Output into a buffered image for reuse
  pg.beginDraw();
  pg.loadPixels();
  for (int y = 0; y < pg.height; y++) {
    for (int x = 0; x < pg.width; x++) {
      int m = 1;
      for (int i = 0; i < numBlobs; i++) {
        // Increase this number to make your blobs bigger
        m += 40000/(vy[i][y] + vx[i][x]+1);
      }
      pg.pixels[x+y*pg.width] = color(0, m+x, (x+m+y)/2);
    }
  }
  pg.updatePixels();
  pg.endDraw();

  // Display the results
  image(pg, 0, 0, width, height); 
    image(visualization,0,height/2-visualization.height/2-100);
    image(interaction,0,height/2-interaction.height/2);
    image(sketches,0,height/2-sketches.height/2+100);
  }

}

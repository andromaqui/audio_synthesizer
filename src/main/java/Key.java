import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Key {
  private final String name;        // key name (e.g., C)
  private final boolean isBlack;    // is it a black key?
  private final char keyStroke;     // keyboard keystroke that correspond to piano key

  // initial font sizes
  private static final int DEFAULT_FONT_SIZE_BLACK_KEY = 16;
  private static final int DEFAULT_FONT_SIZE_WHITE_KEY = 18;

  // rectangle for key
  // (coordinate system is scaled so that white keys have width and height 1.0)
  private final double xmin, xmax, ymin, ymax;

  public Key(double x, String name, char keyStroke, boolean isBlack) {
    this.name = name;
    this.keyStroke = keyStroke;
    this.isBlack = isBlack;

    if (!isBlack) {
      xmin = x;
      xmax = x + 1;
      ymin = 0.0;
      ymax = 1.0;
    }
    else {
      xmin = x - 0.3;
      xmax = x + 0.3;
      ymin = 0.0;
      ymax = 0.6;
    }
  }

  // draw the key using the given background and foreground colors
  public void draw(Graphics2D g, double width, double height,
                    Color backgroundColor, Color foregroundColor, int whiteKeysSize) {
    double SCALE_X = width / whiteKeysSize;
    double SCALE_Y = height;
    Rectangle2D.Double rectangle = new Rectangle2D.Double(xmin * SCALE_X,
                                                          ymin * SCALE_Y,
                                                          (xmax - xmin) * SCALE_X,
                                                          (ymax - ymin) * SCALE_Y);

    // black key
    if (isBlack) {
      g.setColor(backgroundColor);
      g.fill(rectangle);
      g.setFont(getFont(DEFAULT_FONT_SIZE_BLACK_KEY, width, height, whiteKeysSize));
      FontMetrics metrics = g.getFontMetrics();
      int ws = metrics.stringWidth(keyStroke + "");
      g.setColor(foregroundColor);
      g.drawString(keyStroke + "",
                   (float) ((xmin + xmax) / 2.0 * SCALE_X - ws / 2.0),
                   25.0f);
    }

    // white key
    else {
      g.setColor(backgroundColor);
      g.fill(rectangle);

      // include outline (since fill color might be white)
      g.setColor(Color.BLACK);
      g.draw(rectangle);

      g.setFont(getFont(DEFAULT_FONT_SIZE_WHITE_KEY, width, height, whiteKeysSize));
      FontMetrics metrics = g.getFontMetrics();
      int hs = metrics.getHeight();
      int ws = metrics.stringWidth(name);
      g.setColor(foregroundColor);
      g.drawString(keyStroke + "",
                   (float) ((xmin + xmax) / 2.0 * SCALE_X - ws / 2.0),
                   (float) (0.95 * SCALE_Y - hs / 2.0));
    }

  }

  private Font getFont(int defaultFontSize, double width, double height, int whiteKeySize) {
    int initialWidth = 50 * whiteKeySize;
    int initialHeight = 170;

    int size = (int) (width * defaultFontSize / initialWidth);
    if (height < initialHeight / 2.0) size = 0;
    if (width < initialWidth / 2.0) size = 0;
    return new Font("SansSerif", Font.PLAIN, size);
  }

  // the computer keyboard keystroke corresponding to this piano key
  public char getKeyStroke() {
    return keyStroke;
  }
}
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.TreeSet;


// the JPanel for drawing the keyboard
public class PianoKeys extends SynthControlContainer {
  public static final long serialVersionUID = 12558137269921L;

  // white and black keys
  private LinkedList<Key> blackKeys = new LinkedList<Key>();
  private LinkedList<Key> whiteKeys = new LinkedList<Key>();

  // piano key colors
  private static final boolean WHITE_KEY = false;
  private static final boolean BLACK_KEY = true;
  private TreeSet<Character> keysDown = new TreeSet<Character>();

  private Key mouseKey = null;

  public PianoKeys(SynthesizerRemastered synthesizerRemastered) {
    super(synthesizerRemastered);
    this.setPreferredSize(new Dimension(1000, 500));
    this.setFocusable(true);
    initKeyboardData();
  }

  @Override
  public void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Graphics2D g = (Graphics2D) graphics;
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      Dimension size = getSize();
      double width = size.getWidth();
      double height = size.getHeight();

      // first, draw the white keys
      for (Key whiteKey : whiteKeys) {

        // mouse click or key typed
        if ((whiteKey == mouseKey) ||  keysDown.contains(whiteKey.getKeyStroke())) {
          whiteKey.draw(g, width, height, Color.BLUE, Color.WHITE, whiteKeys.size());
        }
        else {
          whiteKey.draw(g, width, height, Color.WHITE, Color.BLACK, whiteKeys.size());
        }
      }

      // then, draw the black keys
      for (Key blackKey : blackKeys) {

        // mouse click or key typed
        if ((blackKey == mouseKey) || keysDown.contains(blackKey.getKeyStroke())) {
          blackKey.draw(g, width, height, Color.BLUE, Color.WHITE, whiteKeys.size());
        }

        // draw as usual
        else {
          blackKey.draw(g, width, height, Color.BLACK, Color.GRAY, whiteKeys.size());
        }
      }
    }

   public void initKeyboardData() {
     String firstWhiteKey = "A";
     String keyboardString ="q2we4r5tz7u8i9opßü´<ysxdcfvbhnjm,l.ö-";

     // determine offset
     String[] whiteKeyNames = { "A", "B", "C", "D", "E", "F", "G" };
     int offset = 0;
     while (!whiteKeyNames[offset].equals(firstWhiteKey)) {
       offset++;
     }

     // create the white and black keys
     for (int i = 0; i < keyboardString.length(); i++) {

       // next key is white
       String whiteKeyName = whiteKeyNames[(offset + whiteKeys.size()) % 7];
       Key whiteKey = new Key(whiteKeys.size(), whiteKeyName, keyboardString.charAt(i), WHITE_KEY);
       whiteKeys.add(whiteKey);

       // next key is black (black keys occur immediately after A, C, D, F, and G)
       if ("ACDFG".contains(whiteKeyName)) {
         i++;
         if (i >= keyboardString.length()) break;
         String blackKeyName = whiteKeyName + "#";
         Key blackKey = new Key(whiteKeys.size(), blackKeyName, keyboardString.charAt(i), BLACK_KEY);
         blackKeys.add(blackKey);
       }
     }
   }

   public void addKeyDown(char key) {
    keysDown.add(key);
   }

   public void removeKeyDown(char key) {
    keysDown.remove(key);
   }
}

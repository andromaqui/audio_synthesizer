import utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class SynthesizerRemastered {

  private static final HashMap<Character, Double> KEY_FREQUENCIES = new HashMap<>();

  private boolean shouldGenerate;
  private final JFrame frame = new JFrame("Synthesizer Remastered");
  private final Oscillator[] oscillators = new Oscillator[2];
  private final PianoKeys keyboard;
  private final AudioThread thread = new AudioThread(() ->
     {
       if(!shouldGenerate) {
         return null;
       }
       short[] s = new short[AudioThread.BUFFER_SIZE];
       for(int i=0; i<AudioThread.BUFFER_SIZE; ++i) {
         double d = 0;

         for(Oscillator oscillator: oscillators) {
            d += oscillator.nextSample() / oscillators.length;
         }

         s[i] = (short) (Short.MAX_VALUE * d);
       }
       return s;
     }
  );

  private final KeyAdapter keyAdapter = new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent e) {
        if(!thread.isRunning()){

          for(Oscillator oscillator : oscillators) {
              oscillator.setKeyFrequency(KEY_FREQUENCIES.get(e.getKeyChar()));
          }
          keyboard.addKeyDown(e.getKeyChar());
          keyboard.repaint();
          shouldGenerate = true;
          thread.triggeredPlayback();
        }
      }

      @Override
      public void keyReleased(final KeyEvent e) {
        shouldGenerate = false;
        keyboard.removeKeyDown(e.getKeyChar());
        keyboard.repaint();
      }
  };

  static {
    final int STARTING_KEY =  16;
    final int KEY_FREQUENCY_INCREMENT = 2;
    final char[] KEYS = "q2we4r5tz7u8i9opßü´<ysxdcfvbhnjm,l.ö-".toCharArray();
    for(int i = STARTING_KEY, key = 0; i < KEYS.length * KEY_FREQUENCY_INCREMENT + STARTING_KEY; i += KEY_FREQUENCY_INCREMENT, ++key) {
        KEY_FREQUENCIES.put(KEYS[key], Utils.Math.getKeyFrequency(i));
    }

    for(Double d : KEY_FREQUENCIES.values()) {
      System.out.println(d);
    }
  }

  SynthesizerRemastered(){
    JPanel invisibleContainer = new JPanel();
    invisibleContainer.setSize(600, 600);
    invisibleContainer.setLayout(new BoxLayout(invisibleContainer, BoxLayout.X_AXIS));
    JPanel container = new JPanel();
    container.setSize(1000, 1000);
    container.setLayout(new GridLayout(2,2));

    // add oscilators to panel
    int y = 0;
    for(int i =0; i < oscillators.length; ++i) {
      oscillators[i] = new Oscillator(this);
      invisibleContainer.add(oscillators[i]);
      y+=105;
    }
    container.add(invisibleContainer);

    // add keyboard to panel
    keyboard = new PianoKeys(this);
    keyboard.setFocusable(true);
    container.add(keyboard);

    // make frame visible
    frame.setSize(1000, 1000);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setBackground(Color.WHITE);
    frame.setLayout(new BorderLayout());
    frame.add(container);
    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        thread.close();
      }
    });

    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.addKeyListener(keyAdapter);
  }

  public static class AudioInfo {
    public static final int SAMPLE_RATE = 44100;
  }

  public KeyAdapter getKeyAdapter() {
    return keyAdapter;
  }
}


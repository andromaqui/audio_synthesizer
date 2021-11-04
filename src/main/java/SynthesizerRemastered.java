
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SynthesizerRemastered {

  private boolean shouldGenerate;
  private int wavePos;
  private final JFrame frame = new JFrame("Synthesizer Remastered");
  private final Oscillator[] oscillators = new Oscillator[3];
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
         //s[i] = (short)(Short.MAX_VALUE * Math.sin((2 * Math.PI * 440) / AudioInfo.SAMPLE_RATE * wavePos++));
       }
       return s;
     }
  );

  private final KeyAdapter keyAdapter = new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent e) {
        if(!thread.isRunning()){
          shouldGenerate = true;
          thread.triggeredPlayback();
        }
      }

      @Override
      public void keyReleased(final KeyEvent e) {
        shouldGenerate = false;
      }
  };

  SynthesizerRemastered(){
    int y = 0;
    for(int i =0; i < oscillators.length; ++i) {
      oscillators[i] = new Oscillator(this);
      oscillators[i].setLocation(5, y);
      frame.add(oscillators[i]);
      y+=105;
    }

    //oscillator.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        thread.close();
      }
    });
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setSize(613, 357);
    frame.setResizable(false);
    frame.setLayout(null);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.addKeyListener(keyAdapter);
  }

  public static class AudioInfo {
    public static final int SAMPLE_RATE = 44100;

  }

  public KeyAdapter getKeyAdapter() {
    return keyAdapter;
  }
}


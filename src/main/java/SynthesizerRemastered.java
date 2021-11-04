import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SynthesizerRemastered {

  private boolean shouldGenerate;
  private int wavePos;
  private final JFrame frame = new JFrame("Synthesizer Remastered");
  private final AudioThread thread = new AudioThread(() ->
   {
     if(!shouldGenerate) {
       return null;
     }
     short[] s = new short[AudioThread.BUFFER_SIZE];
     for(int i=0; i<AudioThread.BUFFER_SIZE; ++i) {
       s[i] = (short)(Short.MAX_VALUE * Math.sin((2 * Math.PI * 440) / AudioInfo.SAMPLE_RATE * wavePos++));
     }
     return s;
   }
    );

  SynthesizerRemastered(){
    Oscillator oscillator = new Oscillator();
    oscillator.setLocation(5, 0);
    frame.add(oscillator);
    oscillator.setVisible(true);
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
    frame.addKeyListener(new KeyAdapter() {
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
    });

  }

  public static class AudioInfo {
    public static final int SAMPLE_RATE = 44100;

  }
}

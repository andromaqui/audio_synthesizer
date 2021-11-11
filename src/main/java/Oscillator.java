import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Oscillator extends SynthControlContainer {

  private static final int TONE_OFFSET_LIMIT = 2000;

  private static final int MAX_VOLUME = 100;
  private static final int MIN_VOLUME = 0;

  private WaveForm waveForm = WaveForm.Sine;
  private final Random random = new Random();
  private int wavePos;
  private int toneOffset;
  private double keyFrequency;
  private double frequency;
  private double volumeMultiplier;
  private double volumeLevel = 100;

  public Oscillator(SynthesizerRemastered synthesizerRemastered) {
    super(synthesizerRemastered);
    setSize(289, 100);
    setBorder(Utils.WindowDesign.LINE_BORDER);
    setLayout(null);

    // CHANGE TONE PARAMETER
    JLabel toneParameter = new JLabel("x0.00");
    toneParameter.setBounds(165, 65, 50, 25);
    toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
    toneParameter.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(final MouseEvent e) {
        final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit()
          .createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                              new Point(0, 0), "blank cursor"
          );
        setCursor(BLANK_CURSOR);
        mouseClickLocation = e.getLocationOnScreen();
      }

      @Override
      public void mouseReleased(final MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
      }
    });
    toneParameter.addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseDragged(final MouseEvent e) {

        if(mouseClickLocation.y != e.getYOnScreen()) {

            boolean mouseMovingUp = mouseClickLocation.y - e.getYOnScreen() > 0;
            if(mouseMovingUp && toneOffset < TONE_OFFSET_LIMIT) {
              ++toneOffset;
            }
            else if (!mouseMovingUp && toneOffset > -TONE_OFFSET_LIMIT) {
              --toneOffset;
            }
            applyToneOffset();
            toneParameter.setText("x" + getToneOffset());
        }
      }
    });
    add(toneParameter);

    JLabel toneText = new JLabel("Tone");
    toneText.setBounds(172, 40, 75, 25);
    add(toneText);



    // CHANGE VOLUME
    JLabel volumeParameter = new JLabel("100%");
    volumeParameter.setBounds(245, 65, 50, 25);
    volumeParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
    volumeParameter.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(final MouseEvent e) {
        final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit()
          .createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                              new Point(0, 0), "blank cursor"
          );
        setCursor(BLANK_CURSOR);
        mouseClickLocation = e.getLocationOnScreen();
      }

      @Override
      public void mouseReleased(final MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
      }
    });

    volumeParameter.addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseDragged(final MouseEvent e) {
        if(mouseClickLocation.y != e.getYOnScreen()) {
          boolean mouseMovingUp = mouseClickLocation.y - e.getYOnScreen() > 0;
          if(mouseMovingUp && volumeLevel < MAX_VOLUME) {
            ++volumeLevel;
          }
          else if (!mouseMovingUp && volumeLevel > MIN_VOLUME) {
            --volumeLevel;
          }
          System.out.println(getVolumeLevel());
          volumeParameter.setText(getVolumeLevel() + "%");
        }
      }
    });
    add(volumeParameter);

    JLabel volumeText = new JLabel("Volume");
    volumeText.setBounds(242, 40, 75, 25);
    add(volumeText);

    // CHANGE WAVEFORM
    JComboBox<WaveForm> comboBox = new JComboBox<>(new WaveForm[]{WaveForm.Sine, WaveForm.Square, WaveForm.Sav,
      WaveForm.Trianle, WaveForm.Noise});
    comboBox.setSelectedItem(WaveForm.Sine);
    comboBox.setBounds(10, 10, 110, 25);
    comboBox.addItemListener(listener -> {

      if (listener.getStateChange() == ItemEvent.SELECTED) {
        waveForm = (WaveForm) listener.getItem();
      }
    });
    add(comboBox);
  }

  private enum WaveForm {
    Sine, Square, Sav, Trianle, Noise
  }

  public double getKeyFrequency() {
    return frequency;
  }

  private double getToneOffset() {
    return toneOffset / 1000d;
  }

  private double getVolumeLevel() {
    return volumeLevel;
  }

  private double getVolumeMultiplier() {
    volumeMultiplier = volumeLevel / 100.0;
    return  volumeMultiplier;
  }

  public void setKeyFrequency(double frequency) {
    keyFrequency = this.frequency = frequency;
    applyToneOffset();
  }

  public double nextSample() {
    double tDivP = (wavePos++ / (double) SynthesizerRemastered.AudioInfo.SAMPLE_RATE / (1d / frequency));
    switch (waveForm) {
      case Sine:
        return (Math.sin(Utils.Math.frequencyToAngularFrequency(frequency) * (wavePos - 1) / SynthesizerRemastered.AudioInfo.SAMPLE_RATE)) * getVolumeMultiplier();
      case Square:
        return (Math.signum(Math.sin(Utils.Math.frequencyToAngularFrequency(frequency) * (wavePos - 1) / SynthesizerRemastered.AudioInfo.SAMPLE_RATE))) * getVolumeMultiplier();
      case Sav:
        return (2d * (tDivP - Math.floor(0.5 + tDivP))) * getVolumeMultiplier();
      case Trianle:
        return (2d * Math.abs((tDivP - Math.floor(0.5 + tDivP))) - 1) * getVolumeMultiplier();
      case Noise:
        return (random.nextDouble()) * getVolumeMultiplier();
      default:
        throw new RuntimeException("Waveform not known");
    }
  }

  private void applyToneOffset() {
    frequency = keyFrequency * Math.pow(2, getToneOffset());
  }
}


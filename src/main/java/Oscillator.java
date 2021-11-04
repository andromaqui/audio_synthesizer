import utils.Utils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.Random;

public class Oscillator extends SynthControlContainer {

  private static final double FREQUENCY = 440;
  private WaveForm waveForm = WaveForm.Sine;
  private final Random random = new Random();
  private int wavePos;

  public Oscillator(SynthesizerRemastered synthesizerRemastered) {

    super(synthesizerRemastered);
    JComboBox<WaveForm> comboBox = new JComboBox<>(new WaveForm[] {WaveForm.Sine, WaveForm.Square, WaveForm.Sav, WaveForm.Trianle, WaveForm.Noise});
    comboBox.setSelectedItem(WaveForm.Sine);
    comboBox.setBounds(10, 10, 75, 25);
    comboBox.addItemListener(listener -> {

      if(listener.getStateChange() == ItemEvent.SELECTED) {
        waveForm = (WaveForm) listener.getItem();
      }
    });
    add(comboBox);
    setSize(289, 100);
    setBorder(Utils.WindowDesign.LINE_BORDER);
    setLayout(null);
  }

  private enum WaveForm {
    Sine, Square, Sav, Trianle, Noise
  }

  public double nextSample() {
    double tDivP = (wavePos++ / (double) SynthesizerRemastered.AudioInfo.SAMPLE_RATE / (1d / FREQUENCY));
    switch (waveForm) {
      case Sine:
        return Math.sin(Utils.Math.frequencyToAngularFrequency(FREQUENCY) * (wavePos - 1) / SynthesizerRemastered.AudioInfo.SAMPLE_RATE);
      case Square:
        return Math.signum(Math.sin(Utils.Math.frequencyToAngularFrequency(FREQUENCY) * (wavePos - 1) / SynthesizerRemastered.AudioInfo.SAMPLE_RATE));
      case Sav:
        return 2d * (tDivP - Math.floor(0.5 + tDivP));
      case Trianle:
        return 2d * Math.abs((tDivP - Math.floor(0.5 + tDivP))) - 1;
      case Noise:
        return random.nextDouble();
      default:
        throw new RuntimeException("Waveform not known");
    }
  }
}


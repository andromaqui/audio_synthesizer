import javax.swing.*;
import java.awt.*;

public class SynthControlContainer extends JPanel {

  protected Point mouseClickLocation;
  protected SynthesizerRemastered synthesizerRemastered;
  protected boolean on;

  public SynthControlContainer(SynthesizerRemastered synthesizerRemastered) {
    this.synthesizerRemastered = synthesizerRemastered;
  }

  public boolean isOn() {
    return on;
  }

  public void setOn(boolean on) {
    this.on = on;
  }

  @Override
  public Component add(Component component) {
    component.addKeyListener(synthesizerRemastered.getKeyAdapter());
    return super.add(component);
  }

  @Override
  public Component add(Component component, int index) {
    component.addKeyListener(synthesizerRemastered.getKeyAdapter());
    return super.add(component, index);
  }

  @Override
  public Component add(String name, Component component) {
    component.addKeyListener(synthesizerRemastered.getKeyAdapter());
    return super.add(name, component);
  }

  @Override
  public void add(Component component, Object constrains) {
    component.addKeyListener(synthesizerRemastered.getKeyAdapter());
    super.add(component, constrains);
  }

  @Override
  public void add(Component component, Object constrains, int index) {
    component.addKeyListener(synthesizerRemastered.getKeyAdapter());
    super.add(component, constrains, index);
  }
}

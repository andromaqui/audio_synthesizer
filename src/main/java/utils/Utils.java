package utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static java.lang.Math.E;
import static java.lang.Math.PI;
import static java.lang.Math.log;
import static java.lang.Math.pow;

public class Utils {

  public static void invokeProcedure(Procedure procedure, boolean printStackTrace){
    try {
      procedure.invoke();
    } catch (Exception e) {
      if (printStackTrace) {
        e.printStackTrace();
      }
    }
  }

  public static class WindowDesign {

    public static final Border LINE_BORDER = BorderFactory.createLineBorder(Color.BLACK);

  }

  public static class Math {
    public static double frequencyToAngularFrequency(double freq) {
      return 2 * PI * freq;
    }

    public static double getKeyFrequency(int keyNum) {
      return pow(root(2, 12), keyNum - 49) * 440;
    }

    public static double root(double num, double root) {
      return pow(E, log(num) / root);
    }
  }
}

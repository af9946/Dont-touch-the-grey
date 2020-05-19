package myGame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader { // load level file from path

  public static String loadFile(String path) {
    StringBuilder bs = new StringBuilder();
    String everything = null;
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      String line = br.readLine();
      while (line != null) {
        bs.append(line);
        bs.append(System.lineSeparator());
        line = br.readLine();
      }
      everything = bs.toString();
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return everything;
  }

  public static int parse(String n) { // parse string to int so that we can
                                      // access it
    try {
      return Integer.parseInt(n);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0;
    }
  }

}

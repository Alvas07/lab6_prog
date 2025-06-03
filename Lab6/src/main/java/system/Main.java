package system;

import managers.Console;

/**
 * Главный класс, запускающий программу.
 *
 * @author Alvas
 * @since 1.0
 */
public class Main {
  /**
   * Точка начала программы.
   *
   * @param args путь до файла с коллекцией.
   * @author Alvas
   * @since 1.0
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Не указан путь до файла с коллекцией.");
    } else {
      Console console = new Console();
      console.start(args);
    }
  }
}

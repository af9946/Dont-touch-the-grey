package myGame;

public class Handler {

  private static Game game;
  private World world;

  public Handler(Game game) {
    this.game = game;
  }

  public static Game getGame() {
    return game;
  }

  public World getWorld() {
    return this.world;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  public KeyAdapter getKeyAdapter() {
    return game.getKeyAdapter();
  }

  public MouseAdapter getMouseAdapter() {
    return game.getMouseAdapter();
  }

}

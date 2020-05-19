package myGame;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Timer;

public class GameView extends View {

  private Player player;
  private World world;
  private Timer timer;
  private static int FPS = 30;

  public GameView(Handler handler) {
    super(handler);
    this.setFocusable(true);
    this.setBackground(Color.WHITE);
    world = new World(handler); // load level file
    handler.setWorld(world);

    this.timer = new Timer(1000 / FPS, event -> {
      this.update();
      this.repaint(); // note that we call repaint, not paintComponent
    });
    // reStart();
  }

  public void loadGame() {
    world.loadGame(); // load level file
  }

  public void reStart() {
    if (player != null)
      this.removeKeyListener(player.getKeyListener());
    player = new Player(handler, 160, 160, 32, 32);
    this.addKeyListener(player.getKeyListener());
    world = new World(handler); // load level file
    handler.setWorld(world);
    world.reStart();
    this.update();
    this.repaint();
    this.requestFocusInWindow();
    start();
  }

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  @Override
  public synchronized void start() {
    timer.start();
  }

  public synchronized void pause() {
    timer.stop();
  }

  @Override
  public void update() { // update world and player
    world.update();
    player.update();
  }

  @Override
  public void paintComponent(Graphics g) {
    world.paintComponent(g);
    player.paintComponent(g);
  }

  @Override
  public void render(Graphics g) {
    // TODO Auto-generated method stub

  }

  public static int getFPS() {
    return FPS;
  }

  public static void setFPS(int fPS) {
    FPS = fPS;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

}

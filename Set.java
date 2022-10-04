public class Set extends Room {

  private Role[] roles = null;
  private String name = null;
  private Scene curScene = null;
  private Shot[] shots = null;
  private int maxShots = 0;
  private int curShots = 0;
  private boolean active = true;
  private int x = 0;
  private int y = 0;
  private int h = 0;
  private int w = 0;

  // constructor
  public Set(String n, int[] neigh, Shot[] shots, int maxShots, Role[] r, int x, int y, int h, int w){
    super(n, neigh);
    this.shots = shots;
    this.maxShots = maxShots;
    this.curShots = maxShots;
    this.roles = r;
    this.x = x;
    this.y = y;
    this.h = h;
    this.w = w;
  }

  public Role[] getRoles() { return this.roles; }

  public Scene getScene() { return this.curScene; }

  public int getShots() { return this.curShots; }

  public Shot[] getAllShots() { return this.shots; }

  public int[] getArea() {
    int[] area = {this.x, this.y, this.h, this.w};
    return area;
  }

  public void setScene(int s) { this.curScene = Deadwood.getScene(s); }

  public void setActive(boolean a) { this.active = a; }

  public void reSetShots() {
    this.curShots = maxShots;
  }

  public boolean isActive() {
    return this.active;
  }

  public void decShots() {
    this.curShots--;
  }
}

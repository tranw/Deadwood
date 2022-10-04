public class Role {

  private int rank = 0;
  private String name = null;
  private String description = null;
  private boolean onCard = false;
  private boolean isTaken = false;
  private int x = 0;
  private int y = 0;
  private int h = 0;
  private int w = 0;

  // constructor
  public Role(int r, String n, String d, boolean c, int x, int y, int h, int w){
    this.rank = r;
    this.name = n;
    this.description = d;
    this.onCard = c;
    this.x = x;
    this.y = y;
    this.h = h;
    this.w = w;
  }

  public int getRank() { return this.rank; }

  public String getName() { return this.name; }

  public String getDescription() { return this.description; }

  public boolean getOnCard() { return this.onCard; }

  public int[] getArea() {
    int[] area = {this.x, this.y, this.h, this.w};
    return area;
  }

  public boolean isTaken() { return this.isTaken; }

  public void setRoleStatus(boolean t) { this.isTaken = t; }



}

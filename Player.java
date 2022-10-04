public class Player {

  private String name = null;
  private int rank = 1;
  private int fame = 0;
  private int money = 0;
  private String roleName = null;
  private int roleIndex = -1;
  private int status = 0;
  private int location = 10;
  private int rehearsal = 0;

  // constructor
  public Player(String N){
    this.name = N;
  }

  public void setRank(int r) { this.rank = r; }

  public void setRoleName(String n) { this.roleName = n; }

  public void setRoleIndex(int i) { this.roleIndex = i; }

  public void setStatus(int s) { this.status = s; }

  public void setLocation(int l) { this.location = l; }

  public int getRank() { return this.rank; }

  public int getFame() { return this.fame; }

  public int getMoney() { return this.money; }

  public String getRoleName() { return this.roleName; }

  public int getRoleIndex() { return this.roleIndex; }

  public int getStatus() { return this.status; }

  public int getLocation() { return this.location; }

  public String getName() { return this.name; }

  public int getRehearsal() { return this.rehearsal; }

  public void changeMoney(int m) {
    this.money += m;
  }

  public void changeFame(int f) {
    this.fame += f;
  }

  public void incRehearsal() {
    this.rehearsal++;
  }

  public void resetRehearsal() {
    this.rehearsal = 0;
  }

}

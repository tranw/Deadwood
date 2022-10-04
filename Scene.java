public class Scene {
  private String name = null;
  private int budget = 0;
  private Role[] roles = null;
  private String description = null;
  private int num = 0;

  // constructor
  public Scene(String n, int b, Role[] r, String d, int number){
    this.name = n;
    this.budget = b;
    this.roles = r;
    this.description = d;
    this.num = number;
  }

  public String getName() { return this.name; }

  public int getBudget() { return this.budget; }

  public Role[] getRoles() { return this.roles; }

  public String getDescription() { return this.description; }

  public int getNumber() { return this.num; }
}

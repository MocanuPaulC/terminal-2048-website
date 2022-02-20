package classes;

public class Player {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.length() >0 && name.length() <=20) {
            this.name = name;
        }else
        {
            System.out.printf("%s is not valid!\n", name);
        }
    }
//---------------------------------------------------------CONSTRUCTOR
    public Player(String name) {
        setName(name);
    }
}

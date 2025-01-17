import java.io.*;
import java.util.*;

public class WarGames implements Serializable {
    private String name;
    private int gold;
    private List<Soldier> army;

    public WarGames(String name, int gold) {
        this.name = name;
        this.gold = gold;
        this.army = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public List<Soldier> getArmy() {
        return army;
    }

    public void addSoldier(Rank rank) {
        if (rank == null) {
            throw new IllegalArgumentException("Unsupported rank");
        }
        army.add(new Soldier(rank));
    }

    public void trainArmy(List<Soldier> army) {
        for (Soldier soldier : army) {
            soldier.train();
        }
    }

    public void attack(WarGames opponent) {
        if (this.army.size() > opponent.army.size()) {
            this.gold += 100;
            opponent.gold -= 100;
        }
    }

    public void saveState(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public static WarGames loadState(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (WarGames) ois.readObject();
        }
    }

    public int calculateArmyStrength() {
        return army.size() * 10;
    }
}

class Soldier implements Serializable {
    private Rank rank;
    private int experience;

    public Soldier(Rank rank) {
        this.rank = rank;
        this.experience = 0;
    }

    public Rank getRank() {
        return rank;
    }

    public int getExperience() {
        return experience;
    }

    public void train() {
        this.experience += 2;
    }
}

enum Rank {
    PRIVATE, CORPORAL, SERGEANT, LIEUTENANT, CAPTAIN, MAJOR
}

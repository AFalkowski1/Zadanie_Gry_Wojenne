import java.io.*;
import java.util.*;

// Enum representing the rank of a soldier
enum Rank {
    PRIVATE(1),
    CORPORAL(2),
    CAPTAIN(3),
    MAJOR(4);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

// Abstract Soldier class
abstract class Soldier implements Serializable {
    protected Rank rank;
    protected int experience;

    public Soldier(Rank rank) {
        this.rank = rank;
        this.experience = 1;
    }

    public int getStrength() {
        return rank.getValue() * experience;
    }

    public Rank getRank() {
        return rank;
    }

    public int getExperience() {
        return experience;
    }

    public abstract void gainExperience();
    public abstract void loseExperience();
    public abstract boolean isAlive();

    @Override
    public String toString() {
        return "Soldier{" +
                "rank=" + rank +
                ", experience=" + experience +
                '}';
    }
}

// Concrete Soldier classes
class PrivateSoldier extends Soldier {
    public PrivateSoldier() {
        super(Rank.PRIVATE);
    }

    @Override
    public void gainExperience() {
        experience++;
        if (experience >= rank.getValue() * 5) {
            promote();
        }
    }

    @Override
    public void loseExperience() {
        experience = Math.max(0, experience - 1);
    }

    @Override
    public boolean isAlive() {
        return experience > 0;
    }

    private void promote() {
        rank = Rank.CORPORAL;
        experience = 1;
    }
}

class CorporalSoldier extends Soldier {
    public CorporalSoldier() {
        super(Rank.CORPORAL);
    }

    @Override
    public void gainExperience() {
        experience++;
        if (experience >= rank.getValue() * 5) {
            promote();
        }
    }

    @Override
    public void loseExperience() {
        experience = Math.max(0, experience - 1);
    }

    @Override
    public boolean isAlive() {
        return experience > 0;
    }

    private void promote() {
        rank = Rank.CAPTAIN;
        experience = 1;
    }
}

class CaptainSoldier extends Soldier {
    public CaptainSoldier() {
        super(Rank.CAPTAIN);
    }

    @Override
    public void gainExperience() {
        experience++;
        if (experience >= rank.getValue() * 5) {
            promote();
        }
    }

    @Override
    public void loseExperience() {
        experience = Math.max(0, experience - 1);
    }

    @Override
    public boolean isAlive() {
        return experience > 0;
    }

    private void promote() {
        rank = Rank.MAJOR;
        experience = 1;
    }
}

// General class
class General implements Serializable {
    private final String name;
    private List<Soldier> army;
    private int gold;

    public General(String name, int initialGold) {
        this.name = name;
        this.gold = initialGold;
        this.army = new ArrayList<>();
    }

    public void addSoldier(Rank rank) {
        int cost = 10 * rank.getValue();
        if (gold >= cost) {
            gold -= cost;
            Soldier soldier;
            switch (rank) {
                case PRIVATE:
                    soldier = new PrivateSoldier();
                    break;
                case CORPORAL:
                    soldier = new CorporalSoldier();
                    break;
                case CAPTAIN:
                    soldier = new CaptainSoldier();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported rank");
            }
            army.add(soldier);
        } else {
            throw new IllegalArgumentException("Not enough gold to recruit a soldier.");
        }
    }

    public void trainArmy(List<Soldier> soldiersToTrain) {
        int totalCost = soldiersToTrain.stream().mapToInt(s -> s.getRank().getValue()).sum();
        if (gold >= totalCost) {
            gold -= totalCost;
            soldiersToTrain.forEach(Soldier::gainExperience);
        } else {
            throw new IllegalArgumentException("Not enough gold for training.");
        }
    }

    public int calculateArmyStrength() {
        return army.stream().mapToInt(Soldier::getStrength).sum();
    }

    public void attack(General opponent) {
        int myStrength = calculateArmyStrength();
        int opponentStrength = opponent.calculateArmyStrength();

        if (myStrength > opponentStrength) {
            winBattle(opponent);
        } else if (myStrength < opponentStrength) {
            opponent.winBattle(this);
        } else {
            handleDraw(opponent);
        }
    }

    private void winBattle(General opponent) {
        int loot = opponent.gold / 10;
        gold += loot;
        opponent.gold -= loot;
        army.forEach(Soldier::gainExperience);
        opponent.army.forEach(Soldier::loseExperience);
        opponent.army.removeIf(s -> !s.isAlive());
    }

    private void handleDraw(General opponent) {
        if (!army.isEmpty()) {
            army.remove(new Random().nextInt(army.size()));
        }
        if (!opponent.army.isEmpty()) {
            opponent.army.remove(new Random().nextInt(opponent.army.size()));
        }
    }

    public void saveState(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public static General loadState(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (General) ois.readObject();
        }
    }

    public List<Soldier> getArmy() {
        return army;
    }

    public int getGold() {
        return gold;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "General{" +
                "name='" + name + '\'' +
                ", army=" + army +
                ", gold=" + gold +
                '}';
    }
}

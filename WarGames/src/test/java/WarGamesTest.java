import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class WarGamesTest {

    private General generalA;
    private General generalB;

    @BeforeEach
    public void setUp() {
        generalA = new General("General A", 1000);
        generalB = new General("General B", 1000);
    }

    @Test
    public void testAddSoldier() {
        generalA.addSoldier(Rank.PRIVATE);
        generalB.addSoldier(Rank.CAPTAIN);

        assertEquals(1, generalA.getArmy().size());
        assertEquals(1, generalB.getArmy().size());
    }

    @Test
    public void testTrainArmy() {
        generalA.addSoldier(Rank.PRIVATE);
        generalA.trainArmy(generalA.getArmy());

        assertEquals(2, generalA.getArmy().get(0).getExperience());
    }

    @Test
    public void testAttack() {
        generalA.addSoldier(Rank.PRIVATE);
        generalA.addSoldier(Rank.CAPTAIN);
        generalB.addSoldier(Rank.CORPORAL);
        generalB.addSoldier(Rank.MAJOR);

        generalA.attack(generalB);

        assertTrue(generalA.getGold() > 1000);
        assertTrue(generalB.getGold() < 1000);
    }

    @Test
    public void testSaveAndLoadState() throws IOException, ClassNotFoundException {
        generalA.addSoldier(Rank.MAJOR);
        generalA.saveState("generalA.ser");

        General loadedGeneral = General.loadState("generalA.ser");

        assertEquals(generalA.getName(), loadedGeneral.getName());
        assertEquals(generalA.calculateArmyStrength(), loadedGeneral.calculateArmyStrength());
    }
}

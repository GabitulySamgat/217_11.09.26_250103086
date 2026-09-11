import java.util.Random;

public class Character {
    private String name;
    private CharacterClass characterClass;
    private int baseAttack;
    private int baseArmor;
    private double critChance; // only used by Rogue

    private static final Random random = new Random();

    public Character(String name, CharacterClass characterClass) {
        this.name = name;
        this.characterClass = characterClass;

        // Baseline stats hardcoded per class (naive on purpose for Phase 1)
        switch (characterClass) {
            case WARRIOR:
                this.baseAttack = 15;
                this.baseArmor = 20;
                this.critChance = 0.0;
                break;
            case MAGE:
                this.baseAttack = 25;
                this.baseArmor = 5;
                this.critChance = 0.0;
                break;
            case ROGUE:
                this.baseAttack = 18;
                this.baseArmor = 10;
                this.critChance = 0.3;
                break;
        }
    }

    public String getName() {
        return name;
    }

    // Executes an attack against a target with the given damage type,
    // prints a breakdown, and returns the final damage dealt.
    public int attack(Target target, DamageType damageType) {
        int rawDamage = baseAttack;

        // Rogue gets a crit chance on physical attacks
        if (characterClass == CharacterClass.ROGUE && damageType == DamageType.PHYSICAL) {
            if (random.nextDouble() < critChance) {
                rawDamage *= 2;
                System.out.println("  >> CRITICAL STRIKE!");
            }
        }

        int finalDamage = target.receiveAttack(damageType, rawDamage);

        System.out.println("Attacker: " + name
                + " | Damage Type: " + damageType
                + " | Raw Damage: " + rawDamage
                + " | Final Damage Dealt: " + finalDamage
                + " | Target HP Remaining: " + target.getCurrentHP());

        return finalDamage;
    }
}

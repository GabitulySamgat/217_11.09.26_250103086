public class Target {
    private String name;
    private TargetType type;
    private int maxHP;
    private int currentHP;

    public Target(String name, TargetType type, int maxHP) {
        this.name = name;
        this.type = type;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    // Applies mitigation rules based on target type and damage type,
    // reduces HP, and returns the final damage actually applied.
    public int receiveAttack(DamageType damageType, int rawDamage) {
        int finalDamage;

        switch (type) {
            case ARMORED_DUMMY:
                // Flat damage mitigation from ALL incoming attacks
                int flatReduction = 5;
                finalDamage = Math.max(rawDamage - flatReduction, 0);
                break;

            case ETHEREAL_WISP:
                if (damageType == DamageType.PHYSICAL) {
                    // High resistance to physical damage
                    finalDamage = (int) Math.round(rawDamage * 0.3);
                } else {
                    // Vulnerable to elemental damage (FIRE or FROST)
                    finalDamage = (int) Math.round(rawDamage * 1.5);
                }
                break;

            default:
                finalDamage = rawDamage;
        }

        currentHP = Math.max(currentHP - finalDamage, 0);
        return finalDamage;
    }
}

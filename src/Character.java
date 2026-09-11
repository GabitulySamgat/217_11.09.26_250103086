import java.util.Random;

public class Character {
    private String name;
    private CharacterClass characterClass;
    private int baseAttack;
    private int baseArmor;
    private double critChance; // only used by Rogue

    // PHASE 2: Necromancer needs HP to check the "below 25%" inversion trigger
    private int maxHP;
    private int currentHP;

    // PHASE 2: Ice Shield equipment state (Feature B)
    private boolean hasIceShield;
    private int attacksReceivedCount;
    private int bonusArmor;

    // PHASE 3: Berserker Elixir state (Feature D)
    private int elixirTurnsRemaining;

    private static final Random random = new Random();

    public Character(String name, CharacterClass characterClass) {
        this.name = name;
        this.characterClass = characterClass;
        this.hasIceShield = false;
        this.attacksReceivedCount = 0;
        this.bonusArmor = 0;
        this.elixirTurnsRemaining = 0;

        // Baseline stats hardcoded per class (naive on purpose for Phase 1)
        switch (characterClass) {
            case WARRIOR:
                this.baseAttack = 15;
                this.baseArmor = 20;
                this.critChance = 0.0;
                this.maxHP = 120;
                break;
            case MAGE:
                this.baseAttack = 25;
                this.baseArmor = 5;
                this.critChance = 0.0;
                this.maxHP = 80;
                break;
            case ROGUE:
                this.baseAttack = 18;
                this.baseArmor = 10;
                this.critChance = 0.3;
                this.maxHP = 100;
                break;
            // PHASE 2: Feature A - Necromancer stats
            case NECROMANCER:
                this.baseAttack = 16;
                this.baseArmor = 8;
                this.critChance = 0.0;
                this.maxHP = 90;
                break;
        }

        this.currentHP = maxHP;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    // PHASE 3: needed so an attacker can check if their target is shielded (for backlash)
    public boolean hasIceShield() {
        return hasIceShield;
    }

    // PHASE 2: Feature B - equip the Ice Shield (any class can call this)
    public void equipIceShield() {
        this.hasIceShield = true;
        System.out.println(name + " equips an Ice Shield.");
    }

    // PHASE 3: Feature D - consume the Berserker Elixir, doubling damage for the next 2 turns
    public void consumeBerserkerElixir() {
        elixirTurnsRemaining = 2;
        System.out.println(name + " consumes a Berserker Elixir! Damage doubled for the next 2 turns.");
    }

    // Executes an attack against a Target (Armored Dummy / Ethereal Wisp).
    // Original Phase 1 method signature - kept working exactly as before,
    // extended internally for Phase 2 (Necromancer) and Phase 3 (Elixir).
    public int attack(Target target, DamageType damageType) {
        int rawDamage = baseAttack;

        // Rogue gets a crit chance on physical attacks
        if (characterClass == CharacterClass.ROGUE && damageType == DamageType.PHYSICAL) {
            if (random.nextDouble() < critChance) {
                rawDamage *= 2;
                System.out.println("  >> CRITICAL STRIKE!");
            }
        }

        // PHASE 3: Feature D - Berserker Elixir doubles this attack's damage
        if (elixirTurnsRemaining > 0) {
            rawDamage *= 2;
            System.out.println("  >> Berserker Elixir active! Damage doubled.");
        }

        // PHASE 2: Feature A - Necromancer inversion below 25% HP
        if (characterClass == CharacterClass.NECROMANCER && currentHP < maxHP * 0.25) {
            int absorbedByTarget = rawDamage / 2;
            int healedToSelf = rawDamage - absorbedByTarget;

            int finalDamage = target.receiveAttack(DamageType.PHYSICAL, absorbedByTarget);
            currentHP = Math.min(maxHP, currentHP + healedToSelf);

            System.out.println("  >> Necromancer's attack inverts! Half absorbed, half heals as Elemental Life-Drain.");
            System.out.println("Attacker: " + name
                    + " | Damage Type: ELEMENTAL_LIFE_DRAIN"
                    + " | Raw Damage: " + rawDamage
                    + " | Final Damage Dealt: " + finalDamage
                    + " | Self HP Healed: " + healedToSelf
                    + " | Necromancer HP: " + currentHP
                    + " | Target HP Remaining: " + target.getCurrentHP());

            CombatLogger.logToCsv(name, target.getName(), DamageType.PHYSICAL, finalDamage);

            // PHASE 3: elixir turn is consumed even on the Necromancer's inverted attack
            if (elixirTurnsRemaining > 0) {
                elixirTurnsRemaining--;
            }
            return finalDamage;
        }

        int finalDamage = target.receiveAttack(damageType, rawDamage);

        System.out.println("Attacker: " + name
                + " | Damage Type: " + damageType
                + " | Raw Damage: " + rawDamage
                + " | Final Damage Dealt: " + finalDamage
                + " | Target HP Remaining: " + target.getCurrentHP());

        // PHASE 2: Feature C - emit the same event to the CSV sink too
        CombatLogger.logToCsv(name, target.getName(), damageType, finalDamage);

        // PHASE 3: tick down the elixir's remaining turns
        if (elixirTurnsRemaining > 0) {
            elixirTurnsRemaining--;
        }

        return finalDamage;
    }

    // PHASE 2: Feature B - characters can now also receive attacks. Needed so the
    // Ice Shield has something to trigger on, and so the Necromancer's own HP
    // can actually drop below 25%.
    public int receiveAttack(DamageType damageType, int rawDamage) {
        attacksReceivedCount++;
        int finalDamage = rawDamage;

        boolean isElemental = (damageType == DamageType.FIRE || damageType == DamageType.FROST);

        if (hasIceShield && isElemental) {
            if (attacksReceivedCount % 3 == 0) {
                // Every 3rd hit received: shield absorbs 100% and converts it into bonus armor
                bonusArmor += rawDamage;
                finalDamage = 0;
                System.out.println("  >> Ice Shield absorbs the hit! +" + rawDamage + " bonus armor for " + name);
            }
            // otherwise: elemental damage passes through normally this time
        }

        // Base armor plus any bonus armor from the shield lightly mitigates the hit
        int totalArmor = baseArmor + bonusArmor;
        finalDamage = Math.max(finalDamage - (totalArmor / 4), 0);

        currentHP = Math.max(currentHP - finalDamage, 0);
        return finalDamage;
    }

    // PHASE 2: overload so characters can attack each other (used to demo the Ice Shield).
    // PHASE 3: also carries the Elixir doubling + backlash-vs-shield check.
    public int attack(Character target, DamageType damageType) {
        int rawDamage = baseAttack;

        // PHASE 3: Feature D - Berserker Elixir doubling
        if (elixirTurnsRemaining > 0) {
            rawDamage *= 2;
            System.out.println("  >> Berserker Elixir active! Damage doubled.");

            // PHASE 3: if the target has an active shield during an elixir turn,
            // the attacker suffers self-inflicted backlash equal to 100% of their base attack
            if (target.hasIceShield()) {
                int backlash = this.baseAttack;
                this.currentHP = Math.max(this.currentHP - backlash, 0);
                System.out.println("  >> Backlash! " + name + " takes " + backlash
                        + " self-inflicted damage from " + target.getName() + "'s active shield.");
            }
        }

        int finalDamage = target.receiveAttack(damageType, rawDamage);

        System.out.println("Attacker: " + name
                + " | Defender: " + target.getName()
                + " | Damage Type: " + damageType
                + " | Raw Damage: " + rawDamage
                + " | Final Damage Dealt: " + finalDamage
                + " | Defender HP Remaining: " + target.getCurrentHP());

        CombatLogger.logToCsv(name, target.getName(), damageType, finalDamage);

        // PHASE 3: tick down the elixir's remaining turns
        if (elixirTurnsRemaining > 0) {
            elixirTurnsRemaining--;
        }

        return finalDamage;
    }
}

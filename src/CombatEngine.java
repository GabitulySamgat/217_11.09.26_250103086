public class CombatEngine {
    public static void main(String[] args) {
        // ============ PHASE 1 (unchanged - zero regression) ============
        Character warrior = new Character("Warrior", CharacterClass.WARRIOR);
        Character mage = new Character("Mage", CharacterClass.MAGE);
        Character rogue = new Character("Rogue", CharacterClass.ROGUE);

        Character[] characters = { warrior, mage, rogue };

        for (Character c : characters) {
            Target dummy = new Target("Armored Dummy", TargetType.ARMORED_DUMMY, 100);
            Target wisp = new Target("Ethereal Wisp", TargetType.ETHEREAL_WISP, 100);

            System.out.println("=== " + c.getName() + " vs Armored Dummy ===");
            DamageType typeForDummy = c.getName().equals("Mage") ? DamageType.FIRE : DamageType.PHYSICAL;
            c.attack(dummy, typeForDummy);

            System.out.println("=== " + c.getName() + " vs Ethereal Wisp ===");
            DamageType typeForWisp = c.getName().equals("Mage") ? DamageType.FROST : DamageType.PHYSICAL;
            c.attack(wisp, typeForWisp);

            System.out.println();
        }

        // ============ PHASE 2 (unchanged - zero regression) ============

        // --- Feature A: Necromancer inversion demo ---
        System.out.println("=== Feature A: Necromancer ===");
        Character necromancer = new Character("Necromancer", CharacterClass.NECROMANCER);
        Target trainingDummy = new Target("Training Dummy", TargetType.ARMORED_DUMMY, 200);

        necromancer.receiveAttack(DamageType.PHYSICAL, 70);
        necromancer.receiveAttack(DamageType.PHYSICAL, 20);
        System.out.println("Necromancer HP after damage: " + necromancer.getCurrentHP());

        necromancer.attack(trainingDummy, DamageType.PHYSICAL); // should trigger inversion
        System.out.println();

        // --- Feature B: Ice Shield demo (every 3rd elemental hit gets absorbed) ---
        System.out.println("=== Feature B: Ice Shield ===");
        Character shieldedWarrior = new Character("Shielded Warrior", CharacterClass.WARRIOR);
        Character attackerMage = new Character("Attacking Mage", CharacterClass.MAGE);
        shieldedWarrior.equipIceShield();

        for (int i = 1; i <= 3; i++) {
            System.out.println("-- Elemental hit #" + i + " --");
            attackerMage.attack(shieldedWarrior, DamageType.FROST);
        }
        System.out.println();

        // ============ PHASE 3 additions ============

        // --- Feature D: Berserker Elixir doubling over 2 turns, then reverting ---
        System.out.println("=== Feature D: Berserker Elixir (doubling) ===");
        Character berserker = new Character("Berserker", CharacterClass.WARRIOR);
        Target berserkerDummy = new Target("Berserker Test Dummy", TargetType.ARMORED_DUMMY, 300);

        berserker.consumeBerserkerElixir();
        System.out.println("-- Turn 1 (elixir active) --");
        berserker.attack(berserkerDummy, DamageType.PHYSICAL);
        System.out.println("-- Turn 2 (elixir active) --");
        berserker.attack(berserkerDummy, DamageType.PHYSICAL);
        System.out.println("-- Turn 3 (elixir expired, back to normal) --");
        berserker.attack(berserkerDummy, DamageType.PHYSICAL);
        System.out.println();

        // --- Feature D: Berserker Elixir backlash when target has an active shield ---
        System.out.println("=== Feature D: Berserker Elixir Backlash vs Shield ===");
        Character recklessAttacker = new Character("Reckless Attacker", CharacterClass.ROGUE);
        Character shieldedDefender = new Character("Shielded Defender", CharacterClass.WARRIOR);
        shieldedDefender.equipIceShield();

        recklessAttacker.consumeBerserkerElixir();
        recklessAttacker.attack(shieldedDefender, DamageType.PHYSICAL); // should backlash
        System.out.println("Reckless Attacker HP after backlash: " + recklessAttacker.getCurrentHP());
        System.out.println();

        // --- Feature C: dump the CSV sink built up over the whole run ---
        System.out.println("=== Feature C: CSV Log Output ===");
        System.out.println(CombatLogger.getCsvLog());
    }
}

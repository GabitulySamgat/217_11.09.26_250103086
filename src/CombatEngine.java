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

        // ============ PHASE 2 additions ============

        // --- Feature A: Necromancer inversion demo ---
        System.out.println("=== Feature A: Necromancer ===");
        Character necromancer = new Character("Necromancer", CharacterClass.NECROMANCER);
        Target trainingDummy = new Target("Training Dummy", TargetType.ARMORED_DUMMY, 200);

        // Bring the Necromancer below 25% HP so the next attack triggers the inversion
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

        // --- Feature C: dump the CSV sink built up over the whole run ---
        System.out.println("=== Feature C: CSV Log Output ===");
        System.out.println(CombatLogger.getCsvLog());
    }
}

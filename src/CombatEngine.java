public class CombatEngine {
    public static void main(String[] args) {
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
    }
}

class LegacyStudentDirectory {
    private final String[] students = {"Alice", "Bob", "Charlie", "David"};
    public int totalEntries() {
        return students.length;
    }
    // Legacy system uses 1-based indexing (1 = Alice, 4 = David)
    public String getStudentAt(int oneBasedIndex) {
        if (oneBasedIndex < 1 || oneBasedIndex > students.length) {
            throw new IndexOutOfBoundsException("Legacy index out of bounds: " + 
oneBasedIndex);
        }
        return students[oneBasedIndex - 1];
    }
}

interface IModernDirectory {
    int size();
    String get(int zeroBasedIndex);
}

class StudentDirectoryAdapter implements IModernDirectory{
    private LegacyStudentDirectory LSD;

    public StudentDirectoryAdapter(LegacyStudentDirectory LSD){
        this.LSD = LSD;
    }

    @Override
    public int size(){
        return LSD.totalEntries();
    }

    @Override
    public String get(int zeroBasedIndex){
        if (zeroBasedIndex < 0 || zeroBasedIndex >= size()){
            throw new IndexOutOfBoundsException();
        }
        return LSD.getStudentAt(zeroBasedIndex + 1);
    }
}

public class Task03Adapter {
    public static void main(String[] args) {
        LegacyStudentDirectory legacy = new LegacyStudentDirectory();
        IModernDirectory adapter = new StudentDirectoryAdapter(legacy);

        System.out.println(adapter.get(0));
        System.out.println(adapter.get(1));
        System.out.println(adapter.get(3));
    }
}

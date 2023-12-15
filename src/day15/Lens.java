package day15;

public record Lens(String label, int focalLength) {
    public boolean equals(Object o) {
        if (o instanceof Lens) {
            return this.label().equals(((Lens) o).label());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < label.length(); i++) {
            hash = (hash + label.charAt(i)) * 17 % 256;
        }
        return hash;
    }
}

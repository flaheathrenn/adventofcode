package day2;

public final class Bag {
    private int red;
    private int green;
    private int blue;

    public Bag(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void update(int red, int green, int blue) {
        this.red = Math.max(this.red, red);
        this.green = Math.max(this.green, green);
        this.blue = Math.max(this.blue, blue);
    }

    public int power() {
        return this.red * this.blue * this.green;
    }

    public boolean canContain(Bag otherBag) {
        return this.red >= otherBag.red
            && this.green >= otherBag.green
            && this.blue >= otherBag.blue;
    }
}
package lesson_1.task3;

public class Task3 {
    public static void main(String[] args) {
        Box<Apple> aBox = new Box<>();
        aBox.add(new Apple());
        aBox.add(new Apple());
        aBox.add(new Apple());

        Box<Apple> aBox2 = new Box<>();
        aBox2.add(new Apple());
        aBox2.add(new Apple());

        Box<Orange> oBox = new Box<>();
        oBox.add(new Orange());
        oBox.add(new Orange());

        System.out.println("aBox weight is equal oBox weight: " + aBox.compare(oBox));

        System.out.println("aBox count: " + aBox.count() + ", aBox2 count: " + aBox2.count());
        aBox.refillTo(aBox2);
        System.out.println("aBox count: " + aBox.count() + ", aBox2 count: " + aBox2.count());

    }
}

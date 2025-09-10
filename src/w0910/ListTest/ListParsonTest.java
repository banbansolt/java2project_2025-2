package w0910.ListTest;

import java.util.ArrayList;

public class ListParsonTest {
    public static void main(String[] args) {
        Person person1 = new Person("유현주", 31, "여성");
        Person person2 = new Person("옥태훈", 22, "남성");
        Person person3 = new Person("박현경", 26, "여성");

        ArrayList<Person> persons = new ArrayList<Person>();
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
    }
}

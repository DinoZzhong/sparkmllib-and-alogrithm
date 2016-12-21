package dino.test.reflect;

/**
 * Created by Dino on 2016/12/7.
 */

class Person{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    String name;
    int age;
    Person(){}
    Person(String name,int age){
        this.name=name;
        this.age=age;

    }
    public void eat(){
        System.out.println("people eating...");
    }
    public void eat(String s){}
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
public class TestDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        Person per = new Person();
        System.out.println(per.getClass().getName());

        Class<?> c = Person.class;
        System.out.println(c.getName());
        //方法3
        System.out.println(Class.forName(c.getName()).getName());

        try {
            test("测试",Person.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

/*
        int maxMemUsage = 100 << 20;
        System.out.println(maxMemUsage+"  "+ maxMemUsage % 16);
        maxMemUsage -= maxMemUsage % 16;
        System.out.println(maxMemUsage);
        System.out.println((129 & 32767));

        byte[] i  = new byte[1024];
        IntBuffer j = ByteBuffer.wrap(i)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        System.out.println(j.capacity()+" "+i.length);
        j.put(3,0);
        j.put(1,2);
        System.out.println(j.get(3)+" "+j.capacity());*/
    }

    public  static  <T> void  test(String name,Class<T> u) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className= u.getName();
        Class<?> c =Class.forName(className);
        Person obj = (Person)c.newInstance();
        obj.setName(name);
        System.out.println(className + " " + obj.toString());

    }
}

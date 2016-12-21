package dino.test.reflect;

/**
 * Created by Dino on 2016/12/21.
 */
public class Man extends Person{
    Man(){}
    // 继承父类的构造方法
    Man(String name,int age){
        super(name,age);
    }
    @Override
    public void eat(String s) {
        System.out.println(super.getName()+" Man eating "+s);
    }
    public void play(){
        System.out.println(super.getName()+" is playng game...");
    }
}


//  子类转父类只能实现父类方法 除非强转
 class TestExtends{
    public static void main(String a[]){
        Person p =new Man("Arron",30);
        p.eat(" bee ");
        p.eat();
        System.out.println(p.toString());
        // 强转
        Man m = (Man)p;
        m.play();

        Person f = new Female("slina",20);
        System.out.println(f.toString());

    }
 }
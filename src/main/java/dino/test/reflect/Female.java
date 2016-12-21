package dino.test.reflect;

/**
 * Created by Dino on 2016/12/21.
 */
public class Female extends Person {
    Female(String name,int age){
        super(name,age);
    }

    public void buy(){
        System.out.println(super.getName() +" is buying something" );
    }
}


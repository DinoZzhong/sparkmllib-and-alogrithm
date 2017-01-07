package dino.test.rpc;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Dino on 2017/1/6.
 *
 *  注意在hadoop rpc中返回的对象需要继承WritableComparable并实现对应的read和write，同时需要get和set
 */
public class Person implements Serializable,WritableComparable<Person> {
    String name;
    String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Person(String name,String value){
        this.value=value;
        this.name=name;

    }

    @Override
    public int compareTo(Person o) {
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(name);
        dataOutput.writeUTF(value);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        name=dataInput.readUTF();
        value=dataInput.readUTF();
    }



    @Override
    public String toString() {
        return name+"||"+value;
    }
}
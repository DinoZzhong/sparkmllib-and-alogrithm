package dino.test.rpc;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

/**
 * Created by Dino on 2017/1/6.
 *
 * 1、rpc接口
 *
 *
 */
public class MyBizImpl implements Mybiz{
    static{
        System.out.println("initial Mybizimpl....");
    }
    @Override
    public String hello(String name) {
        return "HELLO "+name;
    }

    @Override
    public long getProtocolVersion(String s, long l)throws IOException {
        return versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String s, long l, int i) throws IOException {
        return null;
    }

    @Override
    public String chToPinyin(String x) {
        return hanyuToPinyin(x," ");
    }

    private static String hanyuToPinyin(String in,String sep){
        StringBuilder pinyin =new StringBuilder();
        char[] inChar = in.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        int len = inChar.length-1;
        boolean lock = false;
        for (int i=0;i<=len;i++){
            if(inChar[i]>128){ // 只区分中文
                if(lock&& i!= len){
                    pinyin.append(sep);
                    lock=false;
                }
                try{
                    pinyin.append(PinyinHelper.toHanyuPinyinStringArray(inChar[i], format)[0]);
                }catch (Exception e){
                    pinyin.append("**");
                }
                if(i != len)pinyin.append(sep);
            }else {
                pinyin.append(inChar[i]);
                if(!lock) lock=true;
            }
        }

        return pinyin.toString().replaceAll("\\s+"," ");
    }
    @Override
    public Person initPerson(String name,String value){
        return new Person(name,value);
    }

}



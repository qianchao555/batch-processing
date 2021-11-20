package collection.map;

import java.util.HashMap;
import java.util.Set;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/20 16:44
 * @version:1.0
 */
public class HashMapDemo {
    public static void main(String[] args) {
        HashMap hashMap = new HashMap();
        Emp qc = new Emp("qc", 180000, 1);
        Emp qc2 = new Emp("qc", 10000, 2);
        Emp qc3 = new Emp("qc", 10, 3);

        hashMap.put(qc.getId(), qc);
        hashMap.put(qc2.getId(), qc2);
        hashMap.put(qc3.getId(), qc3);
        //lambda遍历
        hashMap.forEach((k,v)->{
            System.out.println(k);
            System.out.println(v);
        });
        System.out.println("");


        //方便遍历
        Set set = hashMap.entrySet();
//        Iterator iterator = set.iterator();
//        while (iterator.hasNext()) {
//            Map.Entry entry = (Map.Entry) iterator.next();
//            Emp value = (Emp) entry.getValue();
//            if (value.getSal() > 18000) {
//                System.out.println(entry);
//            }
//
//        }
//        for (Object obj :set) {
//            //向下转型
//            Map.Entry entry= (Map.Entry) obj;
//            Emp value = (Emp) entry.getValue();
//            if(value.getSal()>18000){
//                System.out.println(entry);
//            }
//        }

    }
}

class Emp {
    private String name;
    private int sal;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Emp(String name, int sal, int id) {
        this.name = name;
        this.sal = sal;
        this.id = id;
    }

    public int getSal() {
        return sal;
    }

    public void setSal(int sal) {
        this.sal = sal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "name='" + name + '\'' +
                ", sal=" + sal +
                ", id=" + id +
                '}';
    }
}

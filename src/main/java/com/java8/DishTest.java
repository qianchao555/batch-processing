package com.java8;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @ClassName DishTest
 * @Author qianchao
 * @Date 2022/12/9
 * @Version V1.0
 **/
public class DishTest {

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.DishType.MEAT),
                new Dish("beef", false, 700, Dish.DishType.MEAT),
                new Dish("chicken", false, 400, Dish.DishType.MEAT),
                new Dish("french fries", true, 530, Dish.DishType.OTHER),
                new Dish("rice", true, 350, Dish.DishType.OTHER),
                new Dish("season fruit", true, 120, Dish.DishType.OTHER),
                new Dish("pizza", true, 550, Dish.DishType.OTHER),
                new Dish("prawns", false, 300, Dish.DishType.FISH),
                new Dish("salmon", false, 450, Dish.DishType.FISH));


        //获取菜肴的名称，并连接起来
//        String collect2 = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));
//        System.out.println(collect2);

        List<String> collect1 =
                //建立流
                menu.stream()

                        //建立操作流水线：下列每一个操作都会产生一个流，这个这些流就会接成一条流水线，于是，可以看作是对源的查询

                        .filter(x -> x.getCalories() > 300)
                        .map(Dish::getName)
                        .limit(3)
                        //处理流水线，将流转换为其他形式并返回结果，它返回的不是流
                        //在此前：不会有任何结果产生
                        .collect(Collectors.toList());
        System.out.println(collect1);

//        Map<Dish.DishType, List<Dish>> collect = menu.stream().collect(groupingBy(Dish::getDishType));
//        for (Map.Entry<Dish.DishType, List<Dish>> dishTypeListEntry : collect.entrySet()) {
//            System.out.print(dishTypeListEntry.getKey()+":");
//            System.out.print(dishTypeListEntry.getValue());
//            System.out.println();
//            System.out.println();
//        }

        //reduce归约
//        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
//        Integer reduce = numbers1.stream().reduce(1, (a, b) -> a * b);
//        System.out.println(reduce);


//        //映射为1 3，1 4，2 3，2 4。。。。
//        List<Integer> numbers2 = Arrays.asList(3, 4);
//
//        List<Integer[]> collect = numbers1.stream()
//                .flatMap(
//                        i -> numbers2.stream()
//                                .map(j -> new Integer[]{i, j}))
//                .collect(Collectors.toList());
//        collect.forEach(x->{
//            Arrays.stream(x).forEach(System.out::print);
//            System.out.println();
//        });

        //分组统计个数
        System.out.print("更加类型统计个数：");
        Map<Dish.DishType, Long> collect = menu.stream()
                .collect(groupingBy(Dish::getDishType, counting()));
        System.out.println(collect);


        //分区函数partitionBy:将List分为2个list,即：一个符合分区条件的list,一个不符合分区条件的list
        //将菜单按照素食和非素食分开
        System.out.println("将菜单按照素食和非素食分开：");
        Map<Boolean, List<Dish>> partitionVegetarian = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));
        System.out.println(partitionVegetarian);

        //多级分组
        menu.stream()
                .collect(partitioningBy(Dish::isVegetarian, partitioningBy(x -> x.getCalories() > 500)));

        int x1 = 10_000;
        System.out.println(x1);


    }

    /**
     * 并行流
     */
    @Test
    public void testParStream() {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.DishType.MEAT),
                new Dish("beef", false, 700, Dish.DishType.MEAT),
                new Dish("chicken", false, 400, Dish.DishType.MEAT),
                new Dish("french fries", true, 530, Dish.DishType.OTHER),
                new Dish("rice", true, 350, Dish.DishType.OTHER),
                new Dish("season fruit", true, 120, Dish.DishType.OTHER),
                new Dish("pizza", true, 550, Dish.DishType.OTHER),
                new Dish("prawns", false, 300, Dish.DishType.FISH),
                new Dish("salmon", false, 450, Dish.DishType.FISH));

        //ForkJoin


    }

    @Test
    public void testParStream2() throws ParseException {
        String s = "202301";

//        LocalDate yyyyMM = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyyMM"));
//        System.out.println(yyyyMM);

        System.out.println("原始：" + s);
        Date dates = new SimpleDateFormat("yyyyMM").parse(s);
        Instant instant = dates.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        System.out.println(localDate);
        String yyyyMM1 = localDate.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
        String yyyyMM2 = localDate.minusMonths(2).format(DateTimeFormatter.ofPattern("yyyyMM"));
        String yyyyMM3 = localDate.minusMonths(3).format(DateTimeFormatter.ofPattern("yyyyMM"));
        System.out.println(yyyyMM1);
        System.out.println(yyyyMM2);
        System.out.println(yyyyMM3);


    }
    @Test
    void test111(){
        String str = "CA_CPD_20230101.txt.Z";
        String str1 = "CA_CPD_20231104.txt.Z";
        String str2 = "CA_CPD_20220102.txt.Z";
        String pattern = "[C][A][_][C][P][D][_]([2-9][0-9][2-9][3-9][0-9][1-9][0-9][2-9])[.][t][x][t][.][Z]$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str2);
        System.out.println(m.matches());

    }
    @Test
    public void test999(){
        String sss="CA_CPD_20230104.txt.Z";

        String str = "CA_CPD_20221213.txt.Z";
//        String pattern = "CA_CPD_20221213.txt.Z";
        String pattern = "CA_CPD_(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})" +
                "(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|\"+\n" +
                "                \"((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|\"+\n" +
                "                \"((0[48]|[2468][048]|[3579][26])00))0229)$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        System.out.println(m.matches());


//        String timeRegex4 = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|"+
//                "((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|"+
//                "((0[48]|[2468][048]|[3579][26])00))0229)$";
//
//        System.out.println(Pattern.matches(timeRegex4, "20181223"));


    }

    @Test
    public void test888() {
        String str = "{" +
                "\"code\":\"20000\"," +
                "\"data\":\"{" +
                "\"access_token\":\"7fad10e69294c\"," +
                "\"refresh_token\":\"7fad10e69294c8a\"," +
                "\"openid\":\"1bdea54ae8b88dd04e\"," +
                "\"scope\":\"user\"," +
                "\"token_type\":\"bearer\"," +
                "\"expires_in\":60," +
                "\"tokenSno\":\"7fad10\"" +
                "}\"," +
                "\"msg\":\"成功\"" +
                "}";

        str = str.substring(1, str.length()-1);
        String[] strs = str.split(",");
        Map<String,String> map = new LinkedHashMap<>();
        for (String string : strs) {
            String key = string.split(":")[0];
            String value = string.split(":")[1];
            // 去掉头部空格
            String key1 = key.trim();
            String value1 = value.trim();
            map.put(key1, value1);
        }
        System.out.println(map.toString());

//        s = s.replace("\\", "");
//        int i = s.indexOf("{", 2);
//        int i1 = s.indexOf("}", 1);
//
//        String substring = s.substring(i, i1+1);
//        System.out.println(substring);
//        JSONObject jsonObject = JSON.parseObject(substring);
//        Object access_token = jsonObject.get("access_token");
//        System.out.println(access_token);
    }


    @Test
    public void test10(){
    String s="{\"code\":\"20000\",\"data\":\"{\"isReal\":\"\",\"srcAppoDuty\":\"二级电话销售服务辅助员\",\"certType\":\"10100\",\"userStatus\":\"1\",\"domainType\":\"\",\"sysAppAccountVo\":{},\"regionName\":\"默认区域\",\"userPhone\":\"18615795079\",\"regDate\":1638444365000,\"expDate\":1680264971000,\"userCode\":\"0000081568\",\"srcDutyGrade\":\"二级\",\"srcSex\":\"女\",\"regionCode\":\"10000\",\"srcDutyStatus\":\"Y\",\"srcWorkPlace\":\"四川省成都市\",\"orgCode\":\"3011\",\"dataStamp\":1672488971000,\"joinCurrDate\":\"2014-09-01\",\"srcBirthDay\":\"1993-08-09\",\"joinSysDate\":\"2014-09-01\",\"loginAcct\":\"jiangsiyi\",\"orgName\":\"销售服务\",\"srcLinkFlag\":\"0\",\"isFirstLogin\":\"1\",\"srcAppoDutyGrade\":\"二级\",\"userName\":\"姜思忆\",\"userId\":\"85696197808967680\",\"orgCodeList\":\"3011:销售服务\",\"srcMemberAttr\":\"地面人员\",\"accountVos\":[],\"certNo\":\"510122199308094248\",\"srcMemberType\":\"直接聘用制人员\",\"firstLoginTime\":1672488950000,\"srcAppoDutyRank\":\"4A\",\"srcDutyName\":\"电话销售服务辅助员\",\"userMail\":\"785642144@qq.com\",\"userPropVos\":[],\"createUser\":\"import\",\"userType\":\"PT01\",\"identId\":\"68ffa55f4533331bac9144f667f06076854225b0297243b01bcdf637776f3f46\",\"srcJobDay\":\"2014-09-01\"}\",\"msg\":\"成功\"}";
        String replace = s.replace("\\", "");
        System.out.println(replace);

        int i = replace.indexOf("{", 2);

        // 最后一个分隔符位置
        int lastIndex = replace.lastIndexOf("}");
        System.out.println(lastIndex);

        // 倒数第二个分隔符位置
        int secondLastIndex = replace.lastIndexOf("}", lastIndex - 1);
        System.out.println(secondLastIndex);


        String substring= replace.substring(i, secondLastIndex+1);

        System.out.println(substring);

        JSONObject jsonObject = JSONObject.parseObject(substring);
        System.out.println(jsonObject);
    }


}

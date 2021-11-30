package stream.shizhan;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2021/11/3
 * @Version OPRA V1.0
 **/
public class Test {
    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "CD");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
        //1、找出2011年发生的所有交易，并按交易额排序（从低到高）。
        List<Transaction> collect = transactions.stream().filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
//        System.out.println(collect);
//        (2) 交易员都在哪些不同的城市工作过？
        //提取与交易相关的每位交易员的所在城市
        List<String> collect1 = transactions.stream().map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());
//        System.out.println(collect1);
        //查找所有来自于剑桥的交易员，并按姓名排序
        List<Trader> collect2 = transactions.stream()
                .map(map -> map.getTrader())//提取交易员
                .filter(trader -> trader.getCity().equals("Cambridge"))//提取交易员来自的地方
                .sorted(Comparator.comparing(trader->trader.getName()))//交易员的姓名排序
                .collect(Collectors.toList());
        //System.out.println(collect2);
        //返回所有交易员的姓名字符串，按字母顺序排序
        String reduce = transactions.stream().map(transaction -> transaction.getTrader().getName())
                .sorted()
                .reduce("", (n1, n2) -> n1 + n2);
        String collect3 = transactions.stream().map(transaction -> transaction.getTrader().getName()).collect(Collectors.joining());
        System.out.println(collect3);
//        List<String> collect3 = (List<String>) reduce
//                .collect(Collectors.toList());
//        System.out.println(reduce);
        //查询是否有在米兰工作的交易员
        List<Trader> milan = transactions.stream().map(transaction -> transaction.getTrader())
                .filter(trader -> trader.getCity().equals("Milan")).collect(Collectors.toList());
//        System.out.println(milan);
        boolean milan1 = transactions.stream().anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));
//        System.out.println(milan1);
//        transactions.stream().filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
//                .map(Transaction::getValue)
//                .forEach(System.out::println);
        Optional<Integer> reduce1 = transactions.stream().map(Transaction::getValue)
                .reduce(Integer::max);
//        System.out.println(reduce1.get());
        Optional<Integer> reduce2 = transactions.stream().map(Transaction::getValue)
                .reduce(Integer::min);
        System.out.println(reduce2);

    }


}

package com.knowledge.designpattern.structured.adapter.demo1;

import com.knowledge.designpattern.structured.adapter.demo1.adapters.SquarePegAdapter;
import com.knowledge.designpattern.structured.adapter.demo1.round.RoundHole;
import com.knowledge.designpattern.structured.adapter.demo1.round.RoundPeg;
import com.knowledge.designpattern.structured.adapter.demo1.square.SquarePeg;

/**
 * @ClassName DemoTest
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class DemoTest {
    public static void main(String[] args) {
        // Round fits round, no surprise.
        //圆孔
        RoundHole hole = new RoundHole(5);
        //圆钉
        RoundPeg rpeg = new RoundPeg(5);
        if (hole.fits(rpeg)) {
            System.out.println("圆钉 r5 fits 圆孔 r5.");
        }
        //小方钉
        SquarePeg smallSqPeg = new SquarePeg(2);
        //大方钉
        SquarePeg largeSqPeg = new SquarePeg(20);
        // hole.fits(smallSqPeg); // Won't compile.

        // Adapter solves the problem.
        //小方钉适配器  将方钉转换为圆钉
        SquarePegAdapter smallSqPegAdapter = new SquarePegAdapter(smallSqPeg);
        //大方钉适配器
        SquarePegAdapter largeSqPegAdapter = new SquarePegAdapter(largeSqPeg);
        //圆孔和圆钉能适配   《==方钉与圆孔的交互   转换为圆钉与圆孔的交互
        if (hole.fits(smallSqPegAdapter)) {
            System.out.println("Square peg w2 fits round hole r5.");
        }
        if (!hole.fits(largeSqPegAdapter)) {
            System.out.println("Square peg w20 does not fit into round hole r5.");
        }
    }
}

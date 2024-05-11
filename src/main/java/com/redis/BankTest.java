package com.redis;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.List;

/**
 * @author Chao C Qian
 * @date 17/07/2023
 */
public class BankTest {
   @Test
    public void testt(){
        String jsonString="[{\"nameOfBank\":\"山西省农村信用社联合社\",\"cnapsCode\":\"402161002352\",\"confirmationType\":\"内容摘要\"},{\"nameOfBank\":\"七台河农村商业银行股份有限公司\",\"cnapsCode\":\"100000000001\",\"confirmationType\":\"confirmationType_1,confirmationType_2,confirmationType_0\"},{\"nameOfBank\":\"兰州银行股份有限公司\",\"cnapsCode\":\"313821001016\",\"confirmationType\":\"confirmationType_1,confirmationType_2,confirmationType_0\"},{\"nameOfBank\":\"重庆富民银行股份有限公司\",\"cnapsCode\":\"323653010015\",\"confirmationType\":\"confirmationType_2,confirmationType_1,confirmationType_0\"},{\"nameOfBank\":\"上海东正汽车金融股份有限公司\",\"cnapsCode\":\"123456789012\",\"confirmationType\":\"confirmationType_2\"},{\"nameOfBank\":\"广西北部湾银行股份有限公司\",\"cnapsCode\":\"313611001018\",\"confirmationType\":\"confirmationType_1\"},{\"nameOfBank\":\"长沙银行股份有限公司新宁支行\",\"cnapsCode\":\"313555829242\",\"confirmationType\":\"confirmationType_0\"},{\"nameOfBank\":\"辽宁振兴银行股份有限公司\",\"cnapsCode\":\"323221000086\",\"confirmationType\":\"confirmationType_1,confirmationType_2,confirmationType_0\"},{\"nameOfBank\":\"甘肃银行股份有限公司天水分行\",\"cnapsCode\":\"123789454612\",\"confirmationType\":\"confirmationType_1,confirmationType_2\"},{\"nameOfBank\":\"测试银行业金融机构01\",\"cnapsCode\":\"300015482385\",\"confirmationType\":\"confirmationType_1,confirmationType_2,confirmationType_0\"},{\"nameOfBank\":\"南京银行股份有限公司\",\"cnapsCode\":\"313301008887\",\"confirmationType\":\"confirmationType_1,confirmationType_2,confirmationType_0\"},{\"nameOfBank\":\"我是测试机构\",\"cnapsCode\":\"B0018A344088\",\"confirmationType\":\"confirmationType_1\"},{\"nameOfBank\":\"无锡锡商银行股份有限公司\",\"cnapsCode\":\"323302000012\",\"confirmationType\":\"confirmationType_1,confirmationType_2,confirmationType_0\"},{\"nameOfBank\":\"中国工商银行股份有限公司\",\"cnapsCode\":\"asdfghjklkjh\",\"confirmationType\":\"confirmationType_1\"},{\"nameOfBank\":\"山西太谷农村商业银行股份有限公司白塔支行\",\"cnapsCode\":\"402175700701\"},{\"nameOfBank\":\"成都青白江融兴村镇银行有限责任公司\",\"cnapsCode\":\"320651013008\"},{\"nameOfBank\":\"白山浑江恒泰村镇银行股份有限公司城东支行\",\"cnapsCode\":\"320246000051\"},{\"nameOfBank\":\"梨树源泰村镇银行股份有限公司梨树支行\",\"cnapsCode\":\"320243100102\"},{\"nameOfBank\":\"集安惠鑫村镇银行股份有限公司清河支行\",\"cnapsCode\":\"320245500029\"},{\"nameOfBank\":\"天津农村商业银行股份有限公司\",\"cnapsCode\":\"317110010019\"},{\"nameOfBank\":\"青岛平度惠民村镇银行股份有限公司明村支行\",\"cnapsCode\":\"320452400041\"},{\"nameOfBank\":\"贵阳农村商业银行股份有限公司清镇支行\",\"cnapsCode\":\"314701018196\"},{\"nameOfBank\":\"贵州乌当农村商业银行股份有限公司双龙支行\",\"cnapsCode\":\"314701014402\"},{\"nameOfBank\":\"吉林汪清农村商业银行股份有限公司天林支行\",\"cnapsCode\":\"314249700180\",\"confirmationType\":\"confirmationType_0,confirmationType_2,confirmationType_1\"},{\"nameOfBank\":\"达州银行股份有限公司渠县工农街支行\",\"cnapsCode\":\"313676206085\"},{\"nameOfBank\":\"四川天府银行股份有限公司达州渠县支行\",\"cnapsCode\":\"313676200051\"},{\"nameOfBank\":\"九江银行股份有限公司上犹支行\",\"cnapsCode\":\"313428688126\"},{\"nameOfBank\":\"安徽马鞍山农村商业银行股份有限公司长丰支行\",\"cnapsCode\":\"402361083714\"},{\"nameOfBank\":\"天长农村商业银行王店支行\",\"cnapsCode\":\"402375219012\"},{\"nameOfBank\":\"安徽石台农村商业银行股份有限公司小河支行\",\"cnapsCode\":\"402379300149\"},{\"nameOfBank\":\"安徽怀宁农村商业银行股份有限公司三桥支行\",\"cnapsCode\":\"402368200463\"},{\"nameOfBank\":\"安徽太湖农村商业银行股份有限公司刘畈支行\",\"cnapsCode\":\"402368500154\"},{\"nameOfBank\":\"淮南淮河农村商业银行股份有限公司芦集支行\",\"cnapsCode\":\"402364030034\"},{\"nameOfBank\":\"淮南淮河农村商业银行股份有限公司夹沟支行\",\"cnapsCode\":\"402364030163\"},{\"nameOfBank\":\"安徽萧县农村商业银行股份有限公司圣泉支行\",\"cnapsCode\":\"402374300227\"},{\"nameOfBank\":\"安徽五河农村商业银行股份有限公司头铺支行\",\"cnapsCode\":\"402363200046\"},{\"nameOfBank\":\"安徽涡阳农村商业银行股份有限公司高炉支行\",\"cnapsCode\":\"402372600033\"}]";

       List<BankInfo> bankInfos = JSONObject.parseArray(jsonString, BankInfo.class);
       System.out.println(bankInfos);
   }
}

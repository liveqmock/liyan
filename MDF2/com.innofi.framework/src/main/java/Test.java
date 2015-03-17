import com.ibm.icu.math.BigDecimal;
import com.innofi.framework.utils.codec.MD5Util;
import com.innofi.framework.utils.date.DateUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import com.innofi.framework.utils.xml.XmlBuilder;
import com.innofi.framework.utils.xml.XmlDocument;
import com.innofi.framework.utils.xml.XmlNode;
import com.innofi.framework.utils.xml.XmlOutputter;
import com.innofi.framework.utils.xml.dom4j.Dom4jXmlBuilder;
import com.innofi.framework.utils.xml.dom4j.Dom4jXmlOutputter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Test {

    public static void main(String args[]) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        String companyId = "RYX";                                                                                   	//合作方id 7
        String mchntCd = "123456789123456";                                                                             //商户编号 15
        String tranDate = DateUtil.formatDateWithShort(new Date());                                                     //交易日期 8
        String tranTime = timeFormat.format(new Date());                                                                //交易时间 6
        String seq = "00000001";                                                                                        //交易流水号 8
        String currency = "RMB";                                                                                        //币种    固定值
        String accNo = "1234567890123456789012345678901234567890";                                                      //收款人账号 40
        String accName = "张三";                                                                                         //收款人账户名	 146
        String bankType = "123456789012";                                                                               //收款人账户联行号	12
        String bankName = "中国银行XXX支行";                                                                              //付款人账户开户行名称	128
        BigDecimal tranAmt = new BigDecimal(2.56);                                                                      //交易金额	15
        String remark = "代付测试";                                                                                      //客户流水摘要	128
        String resv = "备用域信息";                                                                                      //备用域	512

        Map<String, Object> tranMap = new HashMap<String, Object>();
        tranMap.put("COMPANY_ID", companyId);
        tranMap.put("MCHNT_CD", mchntCd);
        tranMap.put("TRAN_DATE", tranDate);
        tranMap.put("TRAN_TIME", tranTime);
        tranMap.put("TRAN_ID", companyId + tranDate + seq);
        tranMap.put("CURRENCY", currency);
        tranMap.put("ACC_NO", accNo);
        tranMap.put("ACC_NAME", accName);
        tranMap.put("BANK_TYPE", bankType);
        tranMap.put("BANK_NAME", bankName);
        tranMap.put("TRANS_AMT", (tranAmt.multiply(new BigDecimal(100))).intValue());
        tranMap.put("REMARK", remark);
        tranMap.put("RESV", resv);


        XmlBuilder xmlBuilder = new Dom4jXmlBuilder();
        XmlDocument document = xmlBuilder.newDocument();
        XmlNode rootNode = document.createRootNode("TRAN_REQ");


        XmlOutputter xmlOutputter = new Dom4jXmlOutputter();


        for (Map.Entry<String, Object> entry : tranMap.entrySet()) {
            XmlNode node = rootNode.addChild(entry.getKey());
            if (Validator.isNotNull(entry.getValue())) {
                node.setContent(entry.getValue().toString());
            } else {
                node.setContent("");
            }
        }

        String body = StringUtil.replace(xmlOutputter.outputString(document), "\n", "");
        String secretKey = "123456";

        String mac = MD5Util.toMD5(body + secretKey);

        String head = (body.length()+15+32)+"";
        head = getContentForRight(head, 6);


        String serviceCode = getContent("1002",15);

        System.out.println(head+serviceCode+body+mac);
    }
    
    public static String getContent(String s,int length){
        if(s==null||s.equals("")){
            s="";
            for(int i = 0 ; i < length ; i++){
                s+=" ";
            }
        }else if(s.length()<length){
            for(int i = s.length() ; i < length ; i++){
                s+=" ";
            }
        }else if(s.length()>length){
        	s = s.substring(0,length);
        }
        return s;
    }
    
    public static String getContentForRight(String s,int length){
        if(s==null||s.equals("")){
            s="";
            for(int i = 0 ; i < length ; i++){
                s="0"+s;
            }
        }else if(s.length()<length){
            for(int i = s.length() ; i < length ; i++){
            	s="0"+s;
            }
        }else if(s.length()>length){
        	s = s.substring(0,length);
        }
        return s;
    }

}

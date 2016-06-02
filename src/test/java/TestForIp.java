import com.fasterxml.jackson.databind.ObjectMapper;
import com.tianze.bean.Circle;
import com.tianze.bean.FenceAlarmBean;
import com.tianze.utils.DateUtils;
import com.tianze.utils.EnumConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-22 15:05)
 * Version: 1.0
 * Updated:
 */
public class TestForIp {
     public static void main(String[] s) throws SocketException {

         //FenceAlarmBean fenceAlarmBean = new FenceAlarmBean(1,1.0,2.0);
         /*StringBuffer sb = new StringBuffer("update ")
                 .append(EnumConfig.DbConfig.DB_USER_CBMS)
                 .append(".")
                 .append(EnumConfig.DbConfig.VEHICLE_ALARM)
                 .append(" set ENDTIME=").append("to_date('")
                 .append(DateUtils.formatDate(new Date(), DateUtils.All_DAY_FORMAT))
                 .append("' , 'yyyy-mm-dd hh24-mi-ss')")
                 .append(",ENDLOCATION='")
                 .append(fenceAlarmBean.getLng())
                 .append(",")
                 .append(fenceAlarmBean.getLat())
                 .append("'")
                 .append("where VEHICLEID=")
                 .append(fenceAlarmBean.getVehicleId())
                 .append(" and ENDTIME is null");
         System.out.println(sb.toString());*/

         Integer a = 123;
         //Double b = (Double) a;
         if(0<0){
             System.out.println(123);
         }
         for(int i=0;i>0;i--){
             System.out.println(i);
         }
        System.out.println(getRealIp());
    }


    public static String getRealIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces =
                NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }
}

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestData {

//    public static void main(String[] args) {
//        TestData td = new TestData();
//
//        // 오늘의 입차 데이터 만들기
//        td.createInCarData();
//        td.printAllParkingInfo();
//
//        System.out.println("=========================================");
//
//        td.createInAndOutData();
//        td.printAllParkingInfo();
//
//        System.out.println("=========================================");
//        td.createTicketData();
//        td.printAllTicketSalesInfo();
//        td.printAllTicketUser();
//    }

    /**
     * 오늘의 입차 데이터 만들기
     */
    public void createInCarData() {
        int cnt = 0;
        while (cnt < MAX_NUMBER) {
            LocalDateTime randomTime = LocalDateTime.of(
                    LocalDateTime.now().getYear(),
                    LocalDateTime.now().getMonth(),
                    LocalDateTime.now().getDayOfMonth(),
                    makeRandomValue(24),
                    makeRandomValue(60),
                    makeRandomValue(60)
            );
            if (!isIn(randomTime, 0)) continue;
            parkingInfoRepository.saveOne(new ParkingInfo(randomTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), carNumbers[cnt++]));
        }
    }

    /**
     * 한 달간 입출차 기록 만들기
     */
    public void createInAndOutData() {
        // 9월
        ParkingInfo p = null; //들어온시간            차넘버x         나간시간             금액           결제시간        p = new ParkingInfo("2022-09-15 12:36:17", carNumbers[0], "2022-09-15 14:50:36", 1500, "2022-09-15 14:50:36");
        p = new ParkingInfo("2022-09-03 12:36:17", carNumbers[1], "2022-09-03 14:50:36", 8000, "2022-09-03 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-05 12:36:17", carNumbers[2], "2022-09-05 14:50:36", 8000, "2022-09-05 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-05 12:36:17", carNumbers[3], "2022-09-05 14:50:36", 58000, "2022-09-05 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-07 12:36:17", carNumbers[4], "2022-09-07 14:50:36", 40000, "2022-09-07 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-07 12:36:17", carNumbers[5], "2022-09-07 14:50:36", 5000, "2022-09-07 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-10 12:36:17", carNumbers[6], "2022-09-10 14:46:36", 9000, "2022-09-10 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-10 12:36:17", carNumbers[7], "2022-09-10 14:47:36", 3000, "2022-09-10 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-19 12:36:17", carNumbers[8], "2022-09-19 14:41:36", 8500, "2022-09-19 14:50:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-09-19 12:36:17", carNumbers[9], "2022-09-19 14:42:36", 8000, "2022-09-19 14:50:36");
        parkingInfoRepository.saveOne(p);

        // 8월
        p = new ParkingInfo("2022-08-01 01:36:17", carNumbers[2], "2022-08-01 14:50:36", 20000, "2022-08-01 14:55:36");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-07 16:36:17", carNumbers[1], "2022-08-07 20:36:17", 11000, "2022-08-07 20:40:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-15 13:36:17", carNumbers[9], "2022-08-15 15:36:17", 8500, "2022-08-15 15:43:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-22 12:36:17", carNumbers[4], "2022-08-22 14:36:17", 4000, "2022-08-22 14:39:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-23 12:36:17", carNumbers[0], "2022-08-23 13:36:17", 9000, "2022-08-23 13:41:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-24 13:36:17", carNumbers[8], "2022-08-24 14:36:17", 24000, "2022-08-24 14:36:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-22 12:36:17", carNumbers[3], "2022-08-22 13:36:17", 1500, "2022-08-22 13:36:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-28 16:36:17", carNumbers[7], "2022-08-28 17:36:17", 8000, "2022-08-28 17:36:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-28 01:36:17", carNumbers[5], "2022-08-28 12:36:17", 5500, "2022-08-28 12:36:17");
        parkingInfoRepository.saveOne(p);
        p = new ParkingInfo("2022-08-30 16:36:17", carNumbers[6], "2022-08-30 19:36:17", 8000, "2022-08-30 19:36:17");
        parkingInfoRepository.saveOne(p);
    }

    /**
     * 정기권 회원기록 만들기
     */
    public void createTicketData() {
        // "67가9900"
        TicketUser tu = null;
        TicketSalesInfo ts = null;
        //------------------
        //"67가9900"
        //30일권 연장 30+30
        tu = new TicketUser(carNumbers[10], "010-4924-2940", "2022-11-15 18:45:26");   //최신 정기권의 만료일자
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[10], "2022-09-16 18:45:27", "2022-10-15 18:45:26", 100000 , "2022-09-16 18:45:27", "30");
        ts = new TicketSalesInfo(carNumbers[10], "2022-10-15 18:45:26", "2022-10-15 18:45:25", 100000 , "2022-09-20 18:45:27", "30");
        ticketSalesRepository.saveOne(ts);   //시작날짜                만료날짜,,            정기권 유형
        //-------------------
        //"90경4671"
        //1일권
        tu = new TicketUser(carNumbers[11], "010-7403-0316", "2022-09-26 15:15:26");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[11], "2022-09-25 15:15:27", "2022-09-26 15:15:26", 10000, "2022-09-25 15:15:27" , "1");
        ticketSalesRepository.saveOne(ts);

        //"81사7777"
        //1일권
        tu = new TicketUser(carNumbers[12], "010-5060-4914", "2022-09-27 07:27:26");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[12], "2022-09-26 07:27:27", "2022-09-27 07:27:26",10000, "2022-09-26 07:27:27" , "1");
        ticketSalesRepository.saveOne(ts);

        ///"49친8063"
        //30일권 연장 30+30
        tu = new TicketUser(carNumbers[13], "010-5683-9820", "2022-11-07 14:50:13");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[13], "2022-09-07 14:50:15", "2022-10-07 14:50:14", 100000 , "2022-09-07 14:50:15", "30");
        ts = new TicketSalesInfo(carNumbers[13], "2022-10-07 14:50:14", "2022-11-07 14:50:13", 100000 , "2022-09-10 14:50:15", "30");
        ticketSalesRepository.saveOne(ts);

        ///"30다9909"
        //30일권
        tu = new TicketUser(carNumbers[14], "010-4924-2940", "2022-10-11 18:01:26");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[14], "2022-09-11 18:01:27", "2022-10-11 18:01:26", 100000, "2022-09-11 18:01:27" , "30");
        ticketSalesRepository.saveOne(ts);

        ///"88장3275"
        //30일권
        tu = new TicketUser(carNumbers[15], "010-6878-6800", "2022-10-20 08:30:49");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[15], "2022-09-20 08:30:50", "2022-10-20 08:30:49", 100000, "2022-09-20 08:30:50" , "30");
        ticketSalesRepository.saveOne(ts);

        //"60다8970"
        //30일권
        tu = new TicketUser(carNumbers[16], "010-4924-2940", "2022-10-25 07:05:09");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[16], "2022-09-25 07:05:10", "2022-10-25 07:05:09", 100000, "2022-09-25 07:05:10" , "30");
        ticketSalesRepository.saveOne(ts);

        ///"30나2079"
        //30일권
        tu = new TicketUser(carNumbers[17], "010-8181-3683", "2022-10-09 11:20:07");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[17], "2022-09-09 11:20:08", "2022-10-09 11:20:07", 100000, "2022-09-09 11:20:08" , "30");
        ticketSalesRepository.saveOne(ts);

        //"61더6655"
        //30일권
        tu = new TicketUser(carNumbers[18], "010-7403-0316", "2022-10-01 15:30:09");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[18], "2022-09-01 15:30:10", "2022-10-01 15:30:09", 100000, "2022-09-01 15:30:10" ,"30");
        ticketSalesRepository.saveOne(ts);

        //"20장7907"
        //30일권
        tu = new TicketUser(carNumbers[19], "010-5060-4914", "2022-10-26 07:01:49");
        ticketUserRepository.saveOne(tu);
        ts = new TicketSalesInfo(carNumbers[19], "2022-09-26 07:01:50", "2022-10-26 07:01:49", 100000,  "2022-09-26 07:01:50" ,"30");
        ticketSalesRepository.saveOne(ts);
    }
    
    ///////////////////////////////////////////// 변경 금지 ///////////////////////////////////////////////////////////////////////
    private ParkingInfoRepository parkingInfoRepository = new ParkingInfoRepository();
    private TicketSalesRepository ticketSalesRepository = new TicketSalesRepository();
    private TicketUserRepository ticketUserRepository = new TicketUserRepository();

    private final int MAX_NUMBER = 10;

    private String[] carNumbers = {
            "45가1434", "13경4321", "25러7705", "18친5697", "68나2359", "41장5523", "55다7894", "16나1234", "89더5566", "79장5678",
            "67가9900", "90경4671", "81사7777", "49친8063", "30다9909", "88장3275", "60다8970", "30나2079", "61더6655", "20장7907"
    };

    private int makeRandomValue(int d) {
        return (int) ((Math.random() * 10000) % d);
    }

    private boolean isIn(LocalDateTime time, int prev) {
        return time.isBefore(LocalDateTime.now())
                && time.isAfter(LocalDateTime.of(2022, LocalDateTime.now().getMonth().getValue(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0).minusMonths(prev));
    }

    public void printAllParkingInfo() {
        HashMap<String, ArrayList<ParkingInfo>> all = parkingInfoRepository.findAll();
        for (String key : all.keySet()) {
            ArrayList<ParkingInfo> list = all.get(key);
            for (ParkingInfo info : list) {
                System.out.println(info);
            }
        }
    }

    public void printAllTicketSalesInfo() {
        Map<String, ArrayList<TicketSalesInfo>> all = ticketSalesRepository.findAll();
        for (String key : all.keySet()) {
            ArrayList<TicketSalesInfo> ticketSalesInfos = all.get(key);
            for (TicketSalesInfo info : ticketSalesInfos) {
                System.out.println(info);
            }
        }
    }

    public void printAllTicketUser() {
        HashMap<String, TicketUser> all = ticketUserRepository.findAll();
        for (String key : all.keySet()) {
            System.out.println(all.get(key));
        }
    }
    ///////////////////////////////////////////// 변경 금지 ///////////////////////////////////////////////////////////////////////
}

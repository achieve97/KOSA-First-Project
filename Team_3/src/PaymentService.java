import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class PaymentService {

    //주차 정보를 가져올 iv
    private ParkingInfoRepository parkingInfoRepository = new ParkingInfoRepository();
    //정기권 고객정보를 가져올 iv
    private TicketUserRepository ticketUserRepository = new TicketUserRepository();
    //회원권 판매에 관한 정보를 가져올 iv
    private TicketSalesRepository ticketSalesRepository = new TicketSalesRepository();

    // 입출차 정보 결제
    public void calculateOutPrice(ParkingInfo parkingInfo) {
        TicketUser savedUser = ticketUserRepository.findOne(parkingInfo.getCarNumber());

        // 회원이면서 정기권이 유효 이므로 결제를 하지 않음
        if (savedUser != null) {
            String[] splitInTime = savedUser.getLastEndTime().split(" ");
            String[] lastFirst = splitInTime[0].split("-");
            String[] lastSecond = splitInTime[1].split(":");
            LocalDateTime lastEndTime = LocalDateTime.of(
                    Integer.parseInt(lastFirst[0]),
                    Integer.parseInt(lastFirst[1]),
                    Integer.parseInt(lastFirst[2]),
                    Integer.parseInt(lastSecond[0]),
                    Integer.parseInt(lastSecond[1]),
                    Integer.parseInt(lastSecond[2])
            );

            if (!LocalDateTime.now().isAfter(lastEndTime)) {
                System.out.println("정기권 차량입니다.");
                return;
            }
        }

        // 회차 판단 (10분 이내)
        String[] sInTime = parkingInfo.getInTime().split(" ");
        String[] lastFirst = sInTime[0].split("-");
        String[] lastSecond = sInTime[1].split(":");
        LocalDateTime inTime = LocalDateTime.of(
                Integer.parseInt(lastFirst[0]),
                Integer.parseInt(lastFirst[1]),
                Integer.parseInt(lastFirst[2]),
                Integer.parseInt(lastSecond[0]),
                Integer.parseInt(lastSecond[1]),
                Integer.parseInt(lastSecond[2])
        );
        long diff = ChronoUnit.SECONDS.between(inTime, LocalDateTime.now());
        if (diff <= DefaultInfo.TURN_TIME) {
            System.out.println("회차 차량입니다.");
            return;
        }

        // 사전 결제 10분 이내
        if (parkingInfo.getPaymentTime() != null) {
            String[] spt = parkingInfo.getPaymentTime().split(" ");
            lastFirst = spt[0].split("-");
            lastSecond = spt[1].split(":");
            LocalDateTime localPaymentTime = LocalDateTime.of(
                    Integer.parseInt(lastFirst[0]),
                    Integer.parseInt(lastFirst[1]),
                    Integer.parseInt(lastFirst[2]),
                    Integer.parseInt(lastSecond[0]),
                    Integer.parseInt(lastSecond[1]),
                    Integer.parseInt(lastSecond[2])
            );

            diff = ChronoUnit.SECONDS.between(localPaymentTime, LocalDateTime.now());
            if (diff <= DefaultInfo.TURN_TIME) {
                System.out.println("이미 결제하셨습니다.");
                return;
            }
        }

        /**
         * 오류가 날 수 있을 것 같은 상황...
         */

        // 주차 금액 계산
        LocalDateTime now = LocalDateTime.now();
        diff = ChronoUnit.SECONDS.between(inTime, now);

        int tempPrice = 0;

        // 비 회차
        if (diff > DefaultInfo.TURN_TIME) { // 회차시간 지났을 경우
            tempPrice += DefaultInfo.PRICE;
            long dif = diff - DefaultInfo.TIME;
            int a = (int) (dif % DefaultInfo.EXTRA_TIME) != 0 ? DefaultInfo.EXTRA_PRICE : 0;
            tempPrice += ((dif / DefaultInfo.EXTRA_TIME) * DefaultInfo.EXTRA_PRICE) + a;
        }

        // 할인 대상인 경우 할인 산정
        if (parkingInfo.isDcType()) {
            tempPrice = (int)(tempPrice * DefaultInfo.DISCOUNT_PRICE);
        }

        // 금액 및 결제시간 산정
        parkingInfo.setPrice(tempPrice);
        parkingInfo.setPaymentTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        parkingInfoRepository.updateOne(parkingInfo);
        System.out.println(parkingInfo.printReceipt());
    }

    // 정기권 결제
    public TicketSalesInfo payTermTicket(TicketUser ticketUser, TicketSalesInfo ticketSalesInfo) {
        // ticketUser[정기권 구매한 사람만 정보] 는 carNumber, phoneNumber, lastEndTime 이 채워져 있음
        // ticketSalesInfo[정기권 구매에 대한 정보] 는 carNumber, startTime, endTime, termType 이 채워져 있음

        // 위의 정보를 활용하여 ticketSalesInfo[정기권 구매에 대한 정보] 의  price 계산 및 paymentTime[결제시간] 를 채워야함(세터!)
        int price = ticketSalesInfo.getTermType().equals("1") ? DefaultInfo.DAILY_TICKET_PRICE : DefaultInfo.MONTHLY_TICKET_PRICE;
        ticketSalesInfo.setPrice(price);

        // LocalDateTime 으로 바꾸기
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = date.format(cal.getTime());
        ticketSalesInfo.setPaymentTime(time);

        // ticketUserRepository[정기권 구매자를 읽고 쓰고 저장소] 를 통해 필요 메서드를 호출하여 ticketUser를 넘김
        ticketUserRepository.saveOne(ticketUser);
        // ticketSalesRepository[정기권 판매 저장소] 를 통해 ticketSalesInfo[정기권 구매에 대한 정보] 를 넘김 (
        ticketSalesRepository.saveOne(ticketSalesInfo);
        return ticketSalesInfo;
    }
}
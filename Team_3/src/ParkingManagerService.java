import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ParkingManagerService {

    private PaymentService paymentService = new PaymentService();
    private TicketUserRepository ticketUserRepository = new TicketUserRepository();
    private ParkingInfoRepository parkingInfoRepository = new ParkingInfoRepository();
    private TicketSalesRepository ticketSalesRepository = new TicketSalesRepository();
    private Scanner sc = new Scanner(System.in);
    private AdminService adminService = new AdminService();
    TestData td = new TestData();

    // 차량 입차
    private void in() {
        // 차량번호 입력
        System.out.println("차량번호를 입력해주세요.");
        String carNumber = sc.nextLine(); //대상문자열
        boolean CarNumber_check = Pattern.matches("([가-힣]{2})?\\s?(\\d{1,3})\\s?([가-힣])\\s?(\\d{4})", carNumber);

        //잘못된 차량번호 입력일 경우
        if(!CarNumber_check) {
            System.out.println("잘못입력하셨습니다.");
            return;
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(cal.getTime());

        // 이미 입차한 차량인지 확인
        ArrayList<ParkingInfo> list = parkingInfoRepository.findOne(carNumber);
        for (ParkingInfo info : list) {
            if (info.getOutTime() == null) {
                System.out.println("이미 들어와 있는 차량입니다.");
                return;
            }
        }

        // 현재시간 및 입력 차량 번호를 통해 입차 정보 저장
        parkingInfoRepository.saveOne(new ParkingInfo(now, carNumber));

        // 입차한 차량의 정기권 유무 판별을 위해 정기권 정보를 얻어옴
        TicketUser ticketUser = ticketUserRepository.findOne(carNumber);


        // 정기권 차량이 아닐경우 입차시간을 보임
        if (ticketUser == null) {
            System.out.println("환영합니다.");
            System.out.println("입차 시간 " + now);
        }
        // 정기권을 보유한 차량일 경우
        else {
            // 회원인 경우 기간권 만료 판단
            String[] lastEndTimeArr = ticketUser.getLastEndTime().split(" ");
            String[] lastFirst = lastEndTimeArr[0].split("-");
            String[] lastSecond = lastEndTimeArr[1].split(":");
            LocalDateTime lastEndTime = LocalDateTime.of(
                    Integer.parseInt(lastFirst[0]),
                    Integer.parseInt(lastFirst[1]),
                    Integer.parseInt(lastFirst[2]),
                    Integer.parseInt(lastSecond[0]),
                    Integer.parseInt(lastSecond[1]),
                    Integer.parseInt(lastSecond[2])
            );

            // 회원이지만 기간권이 만료된 경우
            if (lastEndTime.isBefore(LocalDateTime.now())) {
                System.out.println("기간권이 만료된 차량입니다.\n 입차 시간 " + now);
            }
            // 정기권이 유효한 경우
            else {
                System.out.println("반갑습니다." + "정기권 "+ ticketUser.getLastEndTime() +"까지입니다.");
            }
        }
    }

    // 사전 결제
    private void makePrePayment() {
        // 차량번호 정규식 패턴
        String CPattern = "([가-힣]{2})?\\s?(\\d{1,3})\\s?([가-힣])\\s?(\\d{4})";

        // 차번호 입력
        System.out.println("차량 번호를 입력하세요");
        System.out.print("차량 번호 >> ");
        String carNumber = sc.nextLine();

        // 차량번호 정규식 판별
        boolean CarNumber_check = Pattern.matches(CPattern, carNumber);
        if (!CarNumber_check) {
            System.out.println("잘못입력하셨습니다.");
            return;
        }

        // parkingInfoRepository 를 통하여 입차 정보를 얻음
        ArrayList<ParkingInfo> one = parkingInfoRepository.findOne(carNumber);
        ParkingInfo parkingInfo = null;
        for (ParkingInfo info : one) {
            if (info.getPaymentTime() == null || info.getCarNumber().equals(carNumber)) {
                parkingInfo = info;
                break;
            }
        }

        // 찾아온 정보가 null 인 경우
        if (parkingInfo == null) {
            System.out.println("입차된 차량이 아닙니다.");
            return;
        }

        // 주차정보를 통하여 결제
        paymentService.calculateOutPrice(parkingInfo);
    }

    // 출차
    private void out() {
        System.out.println("차량 번호를 입력해주세요.");
        String carNumber = sc.nextLine();

        // 주차 정보를 얻어옴
        ArrayList<ParkingInfo> list = parkingInfoRepository.findOne(carNumber);
        ParkingInfo parkingInfo = null;
        for (ParkingInfo info : list) {
            if (info.getOutTime() == null) {
                parkingInfo = info;
                break;
            }
        }

        // 입차한 차량이 아닌 경우
        if (parkingInfo == null) {
            System.out.println("입차한 차량이 아닙니다.");
            return;
        }

        // 주차정보를 통해 결제
        paymentService.calculateOutPrice(parkingInfo);
        parkingInfo.setOutTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 취합된 주차 정보를 저장
        parkingInfoRepository.updateOne(parkingInfo);
        System.out.println("출차시간 : " + parkingInfo.getOutTime());
        System.out.println("안녕히가세요.");
    }

    // 정기권 구매
    public void signIn() {
        // 정기권을 사용하기 위해 회원가입
        // 정기권 결제
        // 정기권을 사용하기 위해 회원가입
        String CPattern = "([가-힣]{2})?\\s?(\\d{1,3})\\s?([가-힣])\\s?(\\d{4})"; // 차량번호 정규식 패턴
        String PPattern = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$"; // 핸드폰번호 정규식 하이픈 구별 안함(무조건 고정)
        String carNumber = null; // 차량번호
        String phoneNumber = null; // 전화번호

        System.out.println("=====================회원가입=======================");

        // 차량번호 입력
        System.out.println("차량 번호를 입력해주세요");
        System.out.print("차량 번호 >> ");
        carNumber = sc.nextLine();
        boolean CarRegex = Pattern.matches(CPattern, carNumber); // 차량번호 정규식 판별

        if (!CarRegex) {
            System.out.println("올바른 차량번호가 아닙니다.");
            return;
        }

        // 차량번호 정규식 통과
        //차 번호로 회원판별
        TicketUser ticketUser = ticketUserRepository.findOne(carNumber);
        TicketUser newTicketUser = null;

        // 비회원, 최초구매
        if (ticketUser == null) {
            System.out.println("전화 번호를 입력해주세요");
            System.out.print("전화번호 >> ");
            phoneNumber = sc.nextLine();
            boolean PhoneRegex = Pattern.matches(PPattern, phoneNumber);
            // 전화번호 정규식 통과
            if (PhoneRegex) {
                LocalDateTime now = LocalDateTime.now();
                String nownow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                // 차량번호, 전화번호, 현재시간을 통해 정기권 정보 새로 만듦
                newTicketUser = new TicketUser(carNumber, phoneNumber, nownow);
            }
            // 정규식 통과 실패
            else {
                System.out.println("올바른 전화번호가 아닙니다.");
                return;
            }
        }
        // 입력한 차량의 정기권이 존재하는 경우
        // ticketUserRepository 를 통해 찾아온 정기권 정보의 주소를 newTicketUser 넣음
        else {
            newTicketUser = ticketUser;
        }

        boolean run = true;
        while (run) {
            System.out.println();
            System.out.println("정기권 기간을 선택해주세요. (1 또는 30 입력. -1은 뒤로가기)");
            int termType = choiceCommandNumber(); // 정기권 날짜를 선택하는 로직(숫자만 받아야 함)
            // 입력된 값이 1 or 30일 경우
            if(termType == DefaultInfo.MONTHLY_TICKET_TERM
                    || termType == DefaultInfo.DAILY_TICKET_TERM)
            {
                String[] A = newTicketUser.getLastEndTime().split(" ");
                String[] B = A[0].split("-");
                String[] C = A[1].split(":");
                LocalDateTime D = LocalDateTime.of(
                        Integer.parseInt(B[0]),
                        Integer.parseInt(B[1]),
                        Integer.parseInt(B[2]),
                        Integer.parseInt(C[0]),
                        Integer.parseInt(C[1]),
                        Integer.parseInt(C[2])
                );

                // 정기권 만료일자 구하기
                LocalDateTime E = D.plusDays(termType);
                // 정기권 판매 정보 생성
                TicketSalesInfo newTicketSalesInfo = new TicketSalesInfo(
                        carNumber,
                        D.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        E.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        String.valueOf(termType)
                );

                // 정기권 만료일자 산정
                newTicketUser.setLastEndTime(E.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                // 결제에 정보 전달
                TicketSalesInfo savedTicketSalesInfo = paymentService.payTermTicket(newTicketUser, newTicketSalesInfo);
                System.out.println("결제되었습니다.");
                System.out.println(savedTicketSalesInfo);
                run = false;
            } else if(termType == -1) {
                System.out.println("종료합니다.");
                run = false;
            } else {
                System.out.println("올바른 숫자를 입력해주세요");
            }
        }
    }

    public void run() {
        int input = 0;
        while (true) {
            showParkingOption();
            input = choiceCommandNumber();
            switch (input) {
                case 1: // 입차
                    this.in();
                    break;
                case 2: // 사전 결제
                    this.makePrePayment();
                    break;
                case 3: // 출차:
                    this.out();
                    break;
                case 4: // 회원가입
                    this.signIn();
                    break;
                case 5: // 관리자
                    this.adminService.runAdminService(this);
                    break;
                case 6: // 종료
                    System.out.println("프로그램을 종료합니다.");
                    return;
                case 7: // 더미데이터 생성 
                	td.createInCarData();
                	td.createTicketData();
                	td.createInAndOutData();
                	break;
                case 8: // 생성되었는지 확인
                	td.printAllParkingInfo();
                	td.printAllParkingInfo();
                	td.printAllTicketSalesInfo();
                	break;
            }
        }
    }

    private void showParkingOption() {
        int nowNumber = parkingInfoRepository.findInCar().size();
        System.out.println("===================== What The Parking =====================");
        System.out.printf("현재 주차 가능 대수 : %d", 50 - nowNumber);
        System.out.println();
        System.out.println("============================================================");
        System.out.println("1. 입차 2. 사전 결제 3. 출차 4. 정기권 구매 5. 관리자 6. 종료");
        System.out.println("============================================================");
    }

    private int choiceCommandNumber() {
        // 숫자만 입력받을 수 있게함
        int inputValue = 0;
        boolean isRight = true;
        while (isRight) {
            try {
                inputValue = Integer.parseInt(sc.nextLine());
                isRight = false;
            } catch (Exception e) {
                isRight = false;
                System.out.println("숫자를 입력해주세요~!");
            }
        }
        return inputValue;
    }
}

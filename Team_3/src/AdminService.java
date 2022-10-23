import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AdminService {

    private Admin admin = new Admin("admin", "1q2w3e4r");	//관리자 ID, PW 부여
    private ParkingInfoRepository parkingInfoRepository = new ParkingInfoRepository();
    private TicketSalesRepository ticketSalesRepository = new TicketSalesRepository();
    private TicketUserRepository ticketUserRepository = new TicketUserRepository();
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
    private Scanner sc = new Scanner(System.in);
    private ParkingManagerService parkingManager;

    // 매출 조회를 위해 날짜를 계산
    public String[] sendStartDay() {
        System.out.println("매출 조회할 기간을 입력하세요. ex) 7 ");
        int days = Integer.parseInt(sc.nextLine());
        cal.setTime(new Date());
        String now = time.format(cal.getTime());            // 현재
        cal.add(Calendar.DATE, -days);                      // 매출조회할 기준일
        String startday = time.format(cal.getTime());       // 현재를 통해 시작날짜를 계산함
        return new String[]{startday, now};
    }

    // 매출 조회 (정기권 구매, 입출차)
    public void searchSale() {

        // 조회할 날짜의 시작 날짜 범위를
        String[] result = sendStartDay(); // startDay, now 값

        // 파일에 저장된 매출 기록을 얻음
        ArrayList<ParkingInfo> list1 = parkingInfoRepository.findParkingSalesList(result); // start day 이후 입출차 매출 총배열
        ArrayList<TicketSalesInfo> list2 = ticketSalesRepository.findTicketSalesList(result); // start day 이후 회원권 매출 총배열

        // 매출을 계산을 누적하는 변수
        int inoutSale = 0;
        int memberSale = 0;

        //for문 돌려서 list1, list2 각각 합계 구해서 총합 계산.
        for(int i = 0 ; i < list1.size() ; i++){          // 입출차 매출 총배열 합계
            inoutSale += list1.get(i).getPrice();
        }
        for(int i = 0 ; i < list2.size() ; i++){         // 회원권 매출 총배열 합계
            memberSale += list2.get(i).getPrice();
        }

        int saleList = inoutSale + memberSale;          // 입출차 매출 + 회원권 매출 총합계

        // 출력부
        System.out.println(result[0] + " ~ " + result[1] + " 기간의 매출을 조회합니다.");
        System.out.println("입출차 매출건수 : " + list1.size() + " 회원권 매출건수 : " + list2.size());
        System.out.println("입출차 매출액 : " + inoutSale + " 회원권 매출액 : " + memberSale);
        System.out.println("총 매출 : " + saleList);       // 매출 합계 출력
    }

    // 관리자 회원 관리
    private void manageMember(){
        while (true) {
            System.out.println("[1] 회원등록 [2] 회원삭제 [3] 회원목록조회 [4]나가기");
            int showMemberChoice = Integer.parseInt(sc.nextLine());
            // 회원 등록
            if(showMemberChoice == 1) {
                parkingManager.signIn();
            }
            // 회원 삭제
            else if (showMemberChoice == 2) {
                System.out.println("삭제할 차량번호를 입력해주세요.");
                String carNumber = sc.nextLine();
                TicketUser ticketUser = ticketUserRepository.deleteOne(carNumber);
                if (ticketUser == null) {
                    System.out.println("존재하지 않은 회원입니다.");
                } else {
                    System.out.println(carNumber + " 번호의 회원을 삭제했습니다.");
                }
            }
            // 회원목록조회
            else if (showMemberChoice == 3) {
                ArrayList<TicketUser> ticketUserList = ticketUserRepository.findTicketUserList();
                for (TicketUser ticketUser : ticketUserList) {
                    System.out.println(ticketUser);
                }
            }
            // 나가기
            else if (showMemberChoice == 4) {
                System.out.println("'회원관리' 메뉴를 종료합니다.");
                return;
            }
            // 잘못된 정보 입력시
            else {
                System.out.println("1 ~ 4 숫자를 선택해주세요.");
            }
        }
    }

    // 관리자 주차장 기본 정보 설정
    private void changePrice() {
        while(true) {
            System.out.println("--------------------- < 요금변경 메뉴 > --------------------------");
            System.out.println("변경하실 요금 유형을 선택하세요");
            System.out.println("[1] 기본시간 [2] 기본요금 [3] 추가시간 [4] 추가요금 [5] 나가기");
            int changeDefault = Integer.parseInt(sc.nextLine());
            //기본 시간 변경
            if(changeDefault == 1)  {
                System.out.printf("기본 시간을 재설정하세요. (현재 기본 시간 : %s%s)", DefaultInfo.TIME, "분");
                System.out.println();
                String newTime = sc.nextLine();
                DefaultInfo.TIME = Integer.parseInt(newTime);
                System.out.printf("기본 시간이 %s%s으로 재설정 되었습니다.", newTime, "분");
                System.out.println();

            }
            // 기본 요금 변경
            else if (changeDefault == 2){
                System.out.printf("기본 요금을 재설정하세요. (현재 기본 요금 : %d%s)", DefaultInfo.PRICE, "원");
                System.out.println();
                int newPrice = Integer.parseInt(sc.nextLine());
                DefaultInfo.PRICE = newPrice;
                System.out.printf("기본 요금이 %d%s로 재설정 되었습니다.", newPrice," 원");
                System.out.println();
            }
            // 추가 시간 변경
            else if (changeDefault == 3){
                System.out.printf("추가 시간을 재설정하세요. (현재 추가 시간 : %s%s)", DefaultInfo.EXTRA_TIME,"분");
                System.out.println();
                String newTime2 = sc.nextLine();
                DefaultInfo.EXTRA_TIME = Integer.parseInt(newTime2);
                System.out.printf("추가 시간이 %s%s으로 재설정 되었습니다.", newTime2, "분");
                System.out.println();

            }
            // 추가 요금 변경
            else if (changeDefault == 4) {
                System.out.printf("추가 요금을 재설정하세요. (현재 추가 요금 : %d%s)", DefaultInfo.EXTRA_PRICE,"원");
                System.out.println();
                int newPrice2 = Integer.parseInt(sc.nextLine());
                DefaultInfo.EXTRA_PRICE = newPrice2;
                System.out.printf("추가 요금이 %d%s으로 재설정 되었습니다.", newPrice2, "원");
                System.out.println();
            }
            // 나가기
            else if (changeDefault == 5) {
                return;
            }
            // 잘못된 정보 입력 시 다시 입력창으로 돌아감
            else {
                System.out.println("올바른 메뉴를 선택하세요.");
                break;
            }
        }
    }

    // 관리자 차량번호 조회
    private void searchCarNumber() {
        /**
         * 입차 중인 차량 보여주기
         */
        System.out.println("--------------------- < 차량조회 메뉴 > --------------------------");
        System.out.println("검색하실 차량번호를 입력하세요.");
        System.out.println();
        String CarNumber = sc.nextLine(); //대상문자열
        boolean CarNumber_check = Pattern.matches("([가-힣]{2})?\\s?(\\d{1,3})\\s?([가-힣])\\s?(\\d{4})",CarNumber);
        while(true){
            //유효한 차량번호의 경우
            if(CarNumber_check) {
                // 주차 정보 데이터 가져오기
                ArrayList<ParkingInfo> list = parkingInfoRepository.findOne(CarNumber);
                for (ParkingInfo info : list) {
                    System.out.println(info);
                }
                break;
            }
            //유효하지 않은 차량번호의 경우
            else {
                System.out.println("올바르지 않은 형식의 차량번호입니다. 다시 입력하세요.");
                break;
            }
        }
    }

    // 입차중인 차량의 정보를 출력
    private void printInCar() {
        ArrayList<ParkingInfo> inCarList = parkingInfoRepository.findInCar();
        // 입차시간, 차번호, 할인유무
        for (ParkingInfo parkingInfo : inCarList) {
            System.out.println("차번호 : " + parkingInfo.getCarNumber() +
                               " 입차시간 : " + parkingInfo.getInTime() +
                               " 할인유무 : " + (parkingInfo.isDcType() ? "o" : "x"));
        }
    }

    // 관리자 기능 실행 메서드
    public void runAdminService(ParkingManagerService parkingManager) {
        this.parkingManager = parkingManager;
        while(true) {
            // 관리자 로그인
            System.out.println("관리자모드에 접속하기 위해 아이디와 비밀번호를 입력하세요.");
            System.out.print("ID : ");
            String adminID = sc.nextLine();
            System.out.print("Password : ");
            String adminPw = sc.nextLine();
            if(adminID.equals(admin.getId()) && adminPw.equals(admin.getPassword())) {
                System.out.println("                  *** 환영합니다 'admin'님! ***         ");
                while (true) {
                    System.out.println("--------------------- < 관리자 메뉴 > --------------------------");
                    System.out.println("[1] 매출조회 [2] 회원관리 [3] 요금변경 [4] 입차 중인 차량 [5] 특정번호조회 [6] 나가기");
                    int input = Integer.parseInt(sc.nextLine());
                    switch (input) {
                        case 1: // 매출 조회
                            this.searchSale();
                            break;
                        case 2: // 회원 관리
                            this.manageMember();
                            break;
                        case 3: // 요금 변경
                            this.changePrice();
                            break;
                        case 4: // 입차 차량 보이기
                            this.printInCar();
                            break;
                        case 5: // 입차 중인 차량
                            this.searchCarNumber();
                            break;
                        case 6: // 나가기
                            System.out.println("초기 메뉴로 돌아갑니다.");
                            return;
                        default : System.out.println("올바른 메뉴를 선택하세요.");
                    }
                }
            } else {
                System.out.println("        ** 잘못된 관리자 ID 혹은 Password 입니다.**        ");
                return;
            }
        }
    }
}

import java.io.Serializable;

/**
 * 종료 날짜만 덮어쓰기
 * 회원권 구매시 신규 혹은 연장 판단
 * 입차 시, 회원/비회원 구분 (IO를 통해 찾아보기)
 * 회원 목록 조회
 */

public class TicketUser implements Serializable {
    // 차량정보
    private String carNumber;
    // 휴대전화 번호
    private String phoneNumber;
    // 기간권 만료 날짜
    private String lastEndTime;

    public TicketUser(String carNumber, String phoneNumber, String lastEndTime) {
        this.carNumber = carNumber;
        this.phoneNumber = phoneNumber;
        this.lastEndTime = lastEndTime;
    }

    public String getLastEndTime() {
        return lastEndTime;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setLastEndTime(String lastEndTime) {
        this.lastEndTime = lastEndTime;
    }

    @Override
    public String toString() {
        return "정기권 {" +
                "차량번호 ='" + carNumber + '\'' +
                ", 전화번호 ='" + phoneNumber + '\'' +
                ", 만료날짜 ='" + lastEndTime + '\'' +
                '}';
    }
}

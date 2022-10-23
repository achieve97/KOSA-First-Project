import java.io.Serializable;

/**
 * 회원권 판매할 때 사용, 매출
 * 영수증을 위한 정보
 */
public class TicketSalesInfo implements Serializable {
    //private int id;
    private String carNumber; //
    private String startTime; //
    private String endTime; //
    private int price;
    private String paymentTime;
    private String termType; // 기간권 종류 (2주 4주 ....)

    public TicketSalesInfo(String carNumber, String startTime, String endTime, String termType) {
        this.carNumber = carNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.termType = termType;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public String getTermType() {
        return termType;
    }

    public int getPrice() {
        return price;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Override
    public String toString() {
        return "정기권 {" +
                "차량 번호 ='" + carNumber + '\'' +
                ", 시작 시간 ='" + startTime + '\'' +
                ", 만료 시간 ='" + endTime + '\'' +
                ", 금액 =" + price +
                ", 결제 금액 ='" + paymentTime + '\'' +
                ", 종류 ='" + termType + '\'' +
                '}';
    }
    
    public TicketSalesInfo(String carNumber, String startTime, String endTime, int price, String paymentTime, String termType) {
        this.carNumber = carNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.paymentTime = paymentTime;
        this.termType = termType;
    }
    
    
    
}

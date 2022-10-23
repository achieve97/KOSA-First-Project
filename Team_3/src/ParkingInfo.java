import java.io.Serializable;

// 입출차 기록 데이터, 영수증, 매출 조회
public class ParkingInfo implements Serializable {
    // 입차 시 들어오는 정보
    // 입차 시간
    private String inTime;
    // 이미 입력되는 정보
    private String carNumber;
    // 할인 유무
    private boolean dcType;
    // 경우에 따라 달라짐
    private String outTime;
    // 기간 내 정기권 및 회차를 제외하면 paymentService 에서 결정됨
    private int price;
    // 결제 시간
    private String paymentTime;

    public ParkingInfo(String inTime, String carNumber) {
        this.inTime = inTime;
        this.carNumber = carNumber;
        // 차량 번호에 경, 친, 장이 있으면 할인 유무 필드가 True 가 됨
        if (this.carNumber.contains("경") || this.carNumber.contains("친") || this.carNumber.contains("장")) {
            this.dcType = true;
        }
    }

    public boolean isDcType() {
        return dcType;
    }

    public String getInTime() {
        return inTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public int getPrice() {
        return price;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // 영수증 출력 정보 전달 메서드
    public String printReceipt() {
        return "영수증 {" +
                "입차 시간 ='" + inTime + '\'' +
                ", 결제 금액 =" + price +
                ", 결제 시간 ='" + paymentTime + '\'' +
                ", 차량 번호 ='" + carNumber + '\'' +
                ", 할인 유무 =" + (dcType ? "o" : "x") +
                '}';
    }

    @Override
    public String toString() {
        return "{" +
                "입차 시간 ='" + inTime + '\'' +
                ", 출차 시간 ='" + outTime + '\'' +
                ", 결제 금액 =" + price +
                ", 결제 시간 ='" + paymentTime + '\'' +
                ", 차량 번호 ='" + carNumber + '\'' +
                ", 할인 유무 =" + (dcType ? "o" : "x") +
                '}';
    }
    
    public ParkingInfo(String inTime, String carNumber, String outTime, int price, String paymentTime) {
        this.inTime = inTime;
        this.carNumber = carNumber;
        this.outTime = outTime;
        this.price = price;
        this.paymentTime = paymentTime;
        if (this.carNumber.contains("경") || this.carNumber.contains("친") || this.carNumber.contains("장")) {
            this.dcType = true;
        }
    }
    
    
}

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// 정기권 판매 정보 데이터를 얻을 수 있는 저장소
public class TicketSalesRepository {
    private Map<String, ArrayList<TicketSalesInfo>> ticketSalesMap;
    private File file;

    public TicketSalesRepository() {
        this.ticketSalesMap = new HashMap<>();
        this.file = new File("ticket_sales_info.txt");
    }

    // 정기권 구매 정보 단건 저장
    public void saveOne(TicketSalesInfo ticketSalesInfo) {
        // 파일에 저장된 정기권 판매 정보 얻어오기
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            ticketSalesMap = (HashMap<String, ArrayList<TicketSalesInfo>>) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }

        // 차량 정보를 HashMap에 저장
        String carNumber = ticketSalesInfo.getCarNumber();
        if (ticketSalesMap.containsKey(carNumber)) {
            ticketSalesMap.get(carNumber).add(ticketSalesInfo);
        } else {
            ArrayList<TicketSalesInfo> list = new ArrayList<>();
            list.add(ticketSalesInfo);
            ticketSalesMap.put(carNumber, list);
        }

        // 새롭게 갱신된 HashMap을 파일에 새롭게 overwrite함
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(ticketSalesMap);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }
    }

    /**
     * 정기권매출을 위한 List 정보를 주는 메서드
     */
    public ArrayList<TicketSalesInfo> findTicketSalesList(String[] dayInfo) {

        // 파일에 저장된 데이터를 가져옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            ticketSalesMap = (HashMap<String, ArrayList<TicketSalesInfo>>) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }

        // 파라미터 정보를 LocalDateTime로 바꿈
        String startTime = dayInfo[0];
        String endTime = dayInfo[1];
        String[] splitStart = startTime.split("-");
        String[] splitEnd = endTime.split("-");
        LocalDate startLocalDate = LocalDate.of(
                Integer.parseInt(splitStart[0]),
                Integer.parseInt(splitStart[1]),
                Integer.parseInt(splitStart[2])
        );
        LocalDate endLocalDate = LocalDate.of(
                Integer.parseInt(splitEnd[0]),
                Integer.parseInt(splitEnd[1]),
                Integer.parseInt(splitEnd[2])
        );

        // Map에 들어있는 입출차 기록을 시작날과 마감날 안에 있는 기록만 찾아서 ArrayList에 담음
        ArrayList<TicketSalesInfo> result = new ArrayList<>();
        Set<String> keySet = ticketSalesMap.keySet();
        for (String key : keySet) {
            ArrayList<TicketSalesInfo> list = ticketSalesMap.get(key);
            for (TicketSalesInfo savedInfo : list) {
            	
            	if (savedInfo.getPaymentTime() == null) {
                    continue;
                }
            	
                String[] splitPaymentTime = savedInfo.getPaymentTime().split(" ");
                String[] A = splitPaymentTime[0].split("-");
                LocalDate paymentLocalDate = LocalDate.of(
                        Integer.parseInt(A[0]),
                        Integer.parseInt(A[1]),
                        Integer.parseInt(A[2])
                );
                // 시작날짜보다 앞선 날짜인 경우
                if (startLocalDate.isAfter(paymentLocalDate)) {
                    continue;
                }
                // 끝날짜보다 이후 날짜인 경우
                if (endLocalDate.isBefore(paymentLocalDate)) {
                    continue;
                }
                result.add(savedInfo);
            }
        }
        return result;
    }
    
    /**
     * findOne, 나중에 필요할 수 있음...
     */
    public Map<String, ArrayList<TicketSalesInfo>> findAll() {
        // 파일에 저장된 데이터를 가져옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            ticketSalesMap = (HashMap<String, ArrayList<TicketSalesInfo>>) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }
        return ticketSalesMap;
    }
    
}

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ParkingInfoRepository {

    private HashMap<String, ArrayList<ParkingInfo>> parkingInfoMap;
    private File file;

    public ParkingInfoRepository() {
        this.parkingInfoMap = new HashMap<>();
        this.file = new File("parking_info.txt");
    }

    // 주차 정보 단건 저장
    public void saveOne(ParkingInfo parkingInfo) {

        // 파일에서 데이터를 읽어옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            parkingInfoMap = (HashMap<String, ArrayList<ParkingInfo>>) ois.readObject();
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

        String carNumber = parkingInfo.getCarNumber();
        // 차량 입출차 기록이 있는 경우
        if (parkingInfoMap.containsKey(carNumber)) {
            parkingInfoMap.get(carNumber).add(parkingInfo);
        } else {
            // 차량 입출차 기록이 없는 경우 (완전 처음 들어온 경우)
            ArrayList<ParkingInfo> list = new ArrayList<>();
            list.add(parkingInfo);
            parkingInfoMap.put(carNumber, list);
        }

        // HashMap에 데이터를 기록 후 파일에 overwrite 함
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(parkingInfoMap);
        } catch(Exception e) {
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

    // 특정 차량 입출차 기록 갱신
    public void updateOne(ParkingInfo parkingInfo) {

        // 파일에서 주차 기록을 얻음
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            parkingInfoMap = (HashMap<String, ArrayList<ParkingInfo>>) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e){
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

        /**
         * 아래가 실행이 된다면 파일에서 데이터를 잘 찾아 온 것임
         * ArrayList 를 순회
         */
        // 차량 번호를 통해 HashMap에서 차량의 주차정보 ArrayList를 찾음
        ArrayList<ParkingInfo> list = parkingInfoMap.get(parkingInfo.getCarNumber());
        int len = parkingInfoMap.size();
        // 주차 정보 ArrayList를 순회
        for (int i = 0; i < len; ++i) {
            ParkingInfo savedInfo = list.get(i);

            // 파라미터의 차량번호와 파일에서 찾은 차번호 같으면서
            // 출차 시간이 null인 경우를 찾으면서 결제 시간, 금액, 출차 시간을 갱신함
            if (savedInfo.getCarNumber().equals(parkingInfo.getCarNumber()) && savedInfo.getOutTime() == null) {
                savedInfo.setPaymentTime(parkingInfo.getPaymentTime());
                savedInfo.setPrice(parkingInfo.getPrice());
                savedInfo.setOutTime(parkingInfo.getOutTime());
                break;
            }
        }

        // 갱신된 데이터를 포함하여 append false로 하여 정보를 파일에 overwrite함
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;

        try{
            fos = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(parkingInfoMap);
        }catch(Exception e){
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
                parkingInfoMap.clear();
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }
    }

    // 특정 차량의 주차기록을 찾음
    public ArrayList<ParkingInfo> findOne(String carNumber) {

        // 파일 정보 읽어오기
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            parkingInfoMap = (HashMap<String, ArrayList<ParkingInfo>>) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e){
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

        // HashMap에 특정 차량의 주차정보 ArrayList가 있는 경우 result에 받아 return
        ArrayList<ParkingInfo> result = new ArrayList<>();
        if (parkingInfoMap.containsKey(carNumber)) {
            result = parkingInfoMap.get(carNumber);
        }
        return result;
    }

    // 시작 ~ 끝 날짜의 입출차 정보를 얻음
    public ArrayList<ParkingInfo> findParkingSalesList(String[] dayInfo) {

        // 파일에서 데이터를 얻어옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            parkingInfoMap = (HashMap<String, ArrayList<ParkingInfo>>) ois.readObject();
        } catch(Exception e) {
            System.out.println("불러오는데 실패하였습니다.");
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

        //  파라미터 정보를 LocalDateTime로 바꿈
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
        ArrayList<ParkingInfo> result = new ArrayList<>();
        Set<String> mapKeySet = parkingInfoMap.keySet();
        for (String key : mapKeySet) {
            ArrayList<ParkingInfo> list = parkingInfoMap.get(key);
            for (ParkingInfo savedInfo : list) {
            	
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

    // 입차중인 차량 정보 전달
    public ArrayList<ParkingInfo> findInCar() {

        // 파일 정보 읽어오기
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            parkingInfoMap = (HashMap<String, ArrayList<ParkingInfo>>) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e){
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

        // 입차중인 차량 정보 ArrayList에 넣음
        ArrayList<ParkingInfo> result = new ArrayList<>();
        for (String key : parkingInfoMap.keySet()) {
            ArrayList<ParkingInfo> list = parkingInfoMap.get(key);
            for (ParkingInfo info : list) {
                if (info.getOutTime() == null) {
                    result.add(info);
                }
            }
        }
        return result;
    }
    
    public HashMap<String, ArrayList<ParkingInfo>> findAll() {
        // 파일 정보 읽어오기
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            parkingInfoMap = (HashMap<String, ArrayList<ParkingInfo>>) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e){
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
        return parkingInfoMap;
    }
    
}

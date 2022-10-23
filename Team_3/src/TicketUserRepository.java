import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TicketUserRepository {
    private HashMap<String, TicketUser> map;
    private File file;

    public TicketUserRepository() {
        this.map = new HashMap<>();
        this.file = new File("ticket_user.txt");
    }

    // 정기권 구매자 정보 하나 얻음
    public TicketUser findOne(String carNumber) {

        // 정기권 구매자 정보를 저장한 파일에서
        // 역직렬화를 통해서 데이터를 HashMap에 가져옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            // ObjectInputStream에서 EOFException이 발생하는 경우는 읽어올 데이터가 아예 없는 경우임.
            ois = new ObjectInputStream(bis);
            map = (HashMap) ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch (Exception e) {
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

        // null이 return 되면 데이터가 없는 것임
        return map.get(carNumber);
    }

    // 정기권 정보 단건 저장하기
    public void saveOne(TicketUser ticketUser) {

        // 파일에서 데이터 읽어오기
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            map = (HashMap)ois.readObject();
        } catch (EOFException e) {
            // 읽어올 파일이 없는 경우
        } catch(Exception e) {
            e.printStackTrace();
            map = new HashMap<>();
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

        // 신규 또는 갱신 갱신을 할 수 있음
        // (map 은 put 할 경우 새로운 것이면 그냥 저장하고, 기존에 존재하는 것이면 갱신함)
        map.put(ticketUser.getCarNumber(), ticketUser);

        // 정기권 정보를 파일에 저장
        // 파일에 쓰기
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(map);
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

    // 정기권 매출 조회 정보 리스트 얻음
    public ArrayList<TicketUser> findTicketUserList() {

        // 파일에서 데이터를 읽어옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            map = (HashMap)ois.readObject();
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

        // 파일에서 얻어온 데이터를 ArrayList에 저장함
        ArrayList<TicketUser> result = new ArrayList<>();
        for (String key : map.keySet()) {
            result.add(map.get(key));
        }
        return result;
    }

    // 정기권 정보 단건 삭제하기
    public TicketUser deleteOne(String carNumber) {

        // 파일에서 데이터를 읽어옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            map = (HashMap)ois.readObject();
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

        TicketUser remove = null;

        // map에 차량 번호가 있는 경우
        if (map.containsKey(carNumber)) {
            remove = map.remove(carNumber);
        }

        // 데이터가 삭제된 HashMap을  파일에 직렬화를 통해서 저장
        // new FileOutputStream(file, false) 에서 두번째 인자 append 가 false 인 경우는 통째로 덮어씀
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(map);
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
        return remove;
    }
    
    public HashMap<String, TicketUser> findAll() {
        // 파일에서 데이터를 읽어옴
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            map = (HashMap)ois.readObject();
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
        return map;
    }
    
}

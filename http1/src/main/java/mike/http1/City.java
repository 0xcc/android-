package mike.http1;

/**
 * Created by Administrator on 16-4-27.
 */
public class City {
    private String name;
    private String code;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return  id+" , "+name+", "+code;
    }

}

package cn.hwwwwh.lexiangdaxue.ProductClass.bean;

/**
 * Created by 97481 on 2016/11/13.
 */
public class Category {
    private int id;
    private String summary;
    @Override
    public String toString(){
        return "News{" + "id=" + id + ", summary='" + summary + '\'' + '}';
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getSummary(){
        return this.summary;
    }
    public void setSummary(String  summary){
        this.summary=summary;
    }
}

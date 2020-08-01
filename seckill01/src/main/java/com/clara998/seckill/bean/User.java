package com.clara998.seckill.bean;

/**
 * @author clara
 * @date 2020/7/27
 */
public class User {
    private int id;
    private String name;

    //或者@Data 相当于同时@Setter @Getter
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

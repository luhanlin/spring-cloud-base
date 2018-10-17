package com.luhanlin.common.enums;

/**
 * 数据中心枚举，每个业务模块的数据中心id
 *
 * @create 2018-06-07 16:56
 **/
public enum DataCenterEnum {


    DEMO("DEMO",0);

    private String type;
    private int value;


    DataCenterEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public static int getValueByType(String type) throws Exception{
        for(DataCenterEnum dc : DataCenterEnum.values()){
            if(dc.getType().equals(type)){
                return dc.getValue();
            }
        }
        throw new Exception("未找到相应类型");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

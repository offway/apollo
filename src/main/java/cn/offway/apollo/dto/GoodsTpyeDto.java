package cn.offway.apollo.dto;

import io.swagger.annotations.Api;

import java.util.List;

public class GoodsTpyeDto {

    /**尺码**/
    private List size;

    /**品类**/
    private String category;

    public List getSize() {
        return size;
    }

    public void setSize(List size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

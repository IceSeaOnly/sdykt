package site.binghai.biz.entity;

import lombok.Data;
import site.binghai.lib.interfaces.SessionPersistent;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelectId implements SessionPersistent {
    private List<Long> ids;

    public SelectId() {
        this.ids = new ArrayList<>();
    }
}

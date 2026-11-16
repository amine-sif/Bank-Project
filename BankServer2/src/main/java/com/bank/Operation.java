package com.bank;

import java.io.Serializable;
import java.util.Date;

public abstract class Operation implements Serializable {
    private OperationType type;
    private Date date;

    public Operation(OperationType type) {
        this.type = type;
        this.date = new Date(System.currentTimeMillis()); //To be checked
    }
    @Override
    public String toString() {
        return date + "\t" + type;
    }
}

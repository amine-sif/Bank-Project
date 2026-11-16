package com.bank;

import java.io.Serializable;

public enum OperationType implements Serializable {
    DEPOSIT,
    WITHDRAWAL,
    BILL,
    TRANSFER
}

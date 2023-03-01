package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    @NotBlank(message = "Transfer type must not be blank")
    private int transferTypeId;
    @NotBlank(message = "Transfer status must not be blank")
    private int transferStatusId;
    @NotBlank(message = "Account from must not be blank")
    @Min(message = "Account numbers begin at 2001", value = 2001)
    private int accountFrom;
    @NotBlank(message = "Account to must not be blank")
    @Min(message = "Account numbers begin at 2001", value = 2001)
    private int accountTo;
    @Positive
    private BigDecimal amount;

    private String userFrom;
    private String userTo;

    private String transferTypeDesc;
    private String transferStatusDesc;

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public Transfer(){}
    public Transfer(int transferId, int transferTypeId, int statusId, int accountFrom, int accountTo, BigDecimal amount){
        if(transferId != (Integer) null) {
            this.transferId = transferId;
        }
        this.transferStatusId = statusId;
        this.transferTypeId = transferTypeId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String toString(){
        return "Transfer ID: " + transferId + "\nTransfer Type: " + transferTypeId + " - " + transferTypeDesc +
                "\nTransfer Status: " + transferStatusId + " - " + transferStatusDesc + "\nAccount ID From: " + accountFrom
                + "\nAccount ID To: " + accountTo + "\nAmount to Transfer: " + amount.toString();
    }
}

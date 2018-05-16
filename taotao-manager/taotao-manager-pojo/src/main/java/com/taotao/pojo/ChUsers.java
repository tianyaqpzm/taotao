package com.taotao.pojo;

import java.util.Date;

public class ChUsers {
    private Integer id;

    private String username;

    private String signature;

    private String password;

    private String passwordstr;

    private String chainaddress;

    private Date createdat;

    private Date updatedat;

    private String transactionid;

    private String processor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPasswordstr() {
        return passwordstr;
    }

    public void setPasswordstr(String passwordstr) {
        this.passwordstr = passwordstr == null ? null : passwordstr.trim();
    }

    public String getChainaddress() {
        return chainaddress;
    }

    public void setChainaddress(String chainaddress) {
        this.chainaddress = chainaddress == null ? null : chainaddress.trim();
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid == null ? null : transactionid.trim();
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor == null ? null : processor.trim();
    }
}
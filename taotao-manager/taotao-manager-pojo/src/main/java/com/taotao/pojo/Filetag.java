package com.taotao.pojo;

public class Filetag {
    public Filetag(String name, String hash, byte[] value) {
        this.name = name;
        this.hash = hash;
        this.value = value;
    }

    private String name;

    private String hash;

    private byte[] value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
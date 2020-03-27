package com.github.tiniyield.sequences.benchmarks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiKey {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("registeredTo")
    @Expose
    private String registeredTo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRegisteredTo() {
        return registeredTo;
    }

    public void setRegisteredTo(String registeredTo) {
        this.registeredTo = registeredTo;
    }

    @Override
    public String toString() {
        return "ApiKey{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", secret='" + secret + '\'' +
                ", registeredTo='" + registeredTo + '\'' +
                '}';
    }
}

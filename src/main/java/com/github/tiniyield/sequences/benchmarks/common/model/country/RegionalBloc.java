package com.github.tiniyield.sequences.benchmarks.common.model.country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class RegionalBloc {

    @SerializedName("acronym")
    @Expose
    private String acronym;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("otherAcronyms")
    @Expose
    private List<Object> otherAcronyms = null;
    @SerializedName("otherNames")
    @Expose
    private List<Object> otherNames = null;

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getOtherAcronyms() {
        return otherAcronyms;
    }

    public void setOtherAcronyms(List<Object> otherAcronyms) {
        this.otherAcronyms = otherAcronyms;
    }

    public List<Object> getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(List<Object> otherNames) {
        this.otherNames = otherNames;
    }

    @Override
    public String toString() {
        return "RegionalBloc{" +
                "acronym='" + acronym + '\'' +
                ", name='" + name + '\'' +
                ", otherAcronyms=" + otherAcronyms +
                ", otherNames=" + otherNames +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionalBloc)) return false;
        RegionalBloc that = (RegionalBloc) o;
        return Objects.equals(acronym, that.acronym) &&
                Objects.equals(name, that.name) &&
                Objects.equals(otherAcronyms, that.otherAcronyms) &&
                Objects.equals(otherNames, that.otherNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acronym, name, otherAcronyms, otherNames);
    }
}

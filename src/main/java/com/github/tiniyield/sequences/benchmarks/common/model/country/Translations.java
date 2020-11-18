package com.github.tiniyield.sequences.benchmarks.common.model.country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Translations {

    @SerializedName("de")
    @Expose
    private String de;
    @SerializedName("es")
    @Expose
    private String es;
    @SerializedName("fr")
    @Expose
    private String fr;
    @SerializedName("ja")
    @Expose
    private String ja;
    @SerializedName("it")
    @Expose
    private String it;
    @SerializedName("br")
    @Expose
    private String br;
    @SerializedName("pt")
    @Expose
    private String pt;
    @SerializedName("nl")
    @Expose
    private String nl;
    @SerializedName("hr")
    @Expose
    private String hr;
    @SerializedName("fa")
    @Expose
    private String fa;

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getBr() {
        return br;
    }

    public void setBr(String br) {
        this.br = br;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    @Override
    public String toString() {
        return "Translations{" +
                "de='" + de + '\'' +
                ", es='" + es + '\'' +
                ", fr='" + fr + '\'' +
                ", ja='" + ja + '\'' +
                ", it='" + it + '\'' +
                ", br='" + br + '\'' +
                ", pt='" + pt + '\'' +
                ", nl='" + nl + '\'' +
                ", hr='" + hr + '\'' +
                ", fa='" + fa + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Translations)) return false;
        Translations that = (Translations) o;
        return Objects.equals(de, that.de) &&
                Objects.equals(es, that.es) &&
                Objects.equals(fr, that.fr) &&
                Objects.equals(ja, that.ja) &&
                Objects.equals(it, that.it) &&
                Objects.equals(br, that.br) &&
                Objects.equals(pt, that.pt) &&
                Objects.equals(nl, that.nl) &&
                Objects.equals(hr, that.hr) &&
                Objects.equals(fa, that.fa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(de, es, fr, ja, it, br, pt, nl, hr, fa);
    }
}

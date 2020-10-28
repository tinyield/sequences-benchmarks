
package com.github.tiniyield.sequences.benchmarks.operations.model.country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Country {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("topLevelDomain")
    @Expose
    private List<String> topLevelDomain = null;
    @SerializedName("alpha2Code")
    @Expose
    private String alpha2Code;
    @SerializedName("alpha3Code")
    @Expose
    private String alpha3Code;
    @SerializedName("callingCodes")
    @Expose
    private List<String> callingCodes = null;
    @SerializedName("capital")
    @Expose
    private String capital;
    @SerializedName("altSpellings")
    @Expose
    private List<String> altSpellings = null;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("subregion")
    @Expose
    private String subregion;
    @SerializedName("population")
    @Expose
    private Integer population;
    @SerializedName("latlng")
    @Expose
    private List<Double> latlng = null;
    @SerializedName("demonym")
    @Expose
    private String demonym;
    @SerializedName("area")
    @Expose
    private Double area;
    @SerializedName("gini")
    @Expose
    private Double gini;
    @SerializedName("timezones")
    @Expose
    private List<String> timezones = null;
    @SerializedName("borders")
    @Expose
    private List<String> borders = null;
    @SerializedName("nativeName")
    @Expose
    private String nativeName;
    @SerializedName("numericCode")
    @Expose
    private String numericCode;
    @SerializedName("currencies")
    @Expose
    private List<Currency> currencies = null;
    @SerializedName("languages")
    @Expose
    private List<Language> languages = null;
    @SerializedName("translations")
    @Expose
    private Translations translations;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("regionalBlocs")
    @Expose
    private List<RegionalBloc> regionalBlocs = null;
    @SerializedName("cioc")
    @Expose
    private String cioc;

    public String getName() {
        return name;
    }

    public Country setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getTopLevelDomain() {
        return topLevelDomain;
    }

    public Country setTopLevelDomain(List<String> topLevelDomain) {
        this.topLevelDomain = topLevelDomain;
        return this;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public Country setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
        return this;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public Country setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
        return this;
    }

    public List<String> getCallingCodes() {
        return callingCodes;
    }

    public Country setCallingCodes(List<String> callingCodes) {
        this.callingCodes = callingCodes;
        return this;
    }

    public String getCapital() {
        return capital;
    }

    public Country setCapital(String capital) {
        this.capital = capital;
        return this;
    }

    public List<String> getAltSpellings() {
        return altSpellings;
    }

    public Country setAltSpellings(List<String> altSpellings) {
        this.altSpellings = altSpellings;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Country setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getSubregion() {
        return subregion;
    }

    public Country setSubregion(String subregion) {
        this.subregion = subregion;
        return this;
    }

    public Integer getPopulation() {
        return population;
    }

    public Country setPopulation(Integer population) {
        this.population = population;
        return this;
    }

    public List<Double> getLatlng() {
        return latlng;
    }

    public Country setLatlng(List<Double> latlng) {
        this.latlng = latlng;
        return this;
    }

    public String getDemonym() {
        return demonym;
    }

    public Country setDemonym(String demonym) {
        this.demonym = demonym;
        return this;
    }

    public Double getArea() {
        return area;
    }

    public Country setArea(Double area) {
        this.area = area;
        return this;
    }

    public Double getGini() {
        return gini;
    }

    public Country setGini(Double gini) {
        this.gini = gini;
        return this;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public Country setTimezones(List<String> timezones) {
        this.timezones = timezones;
        return this;
    }

    public List<String> getBorders() {
        return borders;
    }

    public Country setBorders(List<String> borders) {
        this.borders = borders;
        return this;
    }

    public String getNativeName() {
        return nativeName;
    }

    public Country setNativeName(String nativeName) {
        this.nativeName = nativeName;
        return this;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public Country setNumericCode(String numericCode) {
        this.numericCode = numericCode;
        return this;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public Country setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
        return this;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Country setLanguages(List<Language> languages) {
        this.languages = languages;
        return this;
    }

    public Translations getTranslations() {
        return translations;
    }

    public Country setTranslations(Translations translations) {
        this.translations = translations;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public Country setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public List<RegionalBloc> getRegionalBlocs() {
        return regionalBlocs;
    }

    public Country setRegionalBlocs(List<RegionalBloc> regionalBlocs) {
        this.regionalBlocs = regionalBlocs;
        return this;
    }

    public String getCioc() {
        return cioc;
    }

    public Country setCioc(String cioc) {
        this.cioc = cioc;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Country country = (Country) o;
        return Objects.equals(name, country.name) &&
                Objects.equals(topLevelDomain, country.topLevelDomain) &&
                Objects.equals(alpha2Code, country.alpha2Code) &&
                Objects.equals(alpha3Code, country.alpha3Code) &&
                Objects.equals(callingCodes, country.callingCodes) &&
                Objects.equals(capital, country.capital) &&
                Objects.equals(altSpellings, country.altSpellings) &&
                Objects.equals(region, country.region) &&
                Objects.equals(subregion, country.subregion) &&
                Objects.equals(population, country.population) &&
                Objects.equals(latlng, country.latlng) &&
                Objects.equals(demonym, country.demonym) &&
                Objects.equals(area, country.area) &&
                Objects.equals(gini, country.gini) &&
                Objects.equals(timezones, country.timezones) &&
                Objects.equals(borders, country.borders) &&
                Objects.equals(nativeName, country.nativeName) &&
                Objects.equals(numericCode, country.numericCode) &&
                Objects.equals(currencies, country.currencies) &&
                Objects.equals(languages, country.languages) &&
                Objects.equals(translations, country.translations) &&
                Objects.equals(flag, country.flag) &&
                Objects.equals(regionalBlocs, country.regionalBlocs) &&
                Objects.equals(cioc, country.cioc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,
                            topLevelDomain,
                            alpha2Code,
                            alpha3Code,
                            callingCodes,
                            capital,
                            altSpellings,
                            region,
                            subregion,
                            population,
                            latlng,
                            demonym,
                            area,
                            gini,
                            timezones,
                            borders,
                            nativeName,
                            numericCode,
                            currencies,
                            languages,
                            translations,
                            flag,
                            regionalBlocs,
                            cioc);
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", topLevelDomain=" + topLevelDomain +
                ", alpha2Code='" + alpha2Code + '\'' +
                ", alpha3Code='" + alpha3Code + '\'' +
                ", callingCodes=" + callingCodes +
                ", capital='" + capital + '\'' +
                ", altSpellings=" + altSpellings +
                ", region='" + region + '\'' +
                ", subregion='" + subregion + '\'' +
                ", population=" + population +
                ", latlng=" + latlng +
                ", demonym='" + demonym + '\'' +
                ", area=" + area +
                ", gini=" + gini +
                ", timezones=" + timezones +
                ", borders=" + borders +
                ", nativeName='" + nativeName + '\'' +
                ", numericCode='" + numericCode + '\'' +
                ", currencies=" + currencies +
                ", languages=" + languages +
                ", translations=" + translations +
                ", flag='" + flag + '\'' +
                ", regionalBlocs=" + regionalBlocs +
                ", cioc='" + cioc + '\'' +
                '}';
    }
}

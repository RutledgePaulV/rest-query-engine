package com.github.rutledgepaulv.rqe.testsupport;

public class Address {

    private String street;
    private String city;
    private State state;
    private int unit;
    private boolean hasCat;

    public int getUnit() {
        return unit;
    }

    public Address setUnit(int unit) {
        this.unit = unit;
        return this;
    }

    public State getState() {
        return state;
    }

    public Address setState(State state) {
        this.state = state;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public boolean isHasCat() {
        return hasCat;
    }

    public Address setHasCat(boolean hasCat) {
        this.hasCat = hasCat;
        return this;
    }


}

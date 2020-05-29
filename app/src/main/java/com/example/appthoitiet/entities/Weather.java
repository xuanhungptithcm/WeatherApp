package com.example.appthoitiet.entities;

public class Weather {
    private String id;
    private String day;
    private String time;
    private String nhietDoThapNhat;
    private String nhietDoCaoNhat;
    private String nhietDoTrungBinh;
    private String color;
    private String image;
    private String humidity;

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Weather() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day, String type) {
        if(type.equals("INIT")) {
            switch (day) {
                case "1": {
                    this.day = "MONDAY";
                    break;
                }
                case "2": {
                    this.day = "TUESDAY";
                    break;
                }
                case "3": {
                    this.day = "WEDNESDAY";
                    break;
                }
                case "4": {
                    this.day = "THURSDAY";
                    break;
                }
                case "5": {
                    this.day = "FRIDAY";
                    break;
                }
                case "6": {
                    this.day = "SATURDAY";
                    break;
                }
                case "7": {
                    this.day = "SUNDAY";
                    break;
                }
                default: {
                    this.day = "MONDAY";
                }
            }
        } else {
            this.day = day;
        }


    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNhietDoThapNhat() {
        return nhietDoThapNhat;
    }

    public void setNhietDoThapNhat(String nhietDoThapNhat) {
        this.nhietDoThapNhat = nhietDoThapNhat;
    }

    public String getNhietDoCaoNhat() {
        return nhietDoCaoNhat;
    }

    public void setNhietDoCaoNhat(String nhietDoCaoNhat) {
        this.nhietDoCaoNhat = nhietDoCaoNhat;
    }

    public String getNhietDoTrungBinh() {
        return nhietDoTrungBinh;
    }

    public void setNhietDoTrungBinh(String nhietDoTrungBinh) {
        this.nhietDoTrungBinh = nhietDoTrungBinh;
    }
}

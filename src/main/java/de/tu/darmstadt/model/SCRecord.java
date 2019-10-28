package de.tu.darmstadt.model;

public class SCRecord {
    private String time_Stampe;
    private double time;
    private String os_sys;
    private String sc_name;
    private String cpu_id;
    private String params;

    public String getTime_Stampe() {
        return time_Stampe;
    }

    public void setTime_Stampe(String time_Stampe) {
        this.time_Stampe = time_Stampe;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getOs_sys() {
        return os_sys;
    }

    public void setOs_sys(String os_sys) {
        this.os_sys = os_sys;
    }

    public String getSc_name() {
        return sc_name;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public String getCpu_id() {
        return cpu_id;
    }

    public void setCpu_id(String cpu_id) {
        this.cpu_id = cpu_id;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "SCRecord{" +
                "time_Stampe='" + time_Stampe + '\'' +
                ", time=" + time +
                ", os_sys='" + os_sys + '\'' +
                ", sc_name='" + sc_name + '\'' +
                ", cpu_id='" + cpu_id + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}

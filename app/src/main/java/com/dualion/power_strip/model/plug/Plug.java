package com.dualion.power_strip.model.plug;

import com.google.gson.annotations.Expose;
 import com.google.gson.annotations.SerializedName;

public class Plug {

     @Expose
     private String hour;

     @Expose
     private String id;

     @SerializedName("pin_id")
     @Expose
     private String pinId;

     @SerializedName("pin_state")
     @Expose
     private String pinState;

     @SerializedName("pin_name")
     @Expose
     private String component;

     @SerializedName("repeat_frecuency")
     @Expose
     private Frequency repeatFrecuency;

     @SerializedName("repeat_on_days")
     @Expose
     private DaysOfWeek repeatOnDays;

     @SerializedName("scheduler_state")
     @Expose
     private State schedulerState;

     @SerializedName("start_at")
     @Expose
     private String start;

     @SerializedName("stop_at")
     @Expose
     private String stop;

     Plug() {
     }

     public String getHour() {
         return hour;
     }

     public void setHour(String hour) {
         this.hour = hour;
     }

     public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }

     public String getPinId() {
         return pinId;
     }

     public void setPinId(String pinId) {
         this.pinId = pinId;
     }

     public String getPinState() {
         return pinState;
     }

     public void setPinState(String pinState) {
         this.pinState = pinState;
     }

     public String getComponent() {
         return component;
     }

     public void setComponent(String component) {
         this.component = component;
     }

     public Frequency getRepeatFrecuency() {
         return repeatFrecuency;
     }

     public void setRepeatFrecuency(Frequency repeatFrecuency) {
         this.repeatFrecuency = repeatFrecuency;
     }

     public DaysOfWeek getRepeatOnDays() {
         return repeatOnDays;
     }

     public void setRepeatOnDays(DaysOfWeek repeatOnDays) {
         this.repeatOnDays = repeatOnDays;
     }

    public State getSchedulerState() {
        return schedulerState;
    }

    public void setSchedulerState(State schedulerState) {
        this.schedulerState = schedulerState;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public enum State {

         @SerializedName("Activo")
         ACTIVO(0),

         @SerializedName("Parado")
         PARADO(1);

         private final int value;

         public int getValue() {
             return value;
         }

         private State(int value) {
             this.value = value;
         }


    }

     public enum Frequency {

         @SerializedName("Diario")
         DIARIO(0),

         @SerializedName("Semanal")
         SEMANAL(1),

         @SerializedName("")
         NONE(2);

         private final int value;

         public int getValue() {
             return value;
         }

         private Frequency(int value) {
             this.value = value;
         }
     }

     public class DaysOfWeek {

         @Expose
         private String monday;

         @Expose
         private String tuesday;

         @Expose
         private String wednesday;

         @Expose
         private String thursday;

         @Expose
         private String friday;

         @Expose
         private String saturday;

         @Expose
         private String sunday;

         public DaysOfWeek() {
         }

         public String getMonday() {
             return monday;
         }

         public void setMonday(String monday) {
             this.monday = monday;
         }

         public String getTuesday() {
             return tuesday;
         }

         public void setTuesday(String tuesday) {
             this.tuesday = tuesday;
         }

         public String getWednesday() {
             return wednesday;
         }

         public void setWednesday(String wednesday) {
             this.wednesday = wednesday;
         }

         public String getThursday() {
             return thursday;
         }

         public void setThursday(String thursday) {
             this.thursday = thursday;
         }

         public String getFriday() {
             return friday;
         }

         public void setFriday(String friday) {
             this.friday = friday;
         }

         public String getSaturday() {
             return saturday;
         }

         public void setSaturday(String saturday) {
             this.saturday = saturday;
         }

         public String getSunday() {
             return sunday;
         }

         public void setSunday(String sunday) {
             this.sunday = sunday;
         }
     }
 }
package com.dualion.power_strip.model.scheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchedulerSemanal {

	@SerializedName("start_at")
	@Expose
	private String start;

	@SerializedName("stop_at")
	@Expose
	private String stop;

	@Expose
	private String repeat = "Semanal";

	@Expose
	private DaysOfWeek repeatOnDays;

	public SchedulerSemanal() {
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

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public DaysOfWeek getRepeatOnDays() {
		return repeatOnDays;
	}

	public void setRepeatOnDays(DaysOfWeek repeatOnDays) {
		this.repeatOnDays = repeatOnDays;
	}

	public class DaysOfWeek {

		@Expose
		private boolean monday;

		@Expose
		private boolean tuesday;

		@Expose
		private boolean wednesday;

		@Expose
		private boolean thursday;

		@Expose
		private boolean friday;

		@Expose
		private boolean saturday;

		@Expose
		private boolean sunday;

		public DaysOfWeek() {
		}

		public boolean getMonday() {
			return monday;
		}

		public void setMonday(boolean monday) {
			this.monday = monday;
		}

		public boolean getTuesday() {
			return tuesday;
		}

		public void setTuesday(boolean tuesday) {
			this.tuesday = tuesday;
		}

		public boolean getWednesday() {
			return wednesday;
		}

		public void setWednesday(boolean wednesday) {
			this.wednesday = wednesday;
		}

		public boolean getThursday() {
			return thursday;
		}

		public void setThursday(boolean thursday) {
			this.thursday = thursday;
		}

		public boolean getFriday() {
			return friday;
		}

		public void setFriday(boolean friday) {
			this.friday = friday;
		}

		public boolean getSaturday() {
			return saturday;
		}

		public void setSaturday(boolean saturday) {
			this.saturday = saturday;
		}

		public boolean getSunday() {
			return sunday;
		}

		public void setSunday(boolean sunday) {
			this.sunday = sunday;
		}
	}

}

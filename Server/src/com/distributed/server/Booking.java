	package com.distributed.server;
	
	public class Booking {
	
		private static int idCounter = 0; 
		private int startSlot;
		private int endSlot;
		private int ID;
	
		public Booking(int fId, int day, int start, int end){
			this.startSlot = start;
			this.endSlot = end;
			String join = "" + fId + day + idCounter;
			this.ID = Integer.parseInt(join);
			idCounter++;
		}
	
		public void setStartSlot(int startSlot) {
			this.startSlot = startSlot;
		}
	
		public void setEndSlot(int endSlot) {
			this.endSlot = endSlot;
		}
		
		public int getStartSlot() {
			return this.startSlot;
		}
		
		public int getEndSlot() {
			return this.endSlot;
		}
	
		public int getID() {
			return ID;
		}
	
		public boolean conflict(int start, int end) {
			if (start >= this.startSlot && end <= this.endSlot){
				return true;
			} else if (start < this.startSlot && end > this.startSlot){
				return true;
			} else if (start < this.endSlot && end >= this.endSlot){
				return true;
			} else {
				return false;
			}
		}
	
	}
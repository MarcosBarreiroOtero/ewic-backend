package es.ewic.backend.service.shopService;

import es.ewic.backend.model.entry.Entry;
import es.ewic.backend.modelutil.DateUtils;

public class EntryDetails {

	private int entryNumber;
	private String start;
	private String end;
	private long duration;
	private String description;
	private String shopName;
	private String clientName;

	public EntryDetails(Entry entry) {
		this.entryNumber = entry.getIdEntry();
		this.start = DateUtils.formatDateLong(entry.getStart());
		if (entry.getEnd() != null) {
			this.end = DateUtils.formatDateLong(entry.getEnd());
		} else {
			this.end = null;
		}
		this.duration = entry.getDuration();
		this.description = entry.getDescription();
		this.shopName = entry.getShop().getName();
		if (entry.getClient() != null) {
			this.clientName = entry.getClient().completeName();
		} else {
			this.clientName = "";
		}
	}

	public int getEntryNumber() {
		return entryNumber;
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public long getDuration() {
		return duration;
	}

	public String getDescription() {
		return description;
	}

	public String getShopName() {
		return shopName;
	}

	public String getClientName() {
		return clientName;
	}

}

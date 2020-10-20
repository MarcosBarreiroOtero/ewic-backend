package es.ewic.backend.service.shopService;

import es.ewic.backend.model.entry.Entry;
import es.ewic.backend.modelutil.DateUtils;

public class EntryDetails {

	private String start;
	private String end;
	private long duration;
	private String shopName;
	private String clientName;

	public EntryDetails(Entry entry) {
		this.start = DateUtils.formatDateLong(entry.getStart());
		this.end = DateUtils.formatDateLong(entry.getEnd());
		this.duration = entry.getDuration();
		this.shopName = entry.getShop().getName();
		if (entry.getClient() != null) {
			this.clientName = entry.getClient().completeName();
		} else {
			this.clientName = "";
		}
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

	public String getShopName() {
		return shopName;
	}

	public String getClientName() {
		return clientName;
	}

}

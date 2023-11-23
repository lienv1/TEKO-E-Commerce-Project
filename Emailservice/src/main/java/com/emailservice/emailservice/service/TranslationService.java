package com.emailservice.emailservice.service;

import org.springframework.stereotype.Service;

@Service
public class TranslationService {

	private Language language;

	private String thanks = "";
	private String confirmation = "";
	private String company = "";
	private String name = "";
	private String street = "";
	private String state = "";
	private String zip = "";
	private String country = "";
	private String email = "";
	private String phone = "";
	private String deliveryDate = "";
	private String comment = "";
	private String quantity = "";
	private String greeting = "";

	public TranslationService() {

	}

	public enum Language {
		EN, DE, FR, VI, ZH
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}

	public String getThanks() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getConfirmation() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getCompany() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getName() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getStreet() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getState() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getZip() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getCountry() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getEmail() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getPhone() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getDeliveryDate() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getComment() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getQuantity() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

	public String getGreeting() {
		switch (language) {
		case DE:
			return "";
		case FR:
			return "";
		case VI:
			return "";
		case ZH:
			return "";
		default:
			return "";
		}
	}

}

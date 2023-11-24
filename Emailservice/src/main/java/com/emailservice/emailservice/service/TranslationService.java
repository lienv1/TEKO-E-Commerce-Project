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
		EN, DE, FR, VN, ZH
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	
	public void setLanguage(String language) {
		switch (language) {
			case "DE": setLanguage(Language.DE); break;
			case "FR": setLanguage(Language.FR); break;
			case "VN": setLanguage(Language.VN); break;
			case "ZH": setLanguage(Language.ZH); break;
			default: setLanguage(Language.EN); break; 
		}
		
	}

	public Language getLanguage() {
		return language;
	}

	public String getThanks() {
		switch (language) {
		case DE:
			return "Vielen Dank für Ihre Bestellung!";
		case FR:
			return "Merci pour votre commande !";
		case VN:
			return "Cảm ơn bạn đã đặt hàng!";
		case ZH:
			return "感谢您的订单！";
		default:
			return "Thank you for your order!";
		}
	}

	public String getConfirmation() {
		switch (language) {
		case DE:
			return "Bestellbestätigung";
		case FR:
			return "Confirmation de commande";
		case VN:
			return "Xác nhận đơn hàng";
		case ZH:
			return "订单确认";
		default:
			return "Order confirmation";
		}
	}

	public String getCompany() {
		switch (language) {
		case DE:
			return "Firma";
		case FR:
			return "Entreprise";
		case VN:
			return "Công ty";
		case ZH:
			return "公司";
		default:
			return "Company";
		}
	}

	public String getName() {
		switch (language) {
		case DE:
			return "Name";
		case FR:
			return "Nom";
		case VN:
			return "Họ tên đầy đủ";
		case ZH:
			return "全名";
		default:
			return "Name";
		}
	}

	public String getStreet() {
		switch (language) {
		case DE:
			return "Strasse";
		case FR:
			return "Rue";
		case VN:
			return "Đường";
		case ZH:
			return "街道";
		default:
			return "Street";
		}
	}

	public String getState() {
		switch (language) {
		case DE:
			return "Ort";
		case FR:
			return "État";
		case VN:
			return "Tiểu bang";
		case ZH:
			return "州";
		default:
			return "State";
		}
	}

	public String getZip() {
		switch (language) {
		case DE:
			return "Postleitzahl";
		case FR:
			return "Code Postal";
		case VN:
			return "Mã bưu điện";
		case ZH:
			return "邮政编码";
		default:
			return "ZIP";
		}
	}

	public String getCountry() {
		switch (language) {
		case DE:
			return "Land";
		case FR:
			return "Pays";
		case VN:
			return "Quốc gia";
		case ZH:
			return "国家";
		default:
			return "Country";
		}
	}

	public String getEmail() {
		switch (language) {
		case DE:
			return "E-Mail";
		case FR:
			return "E-Mail";
		case VN:
			return "E-Mail";
		case ZH:
			return "电子邮件地址";
		default:
			return "E-Mail";
		}
	}

	public String getPhone() {
		switch (language) {
		case DE:
			return "Telefon";
		case FR:
			return "Téléphone";
		case VN:
			return "Điện thoại";
		case ZH:
			return "电话";
		default:
			return "Phone";
		}
	}

	public String getDeliveryDate() {
		switch (language) {
		case DE:
			return "Lieferdatum";
		case FR:
			return "Date de Livraison";
		case VN:
			return "Ngày giao hàng";
		case ZH:
			return "送货日期";
		default:
			return "Delivery Date";
		}
	}

	public String getComment() {
		switch (language) {
		case DE:
			return "Kommentar";
		case FR:
			return "Commentaire";
		case VN:
			return "Bình luận";
		case ZH:
			return "备注";
		default:
			return "Comment";
		}
	}

	public String getQuantity() {
		switch (language) {
		case DE:
			return "Menge";
		case FR:
			return "Quantité";
		case VN:
			return "Số lượng";
		case ZH:
			return "数量";
		default:
			return "Quantity";
		}
	}

	public String getGreeting() {
		switch (language) {
		case DE:
			return "Freundliche Grüsse";
		case FR:
			return "Cordialement";
		case VN:
			return "Trân trọng";
		case ZH:
			return "致以亲切的问候";
		default:
			return "Kind regards";
		}
	}

}

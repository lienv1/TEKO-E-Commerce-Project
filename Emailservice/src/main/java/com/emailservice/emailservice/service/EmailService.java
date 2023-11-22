package com.emailservice.emailservice.service;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.emailservice.emailservice.environment.CompanyProperties;
import com.emailservice.emailservice.model.Order;
import com.emailservice.emailservice.model.OrderDetail;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TranslationService translator;
	
	@Autowired
	private CompanyProperties companyProperties;

	public EmailService(JavaMailSender mailSender, TranslationService translator, CompanyProperties companyProperties) {
		super();
		this.mailSender = mailSender;
		this.translator = translator;
		this.companyProperties = companyProperties;
	}




	public boolean sendConfirmation(Order order) {
		String text = "";	
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getUser().getEmail());
        message.setSubject("Confirmation mail");
        message.setText(text);
        mailSender.send(message);
		
        try {

			// Message part
			BodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "<H1>Vielen Dank für Ihre Bestellung!</H1><br/>";
			htmlText += "Hiermit bestätigen wir Ihre Bestellung für folgende Artikel: <br> <br>";
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				htmlText += orderDetail.getProduct().getProductId() + " | " + orderDetail.getProduct().getProductName() + " | "+translator.getQuantity()+": "
						+ orderDetail.getQuantity() + " <br>";
			}

			htmlText += "<br> Firma: " + user.getCompany() + "<br> Name: " + user.getLastname() + "<br> Vorname:"
					+ user.getFirstname() + "<br> Strasse: " + user.getStreet() + "<br> Ort: " + user.getPostalCode()
					+ "<br> Tel.:" + user.getPhone() + "<br> Email: " + user.getEmail() + "<br> Kommentar: "
					+ order.getComment() + "<br> Lieferdatum: " + order.getDate().toString().split(" ")[0];

			htmlText += "<br><br> <hr> <br>";

			htmlText += "<div>Freundliche Grüsse</div> <br>";

			htmlText += "<table class=\"MsoNormalTable\" style=\"width:306.9pt\" width=\"409\"><tbody><tr style=\"\"><td rowspan=\"2\" style=\"width:127.6pt; border:none; border-right:solid #D00909 1.0pt; padding:0cm 7.5pt 0cm 0cm\" width=\"170\" valign=\"top\"><p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif; margin-bottom:0cm; text-align:center\" align=\"center\"><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#595959\">&nbsp;</span></p><p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif; margin-bottom:0cm; text-align:center\" align=\"center\"><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#595959\"><span style=\"\">&nbsp;</span><span style=\"\"></span><br><br></span><b><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif\">"+companyProperties.getStreet()+"&nbsp;</span></b></p>\r\n"
					+ "\r\n"
					+ "<p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif; margin-bottom:0cm; text-align:center\" align=\"center\"><b><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif\">"+companyProperties.getZip()+" "+companyProperties.getState() +"&nbsp;</span></b></p>\r\n"
					+ "\r\n" + "<p align=left><img src=\"cid:image\"> </p>" + "\r\n"
					+ "<p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif; margin-bottom:0cm; text-align:center\" align=\"center\"><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif\">&nbsp;</span></p></td><td style=\"width:179.3pt; padding:0cm 0cm 0cm 0cm\" width=\"239\"><table class=\"MsoNormalTable\" style=\"box-sizing:border-box; width:226.983px\"><tbody><tr style=\"\"><td style=\"width:222.983px; padding:0cm 0cm 3.75pt 7.5pt; box-sizing:border-box; height:57.5833px\" valign=\"top\"><p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; line-height:107%; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif\"><b><span style=\"font-size:14.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#D00909\" lang=\"EN-US\"> </span></b><b><span style=\"font-size:14.0pt; font-family:&quot;Microsoft JhengHei&quot;,sans-serif; color:#D00909\" lang=\"ZH-CN\"></span></b><b><span style=\"font-size:14.0pt; font-family:&quot;Microsoft JhengHei&quot;,sans-serif; color:#D00909\" lang=\"EN-US\">&nbsp;</span></b></p><p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; line-height:107%; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif\"><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#545454\" lang=\"EN-US\">"+companyProperties.getCompany()+"</span><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#0079AC\" lang=\"EN-US\"> &nbsp;</span></p></td></tr><tr style=\"\"><td style=\"padding:3.75pt 0cm 3.75pt 7.5pt; box-sizing:border-box; width:222.983px; height:58px\" valign=\"top\"><p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif; margin-bottom:0cm\"><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#D00909\" lang=\"EN-US\">Mobile<b>:</b></span><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#545454\" lang=\"EN-US\"> <span style=\"\">&nbsp;&nbsp; </span>"+companyProperties.getMobile()+"<br></span><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#D00909\" lang=\"EN-US\">Phone<b>:</b></span><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#545454\" lang=\"EN-US\"> <span style=\"\">&nbsp;&nbsp; </span>"+companyProperties.getPhone()+"<br></span><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#D00909\" lang=\"EN-US\">E<b>-</b>Mail<b>:<span style=\"\">&nbsp;&nbsp;&nbsp; </span></b></span><a href=\"mailto:"+companyProperties.getEmail()+"\"><span style=\"\" lang=\"EN-US\">"+companyProperties.getEmail()+"</span></a><span style=\"\" lang=\"EN-US\"> <span style=\"\">&nbsp;&nbsp;&nbsp;</span></span><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#444444\" lang=\"EN-US\">&nbsp;</span></p></td></tr><tr style=\"\"><td style=\"padding:3.75pt 0cm 3.75pt 7.5pt; box-sizing:border-box; width:222.983px; height:3px\" valign=\"top\"><p class=\"MsoNormal\" style=\"margin-top: 0px; margin-bottom: 0px;margin:0cm 0cm 8pt; font-size:11pt; font-family:&quot;Calibri&quot;,sans-serif; margin-bottom:0cm\">Website: <a href=\""+companyProperties.getWeb()+"\">"+companyProperties.getWeb()+"</a> <span class=\"MsoHyperlink\" style=\"color:rgb(5,99,193); text-decoration:underline\"><span style=\"\"><span style=\"\">&nbsp;</span></span></span><span style=\"\"><span style=\"\">&nbsp;</span></span><span style=\"font-size:10.0pt; font-family:&quot;Arial&quot;,sans-serif; color:#0079AC\" lang=\"EN-US\">&nbsp;</span></p></td></tr></tbody></table></td></tr><tr style=\"\"><td style=\"width:179.3pt; padding:0cm 0cm 0cm 0cm\" width=\"239\"><br></td></tr></tbody></table>";

			messageBodyPart.setContent(htmlText, "text/html;charset=utf-8");

			// Image section
			MimeBodyPart imagePart = new MimeBodyPart();
			ClassLoader classLoader = EmailService.class.getClassLoader();
			InputStream imageStream = classLoader.getResourceAsStream("signatur.png");
			byte[] imageBytes = imageStream.readAllBytes(); // Read the image into a byte array
			ByteArrayDataSource dataSource = new ByteArrayDataSource(imageBytes, "image/png");
			imagePart.setDataHandler(new jakarta.activation.DataHandler(dataSource));
			imagePart.setHeader("Content-ID", "<image>");

			// Put all body parts to multipart
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(imagePart);

			Properties properties = System.getProperties();

			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", port); // 465 require ssl for more security
			properties.put("mail.smtp.ssl.enable", "true"); // ssl

			Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, password);
				}
			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("Bestellbestätigung " + order.getOrderId());
			message.setContent(multipart, "UTF-8");
			// message.setText("success"); only text!
			Transport.send(message);

			// Copy for order
			if (testmode) {
				return true;
			}
			MimeMessage message2 = new MimeMessage(session);
			message2.setFrom(new InternetAddress(from));
			message2.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
			message2.setSubject(user.getCompany() + " - Bestellbestätigung " + order.getOrderId());
			message2.setContent(multipart, "UTF-8");
			Transport.send(message2);

			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        
		return false;
	}
}

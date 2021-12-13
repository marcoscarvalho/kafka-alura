package br.com.alura.ecommerce;

public class Email {

	private final String subject, email, body;

	public Email(String subject, String email, String body) {
		super();
		this.email = email;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public String toString() {
		return "Email [subject=" + subject + ", email=" + email + ", body=" + body + "]";
	}

}

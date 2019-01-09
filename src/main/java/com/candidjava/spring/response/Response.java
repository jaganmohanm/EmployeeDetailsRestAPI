package com.candidjava.spring.response;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class Response {
	String Status;
	String message;

	public String getStatus() {
		return Status;
	}

	public void setStatus(String i) {
		Status = i;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Response [Status=" + Status + ", message=" + message + "]";
	}
}

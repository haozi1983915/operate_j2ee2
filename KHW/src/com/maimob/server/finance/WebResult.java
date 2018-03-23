package com.maimob.server.finance;

public class WebResult {
//	code	整数	1-表示成功，其他均为失败，非1时请邮件或其他方式通知到人，查看具体的出错原因
//	msg	字符串	说明信息，code非1情况下是失败原因
//	customer_id
	private String code;
	private String msg;
	private String customer_id;
	private String line_id;
	private String supplier_id;
	private String invoice_title_id;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getLine_id() {
		return line_id;
	}
	public void setLine_id(String line_id) {
		this.line_id = line_id;
	}
	public String getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getInvoice_title_id() {
		return invoice_title_id;
	}
	public void setInvoice_title_id(String invoice_title_id) {
		this.invoice_title_id = invoice_title_id;
	}

}

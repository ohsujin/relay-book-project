package data.send;

import android.widget.EditText;

public class Register {
	EditText School;
	EditText Phone_number;
	EditText passwd;
	
	
	public EditText getSchool() {
		return School;
	}
	public void setSchool(EditText etMessage) {
		School = etMessage;
	}
	public EditText getPhone_number() {
		return Phone_number;
	}
	public void setPhone_number(EditText phone_number) {
		Phone_number = phone_number;
	}
	public EditText getPasswd() {
		return passwd;
	}
	public void setPasswd(EditText passwd) {
		this.passwd = passwd;
	}
	
	
	
}

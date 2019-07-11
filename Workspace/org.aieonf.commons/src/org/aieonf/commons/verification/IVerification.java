package org.aieonf.commons.verification;

import java.util.regex.Pattern;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;

public interface IVerification {

	public String REGEX_AMOUNT = "[0-9]+([,.][0-9]{1,2})?"; 
	public String REGEX_ALPHABET = "^[\\p{IsAlphabetic}\\s]+"; 
	public String REGEX_ALPHABET_ASCII = "^[a-zA-Z\\s]+"; 
	public String REGEX_NAME = "^[\\p{IsAlphabetic}\\s\\-]+"; 
	public String REGEX_NUMBER = "\\d+"; 
	public String REGEX_HOUSE_NUMBER = "([0-9]){1,}([A-Za-z]){0,15}"; //one or more 0-9 and 
	public String REGEX_POSTCODE_NL = "^[1-9][0-9]{3}\\s*[a-zA-Z]{2}$";
	public String REGEX_ENDSWITH_NUMBER = "^.*\\d$";
	public String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public String        REGEX_PHONE = "(^\\+[0-9]{2}|^\\+[0-9]{2}\\(0\\)|^\\(\\+[0-9]{2}\\)\\(0\\)|^00[0-9]{2}|^0)([0-9]{9}$|[0-9\\-\\s]{10}$)";
	public String REGEX_MOBILE_PHONE = "(^\\+[0-9]{2}|^\\+[0-9]{2}\\(0\\)|^\\(\\+[0-9]{2}\\)\\(0\\)|^00[0-9]{2}|^0)(6)([0-9]{8}$|[0-9\\-\\s]{9}$)";
	
	public String VERIFICATION_TYPE = "VerificationType"; 

	public enum VerificationTypes{
		MONEY,
		ALPHABET,
		ALPHABET_ASCII,
		ADDRESS,
		CUSTOM,
		EMAIL,
		HOUSE_NUMBER,
		NUMBERS,
		NAME,
		TELEPHONE,
		TELEPHIONE_NO_MOBILE,
		MOBILE_PHONE,
		POSTCODE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		/**
		 * Verify te given text against the type
		 * @param text
		 * @return
		 */
		public boolean verify( String text ){
			if( Utils.assertNull(text))
				return false;
			VerificationTypes type = valueOf( name());
			Pattern pattern = null;
			String regex = REGEX_ALPHABET;
			switch( type ){
			case ALPHABET:
				regex = REGEX_ALPHABET ;
				break;
			case MONEY:
				regex = REGEX_AMOUNT;
				break;
			case EMAIL:
				regex = REGEX_EMAIL;
				break;
			case NAME:
				regex = REGEX_NAME ;
				break;
			case NUMBERS:
				regex = REGEX_NUMBER;
				break;
			case ADDRESS:
				if( text.matches(REGEX_ALPHABET)){
					return true;
				}
				
				boolean retval = false;
				String[] split = text.split("\\s");
				String last = split[split.length - 1];
				retval = last.matches( REGEX_HOUSE_NUMBER);
				if( retval && Character.isDigit( last.charAt(0))){
					String street = text.substring(0, text.length() - last.length());
					retval = street .matches(REGEX_ALPHABET);
				}
				return retval;
			case HOUSE_NUMBER:
				regex = REGEX_HOUSE_NUMBER;
				break;
			case POSTCODE:
				regex = REGEX_POSTCODE_NL;
				break;
			case TELEPHONE:
				regex = REGEX_PHONE;
				break;
			case TELEPHIONE_NO_MOBILE:
				pattern = Pattern.compile( REGEX_MOBILE_PHONE );
				if( pattern.matcher(text).matches())
					return false;
				regex = REGEX_PHONE;
				break;
			case MOBILE_PHONE:
				regex = REGEX_MOBILE_PHONE;
				break;
			default:
				break;
			}
			pattern = Pattern.compile(regex);
			return pattern.matcher(text).matches();
		}
		
		/**
		 * Verify the given text against the type
		 * @param text
		 * @return
		 */
		public static boolean verify( VerificationTypes type, String text ){
			return type.verify(text);
		}
		
		public static VerificationTypes getVerificationType(String type){
			for(VerificationTypes vt:VerificationTypes.values()){
				if(vt.toString().equals(type)){
					return vt;
				}
			}
			
			return VerificationTypes.TELEPHONE;
		}
	}
}

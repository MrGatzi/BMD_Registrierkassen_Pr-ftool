import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import org.apache.commons.codec.binary.Base64;
//classe die das auslesen, umrechnen und ausgeben der DEP-Files übernimmt
public class __ShowDepFileInConsole{
	String[] QR_Code_Titels = { "", "ZDA: ", "Kassen-ID:", "Belegnummer:", "Beleg-Datum-Uhrzeit:",
			"Betrag-Satz-Normal:", "Betrag-Satz-Ermaessigt-1:", "Betrag-Satz-Ermaessigt-2:", "Betrag-Satz-Null:",
			"Betrag-Satz-Besonders:", "Stand-Umsatz-Zaehler-AES256-ICM_Entschlüsselt:", "Zertifikat-Seriennummer:",
			"Sig-Voriger-Beleg:", "Signatur:", "", "", "" }; // Array mit die vor einem Wert bei der "ShowDEP" und "schowQR" methode angezeigt werden
	//coding
	__Coding code= new __Coding();
	__ReadFile read= new __ReadFile();
	//timer
	long startimer=0;
	long timer=0;
	int countimer=0;
	public String show(String show_2, String show_5) {
		startimer=System.currentTimeMillis();
		StringBuilder outputstring = new StringBuilder();
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			double umsatzählerSoll =0;
			double umsatzähler1 =0;
			double umsatzähler2 =0;
			double umsatzähler3 =0;
			double umsatzähler4 =0;
			double umsatzähler5 =0;
			double umsatzählerAlt =0;
			int wrongchain=0;
			int rightchain=0;
			int FlagControll=0;
			//Outputarea.setText("DEP_FILE:");
			outputstring.append("DEP_FILE:");
			//String DEP = Readtxt(selectedFolder_show_2.getSelectedItem().toString());
			String DEP = read.Readtxt(show_2);
			String FlagSignatur="";
			int indexPrüf=0;
			int forcounter=0;
			indexPrüf = DEP.indexOf("Belege-kompakt");			
			timer("Read text");
			while (indexPrüf>-1) {
				DEP=DEP.substring(indexPrüf, DEP.length());
				String DEP2 = DEP.substring(DEP.indexOf("["), DEP.indexOf("]"));
				indexPrüf = DEP.indexOf("Belege-kompakt", DEP.indexOf("Belege-kompakt") + 1);
				String[] parts =DEP2.split(",");
				for (int i = 0; i < parts.length; i++) {
					parts[i] = parts[i].substring(parts[i].indexOf("\"") + 1);
					parts[i] = parts[i].substring(0,parts[i].indexOf("\""));
					String[] parts3 = parts[i].split("[.]");
					byte[] parts4 = null;
					if (parts3.length > 1) {
						parts4 = code.base64Decode(parts3[1], false);
					} else {
						parts4 = "NOT VALID".getBytes();
					}
					String PartString = new String(parts4, "UTF-8");
					String[] parts2 = PartString.split("_");
					int Flag2 = 0;
					String KassenID = "";
					String BelegID = "";
					//Outputarea.append("\r\nBeleg : "+i);
					outputstring.append("\r\nBeleg : "+i);
					while (Flag2 < parts2.length) {
						if (Flag2 == 2) {
							KassenID = parts2[Flag2];
						}
						if (Flag2 == 3) {
							BelegID = parts2[Flag2];
						}
						if (Flag2 == 5) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatzähler1 =numBig.doubleValue();
						
						}
						if (Flag2 == 6) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatzähler2 =numBig.doubleValue(); 
							
						}
						if (Flag2 == 7) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatzähler3 = numBig.doubleValue();
							
						}
						if (Flag2 == 8) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
								flag = flag.replaceAll(",", "");
								BigInteger numBig = new BigInteger(flag);
							umsatzähler4 = numBig.doubleValue();
						
						}
						if (Flag2 == 9) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatzähler5 = numBig.doubleValue();
							/*umsatzähler5= (double) flag;
							umsatzähler5=Math.round(umsatzähler5*100);*/
						}
						if (Flag2 == 10) {
							outputstring.append("Stand-Umsatz-Zaehler-AES256-ICM_Verschlüsselt: " + parts2[Flag2] + "   \r\n");
							// Abfrage ob STO oder TRA (Trainings beleg oder
							// Storno Beleg)
							// oder Umsatz wert
							if (parts2[Flag2].equals("U1RP")) {
								String d = "STO";
								outputstring.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");
								umsatzählerSoll=umsatzählerAlt+umsatzähler1+umsatzähler2+umsatzähler3+umsatzähler4+umsatzähler5;
								umsatzählerAlt=umsatzählerSoll;
								outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatzählerSoll/100) + " €\r\n");
								rightchain++;
							} else if (parts2[Flag2].equals("VFJB")) {
								
								String d = "TRA";
								outputstring.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");
								
							} else {
								
								//long i1 = CalcNewValue(KassenID, BelegID, parts2[Flag2],selectedFolder_show_5.getSelectedItem().toString());
								long i1 = code.CalcNewValue(KassenID, BelegID, parts2[Flag2],show_5);
								double d = (double) i1;
								double dflag = d / 100;
								outputstring.append(QR_Code_Titels[Flag2] + "  " + dflag + " €   \r\n");
								umsatzählerSoll=umsatzählerAlt+umsatzähler1+umsatzähler2+umsatzähler3+umsatzähler4+umsatzähler5;
								umsatzählerAlt=umsatzählerSoll;
								
								if(d==umsatzählerSoll){
									rightchain++;
									outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatzählerSoll/100) + " €\r\n");
								}else{
             						wrongchain++;
             						umsatzählerAlt=d;
									outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: FEHLER \r\n");
								}
								
							}
						} else {
							outputstring.append(QR_Code_Titels[Flag2] + " " + parts2[Flag2] + "\r\n");
							
						}
						Flag2++;
						//Outputarea.setRows(Outputarea.getRows() + 2);
					}
					if (forcounter == 0) {
						
						MessageDigest md = MessageDigest.getInstance("sha-256");

						// calculate hash value
						md.update(KassenID.getBytes());
	  					byte[] digest = md.digest();
						// extract number of bytes (N, defined in RKsuite)
						// from
						// hash value
						int bytesToExtract = 8;
						byte[] conDigest = new byte[bytesToExtract];
						System.arraycopy(digest, 0, conDigest, 0, bytesToExtract);
						// encode value as BASE64 String ==> chainValue
						byte[] Sig_Vor_Beleg = Base64.encodeBase64(conDigest, false);
						String Sig_Vor_Beleg_String = new String(Sig_Vor_Beleg, "UTF-8");
						
						if (Sig_Vor_Beleg_String.equals(parts2[Flag2 - 1])) {
							FlagControll++;
							outputstring.append("Sig_Voriger_Beleg_Calculated: " + Sig_Vor_Beleg_String + "\r\n");
						}else{
							outputstring.append("Sig_Voriger_Beleg_Calculated: FEHLER \r\n");
						}
					}
					if (forcounter > 0) {
						MessageDigest md = MessageDigest.getInstance("sha-256");

						// calculate hash value
						md.update(FlagSignatur.getBytes());
						byte[] digest = md.digest();

						// extract number of bytes (N, defined in RKsuite)
						// from
						// hash value
						int bytesToExtract = 8;
						byte[] conDigest = new byte[bytesToExtract];
						System.arraycopy(digest, 0, conDigest, 0, bytesToExtract);

						// encode value as BASE64 String ==> chainValue
						byte[] Sig_Vor_Beleg = Base64.encodeBase64(conDigest, false);
						String Sig_Vor_Beleg_String = new String(Sig_Vor_Beleg, "UTF-8");
						
						if (Sig_Vor_Beleg_String.equals(parts2[Flag2 - 1])) {
							FlagControll++;
							outputstring.append("Sig_Voriger_Beleg_Calculated: " + Sig_Vor_Beleg_String + "\r\n");
						}else{
							outputstring.append("Sig_Voriger_Beleg_Calculated: FEHLER \r\n");
						}
					}
					// calculate the next Signature value
					MessageDigest md1 = MessageDigest.getInstance("sha-256");
					md1.update(parts[i].getBytes());
					byte[] digest1 = md1.digest();
					
					// extract number of bytes (N, defined in RKsuite)
					// from
					// hash value
					int bytesToExtract1 = 8;
					byte[] conDigest1 = new byte[bytesToExtract1];
					System.arraycopy(digest1, 0, conDigest1, 0, bytesToExtract1);

					// encode value as BASE64 String ==> chainValue
					byte[] Sig_Nae_Beleg = Base64.encodeBase64(conDigest1, false);
					String Sig_Nae_Beleg_String = new String(Sig_Nae_Beleg, "UTF-8");
					outputstring.append("Sig_Nächster_Beleg_Calculated: " + Sig_Nae_Beleg_String + "\r\n");
					timer("sig-berech-ende");
					
					FlagSignatur = parts[i];
					if (!parts4.toString().equals("NOT VALID")) {
						String parts5 = code.base64UrlDecode(parts3[2]);
						byte[] encodedBytes = Base64.encodeBase64(parts5.getBytes());
						String PartString1 = new String(encodedBytes, "UTF-8");
						outputstring.append("Signatur: " + PartString1 + "\r\n");
					}

					forcounter++;
					//Outputarea.setRows(Outputarea.getRows() + 2);
				}
			}
			outputstring.append("Listen Elemente: "+ forcounter +" , davon richtig verkettet:"+ FlagControll +" \r\n");
			outputstring.append("Berechnete Umsatzzähler: "+ (rightchain+wrongchain) +" , davon richtig verkettet:"+ rightchain +" \r\n");
			timer("updaten");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputstring.toString();
		//Outputarea.setCaretPosition(0);
	}
	private void timer(String text){
		/*countimer++;
		timer = (System.currentTimeMillis()- startimer)-timer;
		System.out.println(text+" ="+countimer+":" + timer);*/
	}
}

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import org.apache.commons.codec.binary.Base64;
//classe die das auslesen, umrechnen und ausgeben der DEP-Files �bernimmt
public class __ShowDepFileInConsole{
	String[] QR_Code_Titels = { "", "ZDA: ", "Kassen-ID:", "Belegnummer:", "Beleg-Datum-Uhrzeit:",
			"Betrag-Satz-Normal:", "Betrag-Satz-Ermaessigt-1:", "Betrag-Satz-Ermaessigt-2:", "Betrag-Satz-Null:",
			"Betrag-Satz-Besonders:", "Stand-Umsatz-Zaehler-AES256-ICM_Entschl�sselt:", "Zertifikat-Seriennummer:",
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
			double umsatz�hlerSoll =0;
			double umsatz�hler1 =0;
			double umsatz�hler2 =0;
			double umsatz�hler3 =0;
			double umsatz�hler4 =0;
			double umsatz�hler5 =0;
			double umsatz�hlerAlt =0;
			int wrongchain=0;
			int rightchain=0;
			int FlagControll=0;
			//Outputarea.setText("DEP_FILE:");
			outputstring.append("DEP_FILE:");
			//String DEP = Readtxt(selectedFolder_show_2.getSelectedItem().toString());
			String DEP = read.Readtxt(show_2);
			String FlagSignatur="";
			int indexPr�f=0;
			int forcounter=0;
			indexPr�f = DEP.indexOf("Belege-kompakt");			
			timer("Read text");
			while (indexPr�f>-1) {
				DEP=DEP.substring(indexPr�f, DEP.length());
				String DEP2 = DEP.substring(DEP.indexOf("["), DEP.indexOf("]"));
				indexPr�f = DEP.indexOf("Belege-kompakt", DEP.indexOf("Belege-kompakt") + 1);
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
							umsatz�hler1 =numBig.doubleValue();
						
						}
						if (Flag2 == 6) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatz�hler2 =numBig.doubleValue(); 
							
						}
						if (Flag2 == 7) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatz�hler3 = numBig.doubleValue();
							
						}
						if (Flag2 == 8) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
								flag = flag.replaceAll(",", "");
								BigInteger numBig = new BigInteger(flag);
							umsatz�hler4 = numBig.doubleValue();
						
						}
						if (Flag2 == 9) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatz�hler5 = numBig.doubleValue();
							/*umsatz�hler5= (double) flag;
							umsatz�hler5=Math.round(umsatz�hler5*100);*/
						}
						if (Flag2 == 10) {
							outputstring.append("Stand-Umsatz-Zaehler-AES256-ICM_Verschl�sselt: " + parts2[Flag2] + "   \r\n");
							// Abfrage ob STO oder TRA (Trainings beleg oder
							// Storno Beleg)
							// oder Umsatz wert
							if (parts2[Flag2].equals("U1RP")) {
								String d = "STO";
								outputstring.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");
								umsatz�hlerSoll=umsatz�hlerAlt+umsatz�hler1+umsatz�hler2+umsatz�hler3+umsatz�hler4+umsatz�hler5;
								umsatz�hlerAlt=umsatz�hlerSoll;
								outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatz�hlerSoll/100) + " �\r\n");
								rightchain++;
							} else if (parts2[Flag2].equals("VFJB")) {
								
								String d = "TRA";
								outputstring.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");
								
							} else {
								
								//long i1 = CalcNewValue(KassenID, BelegID, parts2[Flag2],selectedFolder_show_5.getSelectedItem().toString());
								long i1 = code.CalcNewValue(KassenID, BelegID, parts2[Flag2],show_5);
								double d = (double) i1;
								double dflag = d / 100;
								outputstring.append(QR_Code_Titels[Flag2] + "  " + dflag + " �   \r\n");
								umsatz�hlerSoll=umsatz�hlerAlt+umsatz�hler1+umsatz�hler2+umsatz�hler3+umsatz�hler4+umsatz�hler5;
								umsatz�hlerAlt=umsatz�hlerSoll;
								
								if(d==umsatz�hlerSoll){
									rightchain++;
									outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatz�hlerSoll/100) + " �\r\n");
								}else{
             						wrongchain++;
             						umsatz�hlerAlt=d;
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
					outputstring.append("Sig_N�chster_Beleg_Calculated: " + Sig_Nae_Beleg_String + "\r\n");
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
			outputstring.append("Berechnete Umsatzz�hler: "+ (rightchain+wrongchain) +" , davon richtig verkettet:"+ rightchain +" \r\n");
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

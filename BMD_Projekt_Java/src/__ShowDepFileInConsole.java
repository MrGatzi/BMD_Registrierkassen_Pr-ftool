import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;  

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
	//for Single DEPLines ben�tigt!
	int rightchainS=0;
	int wrongchainS=0;
	double umsatz�hlerAltS =0;
	int flagControllS=0;
	int elementsUsedS=0;
	//Ausgabe eines DEP-Files mit genau einem DEP-Export.
	public String show(String show_2, String show_5,boolean Startbelegflag) throws ParseException {
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
			int wrongCentValueCounter=0;
			int wrongCentValue=0;
			boolean wrongCentValueFlag=false;
			boolean dateFlag=false;
			int belegCounter=0;
			int dateCounter=0;
			int wrongDateChainCounter=0;
			int FlagControll=0;
			int indexPr�f=0;
			int forcounter=0;
			int falseCounter=0;
			Date currentDate=null;
			Date oldDate=null;
			outputstring.append("DEP_FILE:");
			String DEP = read.Readtxt(show_2);
			String FlagSignatur="";
			indexPr�f = DEP.indexOf("Belege-kompakt");		
			HashSet<String> BelegIDSet = new HashSet<String>();
			List<String> wrongDatesBelegNr = new ArrayList<String>();
			List<String> wrongFormatDatesBelegNr = new ArrayList<String>();
			boolean errorBlockerCauseSTOorTRA=Startbelegflag;
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
					outputstring.append("\r\nBeleg : "+i);
					while (Flag2 < parts2.length) {
						if (Flag2 == 2) {
							KassenID = parts2[Flag2];
						}
						if (Flag2 == 3) {
							
							BelegID = parts2[Flag2];
							if(BelegIDSet.contains(BelegID)) {
								parts2[Flag2]=parts2[Flag2]+" Fehler!";
								belegCounter++;
							}else {
								BelegIDSet.add(BelegID);
							}
						}
						if(Flag2==4) {
							dateFlag = Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d", parts2[Flag2]);
							if(dateFlag) {
								String currentDateString = parts2[Flag2].replace('T', ' ');
								currentDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDateString);  
							}
							
						}
						if (Flag2 == 5) {
							
							String flag = parts2[Flag2].replaceAll("\\.", "");
							if(flag.substring(flag.lastIndexOf(",") + 1).length()>2) {
								wrongCentValueCounter++;
								wrongCentValueFlag=true;
							}
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatz�hler1 =numBig.doubleValue();
						
						}
						if (Flag2 == 6) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							if(flag.substring(flag.lastIndexOf(",") + 1).length()>2) {
								wrongCentValueCounter++;
								wrongCentValueFlag=true;
							}
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatz�hler2 =numBig.doubleValue(); 
							
						}
						if (Flag2 == 7) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							if(flag.substring(flag.lastIndexOf(",") + 1).length()>2) {
								wrongCentValueCounter++;
								wrongCentValueFlag=true;
							}
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatz�hler3 = numBig.doubleValue();
							
						}
						if (Flag2 == 8) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							if(flag.substring(flag.lastIndexOf(",") + 1).length()>2) {
								wrongCentValueCounter++;
								wrongCentValueFlag=true;
							}
								flag = flag.replaceAll(",", "");
								BigInteger numBig = new BigInteger(flag);
							umsatz�hler4 = numBig.doubleValue();
						
						}
						if (Flag2 == 9) {
							String flag = parts2[Flag2].replaceAll("\\.", "");
							if(flag.substring(flag.lastIndexOf(",") + 1).length()>2) {
								wrongCentValueCounter++;
								wrongCentValueFlag=true;
							}
							flag = flag.replaceAll(",", "");
							BigInteger numBig = new BigInteger(flag);
							umsatz�hler5 = numBig.doubleValue();
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
									if((forcounter == 0 && Startbelegflag)||errorBlockerCauseSTOorTRA){
										rightchain++;
										umsatz�hlerAlt=d;
										outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + dflag + " �\r\n");
										errorBlockerCauseSTOorTRA=false;
									}else{
										wrongchain++;
										umsatz�hlerAlt=d;
										outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: FEHLER \r\n");
									}
								}
								
							}
						} else {
							outputstring.append(QR_Code_Titels[Flag2] + " " + parts2[Flag2] + "\r\n");
							
						}
						Flag2++;
					}
					if(forcounter == 0 && Startbelegflag){
						FlagControll++;
					}
					if (forcounter == 0 && !Startbelegflag) {
						
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
					
					FlagSignatur = parts[i];
					
					String controllString = new String(parts4);
					if (!controllString.equals("NOT VALID")) {
						String parts5 = code.base64UrlDecode(parts3[2]);
						byte[] encodedBytes = Base64.encodeBase64(parts5.getBytes());
						String PartString1 = new String(encodedBytes, "UTF-8");
						outputstring.append("Signatur: " + PartString1 + "\r\n");
					}else {
						falseCounter++;
					}
					if(wrongCentValueFlag) {
						wrongCentValueFlag=false;
						wrongCentValue++;
					}
					forcounter++;
					if(!dateFlag) {
						outputstring.append("Datum: FEHLER\r\n");
						dateCounter++;
						wrongFormatDatesBelegNr.add(parts2[3]);
					}else {
						if(oldDate!=null) {
							if(currentDate.compareTo(oldDate)<0) {
								wrongDateChainCounter++;
								outputstring.append("Datumverkettung: FEHLER\r\n");
								wrongDatesBelegNr.add(parts2[3]);
								oldDate=currentDate;
							}else {
								oldDate=currentDate;
							}
						}else{
							oldDate=currentDate;
						}
					}
				}
			}
			outputstring.append("\r\nListen Elemente: "+ forcounter +" , davon richtig verkettet:"+ FlagControll +" \r\n");
			outputstring.append("Berechnete Umsatzz�hler: "+ (rightchain+wrongchain) +" , davon richtig verkettet:"+ rightchain +" \r\n");
			outputstring.append("Belege mit falschen Betragsspalten: "+ wrongCentValue +" (insgesamt falsche Spalten: "+wrongCentValueCounter+" ) \r\n");
			outputstring.append("Belege mit falschem Aufbau: "+ falseCounter +" \r\n");
			outputstring.append("Belege mit falscher BelegID: "+ belegCounter +" \r\n");
			outputstring.append("Belege mit falschem Datum: "+ dateCounter +" \r\n");
			if(wrongFormatDatesBelegNr.size()>0) {
				outputstring.append("Folgende Belege sind betroffen : ");
				for(String BelegNr:wrongFormatDatesBelegNr){
					outputstring.append(BelegNr+"  ");
				}
				outputstring.append("\r\n");
			}
			outputstring.append("Belege mit falscher Datumsverkettung: "+ wrongDateChainCounter +" \r\n");
			if(wrongDatesBelegNr.size()>0) {
				outputstring.append("Folgende Belege sind betroffen : ");
				for(String BelegNr:wrongDatesBelegNr){
					outputstring.append(BelegNr+"  ");
				}
				outputstring.append("\r\n");
			}
			
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
	//Run DEP-Test
	public String runDepTest(String DefaultStringDEP, String DefaultStringCRYPTO, boolean futurBox, String outputFile,boolean DetailsBox){
		StringBuilder outputstring = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		String proString="java -Xmx1500m -jar regkassen-verification-depformat-1.1.1.jar";
		if(futurBox==true){
			proString+=" -f";
		}
		if(DetailsBox==true){
			proString+=" -v";
		}
		proString +=" -i " + DefaultStringDEP + " -c " + DefaultStringCRYPTO+" -o ";
		if(outputFile!=null){
			File file = new File(outputFile);
			if(file.isDirectory()==true){
				proString+=outputFile;
			}
			else{
				proString+="OutputFiles";
			}
		}
		
		try {
			process=runtime.exec(proString);
		} catch (IOException e2) {
			System.out.println("Error while calling regkassen-verification-depformat-1.1.1.jar on __ShowDEPFileInConsole.java on Line 290");
			e2.printStackTrace();
		}
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			// Funktionsblock zum schreiben auf die JTextaera
			// da die Jtextarea eine Character begrenzung in der Weite hat
			// (~~~105 Chars) und es Zeilen gibt die mehr beanspruchen
			// muss zuerst gepr�ft werden ob die Zeile gr��er ist. Wenn Sie
			// gr��er ist wird sie soo oft geteilt auf die JTextarea
			// geschrieben
			// bis keine Chars mehr vorhanden sind.
			// nach jeder geschriebenen Zeile wird die JTextarea um eine
			// "row" erweiterd
			// Am schluss wird der Courser wieder ganz am Anfang gestellt
			try {
					while ((line = br.readLine()) !=null) {

						if (line.length() > 105) {
							int lineCounter = line.length();
							int whileFlag = 0;
							while (lineCounter - 105 > 0) {
								outputstring.append(line.substring(whileFlag, whileFlag + 105) + "\r\n");
								whileFlag = whileFlag + 105;
								lineCounter = lineCounter - 105;
							}
							outputstring.append(line.substring(whileFlag, line.length()) + "\r\n");
						} else {
							outputstring.append(line + "\r\n");
						}
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return outputstring.toString();
	}
	//Spezielle Ausgabe von DEP-FIles dies meherer DEP-Exporte in sich haben.
	public String showAdv(String show_2, String show_5) throws NoSuchAlgorithmException, IOException{
		File file = new File(show_2);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error by : ShowDepFileInConsole on Line 335 - scanner can't reach File");
			return "Error Can't Read File";
		}

		int lineNum = 0;
		String line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.contains("Belege-kompakt")) {
				if (scanner.nextLine().contains("\"")) {
					lineNum++;
				}
			}
		}

		scanner.close();
		Scanner scanner2;
		try {
			scanner2 = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error by : ShowDepFileInConsole on Line 355 - scanner can't reach File");
			return "Error Can't Read File";
		}
		String line2;
		String[] inputs = new String[lineNum];
		lineNum = 0;
		while (scanner2.hasNextLine()) {
			line = scanner2.nextLine();
			if (line.contains("Belege-kompakt")) {
				line2 = scanner2.nextLine();
				if (line2.contains("\"")) {
					inputs[lineNum] = line2;
					lineNum++;
				}
			}
		}
		scanner2.close();
		int[] ordered;
		try {
			//System.out.println(decryptDepLine(inputs[1],show_5,0,true,"nedda"));
			/*System.out.println(decryptDepLine(inputs[0],show_5,0,false,inputs[1]));*/
			//System.out.println(checkIfFirstDepLine(inputs[1]));
			ordered =orderDepFiles(inputs);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("Error by : ShowDepFileInConsole on Line 376 - scanner can't reach File");
			return "Error while decypting single DEP-Line";
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error by : ShowDepFileInConsole on Line 381 - scanner can't reach File");
			return "Error while decypting single DEP-Line";
		}
		
		Scanner scanner3;
		boolean take=false;
		StringBuilder depLines = new StringBuilder();
		String depLineBefore="";
		int counterNr=1;
		String[] depLinesToStart = new String[ordered.length];
		for(int i=0; i<ordered.length;i++){
			depLines.append("\r\n-----------------------------------------------------------------------------------------------------------------"+"\r\n");
			depLines.append("KassenNr:"+(i+1)+"\r\n");
			try {
				scanner3 = new Scanner(file);
			} catch (FileNotFoundException e) {
				System.out.println("Error by : ShowDepFileInConsole on Line 355 - scanner can't reach File");
				return "Error Can't Read File";
			}
			lineNum = 0;
			while (scanner3.hasNextLine()) {
				line = scanner3.nextLine();
				if(take&&line.contains("\"")){
					depLines.append(decryptDepLine(line,show_5,counterNr,false,depLineBefore));
					//depLines.append(line+"\n");
					counterNr++;
					depLineBefore=line;
				}
				if(take&&(!line.contains("\""))){
					take=false;
				}
				if (line.contains("Belege-kompakt")) {
					line2 = scanner3.nextLine();
					if (line2.contains("\"")) {
						lineNum++;
						if(lineNum==(ordered[i]+1)){
							take=true;
							//depLines.append(line2+"\n");
							if(counterNr==1){
								depLines.append(decryptDepLine(line2,show_5,counterNr,true,""));
								depLinesToStart[i]=line2;
							}else{
								umsatz�hlerAltS=0;
								for(int j=0; j<i;j++){
									if(j==0){
										depLines.append(decryptDepLine(depLinesToStart[j],show_5,000,true,""));
										depLineBefore=depLinesToStart[j];
									}else{
										depLines.append(decryptDepLine(depLinesToStart[j],show_5,000,false,depLineBefore));
										depLineBefore=depLinesToStart[j];
									}
								}
								depLines.append(decryptDepLine(line2,show_5,counterNr,false,depLineBefore));
							}
							counterNr++;
							depLineBefore=line2;
							depLinesToStart[i]=line2;
						}
					}
				}
			}
			scanner3.close();
		}
		umsatz�hlerAltS=0;
		depLines.append("Listen Elemente: "+ elementsUsedS +" , davon richtig verkettet:"+ flagControllS +" \r\n");
		depLines.append("Berechnete Umsatzz�hler: "+ (rightchainS+wrongchainS) +" , davon richtig verkettet:"+ rightchainS +" \r\n");
		rightchainS=0;
		wrongchainS=0;
		flagControllS=0;
		elementsUsedS=0;
		return depLines.toString();
	}
	//Entschl�sselt eine einzelne DEP Zeile und gibt sie in Text-Form wieder zur�ck
	private String decryptDepLine(String depLine,String cryptoFile,int belegNr,boolean first,String depLineBefor) throws IOException, NoSuchAlgorithmException{
		elementsUsedS++;
		double umsatz�hlerSoll =0;
		double umsatz�hler1 =0;
		double umsatz�hler2 =0;
		double umsatz�hler3 =0;
		double umsatz�hler4 =0;
		double umsatz�hler5 =0;
		
		
		StringBuilder outputstring = new StringBuilder();
		depLine = depLine.substring(depLine.indexOf("\"") + 1);
		depLine = depLine.substring(0,depLine.indexOf("\""));
			String[] parts3 = depLine.split("[.]");
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
			outputstring.append("\r\nBeleg : "+belegNr);
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
						umsatz�hlerSoll=umsatz�hlerAltS+umsatz�hler1+umsatz�hler2+umsatz�hler3+umsatz�hler4+umsatz�hler5;
						umsatz�hlerAltS=umsatz�hlerSoll;
						outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatz�hlerSoll/100) + " �\r\n");
						rightchainS++;
					} else if (parts2[Flag2].equals("VFJB")) {
						
						String d = "TRA";
						outputstring.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");
						
					} else {
						
						//long i1 = CalcNewValue(KassenID, BelegID, parts2[Flag2],selectedFolder_show_5.getSelectedItem().toString());
						long i1 = code.CalcNewValue(KassenID, BelegID, parts2[Flag2],cryptoFile);
						double d = (double) i1;
						double dflag = d / 100;
						outputstring.append(QR_Code_Titels[Flag2] + "  " + dflag + " �   \r\n");
						umsatz�hlerSoll=umsatz�hlerAltS+umsatz�hler1+umsatz�hler2+umsatz�hler3+umsatz�hler4+umsatz�hler5;
						umsatz�hlerAltS=umsatz�hlerSoll;
						
						if(d==umsatz�hlerSoll){
							rightchainS++;
							outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatz�hlerSoll/100) + " �\r\n");
						}else{
     						wrongchainS++;
     						umsatz�hlerAltS=d;
							outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: FEHLER \r\n");
						}
						
					}
				} else {
					outputstring.append(QR_Code_Titels[Flag2] + " " + parts2[Flag2] + "\r\n");
					
				}
				Flag2++;
				//Outputarea.setRows(Outputarea.getRows() + 2);
			}
			if (first){
				
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
					flagControllS++;
					outputstring.append("Sig_Voriger_Beleg_Calculated: " + Sig_Vor_Beleg_String + "\r\n");
				}else{
					outputstring.append("Sig_Voriger_Beleg_Calculated: FEHLER \r\n");
				}
			}else{
				MessageDigest md = MessageDigest.getInstance("sha-256");
				depLineBefor = depLineBefor.substring(depLineBefor.indexOf("\"") + 1);
				depLineBefor = depLineBefor.substring(0,depLineBefor.indexOf("\""));
				// calculate hash value
				md.update(depLineBefor.getBytes());
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
					flagControllS++;
					outputstring.append("Sig_Voriger_Beleg_Calculated: " + Sig_Vor_Beleg_String + "\r\n");
				}else{
					outputstring.append("Sig_Voriger_Beleg_Calculated: FEHLER \r\n");
				}
			}
			// calculate the next Signature value
			MessageDigest md1 = MessageDigest.getInstance("sha-256");
			md1.update(depLine.getBytes());
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
			
		//	FlagSignatur = depLine;
			if (!parts4.toString().equals("NOT VALID")) {
				String parts5 = code.base64UrlDecode(parts3[2]);
				byte[] encodedBytes = Base64.encodeBase64(parts5.getBytes());
				String PartString1 = new String(encodedBytes, "UTF-8");
				outputstring.append("Signatur: " + PartString1 + "\r\n");
			}
			
		return outputstring.toString();
	}
	//Checkt ob die �bergebende Zeile eine "erste Zeile" ist. (sprich ob KassenID = Vorg�ngerwert gleich sind)
	private boolean checkIfFirstDepLine(String depLine) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		
		depLine = depLine.substring(depLine.indexOf("\"") + 1);
		depLine = depLine.substring(0,depLine.indexOf("\""));
			String[] parts3 = depLine.split("[.]");
			byte[] parts4 = null;
			if (parts3.length > 1) {
				parts4 = code.base64Decode(parts3[1], false);
			} else {
				parts4 = "NOT VALID".getBytes();
			}
			String PartString = new String(parts4, "UTF-8");
			String[] parts2 = PartString.split("_");
		MessageDigest md = MessageDigest.getInstance("sha-256");

		// calculate hash value
		md.update(parts2[2].getBytes());
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
		
		if (Sig_Vor_Beleg_String.equals(parts2[parts2.length - 1])) {
			return true;
		}else{
			return false;
		}
		
	}
	//Ordnet depLines und gibt ein Array zur�ck in welcher Reihenfolge sie stehen.
	private int[] orderDepFiles(String[] depLinesInput)throws UnsupportedEncodingException, NoSuchAlgorithmException {
		int[] output = new int[depLinesInput.length];
		String[][] order = new String[depLinesInput.length][3];
		for (int i = 0; i < depLinesInput.length; i++) {
			//prepare the inputLines
			depLinesInput[i] = depLinesInput[i].substring(depLinesInput[i].indexOf("\"") + 1);
			depLinesInput[i] = depLinesInput[i].substring(0, depLinesInput[i].indexOf("\""));
			String[] parts3 = depLinesInput[i].split("[.]");
			
			//Calc Next Sig Value
			byte[] parts4 = null;
			if (parts3.length > 1) {
				parts4 = code.base64Decode(parts3[1], false);
			} else {
				parts4 = "NOT VALID".getBytes();
			}
			String PartString = new String(parts4, "UTF-8");
			String[] parts2 = PartString.split("_");
			MessageDigest md1 = MessageDigest.getInstance("sha-256");
			md1.update(depLinesInput[i].getBytes());
			byte[] digest1 = md1.digest();
			int bytesToExtract1 = 8;
			byte[] conDigest1 = new byte[bytesToExtract1];
			System.arraycopy(digest1, 0, conDigest1, 0, bytesToExtract1);
			byte[] Sig_Nae_Beleg = Base64.encodeBase64(conDigest1, false);
			String Sig_Nae_Beleg_String = new String(Sig_Nae_Beleg, "UTF-8");
			
			

			
			
			//berechne den sigantur Wert des Beleges als w�re er der Start beleg um festzustellen welcher Wert am anfang stehen muss
			if (parts3.length > 1) {
				parts4 = code.base64Decode(parts3[1], false);
			} else {
				parts4 = "NOT VALID".getBytes();
			}
			MessageDigest md = MessageDigest.getInstance("sha-256");
			md.update(parts2[2].getBytes());
			byte[] digest = md.digest();
			int bytesToExtract = 8;
			byte[] conDigest = new byte[bytesToExtract];
			System.arraycopy(digest, 0, conDigest, 0, bytesToExtract);
			byte[] Sig_Vor_Beleg = Base64.encodeBase64(conDigest, false);
			String Sig_Vor_Beleg_String = new String(Sig_Vor_Beleg, "UTF-8");

			
			if (Sig_Vor_Beleg_String.equals(parts2[parts2.length - 1])) {
				order[i][2] = "true";
			} else {
				order[i][2] = "false";
			}
			order[i][0] = parts2[parts2.length - 1];
			order[i][1] = Sig_Nae_Beleg_String;

		}
		//SORT
		for (int j = 0; j < order.length; j++) {
			if(order[j][2]=="true"){
				output[0]=j;
			}
		};
		for (int x = 0; x < order.length-1; x++) {
			for (int l = 0; l < order.length; l++) {
					if(order[output[x]][1].equals(order[l][0])){
						output[x+1]=l;
						l=order.length+1;
				}
			};
		}
		
		return output;

	}
}

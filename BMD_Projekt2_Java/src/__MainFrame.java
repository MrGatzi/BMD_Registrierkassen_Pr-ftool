
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.math.BigInteger;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.image.BufferedImage;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import java.util.List;

public class __MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final long NewValue = 0;
	private JPanel contentPane; // komplettes Layout
	JComboBox<String> selectedFolder_dep; // JComboBox für Demokassen auswahl
	JComboBox<String> selectedFolder_crypto; // JComboBox für OUTPUTFOLDER
	JComboBox<String> selectedFolder_out;
	JTextArea Outputarea; // Textausgabe
	String DEPFile;
	String CryptoFile;
	String OutFolder = "user.home";
	JScrollPane ScrollBar;
	__Coding code = new __Coding();
	String[] QR_Code_Titels = { "", "ZDA: ", "Kassen-ID:", "Belegnummer:", "Beleg-Datum-Uhrzeit:",
			"Betrag-Satz-Normal:", "Betrag-Satz-Ermaessigt-1:", "Betrag-Satz-Ermaessigt-2:", "Betrag-Satz-Null:",
			"Betrag-Satz-Besonders:", "Stand-Umsatz-Zaehler-AES256-ICM_Entschlüsselt:", "Zertifikat-Seriennummer:",
			"Sig-Voriger-Beleg:", "Signatur:", "", "", "" }; // Array mit die
																// vor einem
																// Wert bei der
																// "ShowDEP" und
																// "schowQR"
																// methode
																// angezeigt
																// werden
	double umsatzählerAltS = 0;
	int rightchainS = 0;
	int wrongchainS = 0;
	int flagControllS = 0;
	int elementsUsedS = 0;

	public __MainFrame() {
		try {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
			FrameInit();
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
				}
			});
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void FrameInit() throws Exception {
		// contentPane

		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new GridLayout(2, 1));
		setSize(new Dimension(1000, 800));
		setTitle("Registrierkassen Testsystem");

		JPanel CP_CenterGrid_Flow_1_1 = new JPanel(new BorderLayout());
		JPanel CP_CenterGrid_Flow_1_1_north = new JPanel(new FlowLayout());
		JPanel CP_CenterGrid_Flow_1_1_center = new JPanel(new FlowLayout());
		JPanel CP_CenterGrid_Flow_1_1_south = new JPanel(new FlowLayout());
		JPanel CP_CenterGrid_Flow_1_1_north_grid = new JPanel(new GridLayout(2, 1));
		JPanel CP_CenterGrid_Flow_1_1_north_grid_1 = new JPanel(new FlowLayout());
		JPanel CP_CenterGrid_Flow_1_1_north_grid_2 = new JPanel(new FlowLayout());

		selectedFolder_out = new JComboBox();
		selectedFolder_out.setEditable(false);
		selectedFolder_out.setPreferredSize(new Dimension(600, 25));
		CP_CenterGrid_Flow_1_1_north_grid_1.add(selectedFolder_out);
		JButton out = new JButton();
		out.setText("Select Output");
		outPicker outPick = new outPicker();
		out.addActionListener(outPick);
		CP_CenterGrid_Flow_1_1_north_grid_1.add(out);

		selectedFolder_crypto = new JComboBox();
		selectedFolder_crypto.setEditable(false);
		selectedFolder_crypto.setPreferredSize(new Dimension(600, 25));
		CP_CenterGrid_Flow_1_1_north_grid_2.add(selectedFolder_crypto);

		JButton selectcrypto = new JButton();
		selectcrypto.setText("Select Crypto");
		cryptoPicker cryLis = new cryptoPicker();
		selectcrypto.addActionListener(cryLis);
		CP_CenterGrid_Flow_1_1_north_grid_2.add(selectcrypto);
		CP_CenterGrid_Flow_1_1_north_grid.add(CP_CenterGrid_Flow_1_1_north_grid_1);
		CP_CenterGrid_Flow_1_1_north_grid.add(CP_CenterGrid_Flow_1_1_north_grid_2);
		CP_CenterGrid_Flow_1_1_north.add(CP_CenterGrid_Flow_1_1_north_grid);

		selectedFolder_dep = new JComboBox();
		selectedFolder_dep.setEditable(false);
		selectedFolder_dep.setPreferredSize(new Dimension(600, 25));
		CP_CenterGrid_Flow_1_1_center.add(selectedFolder_dep);

		JButton selectDep = new JButton();
		selectDep.setText("Select DEP");
		depPicker depLis = new depPicker();
		selectDep.addActionListener(depLis);
		CP_CenterGrid_Flow_1_1_center.add(selectDep);

		JButton run = new JButton();
		run.setText("Go Dep_Test");
		run runA = new run();
		run.addActionListener(runA);
		CP_CenterGrid_Flow_1_1_south.add(run);
		run.setPreferredSize(new Dimension(100, 100));
		
		JButton run2 = new JButton();
		run2.setText("Go no Test");
		run2 runA2 = new run2();
		run2.addActionListener(runA2);
		CP_CenterGrid_Flow_1_1_south.add(run2);
		run2.setPreferredSize(new Dimension(100, 100));

		CP_CenterGrid_Flow_1_1.add(CP_CenterGrid_Flow_1_1_center, BorderLayout.CENTER);
		CP_CenterGrid_Flow_1_1.add(CP_CenterGrid_Flow_1_1_north, BorderLayout.NORTH);
		CP_CenterGrid_Flow_1_1.add(CP_CenterGrid_Flow_1_1_south, BorderLayout.SOUTH);

		Outputarea = new JTextArea();
		Outputarea.setEditable(false);
		Outputarea.setPreferredSize(new Dimension(350, 1800));
		Outputarea.setLineWrap(true);
		Outputarea.setWrapStyleWord(true);
		Outputarea.setColumns(10);

		ScrollBar = new JScrollPane(Outputarea);
		ScrollBar.setPreferredSize(new Dimension(400, 300));

		contentPane.add(CP_CenterGrid_Flow_1_1);
		contentPane.add(ScrollBar);

	}

	private class depPicker implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFrame Dialog = new JFrame();
			JFileChooser fileChooser2 = new JFileChooser(new File(OutFolder));
			fileChooser2.setAcceptAllFileFilterUsed(true);
			fileChooser2.setDialogTitle("Wählen Sie ein DEP-File zur überprüfung aus:");
			int result = fileChooser2.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser2.getSelectedFile();
				selectedFolder_dep.addItem("" + selectedFile.getAbsolutePath() + "");
				selectedFolder_dep.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
				DEPFile = selectedFile.getAbsolutePath();
			}
		}
	}

	private class cryptoPicker implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFrame Dialog = new JFrame();
			JFileChooser fileChooser2 = new JFileChooser(new File(OutFolder));
			fileChooser2.setAcceptAllFileFilterUsed(true);
			fileChooser2.setDialogTitle("Wählen Sie ein Crypto File zu überprüfung aus:");

			int result = fileChooser2.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser2.getSelectedFile();
				selectedFolder_crypto.addItem("" + selectedFile.getAbsolutePath() + "");
				selectedFolder_crypto.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
				CryptoFile = selectedFile.getAbsolutePath();
			}
		}
	}

	private class outPicker implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFrame Dialog = new JFrame();
			JFileChooser fileChooser2 = new JFileChooser(new File("user.home"));
			fileChooser2.setAcceptAllFileFilterUsed(true);
			fileChooser2.setDialogTitle("Wählen Sie ein Output Folder aus:");
			fileChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = fileChooser2.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser2.getSelectedFile();
				selectedFolder_out.addItem("" + selectedFile.getAbsolutePath() + "");
				selectedFolder_out.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
				OutFolder = selectedFile.getAbsolutePath();
			}
		}
	}

	private class run implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			Outputarea.setText("Init\r\n");
			Outputarea.update(Outputarea.getGraphics());
			System.out.println("s");
			long Starttime = System.currentTimeMillis();
			int lineNr = 0;
			boolean firstLineFlag = false;
			List<String> firstDepLines = new ArrayList<String>();
			FileInputStream fstream;
			String open = "{\r\n  \"Belege-Gruppe\": [\r\n    {\r\n      \"Signaturzertifikat\": \"\",\r\n      \"Zertifizierungsstellen\": [],\r\n      \"Belege-kompakt\": [";
			String end = "      ]\r\n    }\r\n   ]\r\n}";
			String linebefore = "";
			try {

				fstream = new FileInputStream(DEPFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String line;

				while ((line = br.readLine()) != null) {
					lineNr++;
					if (firstLineFlag) {
						firstDepLines.add(line);
						firstLineFlag = false;
					}
					if (line.contains("Belege-kompakt")) {
						firstLineFlag = true;

					}

				}
				fstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Outputarea.append("DEP-Exporte found : " + firstDepLines.size() + "\r\n");
			Outputarea.update(Outputarea.getGraphics());
			// System.out.println("ms:" + (System.currentTimeMillis() -
			// Starttime) + "| " + lineNr);

			List<String> orderdFirstDepLines = new ArrayList<String>();
			try {
				orderdFirstDepLines = orderDepLines(firstDepLines);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
				Outputarea.append("couldn't order DEP-Lines\r\n");
				e1.printStackTrace();
			}
			Outputarea.append("Dep-Exports ordered\r\n");
			Outputarea.update(Outputarea.getGraphics());
			try {
				File file;
				BufferedWriter writer;
				File file2;
				BufferedWriter writer2;
				int forcounter = 0;
				int forcounter2 = 0;

				for (String element : orderdFirstDepLines) {
					Outputarea.append("Writing File " + (forcounter + 1) + "\r\n");
					Outputarea.update(Outputarea.getGraphics());
					lineNr = 0;
					file = new File(OutFolder+"/Dep_" + (forcounter + 1)
							+ ".json");
					file.createNewFile();
					writer = new BufferedWriter(new FileWriter(file));
					file2 = new File(OutFolder+"/OUT_"
							+ (forcounter + 1) + ".txt");
					file2.createNewFile();
					writer2 = new BufferedWriter(new FileWriter(file2));

					forcounter2 = 0;
					fstream = new FileInputStream(DEPFile);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					String line;
					while ((line = br.readLine()) != null) {

						if (line.contains("Belege-kompakt") && firstLineFlag) {
							writer.newLine();
							firstLineFlag = false;
						}

						if (firstLineFlag && line.contains(".")) {
							lineNr++;
							if (lineNr == 1) {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, true, linebefore));
							} else {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, false, linebefore));
							}
							linebefore = line;
							writer.write(line);
							writer.newLine();

						}
						if (line.contains(element)) {
							writer.write(open);
							writer.newLine();
							for (String element2 : orderdFirstDepLines) {
								if (forcounter2 < forcounter) {
									lineNr++;
									if (lineNr == 1) {
										writer2.write(decryptDepLine(element2, CryptoFile, lineNr, true, linebefore));
									} else {
										writer2.write(decryptDepLine(element2, CryptoFile, lineNr, false, linebefore));
									}
									linebefore = element2;
									writer.write(element2);
									writer.newLine();
								}
								forcounter2++;
							}
							lineNr++;
							if (lineNr == 1) {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, true, linebefore));
							} else {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, false, linebefore));
							}
							linebefore = line;
							writer.write(line);
							writer.newLine();
							firstLineFlag = true;
						}

					}
					writer2.write("Listen Elemente: " + elementsUsedS + " , davon richtig verkettet:" + flagControllS
							+ " \r\n");
					writer2.write("Berechnete Umsatzzähler: " + (rightchainS + wrongchainS)
							+ " , davon richtig verkettet:" + rightchainS + " \r\n");
					if(elementsUsedS==flagControllS){
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Verkettungen korrekt\r\n");
					}else{
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Verkettungen nicht korrekt\r\n");
					};
					if(wrongchainS==0){
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Umsatzzähler korrekt\r\n");
					}else{
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Umsatzzähler nicht korrekt\r\n");
					};
					Outputarea.update(Outputarea.getGraphics());
					umsatzählerAltS = 0;
					elementsUsedS = 0;
					rightchainS = 0;
					wrongchainS = 0;
					flagControllS = 0;
					fstream.close();
					forcounter++;
					writer.write(end);
					writer.flush();
					writer.close();
					writer2.flush();
					writer2.close();
					Outputarea.append("Closed File " + (forcounter) + "\r\n\r\n");
					Outputarea.update(Outputarea.getGraphics());

				}

			} catch (IOException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				File fileDep;
				
				Outputarea.update(Outputarea.getGraphics());
				Outputarea.append("Running Dep-Tests \r\n");
				Outputarea.update(Outputarea.getGraphics());
				int forcounterDep = 0;
				BufferedWriter writerDep;
				for (String element1 : orderdFirstDepLines) {
					forcounterDep++;
					Outputarea.append("Running Dep-Tests for File :" + (forcounterDep) + "\r\n");
					Outputarea.update(Outputarea.getGraphics());
					fileDep = new File(OutFolder+"/DepTest_"+ (forcounterDep) + ".json");
					fileDep.createNewFile();
					writerDep = new BufferedWriter(new FileWriter(fileDep));
					Runtime runtime = Runtime.getRuntime();
					Process process = null;
					process = runtime.exec("java -jar regkassen-verification-depformat-1.0.0.jar -i "+OutFolder+"/Dep_"+ (forcounterDep) + ".json" + " -c " + CryptoFile + " -o OutputFiles");
					InputStream is = process.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader brDep = new BufferedReader(isr);
					String lineDep;
					while ((lineDep = brDep.readLine()) != null) {
						writerDep.write(lineDep+"\r\n");
					}
					writerDep.flush();
					writerDep.close();
					Outputarea.append("Closed File : Dep_" + (forcounterDep) + "\r\n\r\n");
					Outputarea.update(Outputarea.getGraphics());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Outputarea.append("Done\r\n");

		}

	}
	private class run2 implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			Outputarea.setText("Init\r\n");
			Outputarea.update(Outputarea.getGraphics());
			System.out.println("s");
			long Starttime = System.currentTimeMillis();
			int lineNr = 0;
			boolean firstLineFlag = false;
			List<String> firstDepLines = new ArrayList<String>();
			FileInputStream fstream;
			String open = "{\r\n  \"Belege-Gruppe\": [\r\n    {\r\n      \"Signaturzertifikat\": \"\",\r\n      \"Zertifizierungsstellen\": [],\r\n      \"Belege-kompakt\": [";
			String end = "      ]\r\n    }\r\n   ]\r\n}";
			String linebefore = "";
			try {

				fstream = new FileInputStream(DEPFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String line;

				while ((line = br.readLine()) != null) {
					lineNr++;
					if (firstLineFlag) {
						firstDepLines.add(line);
						firstLineFlag = false;
					}
					if (line.contains("Belege-kompakt")) {
						firstLineFlag = true;

					}

				}
				fstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Outputarea.append("DEP-Exporte found : " + firstDepLines.size() + "\r\n");
			Outputarea.update(Outputarea.getGraphics());
			// System.out.println("ms:" + (System.currentTimeMillis() -
			// Starttime) + "| " + lineNr);

			List<String> orderdFirstDepLines = new ArrayList<String>();
			try {
				orderdFirstDepLines = orderDepLines(firstDepLines);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
				Outputarea.append("couldn't order DEP-Lines\r\n");
				e1.printStackTrace();
			}
			Outputarea.append("Dep-Exports ordered\r\n");
			Outputarea.update(Outputarea.getGraphics());
			try {
				File file;
				BufferedWriter writer;
				File file2;
				BufferedWriter writer2;
				int forcounter = 0;
				int forcounter2 = 0;

				for (String element : orderdFirstDepLines) {
					Outputarea.append("Writing File " + (forcounter + 1) + "\r\n");
					Outputarea.update(Outputarea.getGraphics());
					lineNr = 0;
					file = new File(OutFolder+"/Dep_" + (forcounter + 1)
							+ ".json");
					file.createNewFile();
					writer = new BufferedWriter(new FileWriter(file));
					file2 = new File(OutFolder+"/OUT_"
							+ (forcounter + 1) + ".txt");
					file2.createNewFile();
					writer2 = new BufferedWriter(new FileWriter(file2));

					forcounter2 = 0;
					fstream = new FileInputStream(DEPFile);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					String line;
					while ((line = br.readLine()) != null) {

						if (line.contains("Belege-kompakt") && firstLineFlag) {
							writer.newLine();
							firstLineFlag = false;
						}

						if (firstLineFlag && line.contains(".")) {
							lineNr++;
							if (lineNr == 1) {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, true, linebefore));
							} else {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, false, linebefore));
							}
							linebefore = line;
							writer.write(line);
							writer.newLine();

						}
						if (line.contains(element)) {
							writer.write(open);
							writer.newLine();
							for (String element2 : orderdFirstDepLines) {
								if (forcounter2 < forcounter) {
									lineNr++;
									if (lineNr == 1) {
										writer2.write(decryptDepLine(element2, CryptoFile, lineNr, true, linebefore));
									} else {
										writer2.write(decryptDepLine(element2, CryptoFile, lineNr, false, linebefore));
									}
									linebefore = element2;
									writer.write(element2);
									writer.newLine();
								}
								forcounter2++;
							}
							lineNr++;
							if (lineNr == 1) {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, true, linebefore));
							} else {
								writer2.write(decryptDepLine(line, CryptoFile, lineNr, false, linebefore));
							}
							linebefore = line;
							writer.write(line);
							writer.newLine();
							firstLineFlag = true;
						}

					}
					writer2.write("Listen Elemente: " + elementsUsedS + " , davon richtig verkettet:" + flagControllS
							+ " \r\n");
					writer2.write("Berechnete Umsatzzähler: " + (rightchainS + wrongchainS)
							+ " , davon richtig verkettet:" + rightchainS + " \r\n");
					if(elementsUsedS==flagControllS){
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Verkettungen korrekt\r\n");
					}else{
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Verkettungen nicht korrekt\r\n");
					};
					if(wrongchainS==0){
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Umsatzzähler korrekt\r\n");
					}else{
						Outputarea.append("Dep-Tests for File :" + (forcounter+1) + "  -> Umsatzzähler nicht korrekt\r\n");
					};
					umsatzählerAltS = 0;
					elementsUsedS = 0;
					rightchainS = 0;
					wrongchainS = 0;
					flagControllS = 0;
					fstream.close();
					forcounter++;
					writer.write(end);
					writer.flush();
					writer.close();
					writer2.flush();
					writer2.close();
					Outputarea.append("Closed File " + (forcounter) + "\r\n\r\n");
					Outputarea.update(Outputarea.getGraphics());

				}

			} catch (IOException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Outputarea.append("Done\r\n");

		}

	}

	private String calcNextSig(String depLine) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		depLine = depLine.substring(depLine.indexOf("\"") + 1);
		depLine = depLine.substring(0, depLine.indexOf("\""));
		String result = "";
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
		result = new String(Sig_Nae_Beleg, "UTF-8");
		return result;

	}

	private List<String> orderDepLines(List<String> orderdFirstDepLines)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		List<String> result = new ArrayList<String>();
		String nextSig = "";
		for (String element : orderdFirstDepLines) {
			try {
				if (checkIfFirstDepLine(element)) {
					result.add(element);
					nextSig = calcNextSig(element);
				}
			} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (result.size() > 1) {
			nextSig = calcNextSig(result.get(0));
		}
		int whileflag = 0;
		while (whileflag == 0) {
			for (String element : orderdFirstDepLines) {
				try {
					if (checkDepSigValue(element, nextSig)) {
						result.add(element);
						nextSig = calcNextSig(element);
						whileflag = -1;
					}
				} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			whileflag++;
		}

		return result;
	}

	private boolean checkDepSigValue(String depLine, String sig) throws UnsupportedEncodingException {
		depLine = depLine.substring(depLine.indexOf("\"") + 1);
		depLine = depLine.substring(0, depLine.indexOf("\""));
		String[] parts3 = depLine.split("[.]");
		byte[] parts4 = null;
		if (parts3.length > 1) {
			parts4 = code.base64Decode(parts3[1], false);
		} else {
			parts4 = "NOT VALID".getBytes();
		}
		String PartString = new String(parts4, "UTF-8");
		String[] parts2 = PartString.split("_");

		if (sig.equals(parts2[parts2.length - 1])) {
			return true;
		} else {
			return false;
		}

	}

	private boolean checkIfFirstDepLine(String depLine) throws UnsupportedEncodingException, NoSuchAlgorithmException {

		depLine = depLine.substring(depLine.indexOf("\"") + 1);
		depLine = depLine.substring(0, depLine.indexOf("\""));
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
		} else {
			return false;
		}

	}

	private String decryptDepLine(String depLine, String cryptoFile, int belegNr, boolean first, String depLineBefor)
			throws IOException, NoSuchAlgorithmException {
		elementsUsedS++;
		double umsatzählerSoll = 0;
		double umsatzähler1 = 0;
		double umsatzähler2 = 0;
		double umsatzähler3 = 0;
		double umsatzähler4 = 0;
		double umsatzähler5 = 0;

		StringBuilder outputstring = new StringBuilder();
		depLine = depLine.substring(depLine.indexOf("\"") + 1);
		depLine = depLine.substring(0, depLine.indexOf("\""));
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
		outputstring.append("\r\nBeleg : " + belegNr);
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
				umsatzähler1 = numBig.doubleValue();

			}
			if (Flag2 == 6) {
				String flag = parts2[Flag2].replaceAll("\\.", "");
				flag = flag.replaceAll(",", "");
				BigInteger numBig = new BigInteger(flag);
				umsatzähler2 = numBig.doubleValue();

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
				/*
				 * umsatzähler5= (double) flag;
				 * umsatzähler5=Math.round(umsatzähler5*100);
				 */
			}
			if (Flag2 == 10) {
				outputstring.append("Stand-Umsatz-Zaehler-AES256-ICM_Verschlüsselt: " + parts2[Flag2] + "   \r\n");
				// Abfrage ob STO oder TRA (Trainings beleg oder
				// Storno Beleg)
				// oder Umsatz wert
				if (parts2[Flag2].equals("U1RP")) {
					String d = "STO";
					outputstring.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");
					umsatzählerSoll = umsatzählerAltS + umsatzähler1 + umsatzähler2 + umsatzähler3 + umsatzähler4
							+ umsatzähler5;
					umsatzählerAltS = umsatzählerSoll;
					outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatzählerSoll / 100) + " €\r\n");
					rightchainS++;
				} else if (parts2[Flag2].equals("VFJB")) {

					String d = "TRA";
					outputstring.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");

				} else {

					// long i1 = CalcNewValue(KassenID, BelegID,
					// parts2[Flag2],selectedFolder_show_5.getSelectedItem().toString());
					long i1 = code.CalcNewValue(KassenID, BelegID, parts2[Flag2], cryptoFile);
					double d = (double) i1;
					double dflag = d / 100;
					outputstring.append(QR_Code_Titels[Flag2] + "  " + dflag + " €   \r\n");
					umsatzählerSoll = umsatzählerAltS + umsatzähler1 + umsatzähler2 + umsatzähler3 + umsatzähler4
							+ umsatzähler5;
					umsatzählerAltS = umsatzählerSoll;

					if (d == umsatzählerSoll) {
						rightchainS++;
						outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: " + (umsatzählerSoll / 100) + " €\r\n");
					} else {
						wrongchainS++;
						umsatzählerAltS = d;
						outputstring.append("Stand-Umsatz-Zaehler_Sollsumme: FEHLER \r\n");
					}

				}
			} else {
				outputstring.append(QR_Code_Titels[Flag2] + " " + parts2[Flag2] + "\r\n");

			}
			Flag2++;
			// Outputarea.setRows(Outputarea.getRows() + 2);
		}
		if (first) {

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
			} else {
				outputstring.append("Sig_Voriger_Beleg_Calculated: FEHLER \r\n");
			}
		} else {
			MessageDigest md = MessageDigest.getInstance("sha-256");
			depLineBefor = depLineBefor.substring(depLineBefor.indexOf("\"") + 1);
			depLineBefor = depLineBefor.substring(0, depLineBefor.indexOf("\""));
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
			} else {
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
		outputstring.append("Sig_Nächster_Beleg_Calculated: " + Sig_Nae_Beleg_String + "\r\n");

		// FlagSignatur = depLine;
		if (!parts4.toString().equals("NOT VALID")) {
			String parts5 = code.base64UrlDecode(parts3[2]);
			byte[] encodedBytes = Base64.encodeBase64(parts5.getBytes());
			String PartString1 = new String(encodedBytes, "UTF-8");
			outputstring.append("Signatur: " + PartString1 + "\r\n");
		}

		return outputstring.toString();
	}
}

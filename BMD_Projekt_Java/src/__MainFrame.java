
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.image.BufferedImage;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.pdfview.PDFFile;

public class __MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final long NewValue = 0;
	private JPanel contentPane; // komplettes Layout
	JTextArea Outputarea; // Textausgabe
	JScrollPane ScrollBar; // Textausgabe -Scrollbar
	JComboBox<String> StartFolder_selectedFolder; // JComboBox für Demokassen auswahl
	JComboBox<String> OutputFolder_selectedFolder; // JComboBox für OUTPUTFOLDER auswahl
	JComboBox<String> DEP_selectedFolder; // JComboBox für DEP File
	JComboBox<String> QR_selectedFolder; // JComboBox für OR File
	JComboBox<String> Crypto_selectedFolder; // JComboBox für Crypofile 2
	JCheckBox Filter; // JCheckBox um die Fehler anzuzeigen, funktioniert wie
	JCheckBox FutureCheckboxDEP;				// ein Button
	JCheckBox DetailsCheckboxDEP;	
	JCheckBox StartvalueCheckboxDEP;
	JCheckBox FutureCheckboxQR;				// ein Button
	JCheckBox DetailsCheckboxQR;	
	String DefaultString = "C:/Users/"; // Default String wenn kein Ordner
										// ausgewählt wurde für Demokasse
										// zum erstellen
	String DefaultStringCRYPTO = "none"; // Default String wenn kein Ordner
											// ausgewählt wurde für CRYPTO File
											// 1
											// erstellen
	String DefaultStringCRYPTO2 = "none"; // Default String wenn kein Ordner
											// ausgewählt wurde für CRYPTO File
											// 2 erstellen
	String DefaultStringDEP = "none"; // Default String wenn kein Ordner
										// ausgewählt wurde für DEP File
										// erstellen
	String DefaultStringQR = "none"; // Default String wenn kein Ordner
										// ausgewählt wurde für QR File
										// erstellen
	String Refferenztext = ""; // Text zum kontrolieren
	String Prüftext = ""; // einzelner Text abschnitt zum kontorlieren (0-1
							// entscheidung ob "FEHLERHAFT" drinnen ist)
	String FehlerText = ""; // Text wenn ein Fehler darin vorhanden ist, werden
							// "addiert"
	int FehlerTextFlag = 0;
	public Frame yourJFrame; // JFrame für Dialog
	String WahtIsInTextArea = "Nothing"; // gibt an was in der Text Area is.
											// Jedesmal wenn was hinein
											// geschrieben wieder wird dieser
											// String upgedated
	String ErrorLog_Flag = "Nothing"; // gibt an Welcher ErrorLog derzeit aktiv
										// ist. (ob DEP oder QR) wenn keines von
										// beiden angezeigt wird und trotzdem
										// wer den Filter aktiviert wird
										// "nothing" reingeschrieben
	int Old_Rows = 0; // Speicher Variable die sich merkt wie viele Zeilen in
						// der Textarea waren VOR der Fehlersuche
	JTextPane Anzeige; // Textarea wo beim FehlerFilter angezeigt werden kann
						// wie viel der geprüften Blöcke Fehlerhaft sind!
	String[] QR_Code_Titels = { "", "ZDA: ", "Kassen-ID:", "Belegnummer:", "Beleg-Datum-Uhrzeit:",
			"Betrag-Satz-Normal:", "Betrag-Satz-Ermaessigt-1:", "Betrag-Satz-Ermaessigt-2:", "Betrag-Satz-Null:",
			"Betrag-Satz-Besonders:", "Stand-Umsatz-Zaehler-AES256-ICM_Entschlüsselt:", "Zertifikat-Seriennummer:",
			"Sig-Voriger-Beleg:", "Signatur:", "", "", "" }; // Array mit die vor einem Wert bei der "ShowDEP" und "schowQR" methode angezeigt werden
	String PDF_Mem = "-"; //Speicher welches PDF File als letztes gelesen wurde zum öffnen
	String DefaultOutput = "OutputFiles"; //Default Ordner zum Speichern von Outputfiles von 
	String Format = "PUBLIC_KEY"; //Format (ob Public_Key) oder Certifkate
	String defaultFolerOpen ="user.home";  // Default verzeichniss zum speichern der Demokassen und zum öffnen der anderen FileChoosser
	int UIDFlag=1; // Flag die speichert wie viele UID-Kästchen es gibt.
	public Component frame;
	long timer=0;
	long startimer=0;
	int countimer=0;
	static __Coding code= new __Coding();
	static __ReadFile read= new __ReadFile();
	__ShowDepFileInConsole DepFunction = new __ShowDepFileInConsole();
	__ShowQrFileInConsole QRFunction = new __ShowQrFileInConsole();

	public __MainFrame() {
		try {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
			FrameInit();
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					SaveDropdowns();
				}
			});
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Component initialization.
	 *
	 * @throws java.lang.Exception
	 */
	private void FrameInit() throws Exception {
		// contentPane
		
		Icon warnIcon = new ImageIcon("Eye.png");
		
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		setSize(new Dimension(1000, 800));
		setTitle("Registrierkassen Testsystem");

		// GridLayout
		JPanel CP_CenterGrid = new JPanel(new GridLayout(4, 1));
		contentPane.add(CP_CenterGrid, BorderLayout.CENTER);

		// FlowLayout(Demo erstellen) (1-1)
		JPanel CP_CenterGrid_Flow_1_1 = new JPanel(new BorderLayout());
		JPanel CP_CenterGrid_Flow_1_1_north = new JPanel(new FlowLayout());
		JPanel CP_CenterGrid_Flow_1_1_center = new JPanel(new FlowLayout());
		
		// JTextField to show the select Folder
		StartFolder_selectedFolder = new JComboBox();
		StartFolder_selectedFolder.setEditable(false);
		StartFolder_selectedFolder.setPreferredSize(new Dimension(600, 25));
		NewOrderItemListenerDefaultFolder ChangeListenerDefault = new NewOrderItemListenerDefaultFolder();
		StartFolder_selectedFolder.addActionListener(ChangeListenerDefault);
		CP_CenterGrid_Flow_1_1_north.add(StartFolder_selectedFolder);
		// JButton select Folder
		JButton selectFolder = new JButton();
		selectFolder.setText("Select Folder");
		ChooseDemoFolder searchListen = new ChooseDemoFolder();
		selectFolder.addActionListener(searchListen);
		CP_CenterGrid_Flow_1_1_north.add(selectFolder);
		// JButton Create Demo + List
		JButton createDemo = new JButton();
		createDemo.setText("DemoKassen erstellen");
		CreateDemo DemoList = new CreateDemo();
		createDemo.addActionListener(DemoList);
		CP_CenterGrid_Flow_1_1_north.add(createDemo);
		
		OutputFolder_selectedFolder = new JComboBox();
		OutputFolder_selectedFolder.setEditable(false);
		OutputFolder_selectedFolder.setPreferredSize(new Dimension(600, 25));
		NewOrderItemListener ChangeListenerOut = new NewOrderItemListener();
		OutputFolder_selectedFolder.addActionListener(ChangeListenerOut);
		CP_CenterGrid_Flow_1_1_center.add(OutputFolder_selectedFolder);
		// JButton select Folder
		JButton selectOutput = new JButton();
		selectOutput.setText("Select Output Folder");
		ChooseOutputFolder outputListener = new ChooseOutputFolder();
		selectOutput.addActionListener(outputListener);
		CP_CenterGrid_Flow_1_1_center.add(selectOutput);
		// JButton Create Demo + List
		
		CP_CenterGrid_Flow_1_1.add(CP_CenterGrid_Flow_1_1_center,BorderLayout.CENTER);
		CP_CenterGrid_Flow_1_1.add(CP_CenterGrid_Flow_1_1_north,BorderLayout.NORTH);
		CP_CenterGrid.add(CP_CenterGrid_Flow_1_1);

		// BorderLayout DEP-Export Format Check (1-2)
		JPanel CP_CenterGrid_Border_1_2 = new JPanel(new BorderLayout());
		// NorthPart of the CP_CenterGrid_Border_1_2 (JTextfield+JButton)
		JPanel CP_CenterGrid_Border_1_2_Flow_North = new JPanel(new FlowLayout());
		
		// JTextfield to show Path
		DEP_selectedFolder = new JComboBox();
		DEP_selectedFolder.setEditable(false);
		DEP_selectedFolder.setPreferredSize(new Dimension(600, 25));
		NewOrderItemListener ChangeListenerDEP = new NewOrderItemListener();
		DEP_selectedFolder.addActionListener(ChangeListenerDEP);
		CP_CenterGrid_Border_1_2_Flow_North.add(DEP_selectedFolder);
		
		// JButton select Json
		JButton selectDEPFile = new JButton();
		selectDEPFile.setText("Select JSON DEP-EXPORT-FILE ");
		ChooseDEPFile DEPDemoListener = new ChooseDEPFile();
		selectDEPFile.addActionListener(DEPDemoListener);
		CP_CenterGrid_Border_1_2_Flow_North.add(selectDEPFile);
		JButton Show_DEP = new JButton(warnIcon);
		Show_DEP.setPreferredSize(new Dimension(30, 25));
		OpenDepFileSelected Show_DEP_ss = new OpenDepFileSelected();
		Show_DEP.addActionListener(Show_DEP_ss);
		CP_CenterGrid_Border_1_2_Flow_North.add(Show_DEP);
		// southPart of the CP_CenterGrid_Border_1_2
		// (JTextfield+JButton+JButton)
		JPanel CP_CenterGrid_Border_1_2_Flow_South = new JPanel(new FlowLayout());
		// JButton to Run DEP Test
		JButton DEPTestButton = new JButton();
		DEPTestButton.setText("Run DEP Test");
		RunDepTest DEPTestGoListener = new RunDepTest();
		DEPTestButton.addActionListener(DEPTestGoListener);
		CP_CenterGrid_Border_1_2_Flow_South.add(DEPTestButton);
		//JButton to decypt DEP-FIle and write it to console
		JButton ShowDEP = new JButton();
		ShowDEP.setText("Show DEP-File");
		ShowDepFileInConsole ShowDEP_listen = new ShowDepFileInConsole();
		ShowDEP.addActionListener(ShowDEP_listen);
		CP_CenterGrid_Border_1_2_Flow_South.add(ShowDEP);
		
		//JButton for advanced DEP-Files.
		/*JButton ShowDEP2 = new JButton();
		ShowDEP2.setText("Show adv. DEP-File");
		ShowadvDEP_FileInConsole ShowAdvDEP_listen = new ShowadvDEP_FileInConsole();
		ShowDEP2.addActionListener(ShowAdvDEP_listen);
		CP_CenterGrid_Border_1_2_Flow_South.add(ShowDEP2);*/
		
		FutureCheckboxDEP = new JCheckBox("Belege mit Zukunftsdatum gültig");
		CP_CenterGrid_Border_1_2_Flow_South.add(FutureCheckboxDEP);
		DetailsCheckboxDEP = new JCheckBox("Details");
		CP_CenterGrid_Border_1_2_Flow_South.add(DetailsCheckboxDEP);
		StartvalueCheckboxDEP = new JCheckBox("Startbeleg nicht enthalten");
		CP_CenterGrid_Border_1_2_Flow_South.add(StartvalueCheckboxDEP);
		
		
		// CenterPart of the CP_CenterGrid_Border_1_2
		// (JTextfield+JButton+JButton)
		JPanel CP_CenterGrid_Border_1_2_Flow_Center = new JPanel(new FlowLayout());
		// JButton select Json
		JLabel Platzhalter1=new JLabel("");
		Platzhalter1.setPreferredSize(new Dimension(50,50));
		CP_CenterGrid_Border_1_2.add(Platzhalter1, BorderLayout.NORTH);
		CP_CenterGrid_Border_1_2.add(CP_CenterGrid_Border_1_2_Flow_North, BorderLayout.CENTER);
		CP_CenterGrid_Border_1_2.add(CP_CenterGrid_Border_1_2_Flow_South, BorderLayout.SOUTH);
		CP_CenterGrid.add(CP_CenterGrid_Border_1_2);

		// BorderLayout DEP-Export Format Check (1-3)
		JPanel CP_CenterGrid_Border_1_3 = new JPanel(new BorderLayout());
		// NorthPart of the CP_CenterGrid_Border_1_2 (JTextfield+JButton)
		JPanel CP_CenterGrid_Border_1_3_Flow_North = new JPanel(new FlowLayout());
		// JTextfield to show Path
		QR_selectedFolder = new JComboBox();
		QR_selectedFolder.setEditable(false);
		QR_selectedFolder.setPreferredSize(new Dimension(600, 25));		
		NewOrderItemListener ChangeListenerQR = new NewOrderItemListener();
		QR_selectedFolder.addActionListener(ChangeListenerQR);
		CP_CenterGrid_Border_1_3_Flow_North.add(QR_selectedFolder);
		// JButton select Json
		JButton SelectQRFile = new JButton();
		SelectQRFile.setText("Select JSON QR-CODE-REP-FILE ");
		ChooseQRFile QRFileListener = new ChooseQRFile();
		SelectQRFile.addActionListener(QRFileListener);
		CP_CenterGrid_Border_1_3_Flow_North.add(SelectQRFile);
		
		JButton Show_QR = new JButton(warnIcon);
		Show_QR.setPreferredSize(new Dimension(30, 25));
		OpenQrFileSelected Show_QR_ss = new OpenQrFileSelected();
		Show_QR.addActionListener(Show_QR_ss);
		CP_CenterGrid_Border_1_3_Flow_North.add(Show_QR);
		// (JTextfield+JButton+JButton)
		JPanel CP_CenterGrid_Border_1_3_Flow_Center = new JPanel(new FlowLayout());
		// JTextfield to show Path
		Crypto_selectedFolder = new JComboBox();
		Crypto_selectedFolder.setEditable(false);
		Crypto_selectedFolder.setPreferredSize(new Dimension(600, 25));
		NewOrderItemListener ChangeListenerCrypto = new NewOrderItemListener();
		Crypto_selectedFolder.addActionListener(ChangeListenerCrypto);
		CP_CenterGrid_Border_1_3_Flow_Center.add(Crypto_selectedFolder);
		// JButton select Json

		JButton selectCRYPTOFile2 = new JButton();
		selectCRYPTOFile2.setText("Select JSON CRYPTOGRAPHIC-MATERIAL-FILE ");
		ChooseCryptoFile searchCRYPTO2Listen = new ChooseCryptoFile();
		selectCRYPTOFile2.addActionListener(searchCRYPTO2Listen);
		CP_CenterGrid_Border_1_3_Flow_Center.add(selectCRYPTOFile2);
		// create a Button to show the QR_PDF
		
		JButton Show_Crypto = new JButton(warnIcon);
		Show_Crypto.setPreferredSize(new Dimension(30, 25));
		OpenCryptoFileSelected Show_Crypto_ss = new OpenCryptoFileSelected();
		Show_Crypto.addActionListener(Show_Crypto_ss);
		CP_CenterGrid_Border_1_3_Flow_Center.add(Show_Crypto);
		// southPart of the CP_CenterGrid_Border_1_2
		// (JTextfield+JButton+JButton)
		JPanel CP_CenterGrid_Border_1_3_Flow_South = new JPanel(new FlowLayout());
		JButton QRTestButton = new JButton();
		QRTestButton.setText("Run QR Test");
		RunQRTest QRTestGoListener = new RunQRTest();
		QRTestButton.addActionListener(QRTestGoListener);
		CP_CenterGrid_Border_1_3_Flow_South.add(QRTestButton);
		
		JButton QRTestShow = new JButton();
		QRTestShow.setText("Show QR File");
		ShowQrFileInConsole Show =new ShowQrFileInConsole();
		QRTestShow.addActionListener(Show);
		CP_CenterGrid_Border_1_3_Flow_South.add(QRTestShow);
		
		JButton ShowInput = new JButton();
		ShowInput.setText("Show Single QR-Line");
		ShowSingleQrLine Show_QR_Line =new ShowSingleQrLine();
		ShowInput.addActionListener(Show_QR_Line);
		CP_CenterGrid_Border_1_3_Flow_South.add(ShowInput);
		
		FutureCheckboxQR= new JCheckBox("Belege mit Zukunftsdatum gültig");
		CP_CenterGrid_Border_1_3_Flow_South.add(FutureCheckboxQR);
		DetailsCheckboxQR = new JCheckBox("Details");
		CP_CenterGrid_Border_1_3_Flow_South.add(DetailsCheckboxQR);
		
		
		JLabel Platzhalter2=new JLabel("");
		Platzhalter2.setPreferredSize(new Dimension(50,50));
		CP_CenterGrid_Border_1_3.add(Platzhalter2, BorderLayout.NORTH);
		CP_CenterGrid_Border_1_3.add(CP_CenterGrid_Border_1_3_Flow_North, BorderLayout.CENTER);
		CP_CenterGrid_Border_1_3.add(CP_CenterGrid_Border_1_3_Flow_South, BorderLayout.SOUTH);
		CP_CenterGrid_Flow_1_1.add(CP_CenterGrid_Border_1_3_Flow_Center, BorderLayout.SOUTH);
		CP_CenterGrid.add(CP_CenterGrid_Border_1_3);
		JPanel QRboxsAll = new JPanel(new BorderLayout());

		// new JPanel for FilterBox+QRCode Button
		JPanel QRboxs = new JPanel(new FlowLayout());

		//Button um einen neuen DIalog zu erstellen der KAssenID zu Verkettungswert ändert
		JButton CalcChain = new JButton();
		CalcChain.setText("KassenID Converter");
		CalcChain.setPreferredSize(new Dimension(130, 30));
		ConvertIdToChain ChainConverter = new ConvertIdToChain();
		CalcChain.addActionListener(ChainConverter);
		QRboxs.add(CalcChain);
		
		// Button zum geerieren von einem CryptoFile
		JButton GenACrypto = new JButton();
		GenACrypto.setText("Crypto erstellen");
		GenACrypto.setPreferredSize(new Dimension(130, 30));
		GenerateCryptoFile generateCrypto = new GenerateCryptoFile();
		GenACrypto.addActionListener(generateCrypto);
		QRboxs.add(GenACrypto);

		// create Button to Read QR-Codes
		JButton QRReaderButton = new JButton();
		QRReaderButton.setText("Read QR Files");
		QRReaderButton.setPreferredSize(new Dimension(100, 30));
		QRPdfReader QRListener = new QRPdfReader();
		QRReaderButton.addActionListener(QRListener);
		QRboxs.add(QRReaderButton);

		// create a Button to show the QR_PDF
		
		JButton Show_PDF = new JButton(warnIcon);
		Show_PDF.setPreferredSize(new Dimension(30, 30));
		OpenPdfSelected Show_PDF_ss = new OpenPdfSelected();
		Show_PDF.addActionListener(Show_PDF_ss);
		QRboxs.add(Show_PDF);

		// create Button to Export the Text in the JTextarea
		JButton ExportButton = new JButton();
		ExportButton.setText("Export Lines");
		ExportButton.setPreferredSize(new Dimension(100, 30));
		ExportTextFile exportTxTFile = new ExportTextFile();
		ExportButton.addActionListener(exportTxTFile);

		QRboxs.add(ExportButton);

		// create Checkbox
		Filter = new JCheckBox("Filter all Errors");
		QRboxs.add(Filter);
		ErrorCheckBox CBListener = new ErrorCheckBox();
		Filter.addActionListener(CBListener);

		// create Textarea where Mistakes can be counted
		Anzeige = new JTextPane();
		Anzeige.setEditable(false);
		Anzeige.setBackground(Color.LIGHT_GRAY);
		Anzeige.setPreferredSize(new Dimension(200, 25));
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
		Anzeige.setParagraphAttributes(attribs, true);
		Anzeige.setText("Geprüft:X Fehler:X");
		QRboxs.add(Anzeige);

		QRboxsAll.add(QRboxs, BorderLayout.SOUTH);
		CP_CenterGrid.add(QRboxsAll);

		// Outputarea in Scrollbar (1-4)
		Outputarea = new JTextArea();
		Outputarea.setEditable(false);
		Outputarea.setPreferredSize(new Dimension(350, 1800));
		Outputarea.setLineWrap(true);
		Outputarea.setWrapStyleWord(true);
		Outputarea.setColumns(10);

		ScrollBar = new JScrollPane(Outputarea);
		ScrollBar.setPreferredSize(new Dimension(400, 300));

		contentPane.add(ScrollBar, BorderLayout.SOUTH);

		ReadDropdowns();
	}
	// Listener auf den Button "ShowCRYPTO".
	// der Listener öffnet das File wenn ein geeignetes File auf dem PC gespeichert ist.
	private class OpenCryptoFileSelected implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (Desktop.isDesktopSupported()) {
			    try {
			        File myFile = new File(Crypto_selectedFolder.getSelectedItem().toString());
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			    }
			}
		}
	}
	// Listener auf den Button "ShowQR".
	// der Listener öffnet das File wenn ein geeignetes File auf dem PC gespeichert ist.
	private class OpenQrFileSelected implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (Desktop.isDesktopSupported()) {
			    try {
			        File myFile = new File(QR_selectedFolder.getSelectedItem().toString());
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    }
			}
		}
	}
	// Listener auf den Button "ShowDEP".
	// der Listener öffnet das File wenn ein geeignetes File auf dem PC gespeichert ist.
	private class OpenDepFileSelected implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (Desktop.isDesktopSupported()) {
			    try {
			        File myFile = new File(DEP_selectedFolder.getSelectedItem().toString());
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    }
			}
		}
	}
	
	// Funktion die einen Dialog öffnet in den QR_Lines eingefügen werden können. Diese werden dann entschlüsselt und
	// auf der Console angezeigt. 
	// Ausserdem, werden die Zeilen (oder die Zeile) un ein Separates QR-Lines File gespeichert um sie erneut anzeigen zu können.
	private class ShowSingleQrLine implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Outputarea.setText("Read QR-Line:");
			JDialog Dialog = new JDialog((java.awt.Frame) null, "Input QR-Line", true);
			Dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dialog.setSize(400, 200);
			Dimension frameSize = Dialog.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			Dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
			// Dialog.setLocationRelativeTo(frame);
			Dialog.setLayout(new GridLayout(3, 1));
			JPanel Empty = new JPanel();
			Dialog.add(Empty);
			JPanel InputPanel = new JPanel(new FlowLayout());
			JTextArea Input = new JTextArea();
			Input.setPreferredSize(new Dimension(370,25));
			InputPanel.add(Input);
			
			Dialog.add(InputPanel);
			JPanel ButtonPanel2 = new JPanel(new FlowLayout());
			JButton OK = new JButton("OK");
			JButton Abb = new JButton("Abbrechen");
			Abb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Dialog.dispose();
				}
			});
			
			ButtonPanel2.add(OK);
			ButtonPanel2.add(Abb);
			Dialog.add(ButtonPanel2);
			

			OK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(Input.getText().toString().length()<=0){
						Outputarea.append(" No Input!");
					}
					else{
						DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
						Date date = new Date();
						String InputText = Input.getText().toString();
						String save="[\""+InputText+"\"]";
						if (OutputFolder_selectedFolder.getSelectedItem()!=null) {
							try (PrintWriter out = new PrintWriter(OutputFolder_selectedFolder.getSelectedItem().toString()
									+ "/QR_Line_File_at" + dateFormat.format(date) + ".json")) {
								out.println(save);
								QR_selectedFolder.addItem(OutputFolder_selectedFolder.getSelectedItem().toString()
										+ "/QR_Line_File_at" + dateFormat.format(date) + ".json");
								QR_selectedFolder.setSelectedItem(OutputFolder_selectedFolder.getSelectedItem().toString()
										+ "/QR_Line_File_at" + dateFormat.format(date) + ".json");
								if (QR_selectedFolder.getItemCount() > 5) {
									QR_selectedFolder.removeItemAt(0);
								}

							} catch (FileNotFoundException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}else{
							try (PrintWriter out = new PrintWriter(DefaultOutput
									+ "/QR_Line_File_at" + dateFormat.format(date) + ".json")) {
								out.println(save);
								QR_selectedFolder.addItem(DefaultOutput
										+ "/QR_Line_File_at" + dateFormat.format(date) + ".json");
								QR_selectedFolder.setSelectedItem(DefaultOutput
										+ "/QR_Line_File_at" + dateFormat.format(date) + ".json");
								if (QR_selectedFolder.getItemCount() > 5) {
									QR_selectedFolder.removeItemAt(0);
								}

							} catch (FileNotFoundException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}
						
						String[] parts2 = InputText.split("_");
						int Flag2 = 0;
						String KassenID = "";
						String BelegID = "";
						while (Flag2 < parts2.length) {
							if (Flag2 == 2) {
								KassenID = parts2[Flag2];
							}
							if (Flag2 == 3) {
								BelegID = parts2[Flag2];
							}
							if (Flag2 == 10) {
								long i1=0;
								Outputarea.append("Stand-Umsatz-Zaehler-AES256-ICM_Verschlüsselt: "+ parts2[Flag2] + "   \r\n");
								
								// Abfrage ob STO oder TRA (Trainings beleg oder
								// Storno Beleg)
								// oder Umsatz wert
								if (parts2[Flag2].equals("U1RP")) {
									String d = "STO";
									Outputarea.append(QR_Code_Titels[Flag2] + "  " + d + "   \r\n");
								} else if (parts2[Flag2].equals("VFJB")) {
									String d = "TRA";
									Outputarea.append(QR_Code_Titels[Flag2] + "  " + d + "  \r\n");
								} else {
									try {
										i1 = code.CalcNewValue(KassenID, BelegID, parts2[Flag2],Crypto_selectedFolder.getSelectedItem().toString());
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									double d = (double) i1;
									d = d / 100;
									Outputarea.append(QR_Code_Titels[Flag2] + "  " + d + "€   \r\n");
								}
							}else {
								Outputarea.append(QR_Code_Titels[Flag2] + " " + parts2[Flag2] + "\r\n");
								Outputarea.update(Outputarea.getGraphics());
							}
							Flag2++;
							Outputarea.setRows(Outputarea.getRows()+1);
						}
					}
					Dialog.dispose();
				}
			});
			Dialog.setVisible(true);
		}
	}
	
	
	// Listener der auf den Button "Show DEP-File" hört. Das DEP File wird aufgeteilt. 
	// Der passende Bereich wird Baser64 decodiert dann bekommt man einen Code wie bei einem QR-Code.
	// dieser Code wird auf die selbe weise entschlüsselt wie es auch schon beim QR-decoder der Fall ist.
	// und danach auf der Console im Programm ausgegeben.
	private class ShowDepFileInConsole implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//showDepFileinConsole
		    
		
			Outputarea.setText("Show DEP-File: ");
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			
			String outputDepFile="";
			try {
				outputDepFile = DepFunction.show(DEP_selectedFolder.getSelectedItem().toString(), Crypto_selectedFolder.getSelectedItem().toString(),StartvalueCheckboxDEP.isSelected());
			} catch (ParseException e) {
				outputDepFile="Error while Parsing Date!";
				e.printStackTrace();
			}
			//int count =  // commons-lang3-3.6.jar in /lib
			Outputarea.setRows((StringUtils.countMatches(outputDepFile, "\r\n")+10));
			Outputarea.setText(outputDepFile);
			
			Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(DefCursor);
		}
	}
	
	// Listener der ein ausgwähltes QR_Json_File ausliest und in der Console anzeigt
	// ausserdem wird der Umsatzzähler entschlüsselt!
	// egal ob ein QR-File das von PDF oder von Json ausgelesen wurde
	private class ShowQrFileInConsole implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Outputarea.setText("Show QR-File: ");
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			
			String outputQrFile =QRFunction.show(QR_selectedFolder.getSelectedItem().toString(), Crypto_selectedFolder.getSelectedItem().toString());
			int count = StringUtils.countMatches(outputQrFile, "\r\n"); // commons-lang3-3.6.jar in /lib
			Outputarea.setRows(count+10);
			Outputarea.setText(outputQrFile);
			
			Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(DefCursor);
		}
	}
	// Listener die das zuletzt ausgewählte PDF anzeigt wenn ein PDf reader am PC installiert ist
	private class OpenPdfSelected implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (Desktop.isDesktopSupported()) {
			    try {
			        File myFile = new File(PDF_Mem);
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    }
			}
		}
	}

	// Listener der ein JDialog aufruft um die Inputdaten für ein Cryptofile
	// zu bekommen
	// ein Json File unter dem defaultOrdner ertsellt
	// Es wird ein Dialog erstellt. Durch den "+" Button auf dem Dialog können neue Inputfelder eingefügt werden
	// Wird auf "ok" geklickt wird aus den Input daten ein Crypto File erstellt und unter dem Default Ordner abgespeichert
	// und auch sofort ausgewählt.
	private class GenerateCryptoFile implements ActionListener {

		@SuppressWarnings("static-access")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			ArrayList<JTextField> TextFieldArray_IDs = new ArrayList<JTextField>();
			ArrayList<JTextField> TextFieldArray_Keys = new ArrayList<JTextField>();
			ArrayList<JLabel> JLabel_IDs = new ArrayList<JLabel>();
			ArrayList<JLabel> JLabel_Keys = new ArrayList<JLabel>();
			
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
			Date date = new Date();
			Format="PUBLIC_KEY";
			JTextField base64AESKey = new JTextField();
			base64AESKey.setPreferredSize(new Dimension(350, 25));
			JPanel base64AESKey_Panel = new JPanel(new FlowLayout());
			base64AESKey_Panel.add(base64AESKey);

			JTextField PublicKey_k0 = new JTextField();



			JTextField PublicKey_k0_id = new JTextField();
	


			JLabel BaseKey = new JLabel("   BaseKey:");
			JLabel Input1_Key = new JLabel("   UID-1:");
			JLabel Input1 = new JLabel("   Key-1:");

			JDialog Dialog = new JDialog((java.awt.Frame) null, "Create a Crypto File", true);
			Dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dialog.setSize(400, 600);
			Dimension frameSize = Dialog.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			Dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
			// Dialog.setLocationRelativeTo(frame);
			Dialog.setLayout(new BorderLayout());
			JPanel TopPanel = new JPanel(new FlowLayout());
			JPanel BotPanel = new JPanel(new FlowLayout());
			JPanel CenterPanel = new JPanel(new GridLayout(19,1));
			Dialog.add(TopPanel,BorderLayout.NORTH);
			Dialog.add(CenterPanel,BorderLayout.CENTER);
			Dialog.add(BotPanel,BorderLayout.SOUTH);
			
			
			// label with original font
			JPanel ButtonPanel = new JPanel(new FlowLayout());
			JButton Public_Key = new JButton("Public-Key");

			Public_Key.setSelected(true);
			JButton Certificate = new JButton("Certificate");

			Certificate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Public_Key.setSelected(false);
					Certificate.setSelected(true);
					Format = "CERTIFICATE";
					Input1.setText("   Zertifikat1:");
					Input1_Key.setText("   Zertifikat1-ID:");
					for(int i =0, j=2; i < JLabel_IDs.size(); i++,j++) {
						JLabel_IDs.get(i).setText("   Zertifikat"+j+"-ID:");
						JLabel_Keys.get(i).setText("   Zertifikat"+j+":");
						}
				}
			});

			Public_Key.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Public_Key.setSelected(true);
					Certificate.setSelected(false);
					Format = "PUBLIC_KEY";
					Input1.setText("   Key1:");
					Input1_Key.setText("   UID-1:");
					for(int i =0,j=2; i < JLabel_IDs.size(); i++,j++) {
						JLabel_IDs.get(i).setText("   UID-"+j+":");
						JLabel_Keys.get(i).setText("   Key"+j+":");
						}
				}
			});

			ButtonPanel.add(Public_Key);
			ButtonPanel.add(Certificate);
			TopPanel.add(ButtonPanel);

			CenterPanel.add(BaseKey);
			CenterPanel.add(base64AESKey_Panel);
			CenterPanel.add(Input1_Key);
			CenterPanel.add(PublicKey_k0_id);
			CenterPanel.add(Input1);
			CenterPanel.add(PublicKey_k0);

			JPanel ButtonPanel2 = new JPanel(new FlowLayout());
			JButton OK = new JButton("OK");
			JButton Abb = new JButton("Abbrechen");
			Abb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					UIDFlag=1;
					Dialog.dispose();
				}
			});
			JButton Plus = new JButton("+");
			Plus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					UIDFlag++;
					JLabel UIDLabel = new JLabel("   UID-"+UIDFlag+":");
					JLabel_IDs.add(UIDLabel);
					JLabel KeyLabel = new JLabel("   Key"+UIDFlag+":");
					JLabel_Keys.add(KeyLabel);
					JTextField AA = new JTextField();
					AA.setPreferredSize(new Dimension(350, 25));
					JTextField BB = new JTextField();
					BB.setPreferredSize(new Dimension(350, 25));
					TextFieldArray_IDs.add(AA);
					TextFieldArray_Keys.add(BB);
					for(int i =0; i < JLabel_IDs.size(); i++) {
						JLabel Y =JLabel_IDs.get(i);
						JLabel X =JLabel_Keys.get(i);
						JTextField A=TextFieldArray_IDs.get(i);
						JTextField B=TextFieldArray_Keys.get(i);
						CenterPanel.add(Y);
						CenterPanel.add(A);
						CenterPanel.add(X);
						CenterPanel.add(B);
						}
									
		            Dialog.revalidate();
		            Dialog.repaint();
				}
			});
			ButtonPanel2.add(OK);
			ButtonPanel2.add(Abb);
			ButtonPanel2.add(Plus);
			BotPanel.add(ButtonPanel2);
			

			OK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					UIDFlag=1;
					if (base64AESKey.getText().length()>0&&PublicKey_k0_id.getText().length()>0&&PublicKey_k0.getText().length()>0&&PublicKey_k0_id.getText().length()>0) {
						if(OutputFolder_selectedFolder.getSelectedItem()!=null){
						try (PrintWriter out = new PrintWriter(
								OutputFolder_selectedFolder.getSelectedItem().toString()+"/CryptoFile_at" + dateFormat.format(date) + ".json")) {

							String finalString = "{\"base64AESKey\": \"" + base64AESKey.getText()
									+ "\",\"certificateOrPublicKeyMap\": {\"" + PublicKey_k0_id.getText() + "\": {\"id\": \""
									+ PublicKey_k0_id.getText()
									+ "\",\"signatureDeviceType\": \""+Format+"\",\"signatureCertificateOrPublicKey\":\""
									+ PublicKey_k0.getText() + "\"}";
							
							for(int i =0; i < JLabel_IDs.size(); i++) {
								JTextField A=TextFieldArray_IDs.get(i);
								JTextField B=TextFieldArray_Keys.get(i);
								finalString=finalString+",\"" + TextFieldArray_IDs.get(i).getText() + "\": {\"id\": \""
										+ TextFieldArray_IDs.get(i).getText()
										+ "\",\"signatureDeviceType\": \""+Format+"\",\"signatureCertificateOrPublicKey\":\""
										+ TextFieldArray_Keys.get(i).getText() + "\"}";
								
								}
							finalString=finalString+"}}";
							out.println(finalString);

							Crypto_selectedFolder
									.addItem(OutputFolder_selectedFolder.getSelectedItem().toString()+"/CryptoFile_at" + dateFormat.format(date) + ".json");
							Crypto_selectedFolder
									.setSelectedItem(OutputFolder_selectedFolder.getSelectedItem().toString()+"/CryptoFile_at" + dateFormat.format(date) + ".json");
							if (Crypto_selectedFolder.getItemCount() > 5) {
								Crypto_selectedFolder.removeItemAt(0);
							}

						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						}else{
							try (PrintWriter out = new PrintWriter(
									DefaultOutput+"/CryptoFile_at" + dateFormat.format(date) + ".json")) {

								String finalString = "{\"base64AESKey\": \"" + base64AESKey.getText()
										+ "\",\"certificateOrPublicKeyMap\": {\"" + PublicKey_k0_id.getText() + "\": {\"id\": \""
										+ PublicKey_k0_id.getText()
										+ "\",\"signatureDeviceType\": \""+Format+"\",\"signatureCertificateOrPublicKey\":\""
										+ PublicKey_k0.getText() + "\"}";
								
								for(int i =0; i < JLabel_IDs.size(); i++) {
									JTextField A=TextFieldArray_IDs.get(i);
									JTextField B=TextFieldArray_Keys.get(i);
									finalString=finalString+",\"" + TextFieldArray_IDs.get(i).getText() + "\": {\"id\": \""
											+ TextFieldArray_IDs.get(i).getText()
											+ "\",\"signatureDeviceType\": \""+Format+"\",\"signatureCertificateOrPublicKey\":\""
											+ TextFieldArray_Keys.get(i).getText() + "\"}";
									
									}
								finalString=finalString+"}}";
								out.println(finalString);

								Crypto_selectedFolder
										.addItem(DefaultOutput+"/CryptoFile_at" + dateFormat.format(date) + ".json");
								Crypto_selectedFolder
										.setSelectedItem(DefaultOutput+"/CryptoFile_at" + dateFormat.format(date) + ".json");
								if (Crypto_selectedFolder.getItemCount() > 5) {
									Crypto_selectedFolder.removeItemAt(0);
								}

							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}else{
						System.out.println("pls write somehting in, next Time !");
					}
					Dialog.dispose();
				}
			});
			Dialog.setVisible(true);
			UIDFlag=1;

		}
	}

	// Listerner der ein TxT file erstellt und die Daten die in der JTextArea
	// drinnen stehen dorthinein speichert
	// dem Textfile werden verschiedene Namen gegeben jenachdem was in der
	// Textarea drinnen steht. oder der user kann dem File selber einen Namen geben in dem 
	// einen "." mit in den Filedialog schriebt. (bsp: /Desktop/File.txt) <-- wir auf den Punkt überprüft
	// was dazu geschrieben wird steht in dem String : "WahtIsInTextArea"
	// zusätzlich wird das Datum vergeben
	// alles mit _ getrennt
	private class ExportTextFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// öffnet einen Dialog um die Files auszuwählen
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
			Date date = new Date();
			JFrame Dialog = new JFrame();

			JFileChooser fileChooser1 = new JFileChooser(new File(defaultFolerOpen));
			fileChooser1.setAcceptAllFileFilterUsed(false);
			fileChooser1.setDialogTitle("Wählen Sie einen geigneten Speicherplatz aus :");
			fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = fileChooser1.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				
				// wenn der Listener aufgerufen wird dann wird der Mausizeiger
				// auf ein "Warte Symbol" (Lade Kreis) geändert
				// Wird am Ende des Listeners wieder zurückgesetzt.
				Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(hourglassCursor);

				File selectedFile = fileChooser1.getSelectedFile();
				
				try {
					if(selectedFile.toString().contains(".")){
						try (PrintWriter out = new PrintWriter(selectedFile.getAbsolutePath())) {
							out.println(Outputarea.getText());
						}
					}else{
						try (PrintWriter out = new PrintWriter(selectedFile.getAbsolutePath() + "/Export_of_"+ WahtIsInTextArea + "_am_" + dateFormat.format(date) + ".txt");) {
							out.println(Outputarea.getText());
						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(DefCursor);
			}
		}
	}
	
	// Listerner der einen File Dialog öffnen soll wo dann ein QR PDF ausgewählt
	// werden kann
	// Danach wird das PDF in eni JPG umgewandelt und anschließend wird der
	// QRCode ausgelesen
	// am SCHluss wird das JPG wieder gelöscht
	// zum schluss wird das File in ein Json File gespeichert das in dem Ordner
	// "Saved_Files" unterbracht wird (ProjektOrdner)
	// Ausgegeben werden die Daten so das man die Daten auch gut ablesen kann
	// (also mit Bezeichnung) gespeichert werden die Daten in der
	// gleiche Form wie das QR-file das man aus der Demokasse auswählen kann

	private class QRPdfReader implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (Crypto_selectedFolder.getSelectedItem() != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
				Date date = new Date();
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("[");
				Outputarea.setRows(5);
				// öffnet einen Dialog um die Files auszuwählen (auch mehrere)
				WahtIsInTextArea = "PDF_QRCodes";
				ErrorLog_Flag = "Nothing";
				String KassenID=""; // String zum speichern der Kassen ID
				String BelegID=""; // String zum speicher der BelegID
				JFrame Dialog = new JFrame();
				JFileChooser fileChooser2 = new JFileChooser(new File(defaultFolerOpen));
				fileChooser2.setAcceptAllFileFilterUsed(true);
				fileChooser2.setMultiSelectionEnabled(true);
				fileChooser2.setDialogTitle("Wählen Sie PDFS aus:");
				int result = fileChooser2.showOpenDialog(Dialog);
				Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(hourglassCursor);
				// wenn das Öffnen erfolgreich war dann auslesen :
				if (result == JFileChooser.APPROVE_OPTION) {
					File[] files = fileChooser2.getSelectedFiles();			
					PDF_Mem = fileChooser2.getSelectedFile().getAbsolutePath();
					ArrayList<String> SortList = new ArrayList<String>();
					//sortiere die Filenamen
					for(int i=0; i<(files.length); i++){
						SortList.add(files[i].toString());
					}
					Collections.sort(SortList, new NaturalOrderComparator());
					ArrayList<String> Name = new ArrayList<String>();
					//sortiere die Filenamen
					for(int i=0; i<(files.length); i++){
						Name.add(files[i].getName());
					}
					Collections.sort(Name, new NaturalOrderComparator());
					int counter = files.length;
					counter--;
					int ReadCounterMax = counter;
					// Für jedes File das Ausgelesen wurde muss ein PNg erstellt
					// werden
					while (counter >= 0) {
						try {
							createImage(SortList.get(counter), counter);
						} catch (IOException e) {
							//TODO Auto-generated catch block
							e.printStackTrace();
						}
						counter--;
					}
					// wenn der Listener aufgerufen wird dann wird der
					// Mausizeiger auf ein "Warte Symbol" (Lade Kreis) geändert
					// Wird am Ende des Listeners wieder zurückgesetzt.
					
					
					Outputarea.setText("");
					// für jedes PNG das erstellt wurde muss nun der WR code
					// ausgelesen werden und auf die Textarea geschrieben werden
					// Gelöscht werden die Bilder in der Unterfunktion QrReader

					while (ReadCounterMax >= 0) {
						String charset = "UTF-8"; // or "ISO-8859-1"
						Map hintMap = new HashMap();
						hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

						String filePath = "Pic_" + ReadCounterMax + ".png";
						//String filePath = "Pic_1.png";
						try {
							String OutputString = readQRCode(filePath, charset, hintMap);
							int whileLoop = 0;
							stringBuilder.append("\"" + OutputString + "\", ");
							Outputarea.append("\r\n" + Name.get(ReadCounterMax) + ":");
							Outputarea.setRows(Outputarea.getRows() + 1);
							
							//Ausgelsense Daten werden nach jedem "_" gespilted
							// nach dem 2 und 3 kommen wichtige Werte die wir uns mekren müssen (zum Entschlüsseln)
							// der 10 Wert wird nicht ausgegeben sondern in € entschlüsselt (in cent --> /100 =€)
							
							for (String retval : OutputString.split("_")) {
								System.out.println(QR_Code_Titels[whileLoop] + " " + retval);
								if (whileLoop == 2) {
									KassenID=retval;
								}
								if (whileLoop == 3) {
									BelegID=retval;
								}
								if (whileLoop == 10) {	
									long i1=0;
									i1=code.CalcNewValue(KassenID,BelegID,retval,Crypto_selectedFolder.getSelectedItem().toString());
									// Abfrage ob STO oder TRA (Trainings beleg oder Storno Beleg)
									// oder Umsatz wert
									if(i1==98989898){
										String d="STO";
										Outputarea.append(QR_Code_Titels[whileLoop]+"  " + d +"   \r\n");
									}else if(i1==97979797){
										String d="TRA";
										Outputarea.append(QR_Code_Titels[whileLoop]+"  " + d +"  \r\n");
									}else{
										double d = (double)i1;
										d=d/100;
										Outputarea.append(QR_Code_Titels[whileLoop]+"  " + d +"€   \r\n");
									}
								} else {
									Outputarea.append(QR_Code_Titels[whileLoop] + " " + retval + "\r\n");
								}
								Outputarea.setRows(Outputarea.getRows() + 1);
								Outputarea.update(Outputarea.getGraphics());
								whileLoop++;
							}
							Outputarea.append("\n");
							Outputarea.setRows(Outputarea.getRows() + 1);

						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ReadCounterMax--;
					}
					Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(DefCursor);

					try (PrintWriter out = new PrintWriter(
							"Saved_Files/QR_Files_at" + dateFormat.format(date) + ".json")) {
						QR_selectedFolder.addItem("Saved_Files/QR_Files_at" + dateFormat.format(date) + ".json");
						QR_selectedFolder
								.setSelectedItem("Saved_Files/QR_Files_at" + dateFormat.format(date) + ".json");
						if (QR_selectedFolder.getItemCount() > 5) {
							QR_selectedFolder.removeItemAt(0);
						}
						DefaultStringQR = "Saved_Files/QR_Files_at" + dateFormat.format(date) + ".json";
						if(stringBuilder.length()>2){
							stringBuilder.setLength(stringBuilder.length() - 2);
							System.out.println("Something went wrong!");
						}
						stringBuilder.append("]");
						String finalString = stringBuilder.toString();
						out.println(finalString);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("eeeeeee");
					}
				}
				Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(DefCursor);
			}
		}

	}

	// Listerner der überprüfen soll ob es Fehlerhafte blöcke gibt wenn die
	// Checkbox angekreuzt wird.
	// sucht nach "+++++++++++++" --> da das immer der Start von einem Block ist
	// erstellt einen Prüfstring von einem ++++++++ zum andern +++++ und
	// überprüft ob dieser den Wert "Fehlerhaft" besitzt
	// sollte in dem Block das Wort "FEHLERHAFT" drinnen sein wird eine Flag
	// gesetzt "Prüfer" und bei gesetzter Flag wird
	// der Block in den String FehlerText hinzugefügt.
	// beim letzten Block angekommen wird der String Fehlertext auf die Textarea
	// hinausgeschrieben.
	// bei deaktivieren der Checkox wird die CheckboxFlag wieder auf 0 gestellt
	// und der Refferenztext wieder auf die
	// Textarea hinausgeschrieben
	private class ErrorCheckBox implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			// wenn der Listener aufgerufen wird dann wird der Mausizeiger auf
			// ein "Warte Symbol" (Lade Kreis) geändert
			// Wird am Ende des Listeners wieder zurückgesetzt.
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);

			if (Filter.isSelected()) {
				Old_Rows = Outputarea.getRows();
				Outputarea.setRows(5);
				WahtIsInTextArea = "ErrorLog_of_____" + ErrorLog_Flag;
				int Geprüft = 0; // anzahl an Geprüften Blöcken
				int Falsch = 0; // anzahl der falschen Blöcken
				FehlerTextFlag = 1; // Flag die signaliesiert ob die Checkbox
									// "Fehlertext anzeigen" angehakt ist oder
									// nicht
				Refferenztext = Outputarea.getText(); // der zu Prüfende Text
				Prüftext = ""; // einzelner Block der zu Prüfen ist
				FehlerText = "Fehler gefunden in Step 1 :\r\n"; 
				int index = Refferenztext.indexOf("Machine readable code validation #");
				int CounterPrüf=0;
				int index2=0;
				int index3=0;
				int Fehler=0;
				while(index>=0){
					CounterPrüf++;
					index2=Refferenztext.indexOf("Machine readable code validation #", index + 1);
					if (index2 != -1) {
						Prüftext = Refferenztext.substring(index, index2);
						index3= Prüftext.indexOf("FAIL");
						if(index3!=-1){
							Fehler++;
							FehlerText=FehlerText+Prüftext+"\r\n";
							FehlerText=FehlerText+"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\r\n";
						}
					}
					index=index2;
				}
				FehlerText=FehlerText+"-------------------------------------------------------------------------------\r\n";
				FehlerText=FehlerText+"Fehler gefunden in Step 2 :\r\n";
				index = Refferenztext.indexOf("RKSV-DEP-EXPORT-validation #");
				index2=0;
				index3=0;
				while(index>=0){
					CounterPrüf++;
					index2=Refferenztext.indexOf("RKSV-DEP-EXPORT-validation #", index + 1);
					if (index2 != -1) {
						Prüftext = Refferenztext.substring(index, index2);
						index3= Prüftext.indexOf("FAIL");
						if(index3!=-1){
							Fehler++;
							FehlerText=FehlerText+Prüftext+"\r\n";
							FehlerText=FehlerText+"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\r\n";
						}
					}
					index=index2;
				}
				String[] lines = FehlerText.split("\r\n|\r|\n");
				Outputarea.setRows(Outputarea.getRows() + lines.length + 6);
				Outputarea.setText(FehlerText);
				ScrollBar.getVerticalScrollBar().getMinimum();
				Anzeige.setText("Geprüft:" + (CounterPrüf) + " Fehler:" + Fehler);	
			}
																			
				
			// wenn Filter wieder ausgeschlaten wird Reffferenztext wieder auf
			// TextArea schreiben
			if (!Filter.isSelected()) {
				Outputarea.setRows(Old_Rows);
				FehlerTextFlag = 0;
				Outputarea.setText(Refferenztext);
				Anzeige.setText("Geprüft:X Fehler:X");
			}
			Outputarea.setCaretPosition(0);
			Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(DefCursor);
		}

	}

	// Listener auf den Button der Zustänig dafür ist die Demokassen zu
	// erstellen
	// Wenn er ausgeführt wird wird ein cmd Befehl ausgeführt -
	// egkassen-demo-0.7.jar mit DefaultString als Speicherort
	private class CreateDemo implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Outputarea.setRows(5);
			WahtIsInTextArea = "DemoKassenerstellung";
			ErrorLog_Flag = "Nothing";
			if (StartFolder_selectedFolder.getSelectedItem() != null) {
				Outputarea.setText("Erstelle Demo Kassen\r\n");
				DefaultString = StartFolder_selectedFolder.getSelectedItem().toString();
				Outputarea.append("Writing to : " + DefaultString + "\r\n");

				// wenn der Listener aufgerufen wird dann wird der Mausizeiger
				// auf ein "Warte Symbol" (Lade Kreis) geändert
				// Wird am Ende des Listeners wieder zurückgesetzt.
				Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(hourglassCursor);

				Runtime runtime = Runtime.getRuntime();
				Process process = null;
				try {
					process = runtime.exec("java -jar regkassen-demo-1.0.0.jar -o " + DefaultString + " -c");
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // you might need the full path
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;
				try {
					while ((line = br.readLine()) != null) {
						Outputarea.append(line + "\r\n");
						Outputarea.update(Outputarea.getGraphics());
						Outputarea.setCaretPosition(Outputarea.getText().length() - 1);
						Outputarea.setRows(Outputarea.getRows() + 1);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Outputarea.setCaretPosition(0);
				
			} else {
				Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(DefCursor);
				Outputarea.append("Nothing selected!!");
				
			}
			Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(DefCursor);
		}

	}

	// Listener auf den Button der Zustänig dafür ist die DEFile-Test zu
	// erstellen
	// der DEP Test wird über die komandozeile aufgerufen. danach wird jede einzelne Zeile die die Komandozeile ausgibt überprüft
	// und sie wird in die TextArea ausgegeben
	private class RunDepTest implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			WahtIsInTextArea = "DEP_TestResults";
			ErrorLog_Flag = "DEP_TestResults";
			Filter.setSelected(false);
			FehlerTextFlag = 0;
			Outputarea.setRows(5);
			Outputarea.setText("DEPTest : \r\n");
			// wenn der Listener aufgerufen wird dann wird der Mausizeiger auf
			// ein "Warte Symbol" (Lade Kreis) geändert
			// Wird am Ende des Listeners wieder zurückgesetzt.
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			if(OutputFolder_selectedFolder.getSelectedItem()!=null){
				if (DEP_selectedFolder.getSelectedItem() != null && Crypto_selectedFolder.getSelectedItem() != null) {
					String outputDepFile =DepFunction.runDepTest(DEP_selectedFolder.getSelectedItem().toString(), Crypto_selectedFolder.getSelectedItem().toString(), FutureCheckboxDEP.isSelected(), OutputFolder_selectedFolder.getSelectedItem().toString(),DetailsCheckboxDEP.isSelected());
					Outputarea.setRows((StringUtils.countMatches(outputDepFile, "\r\n")+10));
					Outputarea.setText(outputDepFile);
				} else {
					Outputarea.append("DEP or Crypto is missing ! ");
				}	
			}else{
				Outputarea.append("NO OUTPUT - FOLDER !");
			}
			
			Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(DefCursor);
		}

	}

	// Listener auf den Button der Zustänig dafür ist die QRFile-Test zu
	// erstellen
	// der QR-Test wird über die Kommando zeile ausgeführt
	// danach wird  die Antwort der Komandozeile ausgelesen und jede einzelene Zeile auf die Textarea geschrieben
	private class RunQRTest implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			WahtIsInTextArea = "QR_TestResults";
			ErrorLog_Flag = "QR_TestResults";
			Filter.setSelected(false);
			FehlerTextFlag = 0;
			Outputarea.setRows(5);
			Outputarea.setText("QR-FileTest : \r\n");

			// wenn der Listener aufgerufen wird dann wird der Mausizeiger auf
			// ein "Warte Symbol" (Lade Kreis) geändert
			// Wird am Ende des Listeners wieder zurückgesetzt.
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			if(OutputFolder_selectedFolder.getSelectedItem()!=null){
				if (QR_selectedFolder.getSelectedItem() != null&& Crypto_selectedFolder.getSelectedItem() != null) {
					String outputDepFile =QRFunction.runQRTest(QR_selectedFolder.getSelectedItem().toString(), Crypto_selectedFolder.getSelectedItem().toString(), FutureCheckboxQR.isSelected(), OutputFolder_selectedFolder.getSelectedItem().toString(),DetailsCheckboxQR.isSelected());
					Outputarea.setRows((StringUtils.countMatches(outputDepFile, "\r\n")+10));
					Outputarea.setText(outputDepFile);
				} else {
					Outputarea.append("Nothing selected!!");
				
				}
			}else{
				Outputarea.append("NO OUTPUT - FOLDER !");
			}
			Cursor DefCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(DefCursor);
		}

	}

	// Listener auf den Button der Zustänig dafür ist die Demokassen speicherOrt
	// ordner auszuwählen.
	// ACHTUNG dieser speicherplattz ist auch der Default speicherplatz ! 
	private class ChooseDemoFolder implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			JFrame Dialog = new JFrame();
			JFileChooser fileChooser1 = new JFileChooser(new File(defaultFolerOpen));
			fileChooser1.setAcceptAllFileFilterUsed(false);
			fileChooser1.setDialogTitle("Wählen Sie einen Ordner aus wo die Demoversion gespeichert werden kann :");
			fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int result = fileChooser1.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser1.getSelectedFile();
				if (((DefaultComboBoxModel) StartFolder_selectedFolder.getModel()).getIndexOf("" + selectedFile.getAbsolutePath() + "") == -1) {
					StartFolder_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					StartFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					if (StartFolder_selectedFolder.getItemCount() > 10) {
						StartFolder_selectedFolder.removeItemAt(11);
					}
					if(Files.notExists(Paths.get(selectedFile.getAbsolutePath()+"\\output"))){
						try {
							Files.createDirectory(Paths.get(selectedFile.getAbsolutePath()+"\\output"));
						} catch (IOException e) {
							System.out.println("couldn't create output Folder !");
							e.printStackTrace();
						}
					}
					
					if (((DefaultComboBoxModel) OutputFolder_selectedFolder.getModel()).getIndexOf("" + selectedFile.getAbsolutePath() + "\\output") == -1) {
						OutputFolder_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "\\output",0);
						OutputFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "\\output");
						if (OutputFolder_selectedFolder.getItemCount() > 10) {
							OutputFolder_selectedFolder.removeItemAt(11);
						}
					} else {
						OutputFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "\\output");
						int index = OutputFolder_selectedFolder.getSelectedIndex();
						OutputFolder_selectedFolder.removeItemAt(index);
						OutputFolder_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "\\output",0);
						OutputFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "\\output");
					}
					
				} else {
					StartFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					int index = StartFolder_selectedFolder.getSelectedIndex();
					StartFolder_selectedFolder.removeItemAt(index);
					StartFolder_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					StartFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
				}
				DefaultString = selectedFile.getAbsolutePath();
				defaultFolerOpen=selectedFile.getAbsolutePath();
			}
			;

		}

	}
	// Listener mit dem man den Speicherort für die Output files bestimmen kann.
	// öffnet den Default Folder
	private class ChooseOutputFolder implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			JFrame Dialog = new JFrame();
			JFileChooser fileChooser1 = new JFileChooser(new File(defaultFolerOpen));
			fileChooser1.setAcceptAllFileFilterUsed(false);
			fileChooser1.setDialogTitle("Wählen Sie einen Ordner aus wo die OutputFiles gespeichert werden sollen :");
			fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = fileChooser1.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser1.getSelectedFile();
				// Hier wird zuerst überprüft ob es besagtes Item schon in der
				// JComboBox gibt
				if (((DefaultComboBoxModel) OutputFolder_selectedFolder.getModel()).getIndexOf("" + selectedFile.getAbsolutePath() + "") == -1) {
					OutputFolder_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					OutputFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					if (OutputFolder_selectedFolder.getItemCount() > 10) {
						OutputFolder_selectedFolder.removeItemAt(11);
					}
				} else {
					OutputFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					int index = OutputFolder_selectedFolder.getSelectedIndex();
					OutputFolder_selectedFolder.removeItemAt(index);
					OutputFolder_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					OutputFolder_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
				}
			}
		}

	}

	// Listener auf den Button der Zustänig dafür ist die DEPFILE speicherOrt
	// ordner auszuwählen
	private class ChooseDEPFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			JFrame Dialog = new JFrame();
			JFileChooser fileChooser2 = new JFileChooser(new File(defaultFolerOpen));
			fileChooser2.setAcceptAllFileFilterUsed(true);
			fileChooser2.setDialogTitle("Wählen Sie ein DEP-File zur überprüfung aus:");
			int result = fileChooser2.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser2.getSelectedFile();
				if (((DefaultComboBoxModel) DEP_selectedFolder.getModel()).getIndexOf("" + selectedFile.getAbsolutePath() + "") == -1) {
					DEP_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					DEP_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					if (DEP_selectedFolder.getItemCount() > 10) {
						DEP_selectedFolder.removeItemAt(11);
					}
				} else {
					DEP_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					int index = DEP_selectedFolder.getSelectedIndex();
					DEP_selectedFolder.removeItemAt(index);
					DEP_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					DEP_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
				}
				DefaultStringDEP = selectedFile.getAbsolutePath();
			}
			;

		}

	}

	// Listener auf den Button der Zustänig dafür ist die CRYPTOFILE2
	// speicherOrt ordner auszuwählen
	private class ChooseCryptoFile implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {

			JFrame Dialog = new JFrame();
			JFileChooser fileChooser2 = new JFileChooser(new File(defaultFolerOpen));
			fileChooser2.setAcceptAllFileFilterUsed(true);
			fileChooser2.setDialogTitle("Wählen Sie ein Crypto File zu überprüfung aus:");
			int result = fileChooser2.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser2.getSelectedFile();
				if (((DefaultComboBoxModel) Crypto_selectedFolder.getModel())
						.getIndexOf("" + selectedFile.getAbsolutePath() + "") == -1) {
					Crypto_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					Crypto_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					if (Crypto_selectedFolder.getItemCount() > 5) {
						Crypto_selectedFolder.removeItemAt(6);
					}
				} else {
					Crypto_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					int index = Crypto_selectedFolder.getSelectedIndex();
					Crypto_selectedFolder.removeItemAt(index);
					Crypto_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					Crypto_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
				}
				DefaultStringCRYPTO2 = selectedFile.getAbsolutePath();
			}
			;

		}

	}

	// Listener auf den Button der Zustänig dafür ist die QR-File speicherOrt
	// ordner auszuwählen
	private class ChooseQRFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			JFrame Dialog = new JFrame();
			JFileChooser fileChooser2 = new JFileChooser(new File(defaultFolerOpen));
			fileChooser2.setAcceptAllFileFilterUsed(true);
			fileChooser2.setDialogTitle("Wählen Sie ein QR-File zu überprüfung aus:");
			int result = fileChooser2.showOpenDialog(Dialog);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser2.getSelectedFile();
				if (((DefaultComboBoxModel) QR_selectedFolder.getModel()).getIndexOf("" + selectedFile.getAbsolutePath() + "") == -1) {
					QR_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
					QR_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					if (QR_selectedFolder.getItemCount() > 5) {
						QR_selectedFolder.removeItemAt(6);
					}
				} else {
					if(new File(selectedFile.getAbsolutePath()).exists()){
						QR_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
						int index = QR_selectedFolder.getSelectedIndex();
						QR_selectedFolder.removeItemAt(index);
						QR_selectedFolder.insertItemAt("" + selectedFile.getAbsolutePath() + "",0);
						QR_selectedFolder.setSelectedItem("" + selectedFile.getAbsolutePath() + "");
					}
					
				}
				DefaultStringQR = selectedFile.getAbsolutePath();
			}
			;

		}

	}

	// Funktion die aus einem PDF ein PNG / JPG erstellt
	// ACHTUNG ! hat das PDF mehrere seiten wird das PNG immerwieder
	// übeschreiben!

	public static void createImage(String PDFPageDest, int Pic) throws IOException {
		File pdfFile = new File(PDFPageDest);
		RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdf = new PDFFile(buf);
		for (int i = 0; i < pdf.getNumPages(); i++) {
			Rectangle rect = new Rectangle(0, 0, (int) pdf.getPage(i).getBBox().getWidth(),
					(int) pdf.getPage(i).getBBox().getHeight());
			BufferedImage bufferedImage = new BufferedImage(rect.width*5, rect.height*5, BufferedImage.TYPE_INT_RGB);
			Image image = pdf.getPage(i).getImage(rect.width*5, rect.height*5, // width
														// height
					rect, // clip rect
					null, // null for the ImageObserver
					true, // fill background with white
					true // block until drawing is done
			);
			Graphics2D bufImageGraphics = bufferedImage.createGraphics();
			bufImageGraphics.drawImage(image, 0, 0, null);
			ImageIO.write(bufferedImage, "png", new File("Pic_" + Pic + ".png"));
			raf.close();
		}
	}

	// Funktion die einen QR Code auslesen kann wenn man den Filepath , Charset
	// + HintMap übergibt
	// Öffnet das File und liest den QR code aus. Löscht das File wieder und
	// übergibt den Code
	
	public static String readQRCode(String filePath, String charset, Map hintMap)
			throws FileNotFoundException, IOException, NotFoundException {
		FileInputStream fi = null;
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(ImageIO.read(fi = new FileInputStream(filePath)))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
		fi.close();
		Path path = FileSystems.getDefault().getPath("", filePath);
		try {
			Files.delete(path);
		} catch (IOException | SecurityException e) {
			System.err.println(e);
		}
		return qrCodeResult.getText();

	}

	// Funktion die die Items der Dorpdowns nimmt und in seperate TxtFiles
	// speichert um sie beim nächsten aufruf wieder anzeigen zu können
	public void SaveDropdowns() {
		//Dropdown für Ouput Ordner
		int j0 = -1;
		StringBuilder stringBuilder0 = new StringBuilder();
		while (j0 < OutputFolder_selectedFolder.getItemCount() - 1) {
			j0++;
			stringBuilder0.append(OutputFolder_selectedFolder.getItemAt(j0) + "?");
		}
		try (PrintWriter out = new PrintWriter("Saved_Files/OutputSave.txt")) {
			// stringBuilder.setLength(stringBuilder.length() - 1);
			String finalString = stringBuilder0.toString();
			out.println(finalString);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// estes Dropdown für den Ordner der Demokassen
		int j = -1;
		StringBuilder stringBuilder = new StringBuilder();
		while (j < StartFolder_selectedFolder.getItemCount() - 1) {
			j++;
			stringBuilder.append(StartFolder_selectedFolder.getItemAt(j) + "?");
		}
		try (PrintWriter out = new PrintWriter("Saved_Files/DemokassenSave.txt")) {
			// stringBuilder.setLength(stringBuilder.length() - 1);
			String finalString = stringBuilder.toString();
			out.println(finalString);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 2 Dropdown für das auswählen des DEP Files
		int j1 = -1;
		StringBuilder stringBuilder1 = new StringBuilder();
		while (j1 < DEP_selectedFolder.getItemCount() - 1) {
			j1++;
			stringBuilder1.append(DEP_selectedFolder.getItemAt(j1) + "?");
		}
		try (PrintWriter out = new PrintWriter("Saved_Files/DEPFileSave.txt")) {
			// stringBuilder1.setLength(stringBuilder.length() - 1);
			String finalString = stringBuilder1.toString();
			out.println(finalString);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 4 Dropdown für das auswählen des QR Files
		int j3 = -1;
		StringBuilder stringBuilder3 = new StringBuilder();
		while (j3 < QR_selectedFolder.getItemCount() - 1) {
			j3++;
			stringBuilder3.append(QR_selectedFolder.getItemAt(j3) + "?");
		}
		try (PrintWriter out = new PrintWriter("Saved_Files/QRFileSave.txt")) {
			// stringBuilder1.setLength(stringBuilder.length() - 1);
			String finalString = stringBuilder3.toString();
			out.println(finalString);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int j4 = -1;
		StringBuilder stringBuilder4 = new StringBuilder();
		while (j4 < Crypto_selectedFolder.getItemCount() - 1) {
			j4++;
			stringBuilder4.append(Crypto_selectedFolder.getItemAt(j4) + "?");
		}
		try (PrintWriter out = new PrintWriter("Saved_Files/CryptoFileSave.txt")) {
			// stringBuilder1.setLength(stringBuilder.length() - 1);
			String finalString = stringBuilder4.toString();
			out.println(finalString);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// Funktion die die "Save" TxT Files ausliest in denen die Dropdowns menüs
	// gespeichert sind um diese dann wieder in den Text einzunehmen
	public void ReadDropdowns() throws IOException {

		
		File new1 = new File("Saved_Files/DemokassenSave.txt");
		if(new1.exists() && !new1.isDirectory()){
			FileInputStream inputStream = new FileInputStream("Saved_Files/DemokassenSave.txt");
		try {
			String everything = IOUtils.toString(inputStream);
			String[] parts = everything.split("\\?");
			for (int i = 0; (parts.length - 1) > i; i++) {
				StartFolder_selectedFolder.addItem(parts[i]);
			}
			// String part1 = parts[0]; // 004
			// String part2 = parts[1]; // 034556
		} finally {
			inputStream.close();
		}
		}
		File new2 = new File("Saved_Files/DEPFileSave.txt");
		if(new2.exists() && !new2.isDirectory()){
		FileInputStream inputStream2 = new FileInputStream("Saved_Files/DEPFileSave.txt");
		try {
			String everything = IOUtils.toString(inputStream2);
			String[] parts = everything.split("\\?");
			for (int i = 0; (parts.length - 1) > i; i++) {
				DEP_selectedFolder.addItem(parts[i]);
			}
			// String part1 = parts[0]; // 004
			// String part2 = parts[1]; // 034556
		} finally {
			inputStream2.close();
		}
		}
		File new3 = new File("Saved_Files/CryptoFileSave.txt");
		if(new3.exists() && !new3.isDirectory()){
		FileInputStream inputStream3 = new FileInputStream("Saved_Files/CryptoFileSave.txt");
		try {
			String everything = IOUtils.toString(inputStream3);
			String[] parts = everything.split("\\?");
			for (int i = 0; (parts.length - 1) > i; i++) {
				Crypto_selectedFolder.addItem(parts[i]);
			}
		} finally {
			inputStream3.close();
		}
		}
		File new4 = new File("Saved_Files/QRFileSave.txt");
		if(new4.exists() && !new4.isDirectory()){
		FileInputStream inputStream4 = new FileInputStream("Saved_Files/QRFileSave.txt");
		try {
			String everything = IOUtils.toString(inputStream4);
			String[] parts = everything.split("\\?");
			for (int i = 0; (parts.length - 1) > i; i++) {
				QR_selectedFolder.addItem(parts[i]);
			}
		} finally {
			inputStream4.close();
		}
		}
		File new5 = new File("Saved_Files/OutputSave.txt");
		if(new5.exists() && !new5.isDirectory()){
		FileInputStream inputStream5 = new FileInputStream("Saved_Files/OutputSave.txt");
		try {
			String everything = IOUtils.toString(inputStream5);
			String[] parts = everything.split("\\?");
			for (int i = 0; (parts.length - 1) > i; i++) {
				OutputFolder_selectedFolder.addItem(parts[i]);
			}
		} finally {
			inputStream5.close();
		}
		}

	}
	// FUnktion zum erzeugen eines neuen AESKeys
	public static SecretKey createAESKey() {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			int keySize = 256;
			kgen.init(keySize);
			return kgen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Funktion die einen String base64 entschlüsseln kann

	
	
	
	
	//Item Listener der auf das 1. ItemSelect schaut und den Start ordner Wechselt wenn ein neues File ausgewählt wurde
	class NewOrderItemListenerDefaultFolder implements ActionListener{
		public void actionPerformed(ActionEvent arg1) {
			  JComboBox<String> box = (JComboBox)arg1.getSource();
	          String choosenString=(String)box.getSelectedItem();
	          File f = new File(choosenString);
	          if (f.exists()){
	        	  box.removeItem(choosenString);
	        	  box.insertItemAt(choosenString,0);
	        	  box.setSelectedIndex(0);
	        	  defaultFolerOpen= choosenString;
	          }else{
	        	  box.removeItem(choosenString);
	        	  System.out.println("Deleted Choosen DropDown enty cause it does not exist !");
	          }
		         
		         
		}       
	}
	class NewOrderItemListener implements ActionListener{
		public void actionPerformed(ActionEvent arg1) {
		          JComboBox<String> box = (JComboBox)arg1.getSource();
		          String choosenString=(String)box.getSelectedItem();
		          File f = new File(choosenString);
		          if (f.exists()){
		        	  box.removeItem(choosenString);
		        	  box.insertItemAt(choosenString,0);
		        	  box.setSelectedIndex(0);
		          }else{
		        	  box.removeItem(choosenString);
		        	  System.out.println("Deleted Choosen DropDown enty cause it does not exist !");
		          }
		}       
	}

	// Listener der ein JDialog aufruft in dem man eine KassenID in den Verkettungswert umrechnen kann.
	private class ConvertIdToChain implements ActionListener {

		@SuppressWarnings("static-access")
		@Override
		public void actionPerformed(ActionEvent arg0) {

			JDialog Dialog = new JDialog((java.awt.Frame) null, "Convert KassenID", true);
			Dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dialog.setSize(400,180);
			Dimension frameSize = Dialog.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			Dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

			JPanel Layout = new JPanel(new BorderLayout());
			// label with original font
			
			JTextField KassenID = new JTextField();
			KassenID.setPreferredSize(new Dimension(350, 25));
			JTextField Output = new JTextField();
			Output.setPreferredSize(new Dimension(350, 25));
			Output.setEditable(false);
			JLabel Equal = new JLabel("   =   ");
			JPanel InputPanel = new JPanel(new FlowLayout());
			InputPanel.add(KassenID);
			InputPanel.add(Equal);
			InputPanel.add(Output);
			
			JPanel ButtonPanel = new JPanel(new FlowLayout());
			JButton Cancle = new JButton("Abbrechen");
			JButton Calculate = new JButton("Umrechnen");

			Calculate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MessageDigest md;
					try {
						md = MessageDigest.getInstance("sha-256");
						// calculate hash value
						md.update(KassenID.getText().getBytes());
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
						Output.setText(Sig_Vor_Beleg_String);
					} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	
			});
			Cancle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						Dialog.dispose();
					}
			});

		
			ButtonPanel.add(Cancle);
			ButtonPanel.add(Calculate);
			
			Layout.add(InputPanel, BorderLayout.CENTER);
			Layout.add(ButtonPanel, BorderLayout.SOUTH);
			Dialog.add(Layout);
			Dialog.setVisible(true);
		}
	}
	
}

package com.invoices.service;

import com.invoices.domain.ClientCompanyInfo;
import com.invoices.domain.CustodyCharge;
import com.invoices.domain.Invoice;
import com.invoices.utils.ExchangeRateProviderHandler;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is responsible for generating PDF files, based on an invoice.
 * To create a pdf document, you have to call createPdf method.
 * <b><u>WARNING</u></b>: Cyrillic characters are not supported by default in <i>iText7</i> when creating
 * a PDF file. A font that supports <u>Windows-1251 8-bit character encoding</u> is needed
 * to cover languages that use the Cyrillic script.
 * @author psoutzis
 */
@Service
public class PdfService {

    private final float LEADING_SPACE = 0.45f;//space between paragraphs
    private final String IMAGE_PARENT =
            "C:/Users/psoutzis/Desktop/myFolder/projects/invoices/src/main/resources/static/images/";

    /**
     * PageBackgroundEvent is an inner class, needed to add colour to the PDF pages.
     * To change the background colour of the page (or background picture, etc.), just alter
     * the handleEvent() method to your specifications.
     */
    private class PageBackgroundEvent implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();

            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle rect = page.getPageSize();
            Color bgColour = new DeviceRgb(255, 229, 204);
            canvas  .saveState()
                    .setFillColor(bgColour)
                    .rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight())
                    .fillStroke()
                    .restoreState();
        }
    }

    /**
     * Method will create a File(), from the file-path it takes as input. It will write the bytes of the file to
     * the output stream, so tha the user is prompted to download the file if he/she wishes to. The headers are
     * initialized by the method, so the browser will expect a Pdf file.
     * After the file is written to the http request's output stream, it will be deleted from the server.
     * @param response Is the object where the servlet can store data it will send back
     * @param filepath The path to the file to send with the http response
     * @param storeOnServer Indicates if the file should be deleted after it is sent, or stored on server.
     * Set to 'True' to keep on server and 'False' to delete.
     */
    public void sendBytesToBrowser(HttpServletResponse response, String filepath, boolean storeOnServer){
        try {
            //initialize file, http headers, content type, so that browser
            //understands that it's receiving a pdf document
            File file = new File(filepath);
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition","attachment;filename=" + file.getName());
            response.setHeader("Content-Length",String.valueOf(file.length()));

            //Reads from file directly
            FileInputStream fileInputStream = new FileInputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(response.getOutputStream());

            //Set buffer capacity, so there is preferably only 1 loop iteration when writing to output stream
            int bufferCapacity;
            if(file.length() > Integer.MAX_VALUE) bufferCapacity = Integer.MAX_VALUE;
            else if(file.length() < Integer.MIN_VALUE) bufferCapacity = Integer.MIN_VALUE;
            else bufferCapacity = (int)file.length();

            byte[] buffer = new byte[bufferCapacity];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) >= 0)
                //Send to output stream, so user can receive with next http response
                dataOutputStream.write(buffer, 0, len);
            fileInputStream.close();
            if(!storeOnServer)
                deleteFile(file);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Method uses java.nio.* library to delete a file (Files.delete(Path p)), purely for debugging purposes.
     * One big difference of 'boolean File.delete()' and currently used 'void Files.delete(Path p)', is that
     * when the latter fails, it will print a detailed error message instead of just true or false.
     * @param file The file to delete from the system.
     */
    private void deleteFile(File file){
        try{
            Path thisFile = Paths.get(file.getAbsolutePath());
            Files.delete(thisFile);
        }
        catch (IOException ioe){

            System.out.println("Deleting file "+file.getName()+" failed because:\n"+ioe.getMessage());
        }

    }

    /**
     * @param parentPath The parent directory of the PDF.
     * @param name The unique number that will be used to generate this pdf's name.
     * @return The path to this document.
     */
    public String generatePdfPath(String parentPath, String name){
        name = name.trim();
        parentPath = !parentPath.endsWith("/") ? parentPath+"/" : parentPath;

        return parentPath + "invoice_" + name + ".pdf";
    }

    /**
     * Method will replace all whitespace or leading trail from name with the '_' sign
     * @param parentPath The parent directory of the file.
     * @param name The name that the file will receive.
     * @param extension The file extension
     * @return The full path to this file
     */
    public String generateCustomPdfPath(String parentPath, String name, String extension){
        name = name.replaceAll(" ","_" );
        extension = extension.toLowerCase();
        extension = !extension.startsWith(".") ? "."+extension : extension;
        parentPath = !parentPath.endsWith("/") ? parentPath+"/" : parentPath;

        return parentPath + name + extension;
    }

    /**
     * The method that creates the PDF document.
     * If the pages must have a white background (default), just remove the addEvenHandler() line.
     * @param invoice The invoice to model as a PDF file.
     * @param filename The path where the pdf file is going to be stored.
     */
    public void createPdf(Invoice invoice, String filename) {
        Document document;
        try
        {
            OutputStream outputStream = new FileOutputStream(new File(filename));
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundEvent());
            PageSize pageSize = pdfDoc.getDefaultPageSize();
            document = new Document(pdfDoc, pageSize);
            populateDocument(invoice, document, pageSize);
            document.close();

        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /**
     * This method adds all the text and images to the document, while the document is open().
     * @param invoice The invoice to get information from
     * @param document The document to add elements at ( i.e. text, images, watermarks, etc..)
     * @param pageSize An object of type PageSize, that holds dimensional information about the document's page(s).
     * @throws MalformedURLException thrown by Image-type objects
     */
    private void populateDocument(Invoice invoice, Document document, PageSize pageSize)
            throws MalformedURLException
    {
        float pageHeight = pageSize.getHeight();
        float pageWidth = pageSize.getWidth();
        document.setTopMargin(10);
        document.setBottomMargin(5);
        document.setRightMargin(5);

        ClientCompanyInfo company = invoice.getPortfolio().getClientCompanyInfo();
        CustodyCharge charges = invoice.getCustodyCharge();
        String currencyCodeFrom = invoice.getCurrencyRates().getFromCurrency().getCurrencyCode();
        String currencyCodeTo = invoice.getCurrencyRates().getToCurrency().getCurrencyCode();
        String currencySymbolFrom = ExchangeRateProviderHandler.getCurrencySymbol(currencyCodeFrom);
        String currencySymbolTo = ExchangeRateProviderHandler.getCurrencySymbol(currencyCodeTo);

        //If either of currency symbols are equal to 'руб', then change to the official symbol
        if(currencySymbolFrom.equalsIgnoreCase("руб")) currencySymbolFrom = "₽";
        if(currencySymbolTo.equalsIgnoreCase("руб")) currencySymbolTo = "₽";

        final String linebreak = "\n\n";
        final String MK_LOGO = IMAGE_PARENT+"meritkapital.png";
        final String MGROUP_LOGO = IMAGE_PARENT+"mgroup_logo.png";

        Image mkLogo = new Image(ImageDataFactory.create(MK_LOGO));
        mkLogo.scaleToFit(365,240 );
        mkLogo.setOpacity(0.20f);

        Image mgroupLogo = new Image(ImageDataFactory.create(MGROUP_LOGO));
        mgroupLogo.scaleToFit(250,150 );

        List numberAndDate = defaultList();
        String date = String.valueOf(invoice.getInvoiceDate());
        ListItem invoiceNumber = getBoldElement("Invoice No: ",invoice.getInvoiceNumber());
        ListItem invoiceDate = getBoldElement("Invoice Date: ",date);
        numberAndDate.add(invoiceNumber);
        numberAndDate.add(invoiceDate).add(linebreak);

        List companyInfoList = defaultList();
        if(company!=null) {
            ListItem companyName = getBoldElement(company.getName(), "");
            companyInfoList.add(companyName);
            companyInfoList.add(company.getAddress());
            companyInfoList.add(company.getCity()+" "+company.getPostcode());
            companyInfoList.add(company.getCompanyLocation().getCountry());
            ListItem vatNumber = getBoldElement("VAT No: ",company.getVatNumber());
            companyInfoList.add(vatNumber).add(linebreak);
        }
        else{
            ListItem clientName = getBoldElement(invoice.getPortfolio().getClient().getClientName(), "");
            companyInfoList.add(clientName).add(linebreak);
        }

        List descriptionList = defaultList();
        ListItem description = new ListItem();
        Paragraph descPar = defaultParagraph().add("Fee for ");
        Paragraph descPar2 = defaultParagraph();
        Text service = new Text(invoice.getServiceProvided().getServiceName()).setItalic();
        Text frequency = new Text(invoice.getFrequency().getDescription()).setItalic();
        Text period = new Text(invoice.getPeriod().getDescription()).setItalic();
        Text year = new Text(String.valueOf(invoice.getYear())).setItalic();
        Text title = new Text("Description").setBold().setUnderline();
        descPar.add(service).add(" services provided, as per agreement for");
        descPar2.add(frequency).add(" - ").add(period).add(" - ").add(year);
        description.add(new Paragraph().add(title));
        description.add(descPar);
        description.add(descPar2);
        descriptionList.add(description).add(linebreak);

        List column1List = defaultList();
        List column2List = defaultList();
        List column3List = defaultList();
        List xRateList = defaultList();

        float leftPos = pageWidth/15.5f;
        float bottomPos = pageHeight/1.69230f;
        float elementWidth = pageWidth/4f;
        float leftGrowth = pageWidth/3.27272f;
        float vatDetailsBottom = pageHeight/2.18181f;
        float xRateBottom = vatDetailsBottom-30;
        float bankDetailsBottom = vatDetailsBottom-293;

        ListItem column1Item = new ListItem();
        ListItem column2Item = new ListItem();
        ListItem column3Item = new ListItem();
        ListItem xRateItem = new ListItem();

        Paragraph newLineParagraph = defaultParagraph().add(" ");
        Paragraph column1Title = defaultParagraph();
        Paragraph column2Title = defaultParagraph();
        Paragraph column3Title = defaultParagraph();
        Paragraph column1From = defaultParagraph();
        Paragraph column2From = defaultParagraph();
        Paragraph column3From = defaultParagraph();
        Paragraph column1To = defaultParagraph();
        Paragraph column2To = defaultParagraph();
        Paragraph column3To = defaultParagraph();
        Paragraph xRateTitleParagraph = defaultParagraph();
        Paragraph xRateBodyParagraph = new Paragraph();
        Paragraph logoParagraph = new Paragraph();
        Paragraph mgroupParagraph = new Paragraph();

        Text chargeTitle = new Text("Fee").setBold();
        Text vatTitle = new Text("VAT@"+invoice.getVat().getVatRate()).setBold();
        Text totalTitle = new Text("Total").setBold();
        Text xRateTitle = new Text("Exchange Rate").setBold().setUnderline();

        column1Title.add(chargeTitle);
        column2Title.add(vatTitle);
        column3Title.add(totalTitle);

        Float baseCharge = charges.getChargeExcludingVat();
        Float vatCharge = charges.getVatCharge();
        Float totalCharge = charges.getChargeIncludingVat();
        Float exchangeRate = invoice.getCurrencyRates().getExchangeRate();
        column1From.add(currencySymbolFrom).add(String.valueOf(baseCharge));
        column2From.add(currencySymbolFrom).add(String.valueOf(vatCharge));
        column3From.add(currencySymbolFrom).add(String.valueOf(totalCharge));
        baseCharge = ExchangeRateProviderHandler.convertToCurrency(baseCharge,exchangeRate);
        vatCharge = ExchangeRateProviderHandler.convertToCurrency(vatCharge,exchangeRate);
        totalCharge = ExchangeRateProviderHandler.convertToCurrency(totalCharge,exchangeRate);
        column1To.add(currencySymbolTo).add(String.valueOf(baseCharge));
        column2To.add(currencySymbolTo).add(String.valueOf(vatCharge));
        column3To.add(currencySymbolTo).add(String.valueOf(totalCharge));

        String exchangeRateToPrint = "1 "+currencyCodeFrom+" = "+exchangeRate+" "+currencyCodeTo;
        Text xRateItalic = new Text(exchangeRateToPrint).setItalic();

        xRateTitleParagraph.add(xRateTitle);
        xRateBodyParagraph.add("Exchange rate @ ");
        xRateBodyParagraph.add(xRateItalic);

        column1Item.add(column1Title);
        column1Item.add(newLineParagraph);
        column1Item.add(column1From);
        column1Item.add(column1To);
        column1List.add(column1Item);
        column1List.setFixedPosition(leftPos,bottomPos, elementWidth);

        float adjacentListLeft = leftPos+leftGrowth;

        column2Item.add(column2Title);
        column2Item.add(newLineParagraph);
        column2Item.add(column2From);
        column2Item.add(column2To);
        column2List.add(column2Item);
        column2List.setFixedPosition(adjacentListLeft, bottomPos, elementWidth);

        adjacentListLeft += leftGrowth;

        column3Item.add(column3Title);
        column3Item.add(newLineParagraph);
        column3Item.add(column3From);
        column3Item.add(column3To);
        column3List.add(column3Item);
        column3List.setFixedPosition(adjacentListLeft, bottomPos, elementWidth);

        List vatDetails = defaultList();
        Paragraph vatParagraph= new Paragraph(new Text("Reverse Charge").setBold().setUnderline())
                .add(": "+invoice.getReverseCharge().toString());
        ListItem reverseChargeItem = new ListItem();
        reverseChargeItem.add(vatParagraph);
        vatDetails.add(reverseChargeItem);
        vatParagraph= new Paragraph(new Text("VAT Exempt").setBold().setUnderline())
                .add(": "+invoice.getVatExempt().toString());
        ListItem vatExemptItem = new ListItem();
        vatExemptItem.add(vatParagraph);
        vatDetails.add(vatExemptItem);
        vatDetails.add(linebreak);
        vatDetails.setFixedPosition(leftPos,vatDetailsBottom ,null );

        xRateItem.add(xRateTitleParagraph);
        xRateItem.add(xRateBodyParagraph);
        xRateList.add(xRateItem);
        xRateList.setFixedPosition(leftPos, xRateBottom, null);

        List bankDetails = defaultList().setFontSize(12);
        ListItem headerItem = new ListItem();
        Paragraph headerParagraph = new Paragraph(new Text("THIS INVOICE IS NOW PAYABLE").setBold().setUnderline());
        headerItem.add(headerParagraph);
        bankDetails.add(headerItem);
        bankDetails.add("Settlement of our note of fee can be arranged by direct transfer to:");

        Paragraph bankParagraph = new Paragraph(new Text("Bank Account").setBold())
                .add(": "+invoice.getBankAccount().getBank().getName()+" - "+invoice.getBankAccount().getName());
        ListItem bankNameItem = new ListItem();
        bankNameItem.add(bankParagraph);
        bankDetails.add(bankNameItem);

        Paragraph swiftCodeParagraph = defaultParagraph();
        swiftCodeParagraph.add(new Text("SWIFT code").setBold())
                .add(": "+invoice.getBankAccount().getSwiftCode());
        ListItem swiftCodeItem = new ListItem();
        swiftCodeItem.add(swiftCodeParagraph);
        bankDetails.add(swiftCodeItem);

        Paragraph euroAccountParagraph = new Paragraph(new Text("EURO account number").setBold())
                .add(": "+invoice.getBankAccount().getEuroAccNum());
        ListItem euroAccItem = new ListItem();
        euroAccItem.add(euroAccountParagraph);
        bankDetails.add("\n");
        bankDetails.add(euroAccItem);

        Paragraph euroIbanParagraph = defaultParagraph();
        euroIbanParagraph.add(new Text("IBAN").setBold())
                .add(": "+invoice.getBankAccount().getEuroIban());
        ListItem euroIbanItem = new ListItem();
        euroIbanItem.add(euroIbanParagraph);
        bankDetails.add(euroIbanItem);

        Paragraph usdAccountParagraph = new Paragraph(new Text("U.S. Dollars account number").setBold())
                .add(": "+invoice.getBankAccount().getUsdAccNum());
        ListItem usdAccItem = new ListItem();
        usdAccItem.add(usdAccountParagraph);
        bankDetails.add("\n");
        bankDetails.add(usdAccItem);

        Paragraph usdIbanParagraph = defaultParagraph();
        usdIbanParagraph.add(new Text("IBAN").setBold())
                .add(": "+invoice.getBankAccount().getSwiftCode());
        ListItem usdIbanItem = new ListItem();
        usdIbanItem.add(usdIbanParagraph);
        bankDetails.add(usdIbanItem);
        bankDetails.add(linebreak);
        bankDetails.setFixedPosition(leftPos,bankDetailsBottom ,null );

        List mkAddressList = defaultList().setFontSize(8).setItalic();
        mkAddressList.add("MeritKapital Ltd.");
        mkAddressList.add("Eftapaton Court");
        mkAddressList.add("256, Makarios Avenue");
        mkAddressList.add("CY-3105 Limassol, Cyprus");
        mkAddressList.add("P.O. Box: 53180");
        mkAddressList.add("CY-3301 Limassol, Cyprus");
        mkAddressList.add("Tel: +357 25 85 79 00");
        mkAddressList.add("Fax: +357 25 34 03 27");
        mkAddressList.add("info@meritkapital.com");
        mkAddressList.add("www.meritkapital.com");
        mkAddressList.setFixedPosition(460, 715, 120);

        logoParagraph.add(mkLogo);
        logoParagraph.setFixedPosition(130, 750, null);//top right corner
        mgroupParagraph.add(mgroupLogo);
        mgroupParagraph.setFixedPosition(175, 40, null);//bottom of page

        document.add(numberAndDate);
        document.add(companyInfoList);
        document.add(descriptionList);
        document.add(column1List);
        document.add(column2List);
        document.add(column3List);
        document.add(vatDetails);
        document.add(xRateList);
        document.add(bankDetails);
        document.add(logoParagraph);
        document.add(mgroupParagraph);
        document.add(mkAddressList);
    }

    /**
     * @param withFont the string with font applied to it, after being converted to Text first
     * @param plainString the string that will remain without font, to be added after the string with font.
     * @return a ListItem-type object, that contains a sentence with 2 concatenated strings. The first string
     * is always Bold and the second string is as-is.
     */
    private ListItem getBoldElement(String withFont, String plainString)
    {
        ListItem listItem = new ListItem();
        Paragraph paragraph = defaultParagraph();
        Text withFontString = new Text(withFont).setBold();
        paragraph.add(withFontString);
        paragraph.add(plainString);
        listItem.add(paragraph);

        return listItem;
    }

    /**
     * @return a new List() object, with no symbol in front of each element.
     */
    private List defaultList(){

        return new List().setListSymbol("");
    }

    /**
     *
     * @return a new Paragraph() object, but with smaller space left between this
     * paragraph and the previous element.
     * e.g. When the paragraph is inserted it will insert half the space that it
     * was going to insert by default.
     */
    private Paragraph defaultParagraph(){

        return new Paragraph().setMultipliedLeading(LEADING_SPACE);
    }

}


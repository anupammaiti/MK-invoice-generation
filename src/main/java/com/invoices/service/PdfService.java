package com.invoices.service;

import com.invoices.utils.ExchangeRateProviderHandler;
import org.springframework.stereotype.Service;
import com.invoices.domain.ClientCompanyInfo;
import com.invoices.domain.CustodyCharge;
import com.invoices.domain.Invoice;
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
import com.itextpdf.layout.element.List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

/**
 * @author psoutzis
 * This class is responsible for generating PDF files, based on an invoice.
 * To create a pdf document, you have to call createPdf method.
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
     * @param parentPath The parent directory of the PDF.
     * @param documentNumber The unique number that will be used to generate this pdf's name.
     * @return The path to this document.
     */
    public String generatePdfPath(String parentPath, String documentNumber){
        documentNumber = documentNumber.trim();
        parentPath = !parentPath.endsWith("/") ? parentPath+"/" : parentPath;

        String path = parentPath + "invoice_" + documentNumber + ".pdf";

        return path;
    }

    /**
     * Method will replace all whitespace or leading trail from customName with the '_' sign
     * @param parentPath The parent directory of the file.
     * @param customName The name that the file will receive.
     * @param extension The file extension
     * @return The full path to this file
     */
    public String generateCustomPath(String parentPath, String customName, String extension){
        customName = customName.replaceAll(" ","_" );
        extension = extension.toLowerCase();
        extension = !extension.startsWith(".") ? "."+extension : extension;
        parentPath = !parentPath.endsWith("/") ? parentPath+"/" : parentPath;

        String path = parentPath + customName + extension;

        return path;
    }

    /**
     * The method that creates the PDF document.
     * If the pages must have a white background (default), just remove the addEvenHandler() line.
     * @param invoice The invoice to model as a PDF file.
     * @param filename The path where the pdf file is going to be stored.
     * @return the finalized object of type Document, which is the pdf file that was created at
     * the beginning of the method.
     */
    public Document createPdf(Invoice invoice, String filename) {
        Document document = null;
        try
        {
            OutputStream outputStream = new FileOutputStream(new File(filename));
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundEvent());
            PageSize pageSize = pdfDoc.getDefaultPageSize();
            document = new Document(pdfDoc, pageSize);
            document = populateDocument(invoice, document, pageSize);
            document.close();

        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
        return document;
    }

    /**
     * This method adds all the text and images to the document, while the document is open().
     * @param invoice The invoice to get information from
     * @param document The document to add elements at (text, images, watermarks, etc..)
     * @param pageSize An object of type PageSize, that holds dimensional information about the document's page(s).
     * @return The populated document, in human-readable format
     * @throws MalformedURLException thrown by Image-type objects
     */
    private Document populateDocument(Invoice invoice, Document document, PageSize pageSize)
            throws MalformedURLException
    {
        float pageHeight = pageSize.getHeight();
        float pageWidth = pageSize.getWidth();

        ClientCompanyInfo company = invoice.getPortfolio().getClientCompanyInfo();
        CustodyCharge charges = invoice.getCustodyCharge();
        String currencyCodeFrom = invoice.getCurrencyRates().getFromCurrency().getCurrencyCode();
        String currencyCodeTo = invoice.getCurrencyRates().getToCurrency().getCurrencyCode();
        String currencySymbolFrom = ExchangeRateProviderHandler.getCurrencySymbol(currencyCodeFrom);
        String currencySymbolTo = ExchangeRateProviderHandler.getCurrencySymbol(currencyCodeTo);
        final String linebreak = "\n\n";
        final String MK_LOGO = IMAGE_PARENT+"meritkapital.png";
        final String MGROUP_LOGO = IMAGE_PARENT+"mgroup_logo.png";

        Image mkLogo = new Image(ImageDataFactory.create(MK_LOGO));
        Image mgroupLogo = new Image(ImageDataFactory.create(MGROUP_LOGO));
        mkLogo.scaleToFit(200,100 );
        mgroupLogo.setAutoScale(true);

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
        float xRateBottom = pageHeight/2.18181f;

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

        xRateItem.add(xRateTitleParagraph);
        xRateItem.add(xRateBodyParagraph);
        xRateList.add(xRateItem);
        xRateList.setFixedPosition(leftPos, xRateBottom, null);

        logoParagraph.add(mkLogo);
        logoParagraph.setFixedPosition(321, 780, null);//top right corner
        mgroupParagraph.add(mgroupLogo);
        mgroupParagraph.setFixedPosition(70, 75, null);//bottom of page

        document.add(numberAndDate);
        document.add(companyInfoList);
        document.add(descriptionList);
        document.add(column1List);
        document.add(column2List);
        document.add(column3List);
        document.add(xRateList);
        document.add(logoParagraph);
        document.add(mgroupParagraph);

        return document;
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


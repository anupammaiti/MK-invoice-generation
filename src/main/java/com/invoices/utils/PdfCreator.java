package com.invoices.utils;

import com.invoices.domain.ClientCompanyInfo;
import com.invoices.domain.CustodyCharge;
import com.invoices.domain.Invoice;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.List;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PdfCreator {

    private static final float LEADING_SPACE = 0.45f;

    public static Document createPdf(Invoice invoice, String path){
        Document document = null;
        path = !path.endsWith("/") ? path+"/" : path;
        String extension = ".pdf";
        String filename = path + "invoice_"+invoice.getInvoiceNumber() + extension;

        try
        {
            OutputStream outputStream = new FileOutputStream(new File(filename));
            PdfWriter writer = new PdfWriter(outputStream);
            Color bgColour = new DeviceRgb(255, 204, 204);
            PdfDocument pdfDoc = new PdfDocument(writer);
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

    private static Document populateDocument(Invoice invoice, Document document, PageSize page){
        float pageHeight = page.getHeight();
        float pageWidth = page.getWidth();
        //System.out.println("Height: "+pageHeight+"\nWidth: "+pageWidth);

        ClientCompanyInfo company = invoice.getPortfolio().getClientCompanyInfo();
        CustodyCharge charges = invoice.getCustodyCharge();
        String currencyCodeFrom = invoice.getCurrencyRates().getFromCurrency().getCurrencyCode();
        String currencyCodeTo = invoice.getCurrencyRates().getToCurrency().getCurrencyCode();
        String currencySymbolFrom = ExchangeRateProviderHandler.getCurrencySymbol(currencyCodeFrom);
        String currencySymbolTo = ExchangeRateProviderHandler.getCurrencySymbol(currencyCodeTo);
        final String linebreak = "\n\n";

        List numberAndDate = defaultList();
        ListItem invoiceNumber = getBoldElement("Invoice No: ",invoice.getInvoiceNumber());
        numberAndDate.add(invoiceNumber);
        numberAndDate.add("Invoice Date: "+invoice.getInvoiceDate()).add(linebreak);

        List companyInfoList = defaultList();
        ListItem companyName = getBoldElement(company.getName(), "");
        companyInfoList.add(companyName);
        companyInfoList.add(company.getAddress());
        companyInfoList.add(company.getCity()+" "+company.getPostcode());
        companyInfoList.add(company.getCompanyLocation().getCountry());
        ListItem vatNumber = getBoldElement("VAT No: ",company.getVatNumber());
        companyInfoList.add(vatNumber).add(linebreak);

        List descriptionList = defaultList();
        ListItem description = new ListItem();
        Paragraph descPar = defaultParagraph().add("Fee for ");
        Text service = new Text(invoice.getServiceProvided().getServiceName()).setItalic();
        Text frequency = new Text(invoice.getFrequency().getDescription()).setItalic();
        Text period = new Text(invoice.getPeriod().getDescription()).setItalic();
        Text year = new Text(String.valueOf(invoice.getYear())).setItalic();
        Text title = new Text("Description").setBold().setUnderline();
        descPar.add(service).add(" services provided, as per agreement for ");
        descPar.add(frequency).add(" - ").add(period).add(" - ").add(year);
        description.add(new Paragraph().add(title));
        description.add(descPar);
        descriptionList.add(description).add(linebreak);

        List column1List = defaultList();
        List column2List = defaultList();
        List column3List = defaultList();
        List xRateList = defaultList();

        float listLeft = pageWidth/15.5f;
        float listBottom = pageHeight/1.69230f;
        float listWidth = pageWidth/4f;
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
        column1List.setFixedPosition(listLeft,listBottom, listWidth);

        float adjacentListLeft = listLeft+leftGrowth;

        column2Item.add(column2Title);
        column2Item.add(newLineParagraph);
        column2Item.add(column2From);
        column2Item.add(column2To);
        column2List.add(column2Item);
        column2List.setFixedPosition(adjacentListLeft,listBottom, listWidth);

        adjacentListLeft += leftGrowth;

        column3Item.add(column3Title);
        column3Item.add(newLineParagraph);
        column3Item.add(column3From);
        column3Item.add(column3To);
        column3List.add(column3Item);
        column3List.setFixedPosition(adjacentListLeft,listBottom, listWidth);

        xRateItem.add(xRateTitleParagraph);
        xRateItem.add(xRateBodyParagraph);
        xRateList.add(xRateItem);
        xRateList.setFixedPosition(listLeft, xRateBottom, null);

        document.add(numberAndDate);
        document.add(companyInfoList);
        document.add(descriptionList);
        document.add(column1List);
        document.add(column2List);
        document.add(column3List);
        document.add(xRateList);

        return document;
    }

    private static ListItem getBoldElement(String withFont, String plainString)
    {
        ListItem listItem = new ListItem();
        Paragraph paragraph = defaultParagraph();
        Text withFontString = new Text(withFont).setBold();
        paragraph.add(withFontString);
        paragraph.add(plainString);
        listItem.add(paragraph);

        return listItem;
    }


    private static List defaultList(){

        return new List().setListSymbol("");
    }

    private static Paragraph defaultParagraph(){

        return new Paragraph().setMultipliedLeading(LEADING_SPACE);
    }

}

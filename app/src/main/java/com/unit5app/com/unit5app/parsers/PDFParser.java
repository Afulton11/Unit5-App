package com.unit5app.com.unit5app.parsers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

/**
 * Class to turn PDDocuments into usable stuff.
 */
public abstract class PDFParser {
    private static String TAG = "PDFParser";

    /**
     * Converts a PDDocument into a String.
     * @param pdDocument - PDDocument to read from.
     * @return the document as an unformatted String.
     * @throws IOException
     */
    public static String toText(PDDocument pdDocument) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper();

        int numPages = pdDocument.getNumberOfPages(); /* This number should be 1, anyways. */

        pdfTextStripper.setStartPage(1);
        pdfTextStripper.setEndPage(numPages);

        return pdfTextStripper.getText(pdDocument);
    }

    /* TODO: Method that uses RegEx to get only the text we need. */
}

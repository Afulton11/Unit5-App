package com.unit5app.tasks;

public class PDFManager {
    private String TAG = "PDFManager";

}

/* /* TEST CODE, PROOF OF CONCEPT */ /*
PDFGrabber grabber = new PDFGrabber();
    PDDocument doc = grabber.fetchPDF("http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Feb%20Sr%20High%20Lunch.pdf");
        /* END TEST CODE */ /*
/**
     * Retrieves a PDF from the Web as a PDDocument.
     * @param pdfUrl - the location of the PDF on the Internet.
     * @return the PDF specified by the URL passed.
     */ /*
public PDDocument fetchPDF(final String pdfUrl) {
    Thread grabber = new Thread() {
        @Override
        public void run() {
            try {
                pdf = new PDDocument();
                URL url = new URL(pdfUrl);
                Log.d(TAG, "Attempting to connect to PDF at URL:  " + pdfUrl);
                InputStream inputStream = url.openStream();
                Log.d(TAG, "Successfully connected to PDF at URL: " + pdfUrl);


                    /* Store info into a PDDocument */ /*
                pdf.load(inputStream);

                    /* Close the input stream and free its resources. */ /*
                pdf.close();
                inputStream.close();
                Log.d(TAG, "Document stream and input stream closed.");
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
        }
    };
    grabber.start();

    return pdf;
}

  public static String toText(PDDocument pdDocument) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper();

        int numPages = pdDocument.getNumberOfPages(); /* This number should be 1, anyways. */
/*
pdfTextStripper.setStartPage(1);
        pdfTextStripper.setEndPage(numPages);

        return pdfTextStripper.getText(pdDocument);
        }
 */

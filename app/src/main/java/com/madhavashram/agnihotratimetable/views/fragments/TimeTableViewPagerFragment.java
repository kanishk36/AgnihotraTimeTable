package com.madhavashram.agnihotratimetable.views.fragments;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.madhavashram.agnihotratimetable.PDFViewerActivity;
import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;
import com.madhavashram.agnihotratimetable.views.AbstractActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link BaseTimeTableViewPagerFragment} subclass.
 */
@SuppressWarnings("unchecked")
public class TimeTableViewPagerFragment extends BaseTimeTableViewPagerFragment {

    private final int WRITE_EXTERNAL_STORAGE_REQUEST = 10;
    private String city, blankString;
//    private double latitude, longitude;


    public TimeTableViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        blankString = getString(R.string.blank_string);

        Bundle bundle = getArguments();
        if(bundle != null) {
            city = bundle.getString(CommonUtils.CITY_TAG);
//            latitude = bundle.getDouble(CommonUtils.LATITUDE_TAG);
//            longitude = bundle.getDouble(CommonUtils.LONGITUDE_TAG);
        }

        if(city.equals(getString(R.string.city_unknown))) {
            city = getString(R.string.agnihotra);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_timetable, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_savePdf:
                if (ActivityCompat.checkSelfPermission(mFragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST);

                } else {
                    savePDF();
                }
                break;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePDF();
                }

            }
        }
    }

    private void savePDF() {
        new SavePDFTask().execute();
    }

    private class SavePDFTask extends AsyncTask<Void, Void, Void>
    {
        private boolean isPDFGenerated = false;
        private File pdfFile;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtils.showProgressDialog((AbstractActivity) mFragmentActivity, "Generating PDF ...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            previewPDFDoc();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            CommonUtils.dismissProgressDialog((AbstractActivity) mFragmentActivity);
            if(isPDFGenerated) {
                try {

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Uri fileUri;
                    
                    if(Build.VERSION.SDK_INT >= 24) {
                        fileUri = FileProvider.getUriForFile(mFragmentActivity, mFragmentActivity.getPackageName().concat(".provider"), pdfFile);
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    } else {
                        fileUri = Uri.fromFile(pdfFile);
                    }
                    i.setDataAndType(fileUri,"application/pdf");
                    startActivity(i);

                } catch (ActivityNotFoundException ex) {

                    Intent intent = new Intent(mFragmentActivity, PDFViewerActivity.class);
                    intent.putExtra(CommonUtils.PDF_FILE_TAG, pdfFile.getAbsolutePath());
                    startActivity(intent);
                }

            }
        }

        private void previewPDFDoc() {
            String stringArray[] = city.split(",");
            String filename = "";
            for(int i=0;i<stringArray.length-1;i++){
                filename = filename.concat(stringArray[i]).concat("_");
            }
            filename = filename.concat("Timetable.pdf");

            //check if external storage is available so that we can dump our PDF file there
            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                Toast.makeText(mFragmentActivity,"Please mount SD Card.", Toast.LENGTH_SHORT).show();

            } else {
                //path for the PDF file in the external storage
                try {
                    pdfFile = new File(Environment.getExternalStorageDirectory(), filename);
                    generatePdf();
                    isPDFGenerated = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showErrorDialog(mFragmentActivity, "Error creating PDF.");
                        }
                    });

                }
            }
        }

        private void generatePdf() throws Exception {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            String yearStr;
            if((year % 4) == 0) {
                yearStr = "Leap Year";
            } else {
                yearStr = "Non Leap Year";
            }

            //create a new document
            Document document = new Document();
            document.setPageCount(2);
            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            createPages(1, docWriter.getDirectContent(), yearStr, document.leftMargin());
            document.newPage();
            createPages(2, docWriter.getDirectContent(), yearStr, document.leftMargin());
            document.close();
        }

        private boolean isExternalStorageReadOnly() {
            String extStorageState = Environment.getExternalStorageState();
            return (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState));
        }

        private boolean isExternalStorageAvailable() {
            String extStorageState = Environment.getExternalStorageState();
            return (Environment.MEDIA_MOUNTED.equals(extStorageState));
        }

        private void createPages(int pageNumber, PdfContentByte cb, String year, float xPos) throws Exception {

            createHeadings(cb, FontStyle.BOLD, 200, 800, 20, "Agnihotra Time Table");
            createHeadings(cb, FontStyle.BOLD, 250, 778, 16, "Madhavashram");
            createHeadings(cb, FontStyle.BOLD, 180, 760, 14, "Sehore Road, Bairagarh, Bhopal (M.P.)");
            createHeadings(cb, FontStyle.BOLD, 165, 744, 14, "Phone:- +91-9827052445, +91-8120036303");
            createHeadings(cb, FontStyle.BOLD, 200, 715, 12, city+" - "+year);

            //list all the products sold to the customer
            float[] columnWidths = {1.5f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setTotalWidth(500f);

            // First Six Months
            PdfPCell cell = new PdfPCell(new Phrase("Date"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            switch(pageNumber) {
                case 1:
                    // First 6 months
                    createTimeTablePage(table, new String[]{"January", "February", "March", "April", "May", "June"}, true);
                    break;

                case 2:
                    // Last 6 months
                    createTimeTablePage(table, new String[]{"July", "August", "September", "October", "November", "December"}, false);
                    break;
            }
            //absolute location to print the PDF table from
            table.writeSelectedRows(0, -1, xPos, 700, cb);
        }

        private void createHeadings(PdfContentByte cb, FontStyle fontStyle, float x, float y, float fontSize, String text) throws Exception {
            String font;
            switch(fontStyle) {
                case NORMAL:
                    font = BaseFont.HELVETICA;
                    break;
                case BOLD:
                    font = BaseFont.HELVETICA_BOLD;
                    break;
                default:
                    font = BaseFont.HELVETICA;
                    break;
            }
            BaseFont bfBold = BaseFont.createFont(font, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.beginText();
            cb.setFontAndSize(bfBold, fontSize);
            cb.setTextMatrix(x,y);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER,text.trim(),300,y,0.0f);
            cb.endText();
        }

    }

    private void createTimeTablePage(PdfPTable table, String[] months, boolean isFirstHalf) {
        int startIndex, endIndex;
        if(isFirstHalf) {
            startIndex = 0;
            endIndex = 6;

        } else {
            startIndex = 6;
            endIndex = 12;

        }

        PdfPCell cell = new PdfPCell(new Phrase(months[0]));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(months[1]));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(months[2]));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(months[3]));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(months[4]));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(months[5]));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        table.setHeaderRows(2);

        for(int col=0; col < 12; col++) {
            String ampm;
            if(col % 2 == 0) {
                ampm = "A.M.";
            } else {
                ampm = "P.M.";
            }
            cell = new PdfPCell(new Phrase(ampm));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        for(int row=0; row<31; row++)
        {
            cell = new PdfPCell(new Phrase(String.valueOf(row+1)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            for(int col=startIndex; col<endIndex; col++)
            {
                ArrayList<ArrayList<String>> timeMap = timeArray.get(col);

                if(timeMap.size() >= (row+1)) {

                    cell = new PdfPCell(new Phrase(timeMap.get(row).get(0)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(timeMap.get(row).get(1)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                } else {

                    cell = new PdfPCell(new Phrase(" ".concat(blankString).concat(" ")));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ".concat(blankString).concat(" ")));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

            }
        }

    }

}

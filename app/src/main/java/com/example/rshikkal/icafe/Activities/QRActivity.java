package com.example.rshikkal.icafe.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.rshikkal.icafe.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by tusharsi on 18-May-17.
 */

public class QRActivity extends BaseActivity {
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_layout);
        image = (ImageView)findViewById(R.id.image);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        Bundle bundle = getIntent().getExtras();
        try {
            bitMatrix = multiFormatWriter.encode(bundle.getString("json"), BarcodeFormat.QR_CODE,500,500);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BarcodeEncoder barcodeEncoder =  new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        image.setImageBitmap(bitmap);

    }
}

package activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.oli.coach.R;
public class qrCode extends AppCompatActivity {
    private ImageView imageView;
    private String userData;
    private Bitmap icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        imageView = findViewById(R.id.qrCode);
        icon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userData = bundle.getString("data"); // Retrieve the data as a String
            Log.i("56y", userData);
            retrieveUserDataFromFirestore();

        } else {
            Log.i("56y", "null");
        }
    }

    private Bitmap encodeAsBitmap(String str, Bitmap icon) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(str, BarcodeFormat.QR_CODE, 650, 650);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF142C41 : Color.WHITE);
            }
        }

        // Add icon to the QR code
        Bitmap mergedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(mergedBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);

        int iconSize = 150; // Adjust the icon size as needed
        int posX = (bitmap.getWidth() - iconSize) / 2;
        int posY = (bitmap.getHeight() - iconSize) / 2;

        // Scale down the icon to fit the QR code
        Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, iconSize, iconSize, true);
        canvas.drawBitmap(scaledIcon, posX, posY, null);

        return mergedBitmap;
    }

    private void retrieveUserDataFromFirestore() {

        if (userData != null) {

            try {
                Bitmap qrWithIcon = encodeAsBitmap(userData, icon);
                imageView.setImageBitmap(qrWithIcon);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No such document");
        }
    }
}
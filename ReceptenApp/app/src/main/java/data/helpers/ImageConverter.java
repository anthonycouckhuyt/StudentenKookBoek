package data.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by tijs1 on 6-12-2014.
 */
public class ImageConverter {
    public static Bitmap StringToBitmap(String _picture){
        Bitmap picture;
        byte[] byteArray = Base64.decode(_picture, 0);
        picture = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return picture;
    }
    public static String BitmapToString(Bitmap _picture){
        String picture;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        _picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        picture = new String(Base64.encode(byteArray,0));
        return picture;
    }
}

package rkr.simplekeyboard.inputmethod.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import com.google.android.gms.tasks.Tasks;

@RunWith(AndroidJUnit4.class) // 使用 AndroidJUnit4 运行器
// @RunWith(JUnit4.class)
public class OCRTest {

    // @Test
    // public void simpleTest() {
    //     System.out.println("Simple Test Executed");
    //     assert 1==1;
    // }


    
    @Test
    public void testInfer(){
//        Context context = ApplicationProvider.getApplicationContext();
//        InferenceModel model = new InferenceModel(context);
//        String prompt = "Hello, who are you?";
//        model.generateResponse(prompt);

        InferenceApi chatService = new InferenceApi();
        String prompt = "你好，你是谁？";
        System.out.println(chatService.chat(prompt));
    }


    @Test
    public void testRecognizeTextFromAssets() {
        Context context = ApplicationProvider.getApplicationContext();

        Bitmap bitmap = loadImageFromAssets(context, "1119.jpg");
        if (bitmap == null) {
            System.out.println("Failed to load image from assets.");
            return;
        }

        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            // 配置 TextRecognizer，支持中文
            TextRecognizer recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());


            // TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Text result = Tasks.await(recognizer.process(image));
            StringBuilder recognizedText = new StringBuilder();

            for (Text.TextBlock block : result.getTextBlocks()) {
                recognizedText.append(block.getText()).append("\n");
            }

            System.out.println("Recognized Text:");
            System.out.println(recognizedText.toString().trim());
        } catch (Exception e) {
            System.err.println("OCR failed: " + e.getMessage());
        }
    }

    private Bitmap loadImageFromAssets(Context context, String fileName) {
        try (InputStream inputStream = context.getAssets().open(fileName)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }
}

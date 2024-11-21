package rkr.simplekeyboard.inputmethod.ocr;

import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.concurrent.ExecutionException;

public class OCRUtil {

    /**
     * 识别图片中的文字
     *
     * @param image 输入图片
     * @return 识别的文本内容
     * @throws ExecutionException   如果任务执行失败
     * @throws InterruptedException 如果任务被中断
     */
    public static String recognizeText(InputImage image) throws ExecutionException, InterruptedException {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // 处理图片并同步获取结果
        Text result = Tasks.await(recognizer.process(image));
        StringBuilder recognizedText = new StringBuilder();
        for (Text.TextBlock block : result.getTextBlocks()) {
            recognizedText.append(block.getText()).append("\n");
        }
        return recognizedText.toString().trim();
    }
}

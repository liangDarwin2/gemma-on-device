package rkr.simplekeyboard.inputmethod.ocr;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Message {
    private String role;
    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}

class ChatRequest {
    private String model;
    private List<Message> messages;

    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }
}

class ChatMessage {
    private String role;
    private String content;

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}

class Choice {
    private int index;
    private ChatMessage message;
    private String finish_reason;

    public Choice(int index, ChatMessage message, String finish_reason) {
        this.index = index;
        this.message = message;
        this.finish_reason = finish_reason;
    }

    public int getIndex() {
        return index;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public String getFinish_reason() {
        return finish_reason;
    }
}

class ChatResponse {
    private String id;
    private List<Choice> choices;

    public ChatResponse(String id, List<Choice> choices) {
        this.id = id;
        this.choices = choices;
    }

    public String getId() {
        return id;
    }

    public List<Choice> getChoices() {
        return choices;
    }
}

interface ChatApi {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    retrofit2.Call<ChatResponse> chat(@Body ChatRequest request);
}

public class InferenceApi {
    private Retrofit retrofit;
    private ChatApi api;
    private String apiKey = "sk-vrvybcbzusmurtoyegxvcspwmfcgvdjimyyucenpyizwinyf";

    static List<Message> messages = new ArrayList<>();
    static {
        messages.add(new Message("system", "You are a helpful assistant."));
    }

    public InferenceApi() {
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + apiKey)
                        .build();
                    return chain.proceed(request);
                }
            })
            .build();

        retrofit = new Retrofit.Builder()
            .baseUrl("https://api.siliconflow.cn/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        api = retrofit.create(ChatApi.class);
    }

    public String chat(String prompt) {
        // List<Message> messages = new ArrayList<>();
        // messages.add(new Message("system", "You are a helpful assistant."));
        messages.add(new Message("user", prompt));

        ChatRequest request = new ChatRequest("google/gemma-2-9b-it", messages);

        try {
            retrofit2.Call<ChatResponse> call = api.chat(request);
            retrofit2.Response<ChatResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                Choice choice = response.body().getChoices().get(0);
                if (choice != null && choice.getMessage() != null) {
                    return choice.getMessage().getContent();
                }
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
        return "No response content";
    }
}
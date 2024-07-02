package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.oli.coach.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import adapters.chatAdaptor;
import models.Driver;
import models.chatMessageModel;
import models.chatRoomModel;
import models.myUsers;
import models.util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chatRoom extends AppCompatActivity {
    TextView uName;
    ImageButton send;
    EditText typeMessage;
    RecyclerView recyclerView;
    String chatRoomId;
    Driver driver;
    chatRoomModel chatRoomModel;
    chatAdaptor messageAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        driver = util.getUserIdFromIntent(getIntent());
        chatRoomId = util.getChatRoomId(util.currentUserId(), driver.getUid());
        uName = findViewById(R.id.uName);
        send = findViewById(R.id.sendBtn);
        typeMessage = findViewById(R.id.message);
        recyclerView = findViewById(R.id.chat);

        uName.setText(driver.getName());
        send.setOnClickListener(view -> {
            String message = typeMessage.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToUser(message);
        });

        createChatRoomModel();
        messageRecyclerView();

    }


    void messageRecyclerView() {
        Query query = util.getChatRoomMessageRef(chatRoomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<chatMessageModel> options = new FirestoreRecyclerOptions
                .Builder<chatMessageModel>().setQuery(query, chatMessageModel.class).build();
        messageAdaptor = new chatAdaptor(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(messageAdaptor);
        messageAdaptor.startListening();
        messageAdaptor.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message) {

        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(util.currentUserId());
        util.chatRoomRef(chatRoomId).set(chatRoomModel);

        chatMessageModel ChatMessageModel = new chatMessageModel(message, util.currentUserId(), Timestamp.now());
        util.getChatRoomMessageRef(chatRoomId).add(ChatMessageModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        typeMessage.setText("");
                        sendNotification(message);
                    }
                });

    }

    void sendNotification(String message) {

        //current username, message, currentUserId, otherUserToken
        util.adminRef().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                myUsers myAdmin = task.getResult().toObject(myUsers.class);
                try {
                    JSONObject jsonObject = new JSONObject();

                    JSONObject notificationObject = new JSONObject();
                    notificationObject.put("title", myAdmin.getFullName());
                    notificationObject.put("body", message);

                    JSONObject dataObj = new JSONObject();
                    dataObj.put("uId", myAdmin.getUid());

                    jsonObject.put("notification", notificationObject);
                    jsonObject.put("data", dataObj);
                    jsonObject.put("to", driver.getToken());

                    callApi(jsonObject);


                } catch (Exception e) {

                }
            }
        });

    }

    void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder().url(url)
                .post(body)
                .header("Authorization",
                        "Bearer AAAADks6d3s:APA91bGp4EoUwMDo9xHxXba3VWFS4phTxbRMntGJ0mQQymXF5y4VCTJ_C7td-XRY_XDESgfUXdRENFdKf1NiKxWEwD2CN662B6PSeDSSn-dR2MqKUr2uRLKeVfW-NsdHYy5lLlH3imK6")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }


    void createChatRoomModel() {
        util.chatRoomRef(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoomModel = task.getResult().toObject(models.chatRoomModel.class);
                if (chatRoomModel == null) {
                    //it's first time to chat with this user
                    chatRoomModel = new chatRoomModel(
                            chatRoomId,
                            Arrays.asList(util.currentUserId(), driver.getUid()),
                            Timestamp.now(),
                            ""
                    );
                    util.chatRoomRef(chatRoomId).set(chatRoomModel);
                }
            }
        });
    }

    public void backBtn(View view) {
        onBackPressed();
    }

}
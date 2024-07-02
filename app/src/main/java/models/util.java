package models;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class util {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static void passUserID(Intent intent, Driver driver){
        intent.putExtra("uName", driver.getName());
        intent.putExtra("uId", driver.getUid());
        intent.putExtra("token", driver.getToken());
    }

    public static Driver getUserIdFromIntent(Intent intent){
        Driver driver = new Driver();
        driver.setName(intent.getStringExtra("uName"));
        driver.setUid(intent.getStringExtra("uId"));
        driver.setToken(intent.getStringExtra("token"));
        return driver;
    }

    public static DocumentReference chatRoomRef(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatRooms")
                .document(chatRoomId);
    }
    public static CollectionReference getChatRoomMessageRef(String chatRoomId){
        return chatRoomRef(chatRoomId).collection("chats");
    }

    public static String getChatRoomId(String userId1, String userId2){
        if (userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else {
            return userId2+"_"+userId1;
        }
    }
    //for fmc services
    public static DocumentReference adminRef(){
        return FirebaseFirestore.getInstance().collection("admin")
                .document(currentUserId());
    }
    public static CollectionReference driverCollectionRef(){
        return FirebaseFirestore.getInstance().collection("drivers");
    }
    public static boolean isUserLoggedIn() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null;
    }

}

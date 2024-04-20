package com.example.json_api;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class PostDialog {
    private Context context;
    private PostListener listener;
    private int status;

    public PostDialog(Context context, int status, PostListener listener) {
        this.context = context;
        this.listener = listener;
        this.status = status;
    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_dialog_post, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUserID = dialogView.findViewById(R.id.editTextUserId);
        final EditText editTextID = dialogView.findViewById(R.id.editTextId);
        final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextBody = dialogView.findViewById(R.id.editTextBody);

        if (status == 2){
            editTextID.setVisibility(View.GONE);
            editTextUserID.setVisibility(View.GONE);
        }

//        editTextUserID.setText(userId);
//        editTextID.setText(Id);
//        editTextTitle.setText(title);
//        editTextBody.setText(body);

//        dialogBuilder.setTitle("Submit");
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String userId = editTextUserID.getText().toString().trim();
                String Id = editTextID.getText().toString().trim();
                String title = editTextTitle.getText().toString().trim();
                String body = editTextBody.getText().toString().trim();

                // Notify the listener about the update
                listener.onUpdatePost(userId, Id, title, body);
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
    public interface PostListener {
        void onUpdatePost(String userId, String postId, String title, String body);
    }
}

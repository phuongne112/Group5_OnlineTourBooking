package com.example.group5_onlinetourbookingsystem.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.Message;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Message.RecipientType;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;
    private MyDatabaseHelper dbHelper;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
        this.dbHelper = new MyDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message msg = messageList.get(position);
        holder.sender.setText(msg.getSenderName());
        holder.subject.setText(msg.getSubject());
        holder.message.setText(msg.getContent());

        // 🟢 Hiển thị icon trạng thái
        if (msg.isAnswered()) {
            holder.imgStatus.setImageResource(R.drawable.ic_check_green);
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_flag_red);
        }

        holder.btnReply.setOnClickListener(v -> {
            showReplyDialog(msg.getEmail(), msg.getContent(), msg.getId(), msg, holder.getAdapterPosition());
        });

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteHelpMessageById(msg.getId());
            messageList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView sender, subject, message;
        Button btnReply, btnDelete;
        ImageView imgStatus;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.tvSenderName);
            subject = itemView.findViewById(R.id.tvSubject);
            message = itemView.findViewById(R.id.tvMessageContent);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgStatus = itemView.findViewById(R.id.imgStatus); // 🔥 Ensure it's in your XML
        }
    }

    private void showReplyDialog(String recipientEmail, String originalQuestion, int messageId, Message msg, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Reply to Message");

        final EditText input = new EditText(context);
        input.setHint("Enter your reply here...");
        builder.setView(input);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String replyMessage = input.getText().toString();
            if (!replyMessage.isEmpty()) {
                String emailBody = "Chào bạn,\n\n"
                        + "Cảm ơn vì câu hỏi: \"" + originalQuestion + "\"\n\n"
                        + "Phản hồi của chúng tôi:\n" + replyMessage + "\n\n"
                        + "Trân trọng,\nTravelLO Team";

                sendEmail(
                        recipientEmail,
                        "Reply from Admin - Help Center",
                        emailBody
                );

                // 🔁 Update trạng thái câu hỏi
                dbHelper.markHelpMessageAsAnswered(messageId);
                msg.setAnswered(true);
                notifyItemChanged(position);

                showToastOnMainThread("Reply sent!");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void sendEmail(String recipientEmail, String emailTitle, String emailBody) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                final String senderEmail = "travellologr5@gmail.com";
                final String senderPassword = "jidi lqhb rgbb louu";

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(emailTitle);
                message.setText(emailBody);

                Transport.send(message);
                showToastOnMainThread("Email sent successfully!");
            } catch (MessagingException e) {
                e.printStackTrace();
                showToastOnMainThread("Failed to send email: " + e.getMessage());
            }
        });
    }

    private void showToastOnMainThread(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}

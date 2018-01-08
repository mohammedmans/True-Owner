package Actions.SendingEmail; /**
 * Created by maged on 6/20/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void,Void,Void> {

   //-----------------------------------------------------------------------------------
    //dy tt3ml fy Activty class
    /*  private void sendEmail(Context context) {
        //Getting content for email
        String email = "magedtaliawy@gmail.com";
        String subject = "ng7t";
        String message = "Email send";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);


        //Executing sendmail to send email
        sm.execute();
    }*/
    //----------------------------------------------------------------------------------

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email;
    private String subject;
    private String message;
    private String password ;
    Properties props ;


    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;



    //Class Constructor
    public SendMail(Context context, String email, String subject, String message){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
       //progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
        props = new Properties();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
       //progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        try {
            sendMailWithPicture();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //Creating a new session

        return null;
    }






    void sendMail(){
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL,Config.PASSWORD);
                    }
                });
        try {




            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(Config.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);



            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    void sendMailWithPicture()throws MessagingException{



            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(Config.EMAIL));
            //Adding receiver
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            message.setSubject(subject);


            Multipart multipart = new MimeMultipart();



            MimeBodyPart attachPart = new MimeBodyPart();
            String attachFile =  Environment.getExternalStorageDirectory() + "//face0.jpg";//picture location
            Log.e("TAG", attachFile);

            DataSource source = new FileDataSource(attachFile);
            attachPart.setDataHandler(new DataHandler(source));
            attachPart.setFileName(new File(attachFile).getName());

//Trick is to add the content-id header here
            attachPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(attachPart);

//third part for displaying image in the email body
            attachPart = new MimeBodyPart();
            attachPart.setContent("<h1>Succpected Activity from This Person detected</h1>" +
                    "<img src='cid:image_id'>", "text/html");

            multipart.addBodyPart(attachPart);
//Set the multipart message to the email message
            message.setContent(multipart);

            Transport.send(message);
        }






}

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        new Thread(() ->{
            // server socket
            try {
                ServerSocket serverSocket = new ServerSocket(8240);

                //Listen for a connection request
                Socket socket = serverSocket.accept();

                //Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                String input = "home", response;
                String[] words;

                while (true){
                    // recieves message from client

                    input = inputFromClient.readUTF().toLowerCase();
                    words = input.split(" ");

                    if(input.equals("home")) {
                        response = "HOME:\nThe homepage showcases a variety of features including it clock, timely " +
                                "message and quote. The stunning background can be changed by clicking on the " +
                                "gallery icon. Peruse through the preset background and hit apply to make changes.";
                    } else if(input.equals("to do") || input.equals("todo") || input.equals("to-do")) {
                        response = "TO DO:\nThe to do page is a minimalist's approach to an outstanding checklist. " +
                        "Simply enter a task in the field provided and hit the \"Enter\" key or add button to add a " +
                                "new item to your to-do list. To save your to-do list in a different location so " +
                                "you can have it on the go, simply hit save and pick a location. Otherwise, the " +
                                "checklist will be saved as normal when you come back. When a task is complete, " +
                                "simply hit the check mark besides it.";
                    } else if(input.equals("contacts") || input.equals("contact")) {
                        response = "CONTACTS:\nThe contacts page allows you to store contact information. To add a " +
                                "contact, hit the \"+\" icon on the top-right. This guides you to another window " +
                                "in which you can add a contact. To search for a contact, enter their first or last " +
                                "name and hit the \"Enter\" key. To refresh results hit the refresh icon located to " +
                                "left of the \"+\" icon. To remove a contact, click the \"x\" besides it.";
                    } else if(input.equals("voice memos") || input.equals("voice") || input.equals("memos")) {
                        response = "VOICE MEMOS\nTo record your voice simply hit the large record icon. Enter a " +
                                "name of the recording and automatically the recording will begin. An automatic ten " +
                                "second will occur. To playback recordings hit the play button. To remove a " +
                                "recording hit the \"x\" besides it.";
                    } else if(input.equals("password") || input.equals("password generator")) {
                        response = "PASSWORD GENERATOR\nGenerate strong passwords by hitting the generate button.";
                    } else if(input.equals("mail")) {
                        response = "MAIL\nAccess your Gmail from the comfort of the app. Simply login and use your " +
                                "email as you normally would. To reload page please right-click";
                    } else if(input.equals("help")) {
                        response = "HELP\nContact us: help@dashboard.com, or provide feedback sending a message " +
                                "\"Feedback - ...\".";
                    } else if(words[0].equals("feedback")) {
                        response = "Feedback recieved. Thank you.";
                    } else {
                        response = "Sorry please enter a tab name to learn about its features. example: Home";
                    }

                    // send response back to the client
                    outputToClient.writeUTF(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

}

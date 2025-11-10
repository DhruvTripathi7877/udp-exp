
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class QuoteServerThread extends Thread {
    DatagramSocket socket;
    BufferedReader in;
    public QuoteServerThread() throws IOException {
        this("Quote Server");
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(9090);
        try {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        } catch(Exception e) {
            System.out.println("Exception caught while reading from one liners txt file: "+e);
        }
    }

    @Override
    public void run() {
        System.out.println("Inside Run method");

        try {
            String abc;
            while((abc=in.readLine())!=null) {
                byte[] buf = new byte[256];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                System.out.println("I want packet from the client");
                socket.receive(datagramPacket);
                System.out.println("Packet received from the client");

                buf = abc.getBytes();

                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                datagramPacket = new DatagramPacket(buf, buf.length, inetAddress, port);
                socket.send(datagramPacket);
                System.out.println("Packet sent to the client");
            }
            System.out.println("End of server");

            socket.close();
        } catch(Exception e) {
            System.out.println("Exception inside QuoteServerThread implementation: "+e);
        }
    }
}

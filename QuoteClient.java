
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class QuoteClient {
    public static void main(String[] args) throws IOException {
        int port;
        InetAddress inetAddress;
        DatagramSocket socket = null;
        DatagramPacket packet;

        if (args.length != 1) {
            System.out.println("Usage: java QuoteClient <hostname>");
            return;
        }

        socket = new DatagramSocket();
        byte[] buf = new byte[256];
        System.out.println("Hello1");
        inetAddress = InetAddress.getByName(args[0]);
        System.out.println("Hello2");
        packet = new DatagramPacket(buf, buf.length, inetAddress, 4446);
        System.out.println("Sending packet to server: "+inetAddress);
        socket.send(packet);
        System.out.println("Sent packet to server");

        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        System.out.println("Received response from the server");
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the moment: "+received);
    }
}

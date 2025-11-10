import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class MulticastClient {
    public static void main(String[] args) throws IOException {
        InetAddress inetAddress = InetAddress.getByName("228.5.6.7");
        InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, 4446);

        NetworkInterface netif = NetworkInterface.getByName("en0");
        if (netif == null) {
            System.out.println("ERROR: en0 interface not found!");
            return;
        }
        
        MulticastSocket socket = new MulticastSocket(4446);
        socket.setNetworkInterface(netif);  // Set interface before joining

        System.out.println("Client joining group on interface: " + netif.getName());
        socket.joinGroup(socketAddress, netif);

        DatagramPacket datagramPacket;
        System.out.println("Start of client");
        for (int i=0;i<5;i++) {
            byte[] buf = new byte[256];
            datagramPacket = new DatagramPacket(buf, buf.length);
            socket.receive(datagramPacket);

            String received = new String(datagramPacket.getData());
            System.out.println("Quote of the moment: "+received);
        }

        socket.leaveGroup(socketAddress, netif);

        System.out.println("End of client");
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;


// TODO: Dynamically set the network interface based on available interfaces (ListInterfaces.java file has a method for it)
public class MulticastServerThread extends Thread {
    MulticastSocket socket;  // Changed from DatagramSocket
    BufferedReader in;

    public MulticastServerThread() throws IOException {
        this("Multicast Server");
    }

    public MulticastServerThread(String name) throws IOException {
        super(name);
        
        // Create socket without binding to specific port
        socket = new MulticastSocket();
        
        // Get en0 interface
        NetworkInterface netif = NetworkInterface.getByName("wlan2");
        if (netif == null) {
            System.out.println("WARNING: en0 interface not found, using default");
        } else {
            // Set the outgoing interface for multicast packets
            socket.setNetworkInterface(netif);
            System.out.println("Using interface: " + netif.getName());
            
            // Also try setting via setInterface (older API but sometimes works better)
            try {
                java.util.Enumeration<InetAddress> addresses = netif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof java.net.Inet4Address && !addr.isLoopbackAddress()) {
                        socket.setInterface(addr);
                        System.out.println("Set interface address: " + addr.getHostAddress());
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not set interface address: " + e.getMessage());
            }
        }
        
        // Set TTL for multicast packets (1 = local network only)
        socket.setTimeToLive(1);
        
        // Enable socket reuse (might help with VPN issues)
        socket.setReuseAddress(true);

        try {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        } catch(Exception e) {
            System.out.println("Exception while reading one-liners.txt file: "+e);
        }
    }

    public void run() {
        String moreQuotes;

        try {
            while((moreQuotes=in.readLine())!=null) {
                byte[] buf = new byte[256];

                buf = moreQuotes.getBytes();

                InetAddress group = InetAddress.getByName("228.5.6.7");
                DatagramPacket packet;
                packet = new DatagramPacket(buf, buf.length, group, 4446);
                System.out.println("Sending packets");
                socket.send(packet);
                System.out.println("Logging after packet sent");

                try {
                    sleep((long)Math.random() * 5);
                }
                catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException: "+e.getMessage());
        }
        catch(Exception e) {
            System.out.println("Exception occured: "+e);
        }

        socket.close();
    }
    
}
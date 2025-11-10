
import java.io.IOException;
import java.net.InetAddress;

public class QuoteServer {
    public static void main(String[] args) throws IOException {
        System.out.println("HostName: " + InetAddress.getLocalHost().getHostName());
        new QuoteServerThread().start();
    }
}

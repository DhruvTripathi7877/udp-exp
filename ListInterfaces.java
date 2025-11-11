public class ListInterfaces {
    public static void main(String[] args) {
        try {
            java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
            for (java.net.NetworkInterface netif : java.util.Collections.list(nets)) {
                System.out.println("Interface Name: " + netif.getName());
                java.util.Enumeration<java.net.InetAddress> addresses = netif.getInetAddresses();
                for (java.net.InetAddress addr : java.util.Collections.list(addresses)) {
                    System.out.println("  Address: " + addr.getHostAddress());
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing network interfaces: " + e.getMessage());
        }
    }
}
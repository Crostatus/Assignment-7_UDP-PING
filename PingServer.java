import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
//Classe server che invia un echo del pacchetto ricevuto con una probabilità del 75%
public class PingServer {

    public static void main(String[] args) throws Exception{
        //Richiede un argomento
        //args[0]: (int) numero di porta su cui stare in ascolto per ricevere i pacchetti che verranno invitai dal client
        int port = Integer.valueOf(args[0]);
        DatagramSocket serverSocket = new DatagramSocket(port);
        InetAddress ipAddress = InetAddress.getByName("localhost");
        byte[] data = new byte[128];
        long startTime = System.currentTimeMillis();
        DatagramPacket echoPacket = new DatagramPacket(data, data.length, ipAddress, 4444);

        while((System.currentTimeMillis() - startTime) < 30000){
            serverSocket.receive(echoPacket);
            String receivedMessage = new String(echoPacket.getData(), 0, echoPacket.getLength(), "US-ASCII");
            System.out.print(echoPacket.getAddress().getHostAddress() + ":" + echoPacket.getPort() + "> " + receivedMessage + " ACTION: ");
            fakeDelayOrPacketLoss(serverSocket, echoPacket);
        }

    }

    //Metodo usato per decidere se inviare un pacchetto ricevuto o no, e nel caso positivo introdurre un ritardo artificiale
    //per simulare un eventuale traffico in rete
    private static void fakeDelayOrPacketLoss(DatagramSocket serverSoket, DatagramPacket echoPacket) throws Exception{
        boolean sendItBack;
        if(Math.random() < 0.75)
            sendItBack = true;
        else
            sendItBack = false;

        if(sendItBack){
            Random randomDelayGenerator = new Random();
            int delay = randomDelayGenerator.nextInt(500) + 100;
            Thread.sleep(delay);
            try {
                serverSoket.send(echoPacket);
                System.out.println(" delayed " + delay + " ms");
            }
            catch (Exception e){
                System.out.println(" not sent");
            }
        }
        else
            System.out.println(" not sent");
    }

}

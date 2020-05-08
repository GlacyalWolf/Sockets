import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class MainServer {

    /**
     * Server Port
     */
    private static final  int PORT = 5000;

    /**
     * @method main: Server code to answer the client demands through sockets
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Socket del servidor
        ServerSocket serverSocket = null;
        //Socket del client
        Socket clientSocket = null;

        try {
            //Inicialitzem el Socket del servidor amb el port que usarem
            //per esperar les peticions que ens envien a través d'internet
            serverSocket = new ServerSocket(PORT);

            System.out.println("**** Servidor Iniciat ****");
            System.out.println("Esperant al client...");

            while(true){
                //Espera connexió amb el client, si existeix l'accepta
                clientSocket = serverSocket.accept();

                //Llegim el que envia el client
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //Imprimim les dades de sortida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());

                //Es llegeix la petició del client (frase | llibre)
                String request = input.readLine();
                System.out.println("\nClient> petició [" + request +  "]");

                //Crida al mètode "process", i s'espera un resultat
                String strOutput = process(request);

                //S'imprimeix en la consola del "servidor"
                System.out.println("Servidor> Resultat de la petició:");
                System.out.println("Servidor> \"" + strOutput + "\"\n");

                //Buidem contingut del Stream per imprimir el resultat de la petició
                output.flush();
                output.println(strOutput);

                //Tanquem el socket del client
                clientSocket.close();

                //Our backdoor to finish the service
                if(request.equals("stop")) {
                    System.out.println("**** Servidor Finalitzat ****");
                    break;
                }
            }//end while
            //Tanquem el socket del servidor
            serverSocket.close();
        } catch (IOException | NullPointerException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * @method process: Takes the client request and send a result
     * @param request: client wish
     * @return String
     */
    public static String process(String request){
        String result="";

        //Key word: excusa
        ArrayList<String> excusator = frases();
        //Key word: wonder
        ArrayList<String> moralUp = wonder();

        if(request!=null)
            switch(request){
                case "excusa":
                    Collections.shuffle(excusator);
                    result = excusator.get(0);
                    break;
                case "wonder":
                    Collections.shuffle(moralUp);
                    result = moralUp.get(0);
                    break;
                case "exit":
                    result = "See you soon!";
                    break;
                case "stop":
                    result = "Adeu món cruel!";
                    break;
				
                default:
                    result = "La petició no es pot resoldre.";
                    break;
            }
		else{
			result="prueba";
		}
        return result;
    }
    /**
     * @method wonder: Creates a collection of positive sentences
     * @return ArrayList<String>
     * */
    private static ArrayList<String> wonder(){
        String[] up = {
                "El somriure és la forma universal de dir Benvingut!",
                "Swagata, Namaste, Dobrodošli, Yokoso, Benvenuti, Willkommen, Welcome, Benvingut",
                "Des del moment en què vaig agafar el seu llibre vaig caure a terra rodant de riure. Algun dia espero llegir-lo.",
                "El pitjor de l'amor és que molts el confonen amb la gastritis i, quan s'han curat de la indisposició, es troben que s'han casat.",
                "Hi ha moltes coses a la vida més importants que els diners. Però costen tant!",
                "Dues coses són infinites: l'univers i l'estupidesa humana, i jo no estic segur sobre l'univers.",
                "Els savis parlen perquè tenen alguna cosa a dir. Els ximples parlen perquè han de dir alguna cosa.",
                "Riu i el món riurà amb tu; plora i el món, donant-te l'esquena, et deixarà plorar.",
                "Déu em perdonarà: és el seu ofici."};
        ArrayList<String> funList = new ArrayList<>();
        Collections.addAll(funList, up);
        return funList;
    }
    /**
     * @method frases: Creates a collection of job excuses
     * @return ArrayList<String>
     * */
    private static ArrayList<String> frases(){
        String[] phrases = {
                "Les cames no m'arriben a terra! No puc caminar!",
                "No em queda cafè... No puc sortir del llit... Ajuda!",
                "Se m'ha assecat tota la sang.",
                "Avui em sento especialment creatiu i intel·ligent: no ho vaig a desaprofitar treballant.",
                "El meu gos ha mort... per segon cop.",
                "Estic enterrant el meu gos; penjo que sinó s'escapa.",
                "Hi ha una cuca al bany i encara no m'he pogut dutxar.",
                "Ahir em vaig posar a incubar un ou i ara no puc marxar... O vol que mori!! ens veiem d'aquí a 21 díes.",
                "És fosc i núvol, millor m'espere a que es complete la descàrrega.",
                "El meu monitor no sura... que no val com a monitor de salvament?",
                "La meva parella m'ha deixat (no especifiques on, igual t'ha deixat aprop de l'oficina).",
                "Els meus pares m'han donat en adopció i estic coneixent a la meva nova família.",
                "He perdut la porta de casa i no puc sortir!!!"};
        ArrayList<String> phrasesList = new ArrayList<>();
        Collections.addAll(phrasesList, phrases);
        return phrasesList;
    }
}
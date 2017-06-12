import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestAsServer {


/**
 * 
 * @param args
 * @throws InterruptedException
 */
	public static void main(String[] args) throws InterruptedException {
//	стартуем сервер на порту 3345
		
		try	(ServerSocket server= new ServerSocket(3345);){
// становимся в ожидание подключения к сокету под именем - "client" на серверной стороне								
				Socket client = server.accept();
				
// после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением				
				System.out.print("Connection accepted.");
				
// инициируем каналы общения в сокете, для сервера		
				
				// канал чтения из сокета
				DataInputStream in = new DataInputStream(client.getInputStream());
				System.out.println("DataInputStream created");
				
				// канал записи в сокет
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				System.out.println("DataOutputStream  created");
				
// начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт				
				while(!client.isClosed()){
				
				System.out.println("Server reading from channel");
								
// сервер ждёт в канале чтения (inputstream) получения данных клиента				
				String entry = in.readUTF();
								
// после получения данных считывает их				
				System.out.println("READ from client message - "+entry);
				
// и выводит в консоль				
				System.out.println("Server try writing to channel");
				
// инициализация проверки условия продолжения работы с клиентом по этому сокету	по кодовому слову			
				if(entry.equalsIgnoreCase("quit")){
					System.out.println("Client initialize connections suicide ...");
					out.writeUTF("Server reply - "+entry + " - OK");				
					Thread.sleep(3000);
					break;
				}
				
// если условие окончания работы не верно - продолжаем работу - отправляем эхо обратно клиенту 				
				out.writeUTF("Server reply - "+entry + " - OK");				
				System.out.println("Server Wrote message to client.");
				
// освобождаем буфер сетевых сообщений				
				out.flush();	

				}
				
// если условие выхода - верно выключаем соединения				
				System.out.println("Client disconnected");
				System.out.println("Closing connections & channels.");
				
				// закрываем сначала каналы сокета !
				in.close();
				out.close();
				
				// потом закрываем сокет общения на стороне сервера!
				client.close();
				
				// потом закрываем сокет сервера который создаёт сокеты общения
				// хотя при многопоточном применении его закрывать не нужно
				// для возможности поставить этот серверный сокет обратно в ожидание нового подключения
				server.close();
				System.out.println("Closing connections & channels - DONE.");
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
}

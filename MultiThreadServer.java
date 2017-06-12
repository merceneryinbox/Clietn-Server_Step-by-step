import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author mercenery
 *
 */
public class MultiThreadServer {

	static ExecutorService executeIt = Executors.newFixedThreadPool(2);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// стартуем сервер на порту 3345 и инициализируем переменную для обработки консольных команд с самого сервера
		try (ServerSocket server = new ServerSocket(3345)) {
			System.out.println("Server socket created, command console reader for listen to server commands");

			// стартуем цикл при условии что серверный сокет не закрыт
			while (!server.isClosed()) {			

				// становимся в ожидание
				// подключения к сокету общения под именем - "clientDialog" на
				// серверной стороне
				Socket client = server.accept();

				// после получения запроса на подключение сервер создаёт сокет
				// для общения с клиентом и отправляет его в отдельную нить
				// в Runnable(при необходимости можно создать Callable)
				// монопоточную нить = сервер - MonoThreadClientHandler и тот
				// продолжает общение от лица сервера
				executeIt.execute(new MonoThreadClientHandler(client));
				System.out.print("Connection accepted.");
			}

			// закрытие пула нитей после завершения работы всех нитей
			executeIt.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
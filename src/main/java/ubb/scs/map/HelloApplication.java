package ubb.scs.map;

import ubb.scs.map.controller.UtilizatorController;
import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.MessageValidator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.FriendRequestDbRepository;
import ubb.scs.map.repository.database.MessageDbRepository;
import ubb.scs.map.repository.database.PrietenieDbRepository;
import ubb.scs.map.repository.database.UtilizatorDbRepository;
import ubb.scs.map.service_v2.MessageService;
import ubb.scs.map.service_v2.PrietenieService;
import ubb.scs.map.service_v2.UtilizatorService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}

public class HelloApplication extends Application {

    Repository<Long, Utilizator> utilizatorRepository;
    UtilizatorService service;
    PrietenieService service_friendship;
    MessageService service_message;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        String fileN = ApplicationContext.getPROPERTIES().getProperty("data.tasks.messageTask");
//        messageTaskRepository = new InFileMessageTaskRepository
//                (fileN, new MessageTaskValidator());
//        messageTaskService = new MessageTaskService(messageTaskRepository);
        //messageTaskService.getAll().forEach(System.out::println);

        System.out.println("Reading data from file");
        String username="postgres";
        String password="2501";
        String url="jdbc:postgresql://localhost:5432/map-social-network";

        Repository<Long, Utilizator> utilizatorRepository =
                new UtilizatorDbRepository(url,username, password,  new UtilizatorValidator());

        Repository<Long, Prietenie> prietenieRepository =
                new PrietenieDbRepository(url, username, password, new PrietenieValidator(utilizatorRepository));

        Repository<Long, Prietenie> friendRequestRepository =
                new FriendRequestDbRepository(url, username, password, new PrietenieValidator(utilizatorRepository));

        Repository<Long, Message> messageRepository =
                new MessageDbRepository(url, username, password, new MessageValidator());

        service =new UtilizatorService(utilizatorRepository);
        service_friendship = new PrietenieService(prietenieRepository, friendRequestRepository, utilizatorRepository);
        service_message = new MessageService(messageRepository, utilizatorRepository);
        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();


    }

    private void initView(Stage primaryStage) throws IOException {

       // FXMLLoader fxmlLoader = new FXMLLoader();
        //fxmlLoader.setLocation(getClass().getResource("com/example/guiex1/views/utilizator-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/utilizator-view.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);
        userController.setPrietenieService(service_friendship);
        userController.setMessageService(service_message);
    }
}
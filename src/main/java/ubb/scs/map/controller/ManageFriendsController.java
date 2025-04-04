package ubb.scs.map.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service_v2.MessageService;
import ubb.scs.map.service_v2.PrietenieService;
import ubb.scs.map.service_v2.UtilizatorService;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.events.UtilizatorEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ManageFriendsController implements Observer<PrietenieEntityChangeEvent> {
    PrietenieService service;
    ObservableList<Prietenie> model = FXCollections.observableArrayList();
    Utilizator source_user;
    MessageService service_message;

    @FXML
    TableView<Prietenie> tableFriendshipsView;
    @FXML
    TableColumn<Prietenie,String> tableColumnFriendName;
    @FXML
    TableColumn<Prietenie,String> tableColumnFriendsFrom;
    @FXML
    private Button buttonNext;
    @FXML
    private Button buttonPrevious;
    @FXML
    private Label labelPage;

    private int pageSize = 2;
    private int currentPage = 0;
    private int totalNumberOfElements = 0;

    public void setPrietenieService(PrietenieService service, Stage stage, Utilizator u, MessageService service_message) {
        this.service = service;
        this.source_user = u;
        this.service_message = service_message;
        service.addObserver(this);
        totalNumberOfElements = this.service.getNumberOfFriends(source_user.getId());
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFriendName.setCellValueFactory(cellData -> {
            Prietenie prietenie = cellData.getValue();
            long friendId = prietenie.getUser1Id() == source_user.getId() ? prietenie.getUser2Id() : prietenie.getUser1Id();
            Utilizator friend = service.getUserById(friendId);
            return new SimpleStringProperty(friend.getFullName());
        });
        tableColumnFriendsFrom.setCellValueFactory(new PropertyValueFactory<Prietenie, String>("date"));
        tableFriendshipsView.setItems(model);
    }

    private void initModel() {
        Page<Prietenie> page = service.findAllOnPage(new Pageable(currentPage, pageSize), source_user.getId());
//        int maxPage = (int) Math.ceil((double) page.getTotalNumberOfElements() / pageSize) - 1;
        int maxPage = (int) Math.ceil((double) this.totalNumberOfElements / pageSize) - 1;
        if (maxPage == -1) {
            maxPage = 0;
        }
        if (currentPage > maxPage) {
            currentPage = maxPage;
            page = service.findAllOnPage(new Pageable(currentPage, pageSize), source_user.getId());
        }
//        totalNumberOfElements = page.getTotalNumberOfElements();
        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable((currentPage + 1) * pageSize >= totalNumberOfElements);
        List<Prietenie> friendshipList = StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(friendshipList);
        labelPage.setText("Page " + (currentPage + 1) + " of " + (maxPage + 1));
    }

//    private void initModel() {
//        Iterable<Prietenie> result = service.getFriendshipsOfUser(this.source_user.getId());
//        List<Prietenie> friendships = StreamSupport.stream(result.spliterator(), false)
//                .collect(Collectors.toList());
//        model.setAll(friendships);
//    }

    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/send-friend-request.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Send friend request");
            stage.setScene(new Scene(root));
            SendFriendRequestController controller = loader.getController();
            controller.setService(service, stage, source_user);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        Prietenie p =(Prietenie) tableFriendshipsView.getSelectionModel().getSelectedItem();
        if (p != null) {
            Prietenie deleted = service.deletePrietenie(p.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Delete friendship","Prietenia a fost stearsa");
        }
        else MessageAlert.showErrorMessage(null, "NU ati selectat nici un utilizator");
    }

    public void handleIncomingRequests(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/incoming-requests.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Incoming request");
            stage.setScene(new Scene(root));
            IncomingRequestsController controller = loader.getController();
            controller.setService(service, stage, source_user);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleChat(ActionEvent actionEvent) {
        Prietenie selected = tableFriendshipsView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            long friendId = selected.getUser1Id() == source_user.getId() ? selected.getUser2Id() : selected.getUser1Id();
            Utilizator friend = service.getUserById(friendId);
            showChatWindow(friend);
        } else
            MessageAlert.showErrorMessage(null, "NU ati selectat nici un student");
    }

    private void showChatWindow(Utilizator destination_user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/chat.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Chat with " + destination_user.getFullName());
            stage.setScene(new Scene(root));
            ChatController controller = loader.getController();
            controller.setService(service_message, stage, source_user, destination_user);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNextPage(ActionEvent actionEvent) {
        currentPage ++;
        initModel();
    }

    public void onPreviousPage(ActionEvent actionEvent) {
        currentPage --;
        initModel();
    }
}

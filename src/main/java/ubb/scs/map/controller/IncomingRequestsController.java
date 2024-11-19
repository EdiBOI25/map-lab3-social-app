package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service_v2.PrietenieService;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class IncomingRequestsController implements Observer<PrietenieEntityChangeEvent> {
    PrietenieService service;
    ObservableList<Prietenie> model = FXCollections.observableArrayList();
    Utilizator source_user;

    @FXML
    private ListView<Prietenie> listView;

    public void setService(PrietenieService service, Stage stage, Utilizator u) {
        this.service = service;
        this.source_user = u;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        listView.setCellFactory(param -> new ListCell<Prietenie>() {
            @Override
            protected void updateItem(Prietenie item, boolean empty) {
                super.updateItem(item, empty);


                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(10);
                    Long requester_id = item.getUser1Id();
                    Utilizator requester = service.getUserById(requester_id);
                    if (requester != null) {
                        Label nameLabel = new Label(requester.getFullName());
                        Button acceptRequestButton = new Button("Accept");
                        Button declineRequestButton = new Button("Decline");

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);
                        hBox.getChildren().addAll(nameLabel, spacer, acceptRequestButton, declineRequestButton);

                        acceptRequestButton.setOnAction(e -> {
                            Prietenie prietenie = new Prietenie(source_user.getId(), item.getUser1Id(), LocalDate.now());
                            if (service.addFriendshipToDB(prietenie) == null &&
                                    service.deleteRequestFromDB(item.getId()) != null) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Request accepted");
                                alert.setHeaderText("Friend request accepted");
                                alert.setContentText("You are now friends with " + requester.getFullName());
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Accept failed");
                                alert.setHeaderText("Accept failed");
                                alert.setContentText("Couldn't accept friend request");
                                alert.showAndWait();
                            }
                        });

                        declineRequestButton.setOnAction(e -> {
                            if (service.deleteRequestFromDB(item.getId()) != null) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Request declined");
                                alert.setHeaderText("Friend request declined");
                                alert.setContentText("You have declined the friend request");
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Decline failed");
                                alert.setHeaderText("Decline failed");
                                alert.setContentText("Couldn't decline friend request");
                                alert.showAndWait();
                            }
                        });

                        setGraphic(hBox);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        listView.setItems(model);
    }

    private void initModel() {
        Iterable<Prietenie> result = service.getIncomingRequestsForUser(this.source_user.getId());
        List<Prietenie> users = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }
    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }
}

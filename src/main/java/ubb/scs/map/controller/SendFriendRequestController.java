package ubb.scs.map.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class SendFriendRequestController implements Observer<PrietenieEntityChangeEvent> {
    PrietenieService service;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();
    Utilizator source_user;

    @FXML
    private ListView<Utilizator> listView;

    public void setService(PrietenieService service, Stage stage, Utilizator u) {
        this.service = service;
        this.source_user = u;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        listView.setCellFactory(param -> new ListCell<Utilizator>() {
            @Override
            protected void updateItem(Utilizator item, boolean empty) {
                super.updateItem(item, empty);


                if (empty || item == null || item.getFirstName() == null || item.getLastName() == null) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    Label nameLabel = new Label(item.getFullName());
                    Button addFriendButton = new Button("Add friend");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    hBox.getChildren().addAll(nameLabel, spacer, addFriendButton);

                    addFriendButton.setOnAction(e -> {
                        Prietenie prietenie = new Prietenie(source_user.getId(), item.getId(), LocalDate.now());
                        if (service.addFriendRequest(prietenie) == null) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Friend request sent");
                            alert.setHeaderText("Friend request sent");
                            alert.setContentText("Friend request sent to " + item.getFullName());
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Friend request failed");
                            alert.setHeaderText("Friend request failed");
                            alert.setContentText("Friend request to " + item.getFullName() + " failed" +
                                    "\nYou may have already sent a friend request to this user");
                            alert.showAndWait();
                        }
                    });
                    setGraphic(hBox);
                }
            }
        });
        listView.setItems(model);
    }

    private void initModel() {
        Iterable<Utilizator> result = service.getUsersNotFriendsWith(this.source_user.getId());
        List<Utilizator> users = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }
    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }
}

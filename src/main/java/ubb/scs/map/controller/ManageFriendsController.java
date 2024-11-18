package ubb.scs.map.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service_v2.PrietenieService;
import ubb.scs.map.service_v2.UtilizatorService;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.events.UtilizatorEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ManageFriendsController implements Observer<PrietenieEntityChangeEvent> {
    PrietenieService service;
    ObservableList<Prietenie> model = FXCollections.observableArrayList();
    Utilizator source_user;

    @FXML
    TableView<Prietenie> tableFriendshipsView;
    @FXML
    TableColumn<Prietenie,String> tableColumnFriendName;
    @FXML
    TableColumn<Prietenie,String> tableColumnFriendsFrom;

    public void setPrietenieService(PrietenieService service, Stage stage, Utilizator u) {
        this.service = service;
        this.source_user = u;
        service.addObserver(this);
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
        Iterable<Prietenie> result = service.getFriendshipsOfUser(this.source_user.getId());
        List<Prietenie> friendships = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(friendships);
    }

    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        Prietenie p =(Prietenie) tableFriendshipsView.getSelectionModel().getSelectedItem();
        if (p != null) {
            Prietenie deleted = service.deletePrietenie(p.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Delete friendship","Prietenia a fost stearsa");
        }
        else MessageAlert.showErrorMessage(null, "NU ati selectat nici un utilizator");
    }
}
